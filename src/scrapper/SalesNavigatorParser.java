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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import pojo.Info;
import webhandler.FireFoxOperator;

public class SalesNavigatorParser implements Parser {
	private String baseUrl = "https://www.linkedin.com/";
	private LinkedList<Info> proList = null;
	private String industries;
	private String companySize;
	
	public SalesNavigatorParser(){
	}
	
	private void getCommonData() {
		String companyCategoryXpath = "//li/div/div[child::div/div/label[contains(text(),'Industry')]]/ul/li";

		try {
			List<WebElement> companyCategoryElements = FireFoxOperator.driver.findElements(By.xpath(companyCategoryXpath));
			Iterator it = companyCategoryElements.iterator();
			this.industries = ((WebElement)it.next()).getText();
			while(it.hasNext()) {
				this.industries += (", " +((WebElement)it.next()).getText());
			}
//			String companyCategory = FireFoxOperator.driver.findElement(By.xpath(companyCategoryXpath)).getText();
//			this.industries = companyCategory;
		} catch (Exception e) {	e.printStackTrace(); }
		
		String companySizeXpath = "//li/div/div[child::div/div/label[contains(text(),'Company headcount')]]/ul/li/span/span";

		try {
			List<WebElement> companySizeElements = FireFoxOperator.driver.findElements(By.xpath(companySizeXpath));
			Iterator it = companySizeElements.iterator();
			this.companySize = ((WebElement)it.next()).getText();
			while(it.hasNext()) {
				this.companySize += (", " +((WebElement)it.next()).getText());
			}
			//String companySize = FireFoxOperator.driver.findElement(By.xpath(companySizeXpath)).getText();
			//this.companySize = companySize;
		} catch (Exception e) {	e.printStackTrace(); }
		
	}
	
	
	
	public LinkedList<?> parseList(){
		
		proList = new LinkedList<Info>();
		String employeeXpath = "//ol[@class='search-results__result-list']//li//div[contains(@class,'horizontal-person-entity-lockup')]";
		String employeeNameXpath = ".//dt[@class='result-lockup__name']/a";
		String companyNameXpath = ".//dd[@class='result-lockup__highlight-keyword']/span/span[@class='result-lockup__position-company']/a/span[1]";
		String employeeJobTitleXpath = ".//dd[@class='result-lockup__highlight-keyword']/span[1]";
		String employeeLocationXpath = ".//ul/li[@class='result-lockup__misc-item']";
		
		getCommonData();
		
		
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
	  			employee.setFirstName(fname);
	  			String lname = employeeName.substring(employeeName.indexOf(' ')+1, employeeName.length());
	  			employee.setSecondName(lname);
	  			System.out.println("Frist name :- "+ fname + " Last name :- "+ lname );
			} else {
				employee.setFirstName(employeeName);
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
