package webhandler;

import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.security.Credentials;
import org.openqa.selenium.security.UserAndPassword;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import csvhandler.PeopleCsv;
import db.DbPeople;

public class Run {
	static WebDriver driver;

	public static void testOneSection() {
		DbPeople db = new DbPeople();
		db.createNewTable();
		System.out.println(db.countRecords());
		PeopleOperator operator = new PeopleOperator();
		operator.browserLauncher();
		operator.openUrl("file:///C:/Users/JAVA_USER/Downloads/People_list/2.html");
		operator.takeList();
		System.out.println(db.countRecords());
		operator.openUrl("file:///C:/Users/JAVA_USER/Downloads/People_list/1.html");
		operator.takeList();
		System.out.println(db.countRecords());
		operator.closeBrowser();
		
		PeopleCsv csv = new PeopleCsv();
		csv.listtoCsv("Test", 20);
	}
	
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello: Tester");
		takeFullList();
	}
	
	public static void takeFullList() {
		runfirefox();
		driver.get("file:///F:/development/Job_list/1.html");
		
		By listXpathBy = By.xpath("//ul[contains(@class,'jobs-search-results__list')]/li");
		By jobTitleXpathBy = By.xpath(".//div[contains(@class,'content')]/div[contains(@class,'title')][1]");
		
		int countElement = 0;
		do {
		List<WebElement> lists = driver.findElements(listXpathBy);
		System.out.println( "SIZE: " + lists.size());
		if(countElement < lists.size()) {
			countElement += 7; //lists.size();
			scrollUpToWebElement(By.xpath("//ul[contains(@class,'jobs-search-results__list')]/li["+countElement+"]"));
		}
		if(countElement >= lists.size()) break;
		}while(true);
		
		
		List<WebElement> lists = driver.findElements(listXpathBy);
		Iterator<WebElement> it = lists.iterator();
		while(it.hasNext()) {
			WebElement we = it.next();
			System.out.println(we.findElement(jobTitleXpathBy).getText());
		}
		
		
		
		
		
	}
	
	
	public static void clearWhiteSpace() {
		PeopleOperator operator = new PeopleOperator();
		operator.browserLauncher();
		operator.openUrl("file:///F:/development/People_list/Terry-Antony.html");
		
		By jobTitleBy = By.xpath("//section[@id='experience-section']/ul/li[1]//h3");
		try {String jobTitle = FireFoxOperator.driver.findElement(jobTitleBy).getText().trim();
		System.out.println(jobTitle);
		
		System.out.println(jobTitle.replaceAll("[\\r\\n|\\r|\\n|\\s|\\t]", " "));
		}catch (Exception e1) {;}
	}
	
	public static void alertTest() {
		
		String url = "https://dev20-web-jnj.demandware.net/on/demandware.store/Sites-Exuviance-Site/default/Home-Show";
		String username = "storefront";
		String password = "exuweb2020";
		
		
		System.setProperty("webdriver.gecko.driver",
				"Geckodriver\\v0.21.0-win64\\geckodriver.exe");

		driver = new FirefoxDriver();
		
		String urlTemp = url.replaceFirst("https://", "");
		String URL = "https://" + username  + ":" + password + "@" + urlTemp;
		driver.get(URL); // Basically operation done here itself still if not work use further Alert code as well
		
		Alert alert = driver.switchTo().alert();
		alert.accept();
			
		
	}
	

	
	static String infoBtnCssSelector = "#topcard > div.module-footer > ul > li > button";
	static String infoBtnCssSelector1 = ".more-info-tray > table:nth-child(4) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(2) > ul:nth-child(1) > li:nth-child(1) > a:nth-child(1)";

	public static String getPublicLink(String salesProLink) {
		if (salesProLink.toLowerCase().contains("linkedin.com/sales")) {
			driver.get(salesProLink);
			findAndClick(infoBtnCssSelector);
			WebElement element = driver.findElement(By.cssSelector(infoBtnCssSelector1));
			// System.out.println(element.getText());
			return element.getText();
		}
		return salesProLink;
	}

	public static void findAndClick(String selector) {
		By by = By.cssSelector(selector);
		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		driver.findElement(by).click();
	}

	public static String getSourseCode() {
		fullPageScroll();
		String pageSource = "";
		pageSource = driver.getPageSource().toString();
		System.out.println(pageSource);
		return pageSource;
	}

	
	
	public static void scrollUpToWebElement(By by) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].scrollIntoView(true);", driver.findElement(by));
		try{Thread.sleep(7000);}catch(Exception e) {}
	}
	
	public static void fullPageScroll() {
		// https://stackoverflow.com/questions/42982950/how-to-scroll-down-the-page-till-bottomend-page-in-the-selenium-webdriver
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		try {
			jse.executeScript("scroll(0, 250);");
			Thread.sleep(1000);
			jse.executeScript("scroll(0, 550);");
			Thread.sleep(1000);
			jse.executeScript("scroll(0, 750);");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// if I direct go to bottom of the page page full content don't load
		// jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");
	}

	// running system installed firefox [Testing date : 10 Mar 2018]
	// requires : selenium-server-standalone-3.10.0.jar,
	// client-combined-3.10.0.jar & geckodriver.exe

	protected static void runfirefoxDefaultProfile() {

		ProfilesIni profile = new ProfilesIni();
		FirefoxProfile myprofile = profile.getProfile("default");

		myprofile.setPreference("network.proxy.type", 0);
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(FirefoxDriver.PROFILE, myprofile);
		
		System.setProperty("webdriver.gecko.driver",
				"Geckodriver\\v0.21.0-win64\\geckodriver.exe");

		driver = new FirefoxDriver(capabilities);
	
	}
	
	protected static void runfirefox() {

		System.setProperty("webdriver.gecko.driver",
				"Geckodriver\\v0.21.0-win64\\geckodriver.exe");

		driver = new FirefoxDriver();
	
	}

	private static void openGooleSearch() {
		driver.get("http://www.google.com");
		WebElement webElement = driver.findElement(By.id("lst-ib"));
		webElement.sendKeys("Hello");
		webElement.sendKeys(Keys.ENTER);
	}

	private static void openNavLink() {
		// driver.get("C:\\Users\\jAVA_USER\\Desktop\\Sales Navigator\\Cameron
		// Wisdom _ Sales Navigator.htm");
		WebElement webElement = driver.findElement(By.id(""));
		webElement.sendKeys("Hello");
		webElement.sendKeys(Keys.ENTER);

	}

	private static final String LOGINTAG = "login-email";
	private static final String PASSWORDTAG = "login-password";
	private static final String LOGINSUBMITTAG = "login-submit";

	public static boolean linkedinLogin(String user, String password) {
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

}
