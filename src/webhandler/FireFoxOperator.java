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
import pojo.WorkType;
import scrapper.Parser;
import scrapper.SalesNavAccountsParser;
import scrapper.SalesNavListsParser;
import scrapper.SalesNavigatorParser;

public abstract class FireFoxOperator {

	public abstract String checkPageStatus();
	public abstract String takeList();
	public abstract int clearList();
	public abstract int getTotalCounts();
	public abstract void setWorkType(WorkType workMode);
	public abstract int scanCsv(String filepath);
	public abstract int getDetailsInfo(int index);
	
	private String profileName = "default";
	private String geckodriverdir;

	public static WebDriver driver = null;
	private String url = "https://www.linkedin.com/";
	private String salesNavUrl = "https://www.linkedin.com/sales/settings?trk=nav_user_menu_manage_sales_nav";
	private String salesListUrl = "https://www.linkedin.com/sales/lists/people";
	private String selesnavsignouturl = "https://www.linkedin.com/sales/logout?trk=sn_nav2__util_nav_logout";
	private String signouturl = "https://www.linkedin.com/m/logout/";
	private String signInUrl = "https://www.linkedin.com/sales/login";
	
	private Preferences prefs;

	public FireFoxOperator() {
		prefs = Preferences.userRoot().node("db");
		this.profileName = prefs.get("profilename", "");
		// this.filefoxdir = prefs.get("firefoxlocation", "");
		this.geckodriverdir = prefs.get("geckodriverlocation", "");
		// resource location explicitly stated for testing purpose
		// this.profileName = "default";
		// this.geckodriverdir = "Geckodriver\\v0.19.1\\32\\geckodriver.exe";

	}
	
	// commom
	public boolean browserLauncher() {
		
		ProfilesIni profile = new ProfilesIni();
		FirefoxProfile myprofile = profile.getProfile(profileName);

		myprofile.setPreference("network.proxy.type", 0);
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(FirefoxDriver.PROFILE, myprofile);

		System.setProperty("webdriver.gecko.driver", geckodriverdir);

		driver = new FirefoxDriver(capabilities);
		
		driver.get(url);
		
		/*
		System.setProperty("webdriver.gecko.driver", geckodriverdir);
		driver = new FirefoxDriver();
		*/
		return true;
	}

	
	
	public boolean isLoginPage() {

		By profileImageBy = By.xpath("//img[contains(@class,'app-header-item-content__entity-image')]");
		
		try {
			driver.findElement(profileImageBy).getText();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		By salesProfileImageBy = By.xpath("//img[contains(@class,'global-nav__me-photo')]");

		try {
			driver.findElement(salesProfileImageBy).getText();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}

	
	public boolean waitUntillVisible(By by) {
		
		try {
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


	private boolean moveToSignInPage() {
		
		By signInLinkTextBy = By.xpath("//a[contains(text(),'Sign in')]");
		
		try {
			driver.findElement(signInLinkTextBy).click();
			return true;
		} catch (NoSuchElementException nsee) {
			nsee.printStackTrace();
		}
		driver.get(signInUrl);
		return true;
	}
	
	// modified 12 feb 2021
	public boolean linkedinLogin() {
		if(isLoginPage()) return true;

		driver.get(url);	
		moveToSignInPage();
		
		String password = prefs.get("linkedinPassword", "");
		String name = prefs.get("linkedinUser", "");
		
		By userNameBy = By.xpath("//form//input[contains(@id,'user')]");
		By passwordBy = By.xpath("//form//input[contains(@id,'pass')]");
		By loginButtonBy = By.xpath("//form//button[contains(text(),'Sign in')]");
		
		try {
			driver.findElement(userNameBy).clear();
			driver.findElement(userNameBy).sendKeys(name);
			driver.findElement(passwordBy).clear();
			driver.findElement(passwordBy).sendKeys(password);
			driver.findElement(loginButtonBy).click();
		} catch (NoSuchElementException nsee) {
			nsee.printStackTrace();
		}
		
		if(isLoginPage()) return true;
		
		return false;
	}

	protected boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
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

	public int switchingPage(By by) {
		//int switchedpage = 0;
		// driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		try {
			driver.findElement(by).click();
			fullPageScroll();
		} catch (NoSuchElementException e) {
			System.out.println(e.getMessage());;
		}
		System.out.println(currentPageNumber() + " <- currentPageNumber");
		return currentPageNumber();
	}

	public static void fullPageScroll() {
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
			Thread.sleep(500);
			jse.executeScript("scroll(0, 1200);");
			Thread.sleep(500);
			jse.executeScript("scroll(0, 1500);");
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
	
////....................	

	
	public int currentPageNumber() {
		String oldnCurrentSelector = "ul.pagination li.active"; // old
		//String newCurrentSelector = "li.page-list li.active"; // new
		String newCurrentSelector = "//ul/li[contains(@class, 'active')]/span";
		//String salesNavCurrentSelector = "ul.pagination-links li.active.pagination-link-item"; 
		String salesNavCurrentSelector = "//ol[@class='search-results__pagination-list']/li[contains(@class, 'selected')]"; 
		String salesNavListCurrentSelector = "//button[contains(@aria-label,\"current page\")]/span[1]"; 
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
		}  else if (isElementPresent(By.xpath(salesNavListCurrentSelector))) {
			element = driver.findElement(By.xpath(salesNavListCurrentSelector));
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
		String salesNavListnextPageSelector = "//div[contains(@class, \"table-pagination\")]//button[contains(@class, \"pagination__button--next\") and not(contains(@disabled,\"disabled\"))]";
		boolean isNewPresent = isElementPresent(By.xpath(newnextPageSelector));
		boolean isSalesNavPresent = isElementPresent(By.xpath(salesNavnextPageSelector));
		boolean isSalesNavListPresent = isElementPresent(By.xpath(salesNavListnextPageSelector));
		System.out.println(isNewPresent + " <- isNewPresent");

		if (isNewPresent) {
			responsepage = switchingPage(By.xpath(newnextPageSelector));
		} else if (isSalesNavPresent) {
			responsepage = switchingPage(By.xpath(salesNavnextPageSelector));
		}else if (isSalesNavListPresent) {
			responsepage = switchingPage(By.xpath(salesNavListnextPageSelector));
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


	
	public boolean setUrl(String type) {
		String urlString = url;
		if(type.toLowerCase().contains("salesnavleads")) urlString = salesNavUrl;
		if(type.toLowerCase().contains("salesnavaccounts")) urlString = salesNavUrl;
		if(type.toLowerCase().contains("salesnavlists")) urlString = salesListUrl;
		
		driver.get(urlString);
		
		return true;
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

//	String infoBtnCssSelector = "#topcard > div.module-footer > ul > li > button";
//	String infoBtnCssSelector1 = ".more-info-tray > table:nth-child(4) > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(2) > ul:nth-child(1) > li:nth-child(1) > a:nth-child(1)";
//
//	public String getPublicLink(String salesProLink) {
//		driver.get(salesProLink);
//		if (!findAndClick(infoBtnCssSelector))
//			return salesProLink;
//		try {
//			WebElement element = driver.findElement(By.cssSelector(infoBtnCssSelector1));
//			return element.getText();
//		} catch (Exception e) {
//		}
//
//		return salesProLink;
//	}
	
	
	public Info getPublicLinkDetails(String salesProLink) {
		
		if(isLoginPage()) linkedinLogin();
		
		linkOpener(salesProLink);
		
		Info info = generalInfo(salesProLink);
		
		if (isLinkedinMember()) {
			try {
			System.out.println("isLinkedinMember handleing ");
			String txtMatch = "https://www.linkedin.com/in/";
			String[] data = getGoogleResult(info.getCurrentJobTitle(), info.getCurrentCompany());
			System.out.println("isLinkedinMember handleing 0 " + data[0]);
			if(data[0].contains("Not Found")) return info;
			if(data[0].contains(txtMatch)) {
					System.out.println("isLinkedinMember handleing 1 " + "txtMatch");
					info.setLink(data[0]);
					info.setFirstName(data[1]);
					info.setSecondName(data[2]);
				}
			}catch (Exception e){
				System.out.println("isLinkedinMember handleing error ");
				e.printStackTrace();
			}
		}else {
			String buttonClick = "//button[contains(@class,\"right-actions-overflow-menu-trigger\")]";
			if(findAndClick(buttonClick)) {
				try {
					System.out.println("inhere 11");
					String urltxt = findUrlInSourcePage();
					System.out.println("urltxt : "+ urltxt);
					info.setLink(urltxt);	
				} catch (Exception e) {
				}
			}
		}
		System.out.println("return -->>");
		return info;
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
	
	
	public String[] getGoogleResult(String title, String company) {
		String data[] = {"Not Found", "", ""};
			System.out.println("--getGoogleResult");
		createSubTab();
		switchGoogleTab();
		
		String siteName = "LinkedIn";
		
		By searchInputBy = By.xpath("//input[@name=\"q\"]");
		try {
			openUrl("https://www.google.com/");
			WebElement inputElement = driver.findElement(searchInputBy);
			//inputElement.clear();
			inputElement.sendKeys(title + " at "+ company + " " + siteName);
			inputElement.sendKeys(Keys.ENTER);
		}catch (Exception e) {
			e.printStackTrace();
			driver.close();
			switchLinkedinTab();
			return data;
		}
		
		
		By searchResultsBy = By.xpath("//div[@class=\"g\"]");
		if(!waitUntillVisible(searchResultsBy)){
			driver.close();
			switchLinkedinTab();
			return data;
		}
		try {
			By firstLinkBy = By.xpath("//div[@class=\"g\"][1]/div[@class=\"rc\"]/div[@class=\"r\"]/a");
			String firstLink = driver.findElement(firstLinkBy).getAttribute("href");
			if(firstLink.contains("https://www.linkedin.com/in/")) {
				data[0] = firstLink;
				By titleBy = By.xpath("//div[@class=\"g\"][1]/div[@class=\"rc\"]/div[@class=\"r\"]/a/h3");
				String heading = driver.findElement(titleBy).getText();
				System.out.println("heading "+ heading);
				try {
					String name = heading.substring(0, heading.indexOf("-")).trim();
					System.out.println(name);
					
					String fName[];
					if(name.contains(",")) {
						fName = name.substring(0, name.indexOf(",")).split(" ");
						data[1] = fName[0];
						data[2] = fName[(fName.length -1)];
					}else {
						fName = name.split(" ");
						data[1] = fName[0];
						data[2] = fName[(fName.length -1)];
					}
					if(data[1].length() < 1 || data[2].length() < 1) {
						fName = name.split(" ");
						if(data[1].length() < 1) data[1] = fName[0];
						if(data[2].length() < 1) data[2] = fName[1];
					}
				
				} catch (Exception ee) {}
			
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<WebElement> searchResultElements = driver.findElements(searchResultsBy);
		System.out.println(searchResultElements.size());
		int count = 0;
		for (WebElement webElement : searchResultElements) {
			count++;
			try {
			By linkBy = By.xpath("//div[@class=\"g\"]["+count+"]/div[@class=\"rc\"]/div[@class=\"r\"]/a");
			String link = driver.findElement(linkBy).getAttribute("href");
			System.out.println("link " + link);
				if(link.contains("https://www.linkedin.com/in/")) {
					
					if(company.length() > 0){
						String fName = "";
						String lName = "";
						By titleBy = By.xpath("//div[@class=\"g\"]["+count+"]/div[@class=\"rc\"]/div[@class=\"r\"]/a/h3");
						String heading = driver.findElement(titleBy).getText();
						System.out.println("heading "+ heading);
						try {
							String name = heading.substring(0, heading.indexOf("-")).trim();
							System.out.println(name);
							
							String fullName[];
							if(name.contains(",")) {
								fullName = name.substring(0, name.indexOf(",")).split(" ");
								fName = fullName[0];
								lName = fullName[(fullName.length -1)];
							}else {
								fullName = name.split(" ");
								fName = fullName[0];
								lName = fullName[(fullName.length -1)];
							}
							if(fName.length() < 1 || lName.length() < 1) {
								
								fullName = heading.split(" ");
								if(fName.length() < 1) fName = fullName[0];
								if(lName.length() < 1) lName = fullName[1];
							}
							
							} catch (Exception ee) {}
							if(heading.toLowerCase().contains(company.toLowerCase())) {
								data[0] = link; 
								data[1] = fName;
								data[2] = lName;
								break;
								}
							By descriptionBy = By.xpath("//div[@class=\"g\"]["+count+"]/div[@class=\"rc\"]/div[@class=\"s\"]");
							String description = driver.findElement(descriptionBy).getText().replaceAll("[\\t\\n\\r]", " ");
							System.out.println("description "+ description);
							if(description.toLowerCase().contains(company.toLowerCase())) {
								data[0] = link; 
								data[1] = fName;
								data[2] = lName;
								break;
								}
					}
				} 
			}catch (Exception e) {
				e.printStackTrace();
			}	
		}
		
		driver.close();
		switchLinkedinTab();
		
		return data;
		
	}
	
	public boolean openUrl(String url) {
		driver.get(url);
		return true;
	}
	
	private String linkedinTabHandaler = null; 
	private String googleTabHandler = null; 
	
	public void createSubTab() {
		if(linkedinTabHandaler == null)
			linkedinTabHandaler = driver.getWindowHandle();
		System.out.println("--linkedinTabHandaler--"+ linkedinTabHandaler.isEmpty());
		((JavascriptExecutor) driver).executeScript("window.open('about:blank','_blank');");
		//((JavascriptExecutor) driver).executeScript("window.open();");
	   	  Set<String> handles = driver.getWindowHandles();
		  Iterator<String> iterator = handles.iterator();
		  while (iterator.hasNext()) {
			  googleTabHandler = iterator.next();
			  System.out.println("--googleTabHandler--"+ googleTabHandler.isEmpty());
		  }
	}
	
	public void switchGoogleTab() {
		driver.switchTo().window(googleTabHandler);
	}
	public void switchLinkedinTab() {
		driver.switchTo().window(linkedinTabHandaler);
	}
	
	
	public boolean isLinkedinMember() {
		System.out.println("isLinkedinMember");
					
		try {
			By memberNameBy = By.xpath("//div[contains(@class,\"profile-topcard-person-entity__content\")]//span[contains(@class,\"profile-topcard-person-entity__name\")]");
			WebDriverWait wait = new WebDriverWait(driver, 30);
			System.out.println("isLinkedinMember 1 ");
			wait.until(ExpectedConditions.visibilityOfElementLocated(memberNameBy));
			System.out.println("isLinkedinMember 2 ");
			String text = driver.findElement(memberNameBy).getText();
			System.out.println("isLinkedinMember 3 ");
			if(text.contains("LinkedIn Member")) return true;
		} catch (Exception e) {
			System.out.println("isLinkedinMember error ");
			String text = getSourseCode();
			if(text.contains("LinkedIn Member")) return true;
			e.printStackTrace();
		}
		
		return false;
	}
	
	public Info generalInfo(String link) {
		
		Info info = new Info();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
		
		
		By fullNameBy = By.xpath("//div[contains(@class,\"profile-topcard-person-entity__content\")]//span[contains(@class,\"profile-topcard-person-entity__name\")]");
		By locationBy = By.xpath("//div[@class=\"profile-topcard-person-entity__content min-width inline-block\"]/dl/dd[contains(@class,\"mt4\")]/div[contains(@class,\"profile-topcard__location-data\")]");
		By titleBy = By.xpath("//dd[contains(@class,\"profile-topcard__current-positions\")]/div/div[1]/span/span[1]");
		By companyBy = By.xpath("//dd[contains(@class,\"profile-topcard__current-positions\")]/div/div[1]/span/a");
		By company1By = By.xpath("//dd[contains(@class,\"profile-topcard__current-positions\")]/div/div[1]/span/span[2]");
		
		try {
			String name = driver.findElement(fullNameBy).getText().trim();
			if(!name.contains("LinkedIn Member")) {
				String fname[];
				if(name.contains(",")) {
					fname = name.substring(0, name.indexOf(",")).split(" ");
					info.setFirstName(fname[0]);
					info.setSecondName(fname[(fname.length -1)]);
				}else {
					fname = name.split(" ");
					info.setFirstName(fname[0]);
					info.setSecondName(fname[(fname.length -1)]);
				}
				
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			String loacation = driver.findElement(locationBy).getText().replaceAll("[\\t\\n\\r]", " ");
			info.setLocation(loacation);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			String title = driver.findElement(titleBy).getText();
			info.setCurrentJobTitle(title);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			String company = driver.findElement(companyBy).getText();
			info.setCurrentCompany(company);
		} catch (Exception e1) {
			e1.printStackTrace();
			try {
			String company = driver.findElement(company1By).getText();
			info.setCurrentCompany(company);
			} catch (Exception ee) {}
		}
		
		/*
		String company = info.getCurrentCompany();
		String title = info.getCurrentJobTitle();
		
		By titComYearBy = By.xpath("//dd[contains(@class,\"profile-topcard__current-positions\")]/div/div[1]/span");
		By titComBy = By.xpath("//dd[contains(@class,\"profile-topcard__current-positions\")]/div/div[1]/span/span");
			
		try {
			String titComYear = driver.findElement(titComYearBy).getText();
			
			List<WebElement> element = driver.findElements(titComBy);
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		
		if(company == null || company.length() < 1) {
			
		}
		
		if(title == null || title.length() < 1 ) {
			
		}
		*/
		
		return info;
	}
	
	protected boolean findAndClick(String selector) {
		try {
			//By by = By.cssSelector(selector);
			By by = By.xpath(selector);
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
			driver.findElement(by).click();
			return true;
		} catch (Exception e) {
		}
		return false;
	}

}
