package db;

import scrapper.Company;
import scrapper.Info;

public class DBTester {

	public static void main(String[] args) {
		LocalDBHandler dbHandler = new DbCompany();
		dbHandler.createNewTable();
		Company com = new Company("Linkedin_Company_URL", "Company_Name", "Headquarters", "Website", "Founded",	"Company_Size", "Industry", "Company_Type");
		dbHandler.insert(com);
		com = new Company("Linkedin_Company_URL", "Company_Name", "Headquarters", "Website", "Founded",	"Company_Size", "Industry", "Company_Type");
		dbHandler.insert(com);
		com = new Company("Linkedin_Company_URL", "Company_Name", "Headquarters", "Website", "Founded",	"Company_Size", "Industry", "Company_Type");
		dbHandler.insert(com);
		com = new Company("Linkedin_Company_URL", "Company_Name", "Headquarters", "Website", "Founded",	"Company_Size", "Industry", "Company_Type");
		dbHandler.insert(com);
		com = new Company("Linkedin_Company_URL", "Company_Name", "Headquarters", "Website", "Founded",	"Company_Size", "Industry", "Company_Type");
		dbHandler.insert(com);
		com = new Company("Linkedin_Company_URL", "Company_Name", "Headquarters", "Website", "Founded",	"Company_Size", "Industry", "Company_Type");
		dbHandler.insert(com);
		dbHandler.selectRows(6);
		dbHandler.closeConnection();
	}

}
