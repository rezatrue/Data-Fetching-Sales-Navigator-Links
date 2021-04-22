package scrapper;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

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

public class CompanyConvert implements Parser {

	public String baseUrl = "https://www.linkedin.com/";	
	DbCompany localDb;
	
	public CompanyConvert() {
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
	
	private boolean writeToDb(Company com) {				
		//int num = MainController.prefs.getInt("unUpdatedListCount", 0);
		//MainController.prefs.putInt("unUpdatedListCount", (num + count));
		return localDb.update(com, com.getComUrl());
	}
	
	@Override
	public boolean parseData(int index) {
		
		Company company = localDb.selectAtIndex(index);
		if(company == null) return false;
		else {
			FireFoxOperator.driver.get(company.getComUrl());
			By aboutBtnBy = By.xpath("//a[contains(.,'About')]");
			FireFoxOperator.driver.findElement(aboutBtnBy).click();
			FireFoxOperator.fullPageScroll();
			// need scroll 
		}
			
		By titleBy = By.xpath("//h1[@title]");
		By websiteBy = By.xpath("//dd[preceding-sibling::dt[contains(.,'Website')]]/a[1]");
		By industryBy = By.xpath("//dd[preceding-sibling::dt[contains(.,'Industry')]][1]");
		By comSizeBy = By.xpath("//dd[preceding-sibling::dt[contains(.,'Company size')]][1]");
		By headquartersBy = By.xpath("//dd[preceding-sibling::dt[contains(.,'Headquarters')]][1]");
		By typeBy = By.xpath("//dd[preceding-sibling::dt[contains(.,'Type')]][1]");
		By foundedBy = By.xpath("//dd[preceding-sibling::dt[contains(.,'Founded')]][1]");
		
		
		try {
			String title = FireFoxOperator.driver.findElement(titleBy).getText();
			company.setComName(title);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			String website = FireFoxOperator.driver.findElement(websiteBy).getText();
			company.setComWebsite(website);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			String industry = FireFoxOperator.driver.findElement(industryBy).getText();
			company.setComIndustry(industry);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			String comSize = FireFoxOperator.driver.findElement(comSizeBy).getText();
			company.setComSize(comSize);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			String headquarters = FireFoxOperator.driver.findElement(headquartersBy).getText();
			company.setComHeadquarters(headquarters);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			String type = FireFoxOperator.driver.findElement(typeBy).getText();
			company.setComType(type);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			String founded = FireFoxOperator.driver.findElement(foundedBy).getText();
			company.setComFounded(founded);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		return writeToDb(company);
	}
	
	@Override
	public int parseList(){
		return 0;
	}

	@Override
	public int writeToDb(LinkedList<?> list) {
		return 0;
	}




	
	
}
