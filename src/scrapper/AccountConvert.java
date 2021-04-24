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
import db.DbPeople;
import pojo.Company;
import pojo.People;
import webhandler.FireFoxOperator;

public class AccountConvert implements Parser {

	public String baseUrl = "https://www.linkedin.com/";	
	DbCompany localDb;
	
	public AccountConvert() {
		localDb = new DbCompany();
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
	
	private boolean writeToDb(Company com, String selesUrl) {				
		//int num = MainController.prefs.getInt("unUpdatedListCount", 0);
		//MainController.prefs.putInt("unUpdatedListCount", (num + count));
		return localDb.update(com, selesUrl);
	}
	
	private String findComUrlInSourcePage() {
		// "flagshipCompanyUrl":"https://www.linkedin.com/company/nortal/",
		String txtMatch = "https://www.linkedin.com/company/";
		//String source = getSourseCode(); // not need to scroll
		String source = FireFoxOperator.driver.getPageSource().toString();
		if(source.contains(txtMatch)) {
			String fristSubString = source.substring(source.indexOf(txtMatch), source.length());
			String urltxt = fristSubString.substring(0, fristSubString.indexOf(",")-1);
			
			return (urltxt.contains(txtMatch)) ? urltxt : "";
		}
		
		return "";
	}
	
	private String salesComLinkTemp = "linkedin.com/sales/company";
	private String publicComLinkTemp = "linkedin.com/company";
	
	@Override
	public boolean parseData(int index) {
		
		Company company = localDb.selectAtIndex(index);
		String salesUrl = "";
		if(company == null) return false;
		else {
			salesUrl = company.getComUrl();
			if(salesUrl.contains(publicComLinkTemp)) return false;
			FireFoxOperator.driver.get(salesUrl);
			
		}
		
		By dropDownbtnBy = By.xpath("//div[contains(@class,'account-actions')]/div[2]/button");
		
		if(FireFoxOperator.waitUntillVisible(dropDownbtnBy)) {	
			try {
				FireFoxOperator.driver.findElement(dropDownbtnBy).click();
				String urltxt = findComUrlInSourcePage();
				System.out.println("url_com_txt : "+ urltxt);
				if(urltxt.contains(publicComLinkTemp)) company.setComUrl(urltxt);
			} catch (Exception e) {	return false; }
		}else 
			return false;
		//button[contains(.,'read more') or contains(.,'more details')]	
		By moreInfoBy = By.xpath("//button[contains(.,'read more') or contains(.,'more details')]");
		try {FireFoxOperator.driver.findElement(moreInfoBy).click();} 
		catch (Exception e) { 
			//NoSuchElementException 	
			return false;
		}
		
		By websiteBy = By.xpath("//dd[preceding-sibling::dt[contains(.,'Website')]]/a[1]");
		By typeBy = By.xpath("//dd[preceding-sibling::dt[contains(.,'Type')]][1]");
		By foundedBy = By.xpath("//dd[preceding-sibling::dt[contains(.,'Founded')]][1]");
		
		try {
			FireFoxOperator.driver.switchTo().activeElement(); //.findElement(By.xpath("//div[@role='dialog']"))
			WebElement elm = FireFoxOperator.driver.findElement(foundedBy);
		((JavascriptExecutor) FireFoxOperator.driver).executeScript("arguments[0].scrollIntoView(true);",elm);
		} catch (Exception e1) {}
		
		try {
			String website = FireFoxOperator.driver.findElement(websiteBy).getText();
			company.setComWebsite(website);
			System.out.println(website);
		} catch (Exception e1) {}
		
		try {
			String type = FireFoxOperator.driver.findElement(typeBy).getText();
			company.setComType(type);
			System.out.println(type);
		} catch (Exception e1) {}
		
		try {
			String founded = FireFoxOperator.driver.findElement(foundedBy).getText();
			company.setComFounded(founded);
			System.out.println(founded);
		} catch (Exception e1) {}

		By closeBY = By.xpath("//button[contains(@class,'ember-panel__dismiss')]");
		try {FireFoxOperator.driver.findElement(closeBY).click();} catch (Exception e1) {}
		FireFoxOperator.driver.switchTo().activeElement();
		
		return writeToDb(company, salesUrl);
	}
	
	@Override
	public int parseList(){
		return 0;
	}

	@Override
	public int writeToDb(LinkedList<?> list) {
		return 0;
	}




	
	
}
