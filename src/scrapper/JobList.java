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
import db.DbJob;
import db.DbPeople;
import pojo.Company;
import pojo.Job;
import pojo.People;
import webhandler.FireFoxOperator;

public class JobList implements Parser {

	public String baseUrl = "https://www.linkedin.com/";	
	private LinkedList<Job> jobList = null;
	DbJob localDb;
	
	public JobList() {
		localDb = new DbJob();
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
		LinkedList<Job> jobList = (LinkedList<Job>) list;
		int count = 0;
		Iterator<Job> it = jobList.iterator();
		localDb = new DbJob();
			while(it.hasNext()) {
				Job job = (Job) it.next();
				if(localDb.insert(job)) count++;
			}
		int num = MainController.prefs.getInt("unUpdatedListCount", 0);
		MainController.prefs.putInt("unUpdatedListCount", (num + count));
		return count;
	}
	@Override
	public int parseList(){
		jobList = new LinkedList<>();
		By listXpathBy = By.xpath("//ul[contains(@class,'jobs-search-results__list')]/li");
		By jobTitleXpathBy = By.xpath(".//div[contains(@class,'content')]/div[contains(@class,'title')][1]");
		By jobLinkXpathBy = By.xpath(".//div[contains(@class,'content')]/div[contains(@class,'title')][1]/a");
		By companyXpathBy = By.xpath(".//div[contains(@class,'content')]/div[contains(@class,'title')][2]");
		By companyLinkXpathBy = By.xpath(".//div[contains(@class,'content')]/div[contains(@class,'title')][2]/a");
		By locationXpathBy = By.xpath(".//div[contains(@class,'content')]/div[3]");
		
		int countElement = 0;
		boolean continueLooping = true;
		do {
		List<WebElement> lists = FireFoxOperator.driver.findElements(listXpathBy);
		System.out.println( "SIZE: " + lists.size());
		if(countElement < lists.size()) {
			countElement = lists.size();
			FireFoxOperator.scrollUpToWebElement(By.xpath("//ul[contains(@class,'jobs-search-results__list')]/li["+countElement+"]"));
		}
		if(countElement >= lists.size()) continueLooping = false;
		}while(continueLooping);
		

		try {
			List<WebElement> lists = FireFoxOperator.driver.findElements(listXpathBy);
			Iterator<WebElement> it = lists.iterator();
			System.out.println(lists.size() + " : SIZE");
			while(it.hasNext()) {
				System.out.println("IN" + " : List item");
				Job job = new Job();
				WebElement jobElement = it.next();
				String jobLink = "";
				String jobTitle = "";
				try {
					jobTitle = jobElement.findElement(jobTitleXpathBy).getText();
					jobLink = jobElement.findElement(jobLinkXpathBy).getAttribute("href");
					System.out.println("job Title: " + jobTitle);
					System.out.println("job Link: " + jobLink);
				} catch (Exception e) {	e.printStackTrace();}
				String company = "";
				String companyLink = "";
				try {
					company = jobElement.findElement(companyXpathBy).getText();
					companyLink = jobElement.findElement(companyLinkXpathBy).getAttribute("href");
					System.out.println("Company: " + company);
					System.out.println("Link: " + companyLink);
				} catch (Exception e) {	e.printStackTrace();}
				String location = "";
				try {
					location = jobElement.findElement(locationXpathBy).getText();
					System.out.println("location: " + location);
				} catch (Exception e) {	e.printStackTrace();}
				job.setJobLink(jobLink);
				job.setJobTitle(jobTitle);
				job.setCompany(company);
				job.setCompanyLink(companyLink);
				job.setLocation(location);
				
				jobList.add(job);
			}
		} catch (Exception e) {	e.printStackTrace(); }
		return writeToDb(jobList);
		
	}

	@Override
	public boolean parseData(int index) {
		// TODO Auto-generated method stub
		return false;
	}


	
	
}
