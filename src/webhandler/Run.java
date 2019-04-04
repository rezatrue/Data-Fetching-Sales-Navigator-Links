package webhandler;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Run {
	static WebDriver driver;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello:");
		runfirefoxDefaultProfile();
		// openNavLink();
		linkedinLogin("ebrahimthex@gmail.com", "$RFV4rfv");
		/*
		 * driver.get(
		 * "https://www.linkedin.com/sales/profile/78593635,q_0a,NAME_SEARCH?moduleKey=peopleSearchResults"
		 * ); // fullPageScroll();
		 * findAndClick("#topcard > div.module-footer > ul > li > button");
		 * WebElement element = driver.findElement(By.cssSelector(
		 * ".more-info-tray > table:nth-child(4) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(2) > ul:nth-child(1) > li:nth-child(1) > a:nth-child(1)"
		 * ));
		 */
		System.out.println(getPublicLink(
				"https://www.linkedin.com/sales/profile/78593635,q_0a,NAME_SEARCH?moduleKey=peopleSearchResults"));
		// System.out.println(element.getText());
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
		driver.get("https://www.linkedin.com/");

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
