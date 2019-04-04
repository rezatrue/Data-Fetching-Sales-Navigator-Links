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
import org.openqa.selenium.WebElement;

import webhandler.FireFoxOperator;

public class SalesNavAccountsParser extends Parser {

	public SalesNavAccountsParser() {
		super();
	}
	
	public LinkedList<Info> parse(){
		list = new LinkedList<Info>();
		String companiesXpath = "//ol[@class=\"search-results__result-list\"]/li//article";
		String companyNameXpath = ".//dt[@class=\"result-lockup__name\"]/a";
		String companyCategoryXpath = ".//dd/ul/li[@class=\"result-lockup__misc-item\"][1]";
		String companySizeXpath = ".//dd/ul/li[@class=\"result-lockup__misc-item\"][contains(., 'employee')]";
		String companyLocationXpath = ".//dd/ul/li[@class=\"result-lockup__misc-item\"][3]";
		
		List<WebElement> lists = FireFoxOperator.driver.findElements(By.xpath(companiesXpath));
		Iterator<WebElement> it = lists.iterator();
		System.out.println(lists.size() + " : SIZE");
		while(it.hasNext()) {
			System.out.println("IN" + " : SIZE");
			Info company = new Info();
			WebElement companyElement = it.next();
			String companyName = companyElement.findElement(By.xpath(companyNameXpath)).getText();
			System.out.println(companyName + " : companyName");

			String companyURL = companyElement.findElement(By.xpath(companyNameXpath)).getAttribute("href");
			String companyCategory = companyElement.findElement(By.xpath(companyCategoryXpath)).getText();
			String companySize = companyElement.findElement(By.xpath(companySizeXpath)).getText();
			String companyLocation = companyElement.findElement(By.xpath(companyLocationXpath)).getText();
			
			company.setCurrentCompany(commaSkiping(companyName));
			company.setLink(commaSkiping(companyURL));
			company.setIndustry(commaSkiping(companyCategory));
			company.setCompanySize(commaSkiping(companySize));
			company.setLocation(commaSkiping(companyLocation));
			list.add(company);
		}
		return list;
	}
	
	
}
