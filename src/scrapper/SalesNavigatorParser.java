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
import org.openqa.selenium.WebElement;

import webhandler.FireFoxOperator;

public class SalesNavigatorParser extends Parser {
//	public String baseUrl = "https://www.linkedin.com/";
//	public LinkedList<Info> list = null;
	private String industries;
	private String companySize;
	private boolean common;
	
	public SalesNavigatorParser(){
		super();
		common = false;
	}
	
	private void getCommonData() {
		
		try {
			String companyCategoryXpath = "//li/div/div[child::div/div/label[contains(text(),'Industry')]]/ul/li";
			String companyCategory = FireFoxOperator.driver.findElement(By.xpath(companyCategoryXpath)).getText();
			this.industries = companyCategory;
		} catch (Exception e) {	e.printStackTrace(); }
		
		try {
			String companySizeXpath = "//li/div/div[child::div/div/label[contains(text(),'Company headcount')]]/ul/li";
			String companySize = FireFoxOperator.driver.findElement(By.xpath(companySizeXpath)).getText();
			this.companySize = companySize;
		} catch (Exception e) {	e.printStackTrace(); }
		
	}
	
	public LinkedList<Info> parse(){
		list = new LinkedList<Info>();
		String employeeXpath = "//ol[@class='search-results__result-list']/li//div[contains(@class,'horizontal-person-entity-lockup')]";
		String employeeNameXpath = ".//dt[@class='result-lockup__name']/a";
		String companyNameXpath = ".//dd[@class='result-lockup__highlight-keyword']/span/span[@class='result-lockup__position-company']/a/span[1]";
		String employeeJobTitleXpath = ".//dd[@class='result-lockup__highlight-keyword']/span[1]";
		String employeeLocationXpath = ".//ul/li[@class='result-lockup__misc-item']";
		
		if(!common) {
			getCommonData();
			common= true;
		}
		
	try {
		List<WebElement> lists = FireFoxOperator.driver.findElements(By.xpath(employeeXpath));
		Iterator<WebElement> it = lists.iterator();
		while(it.hasNext()) {
			Info employee = new Info();
			WebElement employeeElement = it.next();
		try {
			String employeeName = employeeElement.findElement(By.xpath(employeeNameXpath)).getText();
	  		if (employeeName.contains(" ")) {
	  			String fname = employeeName.substring(0, employeeName.indexOf(' '));
	  			employee.setFirstName(commaSkiping(fname));
	  			String lname = employeeName.substring(employeeName.indexOf(' ')+1, employeeName.length());
	  			employee.setSecondName(commaSkiping(lname));
	  			System.out.println("Frist name :- "+ fname + " Last name :- "+ lname );
			} else {
				employee.setFirstName(commaSkiping(employeeName));
			}
		} catch (Exception e) {	e.printStackTrace(); }
		
  		try {
			String employeeProfileURL = employeeElement.findElement(By.xpath(employeeNameXpath)).getAttribute("href");
			employee.setLink(employeeProfileURL);
  		} catch (Exception e) {	e.printStackTrace(); }
		try {
			String companyName = employeeElement.findElement(By.xpath(companyNameXpath)).getText();
			employee.setCurrentCompany(companyName);
		} catch (Exception e) {	e.printStackTrace(); }
		try {
			String employeeJobTitle = employeeElement.findElement(By.xpath(employeeJobTitleXpath)).getText();
			employee.setCurrentJobTitle(employeeJobTitle);
		} catch (Exception e) {	e.printStackTrace(); }
		try {
			String employeeLocation = employeeElement.findElement(By.xpath(employeeLocationXpath)).getText();
			employee.setLocation(employeeLocation);
		} catch (Exception e) {	e.printStackTrace(); }	
			employee.setIndustry(industries);
			employee.setCompanySize(companySize);
			list.add(employee);
		}
	} catch (Exception e) {	e.printStackTrace(); }
		
	return list;
	}
	
	
	
}
