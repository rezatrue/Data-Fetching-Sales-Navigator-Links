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

import csvhandler.AccountScanner;
import csvhandler.CsvScanner;
import pojo.Company;
import pojo.SearchType;
import pojo.WorkType;
import scrapper.AccountConvert;
import scrapper.AccountList;
import scrapper.CompanyConvert;
import scrapper.CompanyList;
import scrapper.Parser;

public class AccountOperator extends FireFoxOperator{

	private String type = "Account results"; //
	private Parser parser = null; 
	private CsvScanner scanner = null; 
	
	public AccountOperator() {
		parser = new AccountList();
		scanner = new AccountScanner();
	}
	
	@Override
	public void setWorkType(WorkType mode) {
		if(mode == WorkType.LIST)
			parser = new AccountList();
		if(mode == WorkType.CONVERT)
			parser = new AccountConvert();
		
	}
	
	@Override
	public int scanCsv(String path) {
//		LinkedList<?> list = scanner.dataScan(path);
//		System.out.println("list -- "+ list.size());
//		return parser.writeToDb(list);
		return scanner.transferDataCsvToDb(path);
	}
	
	/// ----------------------- List ---------------------------------- ///
	
	@Override
	public String checkPageStatus() {
		By pageElementBy = By.xpath("//a[contains(.,'"+ type +"') and contains(@class,'active')]");
		return isElementPresent(pageElementBy) ? "error:false" : "error: OPPS! Wrong page, Not in " + SearchType.ACCOUNTSEARCH.toString();
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
		/*
		String salesNavnextPageSelectorDisabled = "//button[contains(@class,'search-results__pagination-next-button') and contains(@disabled,'')]";
		boolean isSalesDisabled = isElementPresent(By.xpath(salesNavnextPageSelectorDisabled));
		if(isSalesDisabled) return responsepage;
		
		String salesNavnextPageSelector = "//button[contains(@class,'search-results__pagination-next-button')][child::span[contains(., 'Next')]]";
		boolean isSalesNavPresent = isElementPresent(By.xpath(salesNavnextPageSelector));

		
		if (isSalesNavPresent) {
			responsepage = switchingPage(By.xpath(salesNavnextPageSelector));
		}
		*/
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
		/*
		String salesNavPreviousPageSelectorDisabled = "//button[contains(@class,'search-results__pagination-previous-button') and contains(@disabled,'')]";
		boolean isSalesDisabled = isElementPresent(By.xpath(salesNavPreviousPageSelectorDisabled));
		if(isSalesDisabled) return responsepage;
		
		String salesNavPrevPageSelector = "//button[contains(@class,'search-results__pagination-previous-button')][child::span[contains(., 'Previous')]]"; 
		boolean isSalesNavPresent = isElementPresent(By.xpath(salesNavPrevPageSelector));

		if (isSalesNavPresent) {
			responsepage = switchingPage(By.xpath(salesNavPrevPageSelector));
		}
		*/
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
			//driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
			fullPageScroll();
			//salesPageScroll();
			
		} catch (NoSuchElementException e) {
			System.out.println(e.getMessage());;
		}
		
		System.out.println(currentPageNumber() + " <- currentPageNumber");

		return currentPageNumber();
	}
	
	/// ----------------------- Convert ---------------------------------- ///	

	@Override
	public int getDetailsInfo(int index) {
		// TODO Auto-generated method stub
		return parser.parseData(index)? 1 : 0;
	}
	
	
	// will be needed for Lead covertion
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
	















}
