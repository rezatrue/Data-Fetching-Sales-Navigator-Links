package webhandler;

import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class TestRun {
	private static WebDriver driver;
	private static Document doc;

	public static void main(String[] args) {

		/*
		 * // work on live openChromewithSelenium();
		 * driver.get("https://www.linkedin.com"); loginLinkedinAccount();
		 * System.out.println("login - success");
		 */
		/*
		 * try {
		 * 
		 * // saved company list page
		 * 
		 * File input = new File(
		 * "E:\\Project Findout Linkedin user Email address\\saved pages\\company_people_Search _ LinkedIn1.html"
		 * ); doc = Jsoup.parse(input, "UTF-8", "https://www.linkedin.com/"); }
		 * catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		/*
		 * String pageSource = doc.html(); PeopleList peopleList = new
		 * PeopleList("dnet", pageSource); peopleList.takeList();
		 */
		openChromewithSelenium();
		takeLink();

		System.out.println("- - success - -");
	}

	private static void takeLink() {
		driver.get(
				"E:\\Project Findout Linkedin user Email address\\LInkedin-Sales-pages\\Elcio Salgado Jr _ Sales Navigator(1).html");

		driver.findElement(By.cssSelector("button.contact-info-button")).click();
		System.out.println("clicked");

		String pubLink = "div.module-footer div.more-info-tray table tbody a";
		String text = driver.findElement(By.cssSelector(pubLink)).getText();
		System.out.println(text);
	}

	public static void openChromewithSelenium() {
		/*
		 * System.setProperty("webdriver.chrome.driver",
		 * "../Linkedin Email Hunter/resources/chromedriver.exe"); WebDriver
		 * driver = new ChromeDriver();
		 */
		/*
		 * String userProfile=
		 * "C:\\Users\\java user\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\"
		 * ; ChromeOptions options = new ChromeOptions();
		 * options.addArguments("user-data-dir="+userProfile);
		 * options.addArguments("--start-maximized"); WebDriver driver = new
		 * ChromeDriver(options);
		 */

		System.setProperty("webdriver.chrome.driver", "../Linkedin Email Hunter/resources/chromedriver.exe");
		String chromeProfilePath = "C:\\Users\\java user\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\";
		// String chromeProfilePath = "C:/Users/java
		// user/AppData/Local/Google/Chrome/User Data";
		ChromeOptions chromeProfile = new ChromeOptions();
		chromeProfile.addArguments("chrome.switches", "--disable-extensions");
		chromeProfile.addArguments("user-data-dir=" + chromeProfilePath);

		driver = new ChromeDriver(chromeProfile);

		/*
		 * System.setProperty(
		 * "webdriver.chrome.driver","../Linkedin Email Hunter/resources/chromedriver.exe"
		 * ); ChromeOptions options = new ChromeOptions(); options.
		 * addArguments("user-data-dir=C:/Users/java user/AppData/Local/Google/Chrome/User Data"
		 * ); options.addArguments("--start-maximized"); WebDriver driver = new
		 * ChromeDriver(options);
		 */

	}

	public static void loginLinkedinAccount() {
		/*
		 * driver.findElement(By.cssSelector("input#login-email.login-email")).
		 * sendKeys("rezatrue@yahoo.com"); driver.findElement(By.cssSelector(
		 * "input#login-password.login-password")).sendKeys("1Canada12");
		 */
		driver.findElement(By.cssSelector("input#login-email.login-email")).sendKeys("faysal.uddin@mail.ru");
		driver.findElement(By.cssSelector("input#login-password.login-password")).sendKeys("sj99991212");
		driver.findElement(By.cssSelector("input#login-submit.login.submit-button")).click();

	}

}
