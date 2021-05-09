package csvhandler;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;

import db.DbCompany;
import db.DbPeople;
import db.LocalDBHandler;
import pojo.Company;
import pojo.People;

public class PeopleCsv implements CsvGenerator{
	private LinkedList<People> plist;
	private DbPeople dbPeople;
	
	public PeopleCsv() {
		this.plist = new LinkedList<>();
		dbPeople = new DbPeople();
	}

	@Override
	public int listtoCsv(String keyword, int remaining) {
		// if number is greater than 0 then you can print full list
		int count = 0;
		int permitableNumber = 0;
		if(remaining >= 0 ) permitableNumber = dbPeople.countRecords();
		else permitableNumber = dbPeople.countRecords() + remaining;
		this.plist = dbPeople.selectRows(permitableNumber); 
		 
		System.out.println("list size cg: " + plist.size());
		
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Calendar cal = Calendar.getInstance();
		String fileName = dateFormat.format(cal.getTime());

		FileWriter writer = null;
		try {
			writer = new FileWriter("Lin_peo_" + keyword + "_list_" + fileName + ".csv");

			writer.append("Linkedin_Profile_URL");
			writer.append(",");
			writer.append("First_Name");
			writer.append(",");
			writer.append("Last_Name");
			writer.append(",");
			writer.append("Email_ID");
			writer.append(",");
			writer.append("Address");
			writer.append(",");
			writer.append("Designation");
			writer.append(",");
			writer.append("Service_Range");
			writer.append(",");
			writer.append("Company");
			writer.append(",");
			writer.append("Location");
			writer.append(",");
			writer.append("Degree_Name");
			writer.append(",");
			writer.append("FOS");
			writer.append(",");
			writer.append("Institute");
			writer.append(",");
			writer.append("Dates");
			writer.append(",");
			writer.append("\n");

			if(plist.size() > 0) {
				Iterator<People> it = plist.iterator();
	
				while (it.hasNext()) {
					People info = it.next();
					System.out.println(" --data -- "+ 
							info.getLink() + " getLink " + 
							info.getFirstName() + " getFirstName " + 
							info.getLastName() + " getSecondName ");

					writer.append(commaSkiping(info.getLink()));
					writer.append(",");
					writer.append(commaSkiping(info.getFirstName()));
					writer.append(",");
					writer.append(commaSkiping(info.getLastName()));
					writer.append(",");
					writer.append(commaSkiping(info.getEmail()));
					writer.append(",");
					writer.append(commaSkiping(info.getAddress()));
					writer.append(",");
					writer.append(commaSkiping(info.getCurrentJobTitle()));
					writer.append(",");
					writer.append(commaSkiping(info.getServiceRange()));
					writer.append(",");
					writer.append(commaSkiping(info.getCurrentCompany()));
					writer.append(",");
					writer.append(commaSkiping(info.getCompanyLocation()));
					writer.append(",");
					writer.append(commaSkiping(info.getDegreeName()));
					writer.append(",");
					writer.append(commaSkiping(info.getFos()));
					writer.append(",");
					writer.append(commaSkiping(info.getInstitute()));
					writer.append(",");
					writer.append(commaSkiping(info.getDates()));
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
