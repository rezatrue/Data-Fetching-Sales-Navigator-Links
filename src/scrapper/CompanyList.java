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
import db.DbPeople;
import pojo.Company;
import pojo.People;
import webhandler.FireFoxOperator;

public class CompanyList implements Parser {

	public String baseUrl = "https://www.linkedin.com/";	
	private LinkedList<Company> companyList = null;
	DbCompany localDb;
	
	public CompanyList() {
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
	@Override
	public int writeToDb(LinkedList<?> list) {
		LinkedList<Company> companyList = (LinkedList<Company>) list;
		int count = 0;
		Iterator<Company> it = companyList.iterator();
		localDb = new DbCompany();
			while(it.hasNext()) {
				Company company = (Company) it.next();
				if(localDb.insert(company)) count++;
			}
		int num = MainController.prefs.getInt("unUpdatedListCount", 0);
		MainController.prefs.putInt("unUpdatedListCount", (num + count));
		return count;
	}
	@Override
	public int parseList(){
		
		companyList = new LinkedList<>();
		String companyXpath = "//div[contains(@class,'artdeco-card')]/ul//div[@class='entity-result__item']";
		String nameXpath = ".//span[contains(@class,'title')]/a";
		String industryXpath = ".//div[contains(@class,'primary-subtitle')]";
		try {
			List<WebElement> lists = FireFoxOperator.driver.findElements(By.xpath(companyXpath));
			Iterator<WebElement> it = lists.iterator();
			System.out.println(lists.size() + " : SIZE");
			while(it.hasNext()) {
				System.out.println("IN" + " : SIZE");
				Company company = new Company();
				WebElement companyElement = it.next();
				String name = "";
				String URL = "";
				try {
					name = companyElement.findElement(By.xpath(nameXpath)).getText();
					name = name.replaceAll("[\\r\\n|\\r|\\n|\\s|\\t]", " ").trim();
					System.out.println("Name : " + name);
					URL = companyElement.findElement(By.xpath(nameXpath)).getAttribute("href");
					
				} catch (Exception e) {	e.printStackTrace();}
				
				String industry = "";
						try {
							industry = companyElement.findElement(By.xpath(industryXpath)).getText();
						} catch (Exception e) {	e.printStackTrace();}

				company.setComName(name);
				company.setComUrl(URL);
				company.setComIndustry(industry);
				companyList.add(company);
			}
		} catch (Exception e) {	e.printStackTrace(); }
		return writeToDb(companyList);
	}

	@Override
	public boolean parseData(int index) {
		// TODO Auto-generated method stub
		return false;
	}


	
	
}
