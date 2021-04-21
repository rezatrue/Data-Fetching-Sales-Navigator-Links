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
import pojo.Company;
import webhandler.FireFoxOperator;

public class SalesNavAccountsParser implements Parser {

	public String baseUrl = "https://www.linkedin.com/";	
	private LinkedList<Company> comList = null;
	DbCompany localDb;
	
	public SalesNavAccountsParser() {
		localDb = new DbCompany();
	}
	
	public int deleteAllData() {
		localDb.createNewTable();
		return 0;
	}
	
	private int writeToDb(LinkedList<Company> parsedlist) {
		int count = 0;
		Iterator<Company> it = parsedlist.iterator();
		localDb = new DbCompany();
			while(it.hasNext()) {
				Company com = (Company) it.next();
				if(localDb.insert(com)) count++;
			}
			int num = MainController.prefs.getInt("unUpdatedListCount", 0);
			MainController.prefs.putInt("unUpdatedListCount", (num + count));
		return count;
	}
	
	public int parseList(){
		
		comList = new LinkedList<>();
		String companiesXpath = "//ol[@class='search-results__result-list']//li//article";
		String companyNameXpath = ".//dt[@class='result-lockup__name']/a";
		String companyCategoryXpath = ".//dd/ul/li[@class='result-lockup__misc-item'][1]";
		String companySizeXpath = ".//dd/ul/li[@class='result-lockup__misc-item'][contains(., 'employee')]";
		String companyLocationXpath = ".//dd/ul/li[@class='result-lockup__misc-item'][3]";
		try {
			List<WebElement> lists = FireFoxOperator.driver.findElements(By.xpath(companiesXpath));
			Iterator<WebElement> it = lists.iterator();
			System.out.println(lists.size() + " : SIZE");
			while(it.hasNext()) {
				System.out.println("IN" + " : SIZE");
				Company company = new Company();
				WebElement companyElement = it.next();
				String companyName = null;
				try {
					companyName = companyElement.findElement(By.xpath(companyNameXpath)).getText();
				} catch (Exception e) {	e.printStackTrace();}
				System.out.println(companyName + " : companyName");
	
				String companyURL = "";
						try {
							companyURL = companyElement.findElement(By.xpath(companyNameXpath)).getAttribute("href");
						} catch (Exception e) {	e.printStackTrace();}
				String companyCategory = "";
						try {
						companyCategory = companyElement.findElement(By.xpath(companyCategoryXpath)).getText();
						} catch (Exception e) {	e.printStackTrace();}
				String companySize = "";			
						try {
						companySize = companyElement.findElement(By.xpath(companySizeXpath)).getText();
						} catch (Exception e) {	e.printStackTrace();}
				String companyLocation = "";
						try {
						companyLocation = companyElement.findElement(By.xpath(companyLocationXpath)).getText();
						} catch (Exception e) {	e.printStackTrace();}
				company.setComName(companyName);
				company.setComUrl(companyURL);
				company.setComIndustry(companyCategory);
				company.setComSize(companySize);
				company.setComHeadquarters(companyLocation);
				comList.add(company);
			}
		} catch (Exception e) {	e.printStackTrace(); }
		return writeToDb(comList);
	}



	
}
