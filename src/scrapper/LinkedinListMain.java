package scrapper;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import application.MainController;
import csvhandler.CsvScanner;
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
import webhandler.CompaniesOperator;
import webhandler.FireFoxOperator;
import webhandler.PeopleOperator;

public class LinkedinListMain {
	CsvGenerator csvGenerator;
	private WorkType workMode;
	private SearchType taskType;
	private FireFoxOperator fireFoxOperator;
	private DBHandler dbHandler = null; // server api
	//int unUpdatedListCount = 0;
	
	public LinkedinListMain() {
		dbHandler = new DBHandler(); // need to add param at last
		this.workMode = WorkType.LIST;
		this.taskType = SearchType.PEOPLESEARCH;
		setWorkType();
	}

	// setting working object
	private int setWorkType() {
		if(taskType == SearchType.PEOPLESEARCH)
			fireFoxOperator = new PeopleOperator();
		if(taskType == SearchType.COMPANIESSEARCH)
			fireFoxOperator = new CompaniesOperator();
		if(taskType == SearchType.ACCOUNTSEARCH) 
			fireFoxOperator = new AccountOperator();
		fireFoxOperator.setWorkType(this.workMode);
		return fireFoxOperator.getTotalCounts();
	}
	
	public int setWorkMode(WorkType mtype) {
		System.out.println("Mode: " + mtype.toString());
		this.workMode = mtype;
		return setTaskType(this.taskType);
	}
	public int setTaskType(SearchType type) {
		System.out.println("Task: " + type.toString());
		this.taskType = type;
		return setWorkType();
	}
	
	public String getTaskType() {
		return this.taskType.toString();
	}
	
	// modified 13 Feb 2021
	public boolean login() {
		return fireFoxOperator.linkedinLogin();
	}

	// modified 11 mar 2018
	public boolean launcherBrowser() {
		return fireFoxOperator.browserLauncher();
	}

	public int clearList() {
		return fireFoxOperator.clearList();  // drop & create table
	}
	
	public int getLinkDetails(int index) {		
		return fireFoxOperator.getDetailsInfo(index);
	}
	
////.............................................	

	
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
		// "data:10" // "error:msg"
	}
	
	// modified 11 mar 2018
	public int openPreviousPage() {
		return fireFoxOperator.openPreviousPage();
	}

	public int countData() {
		return fireFoxOperator.getTotalCounts();
	}
	
	public int printList(String keyword, int renum) {
		
		if(taskType == SearchType.PEOPLESEARCH)
			csvGenerator = new PeopleCsv();
		if(taskType == SearchType.COMPANIESSEARCH)
			csvGenerator = new CompanyCsv();
		if(taskType == SearchType.ACCOUNTSEARCH)
			csvGenerator = new CompanyCsv();
		
		int number = csvGenerator.listtoCsv(keyword, renum);
		return number;
	}

	public String userAuthCheck(String user, String password) {
		return dbHandler.userAuth(user, password);
	}


	
}
