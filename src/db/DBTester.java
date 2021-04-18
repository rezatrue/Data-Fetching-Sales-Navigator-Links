package db;

import pojo.Company;
import pojo.Info;
import pojo.People;

public class DBTester {

	public static void main(String[] args) {
		
		LocalDBHandler dbHandler = new DbPeople();
		System.out.println(dbHandler.countRecords());
		//dbHandler.createNewTable();
		
		People people = new People("https://www.linkedin.com/in/akila-akter-473605b5", "first_name", "second_name", "email", "phone", "location", "industry", "currentJobTitle", "currentCompany", "companySize");
		dbHandler.insert(people);
		people = new People("https://www.linkedin.com/in/sakilahmed-net", "first_name", "second_name", "email", "phone", "location", "industry", "currentJobTitle", "currentCompany", "companySize");
		dbHandler.insert(people);
		people = new People("link", "first_name", "second_name", "email", "phone", "location", "industry", "currentJobTitle", "currentCompany", "companySize");
		dbHandler.insert(people);
		people = new People("https://www.linkedin.com/in/asif-rahaman10", "first_name", "second_name", "email", "phone", "location", "industry", "currentJobTitle", "currentCompany", "companySize");
		dbHandler.insert(people);
		people = new People("https://www.linkedin.com/in/salimerid", "first_name", "second_name", "email", "phone", "location", "industry", "currentJobTitle", "currentCompany", "companySize");
		dbHandler.insert(people);
		people = new People("https://www.linkedin.com/in/mahfuj-shikder", "first_name", "second_name", "email", "phone", "location", "industry", "currentJobTitle", "currentCompany", "companySize");
		dbHandler.insert(people);
		dbHandler.selectRows(6);
		
		people = new People("https://www.linkedin.com/in/abir-khan-940177182", "first_name", "second_name", "email", "phone", "location", "industry", "currentJobTitle", "currentCompany", "companySize");
		dbHandler.update(people, "link");
		
		System.out.println(dbHandler.selectAtIndex(0));
		//dbHandler.createNewTable();
		dbHandler.closeConnection();
		
	}

}
