package scrapper;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;

import db.LocalDBHandler;

public class CsvGenerator {

	public CsvGenerator() {
	}

	public int listtoCsv(String keyword, LinkedList<Info> list) {
		
		int count = 0;
		
		System.out.println("list size 1: " + list);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Calendar cal = Calendar.getInstance();
		String fileName = dateFormat.format(cal.getTime());

		FileWriter writer = null;
		try {
			writer = new FileWriter("Linkedin_" + keyword + "_list_" + fileName + ".csv");

			writer.append("Linkedin_Profile_URL");
			writer.append(",");
			writer.append("First_Name");
			writer.append(",");
			writer.append("Last_Name");
			writer.append(",");
			writer.append("Email_ID");
			writer.append(",");
			writer.append("Contact_Number");
			writer.append(",");
			writer.append("Location");
			writer.append(",");
			writer.append("Industry");
			writer.append(",");
			writer.append("Designation");
			writer.append(",");
			writer.append("Company_Name");
			writer.append(",");
			writer.append("Company_Size");
			writer.append(",");
			writer.append("\n");

			System.out.println(" -- out size-- "+ list.size());
			if(list.size() > 0) {
				Iterator<Info> it = list.iterator();
	
				while (it.hasNext()) {
					Info info = (Info) it.next();
					System.out.println(" --data -- "+ 
							info.getLink() + " getLink " + 
							info.getFirstName() + " getFirstName " + 
							info.getSecondName() + " getSecondName " + 
							info.getEmail() + " getEmail " + 
							info.getPhone() + " getPhone " + 
							info.getLocation() + " getLocation " + 
							info.getIndustry() + " getIndustry " + 
							info.getCurrentJobTitle() + " getCurrentJobTitle " + 
							info.getCurrentCompany() + " getCurrentCompany " + 
							info.getCompanySize() + " getCompanySize " );

					writer.append(commaSkiping(info.getLink()));
					writer.append(",");
					writer.append(commaSkiping(info.getFirstName()));
					writer.append(",");
					writer.append(commaSkiping(info.getSecondName()));
					writer.append(",");
					writer.append(commaSkiping(info.getEmail()));
					writer.append(",");
					writer.append(commaSkiping(info.getPhone()));
					writer.append(",");
					writer.append(commaSkiping(info.getLocation()));
					writer.append(",");
					writer.append(commaSkiping(info.getIndustry()));
					writer.append(",");
					writer.append(commaSkiping(info.getCurrentJobTitle()));
					writer.append(",");
					writer.append(commaSkiping(info.getCurrentCompany()));
					writer.append(",");
					String size = info.getCompanySize() +"";
					if(!size.contains("employees") && size.length() > 1) size = size + " employees";
					writer.append(commaSkiping(size));
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

	protected String commaSkiping(String text) {
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
