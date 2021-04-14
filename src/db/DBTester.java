package db;

import pojo.Company;
import pojo.Info;

public class DBTester {

	public static void main(String[] args) {
		
		
		
		LocalDBHandler dbHandler = new DbCompany();
		dbHandler.createNewTable();
		/*
		Company com = new Company("Linkedin_Company_URL1", "Company_Name", "Headquarters", "Website", "Founded",	"Company_Size", "Industry", "Company_Type");
		dbHandler.insert(com);
		com = new Company("Linkedin_Company_URL2", "Company_Name", "Headquarters", "Website", "Founded",	"Company_Size", "Industry", "Company_Type");
		dbHandler.insert(com);
		com = new Company("Linkedin_Company_URL3", "Company_Name", "Headquarters", "Website", "Founded",	"Company_Size", "Industry", "Company_Type");
		dbHandler.insert(com);
		com = new Company("Linkedin_Company_URL4", "Company_Name", "Headquarters", "Website", "Founded",	"Company_Size", "Industry", "Company_Type");
		dbHandler.insert(com);
		com = new Company("Linkedin_Company_URL5", "Company_Name", "Headquarters", "Website", "Founded",	"Company_Size", "Industry", "Company_Type");
		dbHandler.insert(com);
		com = new Company("Linkedin_Company_URL6", "Company_Name", "Headquarters", "Website", "Founded",	"Company_Size", "Industry", "Company_Type");
		dbHandler.insert(com);
		dbHandler.selectRows(6);
		
		com = new Company("Company_URL3", "Company_Name", "Headquarters", "Website", "Founded",	"Company_Size", "Industry", "Company_Type");
		dbHandler.update(com, "Linkedin_Company_URL3");
		
		System.out.println(dbHandler.selectAtIndex(0));
		dbHandler.createNewTable();
		dbHandler.closeConnection();
		*/
	}

}
