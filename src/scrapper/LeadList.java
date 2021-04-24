package scrapper;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

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

public class LeadList implements Parser {

	public String baseUrl = "https://www.linkedin.com/";	
	private LinkedList<Lead> leadList = null;
	DbLead localDb;
	
	public LeadList() {
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
		int num = MainController.prefs.getInt("unUpdatedListCount", 0);
		MainController.prefs.putInt("unUpdatedListCount", (num + count));
		return count;
	}
	@Override
	public int parseList(){
		
		leadList = new LinkedList<>();

		String leadXpath = "//ol/li[contains(@class,'search-results__result-item')]";
		String nameXpath = ".//dt[@class='result-lockup__name']/a";
		String addressXpath = ".//ul[contains(@class,'result-lockup__misc-list')]/li[1]";
		String jobTitleXpath = ".//dd[contains(@class,'highlight-keyword')]/span[1]";
		String durationXpath = ".//dd[3]";
		String companyNameXpath = ".//span[contains(@class,'position-company')]/a/span[1]";
		String companyProfileXpath = ".//span[contains(@class,'position-company')]/a";

		try {
			List<WebElement> lists = FireFoxOperator.driver.findElements(By.xpath(leadXpath));
			Iterator<WebElement> it = lists.iterator();
			System.out.println(lists.size() + " : SIZE");
			while(it.hasNext()) {
				System.out.println("IN" + " : SIZE");
				Lead lead = new Lead();
				WebElement leadElement = it.next();
				String fname = null;
				String lname = null;
				String URL = "";
				try {
					//name = name.replaceAll("[\\r\\n|\\r|\\n|\\s|\\t]", " ").trim();
					String name = leadElement.findElement(By.xpath(nameXpath)).getText().trim();
					int index = name.lastIndexOf(" ");
					fname = name.substring(0, index);
					lname = name.substring(index, name.length());
					URL = leadElement.findElement(By.xpath(nameXpath)).getAttribute("href");
				} catch (Exception e) {	System.out.println(e.getMessage());}
				
				
				String address = "";
				try {
					address = leadElement.findElement(By.xpath(addressXpath)).getText();
				} catch (Exception e) {	System.out.println(e.getMessage());}

				String jobTitle = "";
						try {
							jobTitle = leadElement.findElement(By.xpath(jobTitleXpath)).getText();
						} catch (Exception e) {	System.out.println(e.getMessage());}
				
				String duration = "";
				try {
					duration = leadElement.findElement(By.xpath(durationXpath)).getText();
					System.out.println("duration : " + duration);
				} catch (Exception e) {	System.out.println(e.getMessage());}
				String companyName = "";
				try {
					companyName = leadElement.findElement(By.xpath(companyNameXpath)).getText();
					System.out.println("companyName : " + companyName);
				} catch (Exception e) {	System.out.println(e.getMessage());}
				String companyProfileUrl = "";
				try {
					companyProfileUrl = leadElement.findElement(By.xpath(companyProfileXpath)).getAttribute("href");
					System.out.println("companyProfileUrl : " + companyProfileUrl);
				} catch (Exception e) {	System.out.println(e.getMessage());}
				
				lead.setLink(URL);
				lead.setFirstName(fname);
				lead.setSecondName(lname);
				lead.setLocation(address);
				lead.setCurrentJobTitle(jobTitle);
				lead.setCurrentCompany(companyName);
				lead.setCompanySize(companyProfileUrl);
				lead.setIndustry(duration);
				leadList.add(lead);
			}
		} catch (Exception e) {	e.printStackTrace(); }
		return writeToDb(leadList);
	}

	@Override
	public boolean parseData(int index) {
		// TODO right now we don't need it in Account list
		return false;
	}


	
	
}
