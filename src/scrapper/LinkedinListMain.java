package scrapper;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import application.MainController;
import db.DBHandler;
import db.DbCompany;
import db.DbProfile;
import db.LocalDBHandler;
import webhandler.FireFoxOperator;

public class LinkedinListMain {
	public LinkedList<?> list = null;
	LocalDBHandler localDb;
	CsvGenerator csv;
	private String workMode;
	private String taskType;
	FireFoxOperator fireFoxOperator = new FireFoxOperator();
	// old private BrowserHandler browser = null ;
	//private Parser parser = null; // 1
	private DBHandler dbHandler = null;
	int listSize = 0;
	//int unUpdatedListCount = 0;
	
	public LinkedinListMain() {
		this.workMode = "modelist";
		this.taskType = "";
		list = new LinkedList<Info>();
		localDb = new DbProfile();
		csv = new ProfileCsv();
		listSize = 0;
		// browser = new BrowserHandler(); // create issue as resource location
		// is not same in different PC
		//parser = new NewHtmlParser(); // default selected // 1
		dbHandler = new DBHandler(); // need to add param at last

	}

	public void setWorkMode(String mtype) {
		System.out.println("Mode: " + mtype);
		this.workMode = mtype;
	}
	public void setTaskType(String type) {
		System.out.println("Task: " + type);
		this.taskType = type;
	}
	
	public void setProfileMode(String type) { // 1
		fireFoxOperator.setProfileMode(type);
		
		if(type.equalsIgnoreCase("convert")) {	
			return;
		}
		list = new LinkedList<Info>();
		localDb = new DbProfile();
		csv = new ProfileCsv();
		
		if(type.equalsIgnoreCase("salesnavaccounts")) {			
			list = new LinkedList<Company>();
			localDb = new DbCompany();
			//list = localDb.selectRows(countData());
			csv = new CompanyCsv();
		}
		
	}

	// modified 11 mar 2018 // depricated in 12 Feb 21
	public String searchItemOnPage() {
		return fireFoxOperator.currentPageStatus();
	}

	// modified 13 Feb 2021
	public boolean login() {
		return fireFoxOperator.linkedinLogin();
	}

	// modified 11 mar 2018
	public boolean search(String keyword) {
		return fireFoxOperator.linkedinSearch(keyword);
	}

	// modified 11 mar 2018
	public boolean launcherBrowser() {
		fireFoxOperator = new FireFoxOperator();
		return fireFoxOperator.browserLauncher();

	}

	
	private String salesLinkTemp = "linkedin.com/sales";
	private String publicLinkTemp = "linkedin.com/in";
	
	public int getPublicLinkDetails(int index) {
		
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
	
	public int getCompanyLinkDetails(int index) {
		
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
	
	public void fullPageScroll() {
		fireFoxOperator.fullPageScroll();
	}
	public void salesPageScroll() {
		fireFoxOperator.salesPageScroll();
	}
	// modified 11 mar 2018
	public int openPreviousPage() {
		return fireFoxOperator.openPreviousPage();
	}

	public int getTotalSize() {
		return listSize;
	}

	// modified 28 July 2018
	public int takeList() { // 1
		LinkedList<?> currentlist = fireFoxOperator.takeList();
		return addToDb(currentlist);
	}

	public int addToDb(LinkedList<?> parsedlist) {
		int count = 0;
		ListIterator<?> it = parsedlist.listIterator();
		if(localDb instanceof DbProfile) {
			while(it.hasNext()) {
				Info info = (Info) it.next();
				if(localDb.insert(info)) count++;
			}			
		}
		if(localDb instanceof DbCompany) {
			while(it.hasNext()) {
				Company com = (Company) it.next();
				if(localDb.insert(com)) count++;
			}			
		}
		listSize += count;
		int num = MainController.prefs.getInt("unUpdatedListCount", 0);
		MainController.prefs.putInt("unUpdatedListCount", (num + count));
		return count;
	}
	
	/*
	// comparing links only, this might make the process slow
	public LinkedList<Info> removeDuplicate(LinkedList<Info> parsedlist) {
		LinkedList<Info> uniquelist = new LinkedList<Info>();
		ListIterator<Info> it = parsedlist.listIterator();
		while (it.hasNext()) {
			Info info = (Info) it.next();
			String newLink = info.getLink();
			System.out.println(newLink);
			ListIterator<Info> mainit = list.listIterator();
			boolean add = true;
			while (mainit.hasNext()) {
				String link = (String) mainit.next().getLink();
				if (link.equals(newLink)) {
					add = false;
					System.out.println((add ? "New" : "Duplicate") + "---" + link);
					break;
				}
			}
			if (add)
				uniquelist.add(info);
		}
		System.out.println("unique list size -- " + uniquelist.size());

		return uniquelist;
	}
	*/

	public int clearList() {
		if (localDb.createNewTable()) { // drop & create table
			listSize = 0;
			return 0;
		} else {
			return -1;
		}
	}
	
	public int countData() {
		return localDb.countRecords();
	}
	
	public int printList(String keyword, int num) {
		
		list = localDb.selectRows(num);
		int number = csv.listtoCsv(keyword, list);
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
		CSV_Scanner csv_Scanner = new CSV_Scanner();
		
//		list = csv_Scanner.dataScan(filepath);
//		return list.size();
		return addToDb(csv_Scanner.dataScan(filepath));
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
