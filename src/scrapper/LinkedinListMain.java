package scrapper;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import db.DBHandler;
import webhandler.FireFoxOperator;

public class LinkedinListMain {
	private LinkedList<Info> list = null;
	FireFoxOperator fireFoxOperator = new FireFoxOperator();
	// old private BrowserHandler browser = null ;
	private Parser parser = null;
	private DBHandler dbHandler = null;

	public LinkedinListMain() {
		list = new LinkedList<Info>();
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
		return list.size();
	}

	// modified 11 mar 2018
	public int takeList() {
		LinkedList<Info> currentlist = parser.parse(fireFoxOperator.getSourseCode());
		LinkedList<Info> uniquelist = removeDuplicate(currentlist);

		if (uniquelist.size() > 0) {
			list.addAll(uniquelist);
			return uniquelist.size();
		} else {
			return 0;
		}
	}

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

	public int clearList() {
		list.removeAll(list);
		if (list.size() == 0) {
			return list.size();
		} else {
			return -1;
		}
	}

	public int printList(String keyword) {
		CsvGenerator csv = new CsvGenerator();
		csv.listtoCsv(list, keyword);
		return list.size();
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

	public String getPublicLink(int count) {
		Iterator<Info> it = upLoadedList.iterator();
		int newCount = 0;
		while (it.hasNext()) {
			String link = it.next().getLink();
			if (link.contains("linkedin.com/sales")) {
				upLoadedList.get(newCount).setLink(fireFoxOperator.getPublicLink(link));
				System.out.println(upLoadedList.get(newCount).getLink());
			} else {
				count++;
			}
			newCount++;
			if (newCount > count)
				break;
		}

		CsvGenerator csv = new CsvGenerator();
		String feetback = csv.listtoCsv(upLoadedList, "(" + count + ")-Public-links");
		String msg = (feetback.toLowerCase().contains("error")) ? "Unable to create new file"
				: "New CSV file is created, it has " + (newCount - 1) + " public links";
		return msg;
	}

}
