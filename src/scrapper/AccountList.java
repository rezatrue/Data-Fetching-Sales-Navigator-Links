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
import db.DbAccount;
import pojo.Account;
import pojo.People;
import webhandler.FireFoxOperator;

public class AccountList implements Parser {

	public String baseUrl = "https://www.linkedin.com/";	
	private LinkedList<Account> accountList = null;
	DbAccount localDb;
	
	public AccountList() {
		localDb = new DbAccount();
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
		LinkedList<Account> accountList = (LinkedList<Account>) list;
		int count = 0;
		Iterator<Account> it = accountList.iterator();
		localDb = new DbAccount();
			while(it.hasNext()) {
				Account account = (Account) it.next();
				if(localDb.insert(account)) count++;
			}
		int num = MainController.prefs.getInt("unUpdatedListCount", 0);
		MainController.prefs.putInt("unUpdatedListCount", (num + count));
		return count;
	}
	@Override
	public int parseList(){
		
		accountList = new LinkedList<>();

		String companyXpath = "//ol/li[contains(@class,'search-results__result-item')]";
		String nameXpath = ".//dt[@class='result-lockup__name']/a";
		String industryXpath = ".//ul[contains(@class,'result-lockup__misc-list')]/li[1]";
		String sizeXpath = ".//ul[contains(@class,'result-lockup__misc-list')]/li[contains(., 'employee')]";
		String addressXpath = ".//ul[contains(@class,'result-lockup__misc-list')]/li[3]";
		try {
			List<WebElement> lists = FireFoxOperator.driver.findElements(By.xpath(companyXpath));
			Iterator<WebElement> it = lists.iterator();
			System.out.println(lists.size() + " : SIZE");
			while(it.hasNext()) {
				System.out.println("IN" + " : SIZE");
				Account account = new Account();
				WebElement accountElement = it.next();
				String name = "";
				String URL = "";
				try {
					name = accountElement.findElement(By.xpath(nameXpath)).getText();
					//name = name.replaceAll("[\\r\\n|\\r|\\n|\\s|\\t]", " ").trim();
					System.out.println("Name : " + name);
					URL = accountElement.findElement(By.xpath(nameXpath)).getAttribute("href");
					
				} catch (Exception e) {	e.printStackTrace();}
				
				String industry = "";
						try {
							industry = accountElement.findElement(By.xpath(industryXpath)).getText();
						} catch (Exception e) {	e.printStackTrace();}
				String size = "";
				try {
					size = accountElement.findElement(By.xpath(sizeXpath)).getText();
				} catch (Exception e) {	e.printStackTrace();}
				String address = "";
				try {
					address = accountElement.findElement(By.xpath(addressXpath)).getText();
				} catch (Exception e) {	e.printStackTrace();}
				account.setComName(name);
				account.setComUrl(URL);
				account.setComIndustry(industry);
				account.setComSize(size);
				account.setComHeadquarters(address);
				accountList.add(account);
			}
		} catch (Exception e) {	e.printStackTrace(); }
		return writeToDb(accountList);
	}

	@Override
	public boolean parseData(int index) {
		// TODO right now we don't need it in Account list
		return false;
	}


	
	
}
