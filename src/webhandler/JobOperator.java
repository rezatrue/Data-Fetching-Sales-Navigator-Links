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

import csvhandler.CsvScanner;
import csvhandler.JobScanner;
import db.LocalDBHandler;
import pojo.Company;
import pojo.People;
import pojo.SearchType;
import pojo.WorkType;
import scrapper.JobConvert;
import scrapper.JobList;
import scrapper.Parser;
import scrapper.PeopleConvert;
import scrapper.PeopleList;


public class JobOperator extends FireFoxOperator{
	
	private String type = "Jobs";
	private Parser parser = null; 
	private CsvScanner scanner = null; 
	
	public JobOperator() {
		parser = new JobList();
		scanner = new JobScanner();
	}
	
	public void setWorkType(WorkType mode) {
		if(mode == WorkType.LIST)
			parser = new JobList();
		if(mode == WorkType.CONVERT)
			parser = new JobConvert();
	}
	
	public int scanCsv(String path) {
		LinkedList<?> list = scanner.dataScan(path);
		System.out.println("list -- "+ list.size());
		return parser.writeToDb(list);
	}
	
/// ----------------------- List ---------------------------------- ///	
	@Override
	public String checkPageStatus() {
		//button[contains(@class,'selected') and contains(., 'Jobs')]
		By pageElementBy = By.xpath("//button[contains(@class,'selected') and contains(., '"+ type +"')]");
		return isElementPresent(pageElementBy) ? "error:false" : "error:OPPS! Wrong page, You are not in "+ SearchType.JOBSEARCH;
	}
	
	@Override
	public String takeList() {
		
		By listXpathBy = By.xpath("//ul[contains(@class,'jobs-search-results__list')]/li/div");
		
		int countElement = 0;
		boolean continueLooping = true;
		do {
		List<WebElement> lists = FireFoxOperator.driver.findElements(listXpathBy);
		System.out.println( "current size: " + lists.size());
		if(countElement < lists.size()) {
			countElement = lists.size();
			FireFoxOperator.scrollUpToWebElement(By.xpath("//ul[contains(@class,'jobs-search-results__list')]/li["+countElement+"]"));
		}else
			continueLooping = false;
		}while(continueLooping);
		
		int count = parser.parseList();
		return "data:"+count;  //"data:10" // "error:msg"
		
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
		System.out.println(" <<-- currentPageNumber -->>");
		By currentSelectorBy = By.xpath("//button[contains(@aria-label,'current page')]/span[1]"); 
		WebElement element;
		if (isElementPresent(currentSelectorBy)) {
			//FireFoxOperator.scrollUpToWebElement(currentSelectorBy);
			element = driver.findElement(currentSelectorBy);
			return Integer.parseInt(element.getText());
		} else
			return -1;
	}

	public int openNextPage() {
		//button[contains(@class,'button--next')]
		System.out.println(" <<-- openNextPage clicked");
		int responsepage = 0;
		
		int currentPage = currentPageNumber();
		if(currentPage <= 0) return responsepage;
		
		int nextPageNumber = currentPage + 1;
		System.out.println(nextPageNumber);
		By nextPageSelectorBy = By.xpath("//li/button[contains(.,'"+ nextPageNumber +"')]");
		
		//By nextPageSelectorBy = By.xpath("//li[preceding-sibling::li/button/span[contains(.,'+ currentPage +')]][1]/button/span[1]");
		if(isElementPresent(nextPageSelectorBy)) {
			responsepage = switchingPage(nextPageSelectorBy);
		}else {
			responsepage = switchingPage(By.xpath("//li[preceding-sibling::li/button[contains(.,'"+currentPage+"')]]/button[contains(.,'…')]"));
		}
		
		return responsepage;

	}

	public int openPreviousPage() {
		int responsepage = 0;
		
		int currentPage = currentPageNumber();
		if(currentPage <= 0) return responsepage;
		
		int prevtPageNumber = currentPage - 1;
		System.out.println(prevtPageNumber);
		By prevPageSelectorBy = By.xpath("//li/button[contains(.,'"+ prevtPageNumber +"')]");
		if(isElementPresent(prevPageSelectorBy)) {
			responsepage = switchingPage(prevPageSelectorBy);
		}else {
			responsepage = switchingPage(By.xpath("//li[following-sibling::li/button[contains(.,'1')]]/button[contains(.,'…')]"));
		}
		

		return responsepage;

	}
	@Override
	public int switchingPage(By by) {
		// driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		try {
			FireFoxOperator.scrollUpToWebElement(by);
			driver.findElement(by).click();			
		} catch (NoSuchElementException e) {
			System.out.println(e.getMessage());;
		}
		
		System.out.println(currentPageNumber() + " <- currentPageNumber");

		return currentPageNumber();
	}
	
/// ----------------------- Convert ---------------------------------- ///	
	
	public int getDetailsInfo(int index) {
		return 0;

	}





}
