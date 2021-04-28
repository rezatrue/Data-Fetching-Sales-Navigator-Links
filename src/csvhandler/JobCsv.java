package csvhandler;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;

import db.DbJob;
import pojo.Job;

public class JobCsv implements CsvGenerator{
	private LinkedList<Job> jlist;
	private DbJob dbJob;
	
	public JobCsv() {
		this.jlist = new LinkedList<>();
		dbJob = new DbJob();
	}

	@Override
	public int listtoCsv(String keyword, int remaining) {
		// if number is greater than 0 then you can print full list
		int count = 0;
		int permitableNumber = 0;
//		if(remaining >= 0 ) permitableNumber = dbJob.countRecords();
//		else permitableNumber = dbJob.countRecords() + remaining;
		permitableNumber = dbJob.countRecords(); // testing
		this.jlist = dbJob.selectRows(permitableNumber); 
		 
		System.out.println("list size cg: " + jlist.size());
		
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Calendar cal = Calendar.getInstance();
		String fileName = dateFormat.format(cal.getTime());

		FileWriter writer = null;
		try {
			writer = new FileWriter("Linkedin_Job_" + keyword + "_list_" + fileName + ".csv");

			writer.append("Job_Link");
			writer.append(",");
			writer.append("Job_Title");
			writer.append(",");
			writer.append("Company");
			writer.append(",");
			writer.append("Company_Link");
			writer.append(",");
			writer.append("Location");
			writer.append(",");
			writer.append("Job_Description");
			writer.append(",");
			writer.append("\n");

			if(jlist.size() > 0) {
				Iterator<Job> it = jlist.iterator();
	
				while (it.hasNext()) {
					Job job = it.next();
					System.out.println(" --data -- "+ 
							job.getJobLink() + " jobLink " + 
							job.getJobTitle() + " jobTitle " + 
							job.getCompany() + " company " + 
							job.getCompanyLink() + " link " + 
							job.getLocation() + " location " + 
							job.getJobDescription() + " description ");

					writer.append(commaSkiping(job.getJobLink()));
					writer.append(",");
					writer.append(commaSkiping(job.getJobTitle()));
					writer.append(",");
					writer.append(commaSkiping(job.getCompany()));
					writer.append(",");
					writer.append(commaSkiping(job.getCompanyLink()));
					writer.append(",");
					writer.append(commaSkiping(job.getLocation()));
					writer.append(",");
					writer.append(commaSkiping(job.getJobDescription()));
					writer.append("\n");
					count++;
				}
			}

		} catch (IOException e) {
			System.out.println(" csv g Error : " + e.getMessage());
		}finally {
			try {
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
		return count;
	}
	

	private String commaSkiping(String text) {
		try {
		String newText = text ;
		if (newText.contains(","))
			if (!newText.startsWith("\"") && !newText.endsWith("\""))
				newText = "\"" + newText + "\"";
		return newText;
		}catch (Exception e) {
			return "";
		}
	}



}
