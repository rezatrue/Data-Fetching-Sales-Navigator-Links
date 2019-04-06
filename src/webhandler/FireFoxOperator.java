package webhandler;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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

import scrapper.Info;
import scrapper.HtmlParser;
import scrapper.Parser;
import scrapper.SalesNavAccountsParser;
import scrapper.SalesNavigatorParser;

public class FireFoxOperator {

	private String profileName = "default";
	private String geckodriverdir;

	public static WebDriver driver = null;
	private String url = "https://www.linkedin.com/";
	private String salesNavUrl = "https://www.linkedin.com/sales/settings?trk=nav_user_menu_manage_sales_nav";
	private String selesnavsignouturl = "https://www.linkedin.com/sales/logout?trk=sn_nav2__util_nav_logout";
	private String signouturl = "https://www.linkedin.com/m/logout/";

	private Preferences prefs;

	private Parser parser = null; // changing
	
	public FireFoxOperator() {
		prefs = Preferences.userRoot().node("db");
		this.profileName = prefs.get("profilename", "");
		// this.filefoxdir = prefs.get("firefoxlocation", "");
		this.geckodriverdir = prefs.get("geckodriverlocation", "");
		parser = new HtmlParser(); // changing
		// resource location explicitly stated for testing purpose
		// this.profileName = "default";
		// this.geckodriverdir = "Geckodriver\\v0.19.1\\32\\geckodriver.exe";

	}

	
	public void setProfileMode(String type) {
		System.out.println(" --- " + type);
		parser = null;
		if (type.toLowerCase().contains("salesnavleads")) {
			parser = new SalesNavigatorParser();
			this.setUrl("salesnavleads");
		} else if (type.toLowerCase().contains("salesnavaccounts")) {
			parser = new SalesNavAccountsParser();
			this.setUrl("salesnavaccounts");
		} else {
			parser = new HtmlParser(); // default selected
			this.setUrl("profilesearch");
		}

	}
	
	public LinkedList<Info> takeList() { // 1
		LinkedList<Info> currentlist = parser.parse();
		return currentlist;
	}
	
	
	
	
	public boolean browserLauncher() {

		ProfilesIni profile = new ProfilesIni();
		FirefoxProfile myprofile = profile.getProfile(profileName);

		myprofile.setPreference("network.proxy.type", 0);
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(FirefoxDriver.PROFILE, myprofile);

		System.setProperty("webdriver.gecko.driver", geckodriverdir);

		driver = new FirefoxDriver(capabilities);

		driver.get(url);

		return true;
	}

	private final String LOGINTAG = "login-email";
	private final String PASSWORDTAG = "login-password";
	private final String LOGINSUBMITTAG = "login-submit";

	public boolean linkedinLogin(String user, String password) {
		try {
			driver.findElement(By.id(LOGINTAG)).clear();
			driver.findElement(By.id(LOGINTAG)).sendKeys(user);
			driver.findElement(By.id(PASSWORDTAG)).clear();
			driver.findElement(By.id(PASSWORDTAG)).sendKeys(password);
			driver.findElement(By.id(LOGINSUBMITTAG)).click();
			return true;
		} catch (Exception e) {
			// e.printStackTrace();
			return false;
		}
	}

	private final String SEARCHCSSTAG = "input.ember-text-field.ember-view";
	private final String SEARCHBUTTONTAG = "button.nav-search-button";

	public boolean linkedinSearch(String keyword) {
		try {
			if (isElementPresent(By.cssSelector(SEARCHCSSTAG))) {
				// new version
				driver.findElement(By.cssSelector(SEARCHCSSTAG)).clear();
				driver.findElement(By.cssSelector(SEARCHCSSTAG)).sendKeys(keyword);
				driver.findElement(By.cssSelector(SEARCHBUTTONTAG)).click();
			} else if (isElementPresent(By.id("main-search-box"))) {
				// old version
				driver.findElement(By.id("main-search-box")).clear();
				driver.findElement(By.id("main-search-box")).sendKeys(keyword);
				driver.findElement(By.name("search")).click();
			} else
				return false;
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	// return which are is currently I am on
	public final static String LOGINPAGE = "loginpage";
	public final static String SEARCHPAGE = "searchpage";

	public String currentPageStatus() {
		if (isElementPresent(By.id(LOGINTAG)))
			return LOGINPAGE;
		if (isElementPresent(By.cssSelector(SEARCHCSSTAG)))
			return SEARCHPAGE;
		return null;
	}

	public boolean signOut() {
		System.out.println("--sign out");
		try {
			String url = driver.getCurrentUrl().trim();
			if (url.contains("/sales/"))
				driver.get(selesnavsignouturl);
			else
				driver.get(signouturl);
			driver.manage().timeouts().implicitlyWait(7, TimeUnit.SECONDS);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	public boolean closeBrowser() {
		driver.quit();
		return true;
	}

	public String getSourseCode() {
		fullPageScroll();
		String pageSource = "";
		pageSource = driver.getPageSource().toString();
		// System.out.println(pageSource);
		return pageSource;
	}

	public int currentPageNumber() {
		String oldnCurrentSelector = "ul.pagination li.active"; // old
		//String newCurrentSelector = "li.page-list li.active"; // new
		String newCurrentSelector = "//ul/li[contains(@class, 'active')]/span";
		//String salesNavCurrentSelector = "ul.pagination-links li.active.pagination-link-item"; 
		String salesNavCurrentSelector = "//ol[@class='search-results__pagination-list']/li[contains(@class, 'selected')]"; 
		// li.page-list li.active
		WebElement element;
		if (isElementPresent(By.cssSelector(oldnCurrentSelector))) {
			element = driver.findElement(By.cssSelector(oldnCurrentSelector));
			return Integer.parseInt(element.getText());
		} else if (isElementPresent(By.xpath(newCurrentSelector))) {
			element = driver.findElement(By.xpath(newCurrentSelector));
			return Integer.parseInt(element.getText());
		} else if (isElementPresent(By.xpath(salesNavCurrentSelector))) {
			element = driver.findElement(By.xpath(salesNavCurrentSelector));
			return Integer.parseInt(element.getText());
		} else
			return -1;

	}

	public int openNextPage() {
		System.out.println(" <- openNextPage clicked");
		int responsepage = 0;
		String newnextPageSelectorDisabled = "//button[contains(@class, 'artdeco-pagination__button--next')][contains(@class, 'artdeco-button--disabled')][child::span[contains(., 'Next')]]";
		String salesNavnextPageSelectorDisabled = "//button[contains(@class,'search-results__pagination-next-button')][contains(@disabled)][child::span[contains(., 'Next')]]";
		boolean isNewDisabled = isElementPresent(By.xpath(newnextPageSelectorDisabled));
		boolean isSalesDisabled = isElementPresent(By.xpath(salesNavnextPageSelectorDisabled));
		if(isNewDisabled || isSalesDisabled) return responsepage;
		
		
		String newnextPageSelector = "//button[contains(@class, 'artdeco-pagination__button--next')][child::span[contains(., 'Next')]]";
		String salesNavnextPageSelector = "//button[contains(@class,'search-results__pagination-next-button')][child::span[contains(., 'Next')]]";
		boolean isNewPresent = isElementPresent(By.xpath(newnextPageSelector));
		boolean isSalesNavPresent = isElementPresent(By.xpath(salesNavnextPageSelector));
		System.out.println(isNewPresent + " <- isNewPresent");

		if (isNewPresent) {
			responsepage = switchingPage(By.xpath(newnextPageSelector));
		} else if (isSalesNavPresent) {
			responsepage = switchingPage(By.xpath(salesNavnextPageSelector));
		}
		System.out.println(responsepage + " <- responsepage");

		return responsepage;

	}

	public int openPreviousPage() {
		int currentpage = currentPageNumber();
		int responsepage = -1;
		String oldPrevPageSelector = "li.prev > a";
		String newPrevPageSelector = "//button[contains(@class, 'artdeco-pagination__button--previous')][child::span[contains(., 'Previous')]]"; // button.prev
		String salesNavPrevPageSelector = "//button[contains(@class,'search-results__pagination-previous-button')][child::span[contains(., 'Previous')]]"; // a.prev-pagination.page-link
		boolean isOldPresent = isElementPresent(By.cssSelector(oldPrevPageSelector));
		boolean isNewPresent = isElementPresent(By.xpath(newPrevPageSelector));
		boolean isSalesNavPresent = isElementPresent(By.xpath(salesNavPrevPageSelector));

		if (isOldPresent) {
			responsepage = switchingPage(By.cssSelector(oldPrevPageSelector));
		} else if (isNewPresent) {
			responsepage = switchingPage(By.xpath(newPrevPageSelector));
		} else if (isSalesNavPresent) {
			responsepage = switchingPage(By.xpath(salesNavPrevPageSelector));
		} else {
			responsepage = -1;
		}

		return responsepage;

	}

	public int switchingPage(By by) {
		//int switchedpage = 0;
		// driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		try {
			driver.findElement(by).click();
			fullPageScroll();
			if(parser instanceof SalesNavigatorParser) salesPageScroll();
			if(parser instanceof SalesNavAccountsParser) salesPageScroll();
		} catch (NoSuchElementException e) {
			System.out.println(e.getMessage());;
		}
		/*
		int limits = 5;
		do {
			driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
			switchedpage = currentPageNumber();
			limits--;
		} while ((switchedpage == 0) && limits > 0);
		*/
		System.out.println(currentPageNumber() + " <- currentPageNumber");

		return currentPageNumber();
	}

	public boolean setUrl(String type) {
		if (type.toLowerCase().contains("salesnav"))
			driver.get(salesNavUrl);
		else
			driver.get(url);
		return true;
	}

	public void fullPageScroll() {
		// https://stackoverflow.com/questions/42982950/how-to-scroll-down-the-page-till-bottomend-page-in-the-selenium-webdriver
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		try {
			jse.executeScript("scroll(0, 250);");
			Thread.sleep(500);
			jse.executeScript("scroll(0, 550);");
			Thread.sleep(1000);
			jse.executeScript("scroll(0, 750);");
			Thread.sleep(500);
			jse.executeScript("scroll(0, 950);");
			Thread.sleep(1000);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// if I direct go to bottom of the page page full content don't load
		// jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");
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
			Thread.sleep(1000);
			jse.executeScript("scroll(0, 2950);");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	// For public profile link
	public boolean newTabOpener() {
		String selectLinkOpeninNewTab = Keys.chord(Keys.CONTROL, "t");
		driver.findElement(By.tagName("html")).sendKeys(selectLinkOpeninNewTab);
		return true;
	}

	public boolean newTabCloser() {
		String selectLinkCloseNewTab = Keys.chord(Keys.CONTROL, "w");
		driver.findElement(By.tagName("html")).sendKeys(selectLinkCloseNewTab);
		return true;
	}

	public boolean linkOpener(String profileUrl) {
		// driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		driver.get(profileUrl);
		return true;
	}

	public String collectProfileLink() {
		return driver.findElement(By.tagName("a")).getText();

	}

	String infoBtnCssSelector = "#topcard > div.module-footer > ul > li > button";
	String infoBtnCssSelector1 = ".more-info-tray > table:nth-child(4) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(2) > ul:nth-child(1) > li:nth-child(1) > a:nth-child(1)";

	public String getPublicLink(String salesProLink) {
		driver.get(salesProLink);
		if (!findAndClick(infoBtnCssSelector))
			return salesProLink;
		try {
			WebElement element = driver.findElement(By.cssSelector(infoBtnCssSelector1));
			return element.getText();
		} catch (Exception e) {
		}

		return salesProLink;
	}

	private boolean findAndClick(String selector) {
		try {
			By by = By.cssSelector(selector);
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			driver.findElement(by).click();
			return true;
		} catch (Exception e) {
		}
		return false;
	}

}
