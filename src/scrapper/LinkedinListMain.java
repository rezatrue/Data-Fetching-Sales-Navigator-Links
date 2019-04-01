package scrapper;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import db.DBHandler;
import db.LocalDBHandler;
import webhandler.FireFoxOperator;

public class LinkedinListMain {
	public LinkedList<Info> list = null;
	LocalDBHandler localDb;
	FireFoxOperator fireFoxOperator = new FireFoxOperator();
	// old private BrowserHandler browser = null ;
	private Parser parser = null;
	private DBHandler dbHandler = null;
	int listSize = 0;

	public LinkedinListMain() {
		list = new LinkedList<Info>();
		localDb = new LocalDBHandler();
		listSize = 0;
		// browser = new BrowserHandler(); // create issue as resource location
		// is not same in different PC
		parser = new NewHtmlParser(); // default selected
		dbHandler = new DBHandler(); // need to add param at last

	}

	public void setProfileMode(String type) {
		System.out.println(" --- " + type);
		parser = null;
		if (type.toLowerCase().contains("salesnavleads")) {
			parser = new SalesNavigatorParser();
			fireFoxOperator.setUrl("salesnavleads");
		} else if (type.toLowerCase().contains("salesnavaccounts")) {
			parser = new SalesNavAccountsParser();
			fireFoxOperator.setUrl("salesnavaccounts");
		} else {
			parser = new NewHtmlParser(); // default selected
			fireFoxOperator.setUrl("profilesearch");
		}

	}

	public String getInductry() {
		return fireFoxOperator.getInductry();
	}
	
	// modified 11 mar 2018
	public String searchItemOnPage() {
		return fireFoxOperator.currentPageStatus();
	}

	// modified 11 mar 2018
	public boolean login(String user, String password) {
		return fireFoxOperator.linkedinLogin(user, password);
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

	// modified 11 mar 2018
	public boolean signedOut() {
		return fireFoxOperator.signOut();
	}

	// modified 11 mar 2018
	public boolean closeBrowser() {
		return fireFoxOperator.closeBrowser();
	}

	// modified 11 mar 2018
	public int currentpage() {
		return fireFoxOperator.currentPageNumber();
	}

	// modified 11 mar 2018
	public int openNextPage() {
		return fireFoxOperator.openNextPage();
	}

	// modified 11 mar 2018
	public int openPreviousPage() {
		return fireFoxOperator.openPreviousPage();
	}

	public int getTotalSize() {
		return listSize;
	}

	// modified 28 July 2018
	public int takeList() {
		LinkedList<Info> currentlist = parser.parse(fireFoxOperator.getSourseCode());
		
		return addToDb(currentlist);
		/*
		LinkedList<Info> uniquelist = removeDuplicate(currentlist);
		if (uniquelist.size() > 0) {
			list.addAll(uniquelist);
			return uniquelist.size();
		} else {
			return 0;
		}
		*/
	}

	public int addToDb(LinkedList<Info> parsedlist) {
		int count = 0;
		ListIterator<Info> it = parsedlist.listIterator();
		while(it.hasNext()) {
			Info info = (Info) it.next();
			if(localDb.insert(info)) count++;
		}
		listSize += count; 
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
	
	public int printList(String keyword) {
		list = localDb.selectAll();
		CsvGenerator csv = new CsvGenerator();
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


}
