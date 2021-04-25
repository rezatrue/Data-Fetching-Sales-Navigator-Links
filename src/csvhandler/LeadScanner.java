package csvhandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import pojo.Lead;

public class LeadScanner extends CsvScanner{


	public LeadScanner() {
		super();
	}
	
	
	private LinkedList<Lead> list = null;

	public LinkedList<Lead> dataScan(String filePath) {
		location = filePath.substring(0, filePath.lastIndexOf("\\")+1);
		list = new LinkedList<>();
		Lead lead = null;
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(filePath));
			boolean rightFormat = false;
			if (scanner.hasNext()) {
				List<String> line = parseLine(scanner.nextLine());

				if ("Linkedin_Profile_URL" != line.get(0).toString() || "First_Name" != line.get(1).toString()
						|| "Last_Name" != line.get(2).toString() || "Address" != line.get(3).toString()
						|| "Service_Age" != line.get(4).toString() || "Designation" != line.get(5).toString()
						|| "Company_Name" != line.get(5).toString() || "Company_Profile" != line.get(7).toString()) {
					System.out.println("WRONG FILE");
					rightFormat = true;
				}

			}
			if (!scanner.hasNext())
				System.out.println("EMPTY FILE");
			
			if(rightFormat) {
				while (scanner.hasNext()) {
					List<String> line = parseLine(scanner.nextLine());
					lead = new Lead(removingQuotes(line.get(0)), removingQuotes(line.get(1)), removingQuotes(line.get(2)), removingQuotes(line.get(3)),
							removingQuotes(line.get(4)), removingQuotes(line.get(5)), removingQuotes(line.get(6)), removingQuotes(line.get(7)) );
					list.add(lead);
					System.out.println("[Linkedin_Profile_URL= " + line.get(0) + ", First_Name= " + line.get(1)
							+ " , Last_Name=" + line.get(2) + " , Address=" + line.get(3) + ", Service_Age= " + line.get(4) + ", Designation= "
							+ line.get(5) + " , Company_Name=" + line.get(6) + ", Company_Profile= " + line.get(7) + "]");
	
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
