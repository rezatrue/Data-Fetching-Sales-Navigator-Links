package csvhandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import db.DbAccount;
import pojo.Account;

public class AccountScanner extends CsvScanner{

	private DbAccount db = null;
	public AccountScanner() {
		super();
		db = new DbAccount();
	}
	
	private LinkedList<Account> list = null;

	public LinkedList<Account> dataScan(String filePath) {
		return list;
	}
	
	public int transferDataCsvToDb(String filePath) {
		location = filePath.substring(0, filePath.lastIndexOf("\\")+1);
		Account account = null;
		Scanner scanner = null;
		int insertCount = 0;
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
					account = new Account(removingQuotes(line.get(0)), removingQuotes(line.get(1)), removingQuotes(line.get(2)), removingQuotes(line.get(3)),
							removingQuotes(line.get(4)), removingQuotes(line.get(5)), removingQuotes(line.get(6)), removingQuotes(line.get(7)));
					insertCount += db.insert(account) ? 1 : 0;
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
		return insertCount;
	}

}
