package scrapper;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import application.MainController;
import db.DbCompany;
import db.DbLead;
import db.DbPeople;
import pojo.Company;
import pojo.Lead;
import pojo.People;
import webhandler.FireFoxOperator;

public class LeadConvert implements Parser {

	public String baseUrl = "https://www.linkedin.com/";	
	DbLead localDb;
	
	public LeadConvert() {
		localDb = new DbLead();
	}
	
	@Override
	public int getTotalCounts() {
		return localDb.countRecords();
	}
	
	@Override
	public int deleteAllData() {
		localDb.createNewTable();
		return 0;
	}
	
	private boolean writeToDb(Lead lead, String selesUrl) {				
		int num = MainController.prefs.getInt("unUpdatedListCount", 0);
		MainController.prefs.putInt("unUpdatedListCount", (num + 1));
		return localDb.update(lead, selesUrl);
	}
	
	private String findPeopleUrlInSourcePage() {
		String txtMatch = "https://www.linkedin.com/in/";
		String source = FireFoxOperator.driver.getPageSource().toString();

		if(source.contains(txtMatch)) {
			String fristSubString = source.substring(source.indexOf(txtMatch), source.length());
			String urltxt = fristSubString.substring(0, fristSubString.indexOf(",")-1);
			
			return (urltxt.contains(txtMatch)) ? urltxt : "";
		}
		
		return "";
	}
	
	private String salesLinkTemp = "linkedin.com/sales";
	private String publicLinkTemp = "linkedin.com/in";
	
	@Override
	public boolean parseData(int index) {
		
		Lead lead = localDb.selectAtIndex(index);
		String salesUrl = "";
		if(lead == null) return false;
		else {
			salesUrl = lead.getLink();
			if(salesUrl.contains(publicLinkTemp)) return false;
			FireFoxOperator.driver.get(salesUrl);
			
		}
		
		//div[contains(@class,'artdeco-dropdown')]/button[contains(@class,'circle')]
		By dropDownbtnBy = By.xpath("//div[contains(@class,'artdeco-dropdown')]/button[contains(@class,'tertiary')]");
		
		if(FireFoxOperator.waitUntillVisible(dropDownbtnBy)) {	
			try {
				FireFoxOperator.driver.findElement(dropDownbtnBy).click();
				String urltxt = findPeopleUrlInSourcePage();
				System.out.println("url_com_txt : "+ urltxt);
				if(urltxt.contains(publicLinkTemp)) lead.setLink(urltxt);
			} catch (Exception e) {	return false; }
		}else 
			return false;
		
		return writeToDb(lead, salesUrl);
	}
	
	@Override
	public int parseList(){
		return 0;
	}

	@Override
	public int writeToDb(LinkedList<?> list) {
		LinkedList<Lead> leadList = (LinkedList<Lead>) list;
		int count = 0;
		Iterator<Lead> it = leadList.iterator();
		localDb = new DbLead();
			while(it.hasNext()) {
				Lead lead = (Lead) it.next();
				if(localDb.insert(lead)) count++;
			}
		return count;

	}




	
	
}
