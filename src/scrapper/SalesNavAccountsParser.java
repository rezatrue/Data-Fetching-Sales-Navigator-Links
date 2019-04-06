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

import webhandler.FireFoxOperator;

public class SalesNavAccountsParser extends Parser {

	public SalesNavAccountsParser() {
		super();
	}
	
	
	public LinkedList<Info> parse(){
		list = new LinkedList<Info>();
		String companiesXpath = "//ol[@class=\"search-results__result-list\"]//li//article";
		String companyNameXpath = ".//dt[@class=\"result-lockup__name\"]/a";
		String companyCategoryXpath = ".//dd/ul/li[@class=\"result-lockup__misc-item\"][1]";
		String companySizeXpath = ".//dd/ul/li[@class=\"result-lockup__misc-item\"][contains(., 'employee')]";
		String companyLocationXpath = ".//dd/ul/li[@class=\"result-lockup__misc-item\"][3]";
		try {
			List<WebElement> lists = FireFoxOperator.driver.findElements(By.xpath(companiesXpath));
			Iterator<WebElement> it = lists.iterator();
			System.out.println(lists.size() + " : SIZE");
			while(it.hasNext()) {
				System.out.println("IN" + " : SIZE");
				Info company = new Info();
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
				company.setCurrentCompany(companyName);
				company.setLink(companyURL);
				company.setIndustry(companyCategory);
				company.setCompanySize(companySize);
				company.setLocation(companyLocation);
				list.add(company);
			}
		} catch (Exception e) {	e.printStackTrace(); }
		return list;
	}
	
	
}
