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
import db.DbPeople;
import pojo.Company;
import pojo.People;
import webhandler.FireFoxOperator;

public class PeopleParser implements Parser {

	public String baseUrl = "https://www.linkedin.com/";	
	private LinkedList<People> peopleList = null;
	DbPeople localDb;
	
	public PeopleParser() {
		localDb = new DbPeople();
	}
	
	public int deleteAllData() {
		localDb.createNewTable();
		return 0;
	}
	
	private int writeToDb(LinkedList<People> parsedlist) {
		int count = 0;
		Iterator<People> it = peopleList.iterator();
		localDb = new DbPeople();
			while(it.hasNext()) {
				People people = (People) it.next();
				if(localDb.insert(people)) count++;
			}
			//int num = MainController.prefs.getInt("unUpdatedListCount", 0);
			//MainController.prefs.putInt("unUpdatedListCount", (num + count));
		return count;
	}
	
	public int parse(){
		
		peopleList = new LinkedList<>();
		String peopleXpath = "//div[contains(@class,'artdeco-card')]/ul//div[@class='entity-result__item']";
		String profileXpath = ".//span[contains(@class,'title')]/a";
		String nameXpath = ".//span[contains(@class,'title')]/a/span/span";
		String titleXpath = ".//div[contains(@class,'primary-subtitle')]";
		String locationXpath = ".//div[contains(@class,'secondary-subtitle')]";
		try {
			List<WebElement> lists = FireFoxOperator.driver.findElements(By.xpath(peopleXpath));
			Iterator<WebElement> it = lists.iterator();
			System.out.println(lists.size() + " : SIZE");
			while(it.hasNext()) {
				System.out.println("IN" + " : SIZE");
				People people = new People();
				WebElement peopleElement = it.next();
				String fname = null;
				String lname = null;
				try {
					String name = peopleElement.findElement(By.xpath(nameXpath)).getText().trim();
					System.out.println("Name : " + name);
					int index = name.lastIndexOf(" ");
					fname = name.substring(0, index);
					lname = name.substring(index, name.length());
				} catch (Exception e) {	e.printStackTrace();}
				
	
				String URL = "";
						try {
							URL = peopleElement.findElement(By.xpath(profileXpath)).getAttribute("href");
						} catch (Exception e) {	e.printStackTrace();}
				String title = "";
						try {
						title = peopleElement.findElement(By.xpath(titleXpath)).getText();
						} catch (Exception e) {	e.printStackTrace();}
				String location = "";
						try {
						location = peopleElement.findElement(By.xpath(locationXpath)).getText();
						} catch (Exception e) {	e.printStackTrace();}
				people.setFirstName(fname);
				people.setSecondName(lname);
				people.setLink(URL);
				people.setCurrentJobTitle(title);
				people.setLocation(location);
				peopleList.add(people);
			}
		} catch (Exception e) {	e.printStackTrace(); }
		return writeToDb(peopleList);
	}


	
	
}
