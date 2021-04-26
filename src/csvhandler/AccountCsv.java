package csvhandler;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;

import db.DbAccount;
import db.LocalDBHandler;
import pojo.Account;

public class AccountCsv implements CsvGenerator{
	private LinkedList<Account> clist;
	private DbAccount dbAccount;
	
	public AccountCsv() {
		this.clist = new LinkedList<>();
		dbAccount = new DbAccount();
	}

	public int listtoCsv(String keyword, int num) {
		System.out.println("number " + num);
		clist = dbAccount.selectRows(num); 
		int count = 0;
		
		System.out.println("list size cg: " + clist.size());
		
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Calendar cal = Calendar.getInstance();
		String fileName = dateFormat.format(cal.getTime());

		FileWriter writer = null;
		try {
			writer = new FileWriter("Lin_Acc_" + keyword + "_list_" + fileName + ".csv");

			writer.append("Linkedin_Company_URL");
			writer.append(",");
			writer.append("Company_Name");
			writer.append(",");
			writer.append("Headquarters");
			writer.append(",");
			writer.append("Website");
			writer.append(",");
			writer.append("Founded");
			writer.append(",");
			writer.append("Company_Size");
			writer.append(",");
			writer.append("Industry");
			writer.append(",");
			writer.append("Company_Type");
			writer.append(",");
			writer.append("\n");

			System.out.println(" -- out size-- "+ clist.size());
			if(clist.size() > 0) {
				Iterator<Account> it = clist.iterator();
	
				while (it.hasNext()) {
					Account acc = it.next();
					System.out.println(" --data -- "+ 
							acc.getComUrl() + " getLink " + 
							acc.getComName() + " getName ");

					writer.append(commaSkiping(acc.getComUrl()));
					writer.append(",");
					writer.append(commaSkiping(acc.getComName()));
					writer.append(",");
					writer.append(commaSkiping(acc.getComHeadquarters()));
					writer.append(",");
					writer.append(commaSkiping(acc.getComWebsite()));
					writer.append(",");
					writer.append(commaSkiping(acc.getComFounded()));
					writer.append(",");
					writer.append(commaSkiping(acc.getComSize()));
					writer.append(",");
					writer.append(commaSkiping(acc.getComIndustry()));
					writer.append(",");
					writer.append(commaSkiping(acc.getComType()));
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
