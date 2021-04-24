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

import csvhandler.CompanyScanner;
import csvhandler.CsvScanner;
import csvhandler.LeadScanner;
import pojo.Company;
import pojo.WorkType;
import scrapper.AccountConvert;
import scrapper.AccountList;
import scrapper.CompanyConvert;
import scrapper.CompanyList;
import scrapper.LeadConvert;
import scrapper.LeadList;
import scrapper.Parser;
import scrapper.SalesNavListsParser;
import scrapper.SalesNavigatorParser;

public class LeadOperator extends FireFoxOperator{

	private String type = "Lead results";
	private Parser parser = null; 
	private CsvScanner scanner = null; 
	
	public LeadOperator() {
		parser = new LeadList();
		scanner = new LeadScanner();
	}
	
	@Override
	public void setWorkType(WorkType mode) {
		if(mode == WorkType.LIST)
			parser = new LeadList();
		if(mode == WorkType.CONVERT)
			parser = new LeadConvert(); //
		
	}
	
	@Override
	public int scanCsv(String path) {
		LinkedList<?> list = scanner.dataScan(path);
		System.out.println("list -- "+ list.size());
		return parser.writeToDb(list);
	}
	
	/// ----------------------- List ---------------------------------- ///
	
	@Override
	public String checkPageStatus() {
		By pageElementBy = By.xpath("//a[contains(.,'"+ type +"') and contains(@class,'active')]");
		return isElementPresent(pageElementBy) ? "error:false" : "error: OPPS! You are in wrong page";
	}
	@Override
	public String takeList() {
		fullPageScroll();
		salesPageScroll();
		int count = parser.parseList();
		return "data:"+count;  //"page:10" // "error:msg"
	}
		
	@Override
	public int clearList() {
		parser.deleteAllData();
		return 0;
	}
	  
	@Override
	public int getTotalCounts() {
		return parser.getTotalCounts();
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
		By salesNavnextPageSelectorBy = (By.xpath("//button[contains(@class,'search-results__pagination-next-button')][child::span[contains(., 'Next')]]"));
		if(isElementPresent(salesNavnextPageSelectorBy) && driver.findElement(salesNavnextPageSelectorBy).isEnabled()) {
			responsepage = switchingPage(salesNavnextPageSelectorBy);
		}
		
		System.out.println(responsepage + " <- responsepage");

		return responsepage;

	}

	public int openPreviousPage() {

		int responsepage = 0;
		By salesNavPrevPageSelectorBy = (By.xpath("//button[contains(@class,'search-results__pagination-previous-button')][child::span[contains(., 'Previous')]]")); 
		if(isElementPresent(salesNavPrevPageSelectorBy) && driver.findElement(salesNavPrevPageSelectorBy).isEnabled()) {
			responsepage = switchingPage(salesNavPrevPageSelectorBy);
		}
		
		return responsepage;
	}
	
	@Override
	public int switchingPage(By by) {
		// driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		try {
			driver.findElement(by).click();
			System.out.println("---Page Switched-----");
			//driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			fullPageScroll();
			//salesPageScroll();
			
		} catch (NoSuchElementException e) {
			System.out.println(e.getMessage());;
		}
		
		System.out.println(currentPageNumber() + " <- currentPageNumber");

		return currentPageNumber();
	}
	
	public void salesPageScroll() {
		// https://stackoverflow.com/questions/42982950/how-to-scroll-down-the-page-till-bottomend-page-in-the-selenium-webdriver
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		try {
			jse.executeScript("scroll(0, 1450);");
			Thread.sleep(1000);
			jse.executeScript("scroll(0, 1950);");
			Thread.sleep(500);
			jse.executeScript("scroll(0, 2450);");
			Thread.sleep(500);
			jse.executeScript("scroll(0, 2950);");
			Thread.sleep(1000);
			jse.executeScript("scroll(0, 3450);");
			Thread.sleep(700);
			jse.executeScript("scroll(0, 4200);");
			Thread.sleep(1200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/// ----------------------- Convert ---------------------------------- ///	

	@Override
	public int getDetailsInfo(int index) {
		// TODO Auto-generated method stub
		return parser.parseData(index)? 1 : 0;
	}
	

	















}
