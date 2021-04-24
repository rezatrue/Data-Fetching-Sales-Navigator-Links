package csvhandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import pojo.Company;
import pojo.People;

public class CompanyScanner extends CsvScanner{


	public CompanyScanner() {
		super();
	}
	
	
	private LinkedList<Company> list = null;

	public LinkedList<Company> dataScan(String filePath) {
		location = filePath.substring(0, filePath.lastIndexOf("\\")+1);
		list = new LinkedList<>();
		Company company = null;
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(filePath));
			boolean rightFormat = false;
			if (scanner.hasNext()) {
				List<String> line = parseLine(scanner.nextLine());

				
				if ("Linkedin_Company_URL" != line.get(0).toString() || "Company_Name" != line.get(1).toString()
						|| "Headquarters" != line.get(2).toString() || "Website" != line.get(3).toString()
						|| "Founded" != line.get(4).toString() || "Company_Size" != line.get(5).toString()
						|| "Industry" != line.get(6).toString() || "Company_Type" != line.get(7).toString()) {
					System.out.println("WRONG FILE");
					rightFormat = true;
				}

			}
			if (!scanner.hasNext())
				System.out.println("EMPTY FILE");
			
			if(rightFormat) {
				while (scanner.hasNext()) {
					List<String> line = parseLine(scanner.nextLine());
					company = new Company(removingQuotes(line.get(0)), removingQuotes(line.get(1)), removingQuotes(line.get(2)), removingQuotes(line.get(3)),
							removingQuotes(line.get(4)), removingQuotes(line.get(5)), removingQuotes(line.get(6)), removingQuotes(line.get(7)));
					list.add(company);
					System.out.println("[Linkedin_Company_URL= " + line.get(0) + ", Company_Name= " + line.get(1)
							+ " , Headquarters=" + line.get(2) + ", Website= " + line.get(3) + ", Founded= "
							+ line.get(4) + " , Company_Size=" + line.get(5) + ", Industry= " + line.get(6) + ", Company_Type= "
							+ line.get(7) + "]");
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
