package csvhandler;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;

import db.DbLead;
import db.LocalDBHandler;
import pojo.Lead;

public class LeadCsv implements CsvGenerator{
	private LinkedList<Lead> plist;
	private DbLead dbLead;
	
	public LeadCsv() {
		this.plist = new LinkedList<>();
		dbLead = new DbLead();
	}

	@Override
	public int listtoCsv(String keyword, int remaining) {
		// if number is greater than 0 then you can print full list
		int count = 0;
		int permitableNumber = 0;
		if(remaining >= 0 ) permitableNumber = dbLead.countRecords();
		else permitableNumber = dbLead.countRecords() + remaining;
		this.plist = dbLead.selectRows(permitableNumber); 
		 
		System.out.println("list size cg: " + plist.size());
		
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

			if(plist.size() > 0) {
				Iterator<Lead> it = plist.iterator();
	
				while (it.hasNext()) {
					Lead lead = it.next();
					System.out.println(" --data -- "+ 
							lead.getLink() + " getLink " + 
							lead.getFirstName() + " getFirstName " + 
							lead.getSecondName() + " getSecondName " + 
							lead.getEmail() + " getEmail " + 
							lead.getPhone() + " getPhone " + 
							lead.getLocation() + " getLocation " + 
							lead.getIndustry() + " getIndustry " + 
							lead.getCurrentJobTitle() + " getCurrentJobTitle " + 
							lead.getCurrentCompany() + " getCurrentCompany " + 
							lead.getCompanySize() + " getCompanySize " );

					writer.append(commaSkiping(lead.getLink()));
					writer.append(",");
					writer.append(commaSkiping(lead.getFirstName()));
					writer.append(",");
					writer.append(commaSkiping(lead.getSecondName()));
					writer.append(",");
					writer.append(commaSkiping(lead.getEmail()));
					writer.append(",");
					writer.append(commaSkiping(lead.getPhone()));
					writer.append(",");
					writer.append(commaSkiping(lead.getLocation()));
					writer.append(",");
					writer.append(commaSkiping(lead.getIndustry()));
					writer.append(",");
					writer.append(commaSkiping(lead.getCurrentJobTitle()));
					writer.append(",");
					writer.append(commaSkiping(lead.getCurrentCompany()));
					writer.append(",");
					writer.append(commaSkiping("["+lead.getCompanySize()+"]"));
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
