package webhandler;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator;

import pojo.Company;
import pojo.Info;
import scrapper.HtmlParser;
import scrapper.Parser;
import scrapper.SalesNavAccountsParser;
import scrapper.SalesNavListsParser;
import scrapper.SalesNavigatorParser;

public class AccountOperator extends FireFoxOperator{

	private Parser parser = null; 
	
	public AccountOperator() {
		parser = new SalesNavAccountsParser(); // changing
	}
	
	
	@Override
	public String checkPageStatus() {
		By pageElementBy = By.xpath("//a[contains(.,'Account results') and contains(@class,'active')]");
		return isElementPresent(pageElementBy) ? "error:false" : "error: OPPS! You are in wrong page";
	}
	@Override
	public String takeList() {
		fullPageScroll();
		salesPageScroll();
		int count = parser.parse();
		return "data:"+count;  //"page:10" // "error:msg"
	}
		
	@Override
	public int clearList() {
		parser.deleteAllData();
		return 0;
	}
	
	public int currentPageNumber() {
		String salesNavCurrentSelector = "//ol[@class='search-results__pagination-list']/li[contains(@class, 'selected')]"; 
		WebElement element;
		if (isElementPresent(By.xpath(salesNavCurrentSelector))) {
			element = driver.findElement(By.xpath(salesNavCurrentSelector));
			return Integer.parseInt(element.getText());
		} else
			return -1;
	}

	public int openNextPage() {
		System.out.println(" <- openNextPage clicked");
		int responsepage = 0;
		//String salesNavnextPageSelectorDisabled = "//button[contains(@class,'search-results__pagination-next-button')][contains(@disabled,'')][child::span[contains(., 'Next')]]";
		String salesNavnextPageSelectorDisabled = "//button[contains(@class,'search-results__pagination-next-button') and contains(@disabled,'')]";
		boolean isSalesDisabled = isElementPresent(By.xpath(salesNavnextPageSelectorDisabled));
		if(isSalesDisabled) return responsepage;
		
		String salesNavnextPageSelector = "//button[contains(@class,'search-results__pagination-next-button')][child::span[contains(., 'Next')]]";
		boolean isSalesNavPresent = isElementPresent(By.xpath(salesNavnextPageSelector));

		if (isSalesNavPresent) {
			responsepage = switchingPage(By.xpath(salesNavnextPageSelector));
		}
		System.out.println(responsepage + " <- responsepage");

		return responsepage;

	}

	public int openPreviousPage() {
		int responsepage = 0;
		String salesNavPreviousPageSelectorDisabled = "//button[contains(@class,'search-results__pagination-previous-button') and contains(@disabled,'')]";
		boolean isSalesDisabled = isElementPresent(By.xpath(salesNavPreviousPageSelectorDisabled));
		if(isSalesDisabled) return responsepage;
		
		String salesNavPrevPageSelector = "//button[contains(@class,'search-results__pagination-previous-button')][child::span[contains(., 'Previous')]]"; 
		boolean isSalesNavPresent = isElementPresent(By.xpath(salesNavPrevPageSelector));

		if (isSalesNavPresent) {
			responsepage = switchingPage(By.xpath(salesNavPrevPageSelector));
		}

		return responsepage;

	}
	@Override
	public int switchingPage(By by) {
		// driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		try {
			driver.findElement(by).click();
			fullPageScroll();
			salesPageScroll();
			
		} catch (NoSuchElementException e) {
			System.out.println(e.getMessage());;
		}
		
		System.out.println(currentPageNumber() + " <- currentPageNumber");

		return currentPageNumber();
	}
	
	public Company getCompanyLinkDetails(String salesComLink) {
		
		Company com = new Company();
		
		if(isLoginPage()) linkedinLogin();
		
		driver.get(salesComLink);
		/*
		By titleBy = By.xpath("//div[contains(@class,'basic-info')]//div[contains(@class,'title')][1]");
		By subtitleBy = By.xpath("//div[contains(@class,'basic-info')]//div[contains(@class,'subtitle')]/div");

		try {
			String name = driver.findElement(titleBy).getText();
			com.setComName(name);
		} catch (Exception e1) {}
		
		try {
			String txt = driver.findElement(subtitleBy).getText().replaceAll("\\s+", " ");
			System.out.println(txt);
			String industry = txt.subSequence(0, txt.indexOf("·")).toString().trim();
			com.setComIndustry(industry);
			System.out.println(industry + " : industry");
			String comSize = txt.substring(txt.indexOf("·")+1, txt.length()).trim();
			com.setComSize(comSize);
			System.out.println(comSize + " : comSize");
		} catch (Exception e1) {}
		*/
		String dropDownbtn = "//div[contains(@class,'account-actions')]/div[2]/button";
		if(findAndClick(dropDownbtn)) {
			try {
				String urltxt = findComUrlInSourcePage();
				System.out.println("url_com_txt : "+ urltxt);
				if(urltxt.contains("linkedin.com/company/")) com.setComUrl(urltxt);
			} catch (Exception e) {	}
		}
		
		By moreInfoBy = By.xpath("//button[contains(.,'read more')]");
		try {driver.findElement(moreInfoBy).click();} 
		catch (Exception e) { 
			//NoSuchElementException 	
			return com;
		}
		
		By websiteBy = By.xpath("//dd[preceding-sibling::dt[contains(.,'Website')]]/a[1]");
		By typeBy = By.xpath("//dd[preceding-sibling::dt[contains(.,'Type')]][1]");
		By foundedBy = By.xpath("//dd[preceding-sibling::dt[contains(.,'Founded')]][1]");
		
		try {
		driver.switchTo().activeElement(); //.findElement(By.xpath("//div[@role='dialog']"))
		WebElement elm = driver.findElement(foundedBy);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",elm);
		} catch (Exception e1) {}
		
		try {
			String website = driver.findElement(websiteBy).getText();
			com.setComWebsite(website);
			System.out.println(website);
		} catch (Exception e1) {}
		
		try {
			String type = driver.findElement(typeBy).getText();
			com.setComType(type);
			System.out.println(type);
		} catch (Exception e1) {}
		
		try {
			String founded = driver.findElement(foundedBy).getText();
			com.setComFounded(founded);
			System.out.println(founded);
		} catch (Exception e1) {}
		
		/*
		By headquartersBy = By.xpath("//dd[preceding-sibling::dt[contains(.,'Headquarters')]][1]");
		try {
			String headquarters = driver.findElement(headquartersBy).getText();
			com.setComHeadquarters(headquarters);
			System.out.println(headquarters);
		} catch (Exception e1) {}
		*/
		
		By closeBY = By.xpath("//button[contains(@class,'ember-panel__dismiss')]");
		try {driver.findElement(closeBY).click();} catch (Exception e1) {}
		driver.switchTo().activeElement();
		
		System.out.println("--done--");
		return com;
	}
	
	
	public String findUrlInSourcePage() {
		String txtMatch = "https://www.linkedin.com/in/";
		String source = getSourseCode();

		if(source.contains(txtMatch)) {
			String fristSubString = source.substring(source.indexOf(txtMatch), source.length());
			String urltxt = fristSubString.substring(0, fristSubString.indexOf(",")-1);
			
			return (urltxt.contains(txtMatch)) ? urltxt : "";
		}
		
		return "";
	}
	
	public String findComUrlInSourcePage() {
		// "flagshipCompanyUrl":"https://www.linkedin.com/company/nortal/",
		String txtMatch = "https://www.linkedin.com/company/";
		//String source = getSourseCode(); // not need to scroll
		String source = driver.getPageSource().toString();
		if(source.contains(txtMatch)) {
			String fristSubString = source.substring(source.indexOf(txtMatch), source.length());
			String urltxt = fristSubString.substring(0, fristSubString.indexOf(",")-1);
			
			return (urltxt.contains(txtMatch)) ? urltxt : "";
		}
		
		return "";
	}
	
	
	// Company about tab info
	public Company generalCompanyInfo(String link) {
		
		Company com = new Company();
		
		driver.get(link);
		By aboutBtnBy = By.xpath("//a[contains(.,'About')]");
		driver.findElement(aboutBtnBy).click();
		
		By titleBy = By.xpath("//h1[@title]");
		By websiteBy = By.xpath("//dd[preceding-sibling::dt[contains(.,'Website')]]/a[1]");
		By industryBy = By.xpath("//dd[preceding-sibling::dt[contains(.,'Industry')]][1]");
		By comSizeBy = By.xpath("//dd[preceding-sibling::dt[contains(.,'Company size')]][1]");
		By headquartersBy = By.xpath("//dd[preceding-sibling::dt[contains(.,'Headquarters')]][1]");
		By typeBy = By.xpath("//dd[preceding-sibling::dt[contains(.,'Type')]][1]");
		By foundedBy = By.xpath("//dd[preceding-sibling::dt[contains(.,'Founded')]][1]");
		
		com.setComUrl(link);
		
		try {
			String title = driver.findElement(titleBy).getText();
			com.setComName(title);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			String website = driver.findElement(websiteBy).getText();
			com.setComWebsite(website);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			String industry = driver.findElement(industryBy).getText();
			com.setComIndustry(industry);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			String comSize = driver.findElement(comSizeBy).getText();
			com.setComSize(comSize);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			String headquarters = driver.findElement(headquartersBy).getText();
			com.setComHeadquarters(headquarters);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			String type = driver.findElement(typeBy).getText();
			com.setComType(type);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			String founded = driver.findElement(foundedBy).getText();
			com.setComFounded(founded);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		return com;
	}






}
