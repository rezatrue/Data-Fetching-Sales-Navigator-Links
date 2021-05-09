package csvhandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import pojo.People;

public class PeopleScanner extends CsvScanner{


	public PeopleScanner() {
		super();
	}
	
	
	private LinkedList<People> list = null;

	public LinkedList<People> dataScan(String filePath) {
		location = filePath.substring(0, filePath.lastIndexOf("\\")+1);
		list = new LinkedList<>();
		People people = null;
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(filePath));
			boolean rightFormat = false;
			if (scanner.hasNext()) {
				List<String> line = parseLine(scanner.nextLine());

				if ("Linkedin_Profile_URL" != line.get(0).toString() || "First_Name" != line.get(1).toString()
						|| "Last_Name" != line.get(2).toString() || "Email_ID" != line.get(3).toString()
						|| "Address" != line.get(4).toString() || "Designation" != line.get(5).toString()
						|| "Service_Range" != line.get(6).toString() || "Company" != line.get(7).toString()
						|| "Location" != line.get(8).toString() || "Degree_Name" != line.get(9).toString()
						|| "FOS" != line.get(10).toString() || "Institute" != line.get(11).toString()
						|| "Dates" != line.get(12).toString()) {
					System.out.println("WRONG FILE");
					rightFormat = true;
				}

			}
			if (!scanner.hasNext())
				System.out.println("EMPTY FILE");
			
			if(rightFormat) {
				while (scanner.hasNext()) {
					List<String> line = parseLine(scanner.nextLine());
					people = new People(removingQuotes(line.get(0)), removingQuotes(line.get(1)), removingQuotes(line.get(2)), removingQuotes(line.get(3)),
							removingQuotes(line.get(4)), removingQuotes(line.get(5)), removingQuotes(line.get(6)), removingQuotes(line.get(7)), 
							removingQuotes(line.get(8)), removingQuotes(line.get(9)), removingQuotes(line.get(10)), removingQuotes(line.get(11)),
							removingQuotes(line.get(12)));
					list.add(people);
					System.out.println("[Linkedin_Profile_URL= " + line.get(0) + ", First_Name= " + line.get(1)
							+ " , Last_Name=" + line.get(2)+ "]");
	
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		scanner.close();
		return list;
	}

}
