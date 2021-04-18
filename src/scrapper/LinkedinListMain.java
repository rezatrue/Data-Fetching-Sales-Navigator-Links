package scrapper;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import application.MainController;
import csvhandler.CSV_Scanner;
import csvhandler.CompanyCsv;
import csvhandler.CsvGenerator;
import csvhandler.PeopleCsv;
import db.DBHandler;
import db.DbCompany;
import db.DbPeople;
import db.LocalDBHandler;
import pojo.SearchType;
import pojo.WorkType;
import pojo.Company;
import webhandler.AccountOperator;
import webhandler.FireFoxOperator;
import webhandler.PeopleOperator;

public class LinkedinListMain {
	CsvGenerator csvGenerator;
	private WorkType workMode;
	private SearchType taskType;
	private FireFoxOperator fireFoxOperator;
	private DBHandler dbHandler = null; // server api
	int listSize = 0;
	//int unUpdatedListCount = 0;
	
	public LinkedinListMain() {
		dbHandler = new DBHandler(); // need to add param at last
		this.workMode = WorkType.LIST;
		this.taskType = SearchType.PEOPLESEARCH;
		listSize = 0;
		setWorkType();
	}

	// setting working object
	private void setWorkType() {
		if(workMode == WorkType.LIST && taskType == SearchType.PEOPLESEARCH)
			fireFoxOperator = new PeopleOperator();
		if(workMode == WorkType.LIST && taskType == SearchType.ACCOUNTSEARCH)
			fireFoxOperator = new AccountOperator();
	}
	
	public void setWorkMode(WorkType mtype) {
		System.out.println("Mode: " + mtype.toString());
		this.workMode = mtype;
		setWorkType();
	}
	public void setTaskType(SearchType type) {
		System.out.println("Task: " + type.toString());
		this.taskType = type;
		setWorkType();
	}
	
	// modified 13 Feb 2021
	public boolean login() {
		return fireFoxOperator.linkedinLogin();
	}

	// modified 11 mar 2018
	public boolean launcherBrowser() {
		return fireFoxOperator.browserLauncher();
	}

	
	private String salesLinkTemp = "linkedin.com/sales";
	private String publicLinkTemp = "linkedin.com/in";
	
	// ata ki?
	public int getPublicLinkDetails(int index) {
		// dummy
		LocalDBHandler localDb = null;
		//String salesLink = list.get(index).getLink();
		String salesLink = localDb.selectAtIndex(index);
		System.out.println("salesLink : " + salesLink);
		if (salesLink.contains(publicLinkTemp)) return 0;
		if (salesLink.contains(salesLinkTemp)) {
			Info newInfo = fireFoxOperator.getPublicLinkDetails(salesLink);
			String publicProfileLink = newInfo.getLink();
			System.out.println("set in list -->>" + publicProfileLink);
			if(publicProfileLink == null || publicProfileLink.length() == 0){
				System.out.println("return -->> -1");
				return 0;
			}
			if(newInfo.getLink().contains("linkedin.com/in")) {
				//converted += 1;
				//list.set(index, newInfo);
				localDb.update(newInfo, salesLink);
				return 1;
			}else {
				return 0;
			}
		}
		return 0;

	}
	
	private String salesComLinkTemp = "linkedin.com/sales/company";
	private String publicComLinkTemp = "linkedin.com/company";
	
	// ata ki?
	public int getCompanyLinkDetails(int index) {
		// dummy
		LocalDBHandler localDb = null;
		String salesComLink = localDb.selectAtIndex(index);
		
		System.out.println("salesComLink : " + salesComLink);
		if (salesComLink.contains(publicComLinkTemp)) return 0;
		if (salesComLink.contains(salesComLinkTemp)) {
			Company newCompany = fireFoxOperator.getCompanyLinkDetails(salesComLink); 
			/*
			String publicCompanyLink = newCompany.getComUrl();
			System.out.println("set in list -->>" + publicCompanyLink);
			if(publicCompanyLink == null || publicCompanyLink.length() == 0){
			*/
			if(newCompany == null){
				System.out.println("return -->> -1");
				return 0;
			}
			System.out.println(newCompany.getComUrl() + " -- :: --");
			if(newCompany.getComUrl() != null && newCompany.getComUrl().contains(publicComLinkTemp)) {
				//converted += 1;
				//list.set(index, newInfo);
				localDb.update(newCompany, salesComLink);
				return 1;
			}else {
				return 0;
			}
		}
		return 0;

	}
	
	
	
	// modified 11 mar 2018
	public boolean signedOut() {
		return fireFoxOperator.signOut();
	}

	// modified 11 mar 2018
	public boolean closeBrowser() {
		return fireFoxOperator.closeBrowser();
	}

	// modified 27 Jan 2021
	public int currentpage() {
		return fireFoxOperator.currentPageNumber();
	}

	// modified 27 Jan 2021
	public int openNextPage() {
		return fireFoxOperator.openNextPage();
	}
	
	public String startListing() {

		String msg = fireFoxOperator.checkPageStatus();
		if(!msg.contains("false")) return msg;
		return fireFoxOperator.takeList();
		// "data:10" // "page:10" // "error:msg"
	}
	
	// modified 11 mar 2018
	public int openPreviousPage() {
		return fireFoxOperator.openPreviousPage();
	}

	public int getTotalSize() {
		return listSize;
	}

	public int clearList() {
		return fireFoxOperator.clearList();  // drop & create table
	}
	
	public int countData() {
		//return localDb.countRecords();
		return 0;
	}
	
	public int printList(String keyword, int num) {
		if(workMode == WorkType.LIST && taskType == SearchType.ACCOUNTSEARCH)
			csvGenerator = new CompanyCsv();
		int number = csvGenerator.listtoCsv(keyword, num);
		return number;
	}

	public String userAuthCheck(String user, String password) {
		return dbHandler.userAuth(user, password);
	}

	private LinkedList<Info> upLoadedList = null;

	public String scanCSV(String filePath) {
		upLoadedList = new LinkedList<>();
		CSV_Scanner csv_Scanner = new CSV_Scanner();
		if (filePath.endsWith(".csv")) {
			upLoadedList = csv_Scanner.dataScan(filePath);
			return "listsize " + upLoadedList.size();
		} else
			return "ERROR !!! : It's not a CSV file";

		// possible errors: file is not properly formated
		// wrong file
		// null file / no data
	}
	
	//.....
	public int readCsvFile(String filepath) {
		//CSV_Scanner csv_Scanner = new CSV_Scanner();
		
//		list = csv_Scanner.dataScan(filepath);
//		return list.size();
		//return addToDb(csv_Scanner.dataScan(filepath));
		return 0;
	}

	
	/*
	public int numberOfSalesLink() {
		//converted = 0;
		int totalSalesLink = 0;
		for (Info info : list) {
			totalSalesLink += info.getLink().contains(salesLinkTemp)? 1 : 0;
		}
		return totalSalesLink;
	}
	*/
	
}
