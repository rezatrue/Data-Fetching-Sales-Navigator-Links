package scrapper;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import application.MainController;
import pojo.Info;
import webhandler.FireFoxOperator;

public class HtmlParser implements Parser  {
	private String baseUrl = "https://www.linkedin.com/";
	private LinkedList<Info> proList = null;
	private String industries;
	
	public HtmlParser(){
	}
	
	private boolean isElementPresent(By by) {
		try {
			FireFoxOperator.driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	
	private void getInductry() {
		String industries = "";

		String industrySelector = "//li[contains(@class, 'search-s-facet--industry')]"; 
		String singleIndustrySelector = "//li/input[contains(@id, 'industry')]";
		boolean isindustryPresent = isElementPresent(By.xpath(industrySelector));
		if(isindustryPresent) {
			WebElement element = FireFoxOperator.driver.findElement(By.xpath(industrySelector));
			industries = element.getText();
			if(industries.matches("Industries.\\([2-9]\\)")) {
				industries = "";
				element.click();
				try {Thread.sleep(1000);					
				}catch (InterruptedException e) { e.printStackTrace();	}
				List<WebElement> lists = FireFoxOperator.driver.findElements(By.xpath(singleIndustrySelector));
				Iterator<WebElement> it = lists.iterator();
				WebElement webElement = it.next();
				//stackoverflow.com/questions/8187772/selenium-checkbox-attribute-checked
				//System.out.println(webElement.getAttribute("checked"));
				if(webElement.isSelected()) {
					industries = webElement.findElement(By.xpath("../label/p/span")).getText();
				}
				while(it.hasNext()) {
					webElement = it.next();
					//System.out.println(webElement.getAttribute("checked"));
					if(webElement.isSelected()) {
						industries = industries + ", " + webElement.findElement(By.xpath("../label/p/span")).getText();
					}
				}
				element.click();
			}
			System.out.println(industries + " : industries");
		}
		this.industries =  industries;
	}
	
	public LinkedList<Info> parse(){
		proList = new LinkedList<Info>();
		String employeeXpath = "//div[contains(@class,'blended-srp-results-js')]/ul[contains(@class,'search-results__list')]/li";
		String employeeNameXpath = ".//div[@class='search-result__wrapper']/div[contains(@class,'search-result__info')]/a[contains(@class,'search-result__result-link')]//span[@class='name actor-name']";
		String employeeProfileUrlXpath = ".//div[@class='search-result__wrapper']/div[contains(@class,'search-result__info')]/a[contains(@class,'search-result__result-link')]";
		String employeeLocationXpath = ".//div[@class='search-result__wrapper']/div[contains(@class,'search-result__info')]/p[contains(@class,'subline-level-2')]";
		String employeeJobTitleCompanyXpathP1 = ".//div[@class='search-result__wrapper']/div[contains(@class,'search-result__info')]/p[contains(@class,'search-result__snippets')]"; // 1st priority - if contains 'Current:' + company after 'at'
		String employeeJobTitleCompanyXpathP2 = ".//div[@class='search-result__wrapper']/div[contains(@class,'search-result__info')]/p[contains(@class,'subline-level-1')]"; // 2nd priority - if contains 'at'  + company after 'at'
		String employeeJobTitleCompanyXpathP3 = ""; // 3rd priority - if contains ','  + company after ','
		
		
		getInductry();
		
	try {
		List<WebElement> lists = FireFoxOperator.driver.findElements(By.xpath(employeeXpath));
		Iterator<WebElement> it = lists.iterator();
		while(it.hasNext()) {
			Info employee = new Info();
			WebElement employeeElement = it.next();
		try {
			String employeeName = employeeElement.findElement(By.xpath(employeeNameXpath)).getText();
			String employeeProfileUrl = employeeElement.findElement(By.xpath(employeeProfileUrlXpath)).getAttribute("href");
			employee.setLink(employeeProfileUrl);
	  		if (employeeName.contains(" ")) {
	  			String fname = employeeName.substring(0, employeeName.indexOf(' '));
	  			employee.setFirstName(fname);
	  			String lname = employeeName.substring(employeeName.indexOf(' ')+1, employeeName.length());
	  			employee.setSecondName(lname);
			} else {
				employee.setFirstName(employeeName);
			}
		} catch (Exception e) {	e.printStackTrace(); }
		
		try {
			String employeeLocation = employeeElement.findElement(By.xpath(employeeLocationXpath)).getText();
			employee.setLocation(employeeLocation);
		} catch (Exception e) {	e.printStackTrace(); }
		
		
		boolean titleCompanyFound = false;
		String start = "Current:";
		String devide = " at ";
		try {
			String JobTitleCompany = employeeElement.findElement(By.xpath(employeeJobTitleCompanyXpathP1)).getText();
			if(JobTitleCompany.contains(start) && JobTitleCompany.contains(devide)) {
				String jobTitle = JobTitleCompany.substring(start.length() + 1 , JobTitleCompany.indexOf(devide));
				String company = JobTitleCompany.substring(JobTitleCompany.indexOf(devide) + devide.length() , JobTitleCompany.length());
				employee.setCurrentJobTitle(jobTitle);
				employee.setCurrentCompany(company);
				titleCompanyFound = true;
			}
			
		} catch (Exception e) {	e.printStackTrace(); }
		
		if(!titleCompanyFound) {
			try {
				String JobTitleCompany = employeeElement.findElement(By.xpath(employeeJobTitleCompanyXpathP2)).getText();
				if(JobTitleCompany.contains(devide)) {
					String jobTitle = JobTitleCompany.substring(0 , JobTitleCompany.indexOf(devide));
					String company = JobTitleCompany.substring(JobTitleCompany.indexOf(devide) + devide.length() , JobTitleCompany.length());
					employee.setCurrentJobTitle(jobTitle);
					employee.setCurrentCompany(company);
					titleCompanyFound = true;
				}else {
					employee.setCurrentJobTitle(JobTitleCompany);
				}
			} catch (Exception e) {	e.printStackTrace(); }
		}

			employee.setIndustry(industries);
			System.out.println(employee.getFirstName() + " : "
					+ employee.getSecondName() + " : "
					+ employee.getLink() + " : "
					+ employee.getIndustry() + " : "
					+ employee.getCurrentJobTitle() + " : "
					+ employee.getLocation() + " : "
					+ employee.getCompanySize() + " : "
					+ employee.getCurrentCompany()
					);
			
			proList.add(employee);
		}
	} catch (Exception e) {	e.printStackTrace(); }
		
	return proList;
	}

	@Override
	public LinkedList<?> parse(String html) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
