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

import pojo.Info;
import webhandler.FireFoxOperator;
// only take name & link for conversion

public class SalesNavListsParser implements Parser {
	private String baseUrl = "https://www.linkedin.com/";
	private LinkedList<Info> proList = null;
	
	
	public SalesNavListsParser() {
	}
	
	
	public LinkedList<Info> parse(){
		proList = new LinkedList<Info>();
		
		String employeeXpath = "//tbody[@class=\"ember-view\"]/tr[contains(@class,\"table-row\")]";
		String employeeNamePlinkXpath = ".//a[contains(@class,'view-profile-name-link')]";
		String employeeLocationXpath = ".//td[contains(@class,'geography')]";
		/*
		String employeeJobTitleXpath = "";
		String companyNameXpath = "";
		*/
		
		
		try {
			List<WebElement> lists = FireFoxOperator.driver.findElements(By.xpath(employeeXpath));
			Iterator<WebElement> it = lists.iterator();
			while(it.hasNext()) {
				Info employee = new Info();
				WebElement employeeElement = it.next();
			try {
				String employeeName = employeeElement.findElement(By.xpath(employeeNamePlinkXpath)).getText();
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
				String employeeProfileURL = employeeElement.findElement(By.xpath(employeeNamePlinkXpath)).getAttribute("href");
				employee.setLink(employeeProfileURL);
	  		} catch (Exception e) {	e.printStackTrace(); }
	  		
	  		try {
				String employeeLocation = employeeElement.findElement(By.xpath(employeeLocationXpath)).getText();
				employee.setLocation(employeeLocation);
			} catch (Exception e) {	e.printStackTrace(); }
	  		/*
	  		try {
				String employeeJobTitle = employeeElement.findElement(By.xpath(employeeJobTitleXpath)).getText();
				employee.setCurrentJobTitle(employeeJobTitle);
			} catch (Exception e) {	e.printStackTrace(); }
	  		
			try {
				String companyName = employeeElement.findElement(By.xpath(companyNameXpath)).getText();
				employee.setCurrentCompany(companyName);
			} catch (Exception e) {	e.printStackTrace(); }
			*/
			
			
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
