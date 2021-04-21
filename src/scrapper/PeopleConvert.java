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
import db.DbPeople;
import pojo.Company;
import pojo.People;
import webhandler.FireFoxOperator;

public class PeopleConvert implements Parser {

	public String baseUrl = "https://www.linkedin.com/";	
	DbPeople localDb;
	
	public PeopleConvert() {
		localDb = new DbPeople();
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
	
	private boolean writeToDb(Object obj) {				
		//int num = MainController.prefs.getInt("unUpdatedListCount", 0);
		//MainController.prefs.putInt("unUpdatedListCount", (num + count));
		return localDb.update(obj, ((People) obj).getLink());
	}
	
	@Override
	public boolean parseData(int index) {
		
		People people = localDb.selectAtIndex(index);
		if(people == null) return false;
		else {
			FireFoxOperator.driver.get(people.getLink());
			FireFoxOperator.fullPageScroll();
			// need scroll 
		}
		
		//section[@id='experience-section']/ul/li[1]//h3 - title
		//section[@id='experience-section']/ul/li[1]//p[contains(@class,'pv-entity__secondary-title')] -- company
		//section[@id='experience-section']/ul/li[1]//h4[contains(@class,'pv-entity__date-range')]/span[2] -- service range
		//section[@id='experience-section']/ul/li[1]//h4[contains(@class,'pv-entity__location')]/span[2] -- company location

		By experienceBy = By.xpath("//section[@id='experience-section']/ul/li[1]");
		WebElement experienceWE = null;
		try {
			experienceWE = FireFoxOperator.driver.findElement(experienceBy);
		} catch (Exception e1) {
			//e1.printStackTrace();
		}
		if(experienceWE!=null) {
			By jobTitleBy = By.xpath(".//h3");
			try {String jobTitle = experienceWE.findElement(jobTitleBy).getText().trim();
			// need to replace new line tab & white spaces
			people.setCurrentJobTitle(jobTitle.replaceAll("[\\r\\n|\\r|\\n|\\s|\\t]", " "));
			}catch (Exception e1) {;}
			
			By conpamyBy = By.xpath(".//p[contains(@class,'pv-entity__secondary-title')]");
			try {String conpamy = experienceWE.findElement(conpamyBy).getText().trim();
			
			}catch (Exception e1) {;}
			By serviceRangeBy = By.xpath(".//h4[contains(@class,'pv-entity__date-range')]/span[2]");
			try {String serviceRange = experienceWE.findElement(serviceRangeBy).getText().trim();
			
			}catch (Exception e1) {;}
			By companyLocationBy = By.xpath(".//h4[contains(@class,'pv-entity__location')]/span[2]");
			try {String companyLocation = experienceWE.findElement(companyLocationBy).getText().trim();
			
			}catch (Exception e1) {;}
		}
		
		//section[@id='education-section']/ul/li[1]//div[@class='pv-entity__degree-info']/h3  -- institue
		//section[@id='education-section']/ul/li[1]//div[@class='pv-entity__degree-info']/p[contains(@class,'degree-name')]/span[2]
		//section[@id='education-section']/ul/li[1]//div[@class='pv-entity__degree-info']/p[contains(@class,'fos')]/span[2]
		//section[@id='education-section']/ul/li[1]//div[@class='pv-entity__degree-info']/following-sibling::p[contains(@class,'dates')]/span[2]
		
		
		By educationBy = By.xpath("//section[@id='education-section']/ul/li[1]//div[@class='pv-entity__degree-info']");
		WebElement educationWE = null;
		try {
			educationWE = FireFoxOperator.driver.findElement(educationBy);
		} catch (Exception e1) { e1.printStackTrace(); }
		
		if(educationWE!=null) {
			By instituteBy = By.xpath("./h3");
			try {String institute = educationWE.findElement(instituteBy).getText().trim();
			
			}catch (Exception e1) {;}
			By degreeNameBy = By.xpath("./p[contains(@class,'degree-name')]/span[2]");
			try {String degreeName = educationWE.findElement(degreeNameBy).getText().trim();
			
			}catch (Exception e1) {;}
			By fosBy = By.xpath("./p[contains(@class,'fos')]/span[2]");
			try {String fos = educationWE.findElement(fosBy).getText().trim();
			
			}catch (Exception e1) {;}
			By datesBy = By.xpath("./following-sibling::p[contains(@class,'dates')]/span[2]");
			try {String dates = educationWE.findElement(datesBy).getText().trim();
			
			}catch (Exception e1) {;}
		}
		
		
		//a[child::span[contains(.,'Contact info')]] --- click

		//close button
		//div[@id='artdeco-modal-outlet']/div[@aria-hidden="false"]//button[contains(@class,'dismiss')]

		//section[child::header[contains(.,'Email')]]/div/a[contains(@href,'mail')]

		// website sugession can be taken
		
		By contactInfoBy = By.xpath("//a[child::span[contains(.,'Contact info')]]");
		try {
			FireFoxOperator.driver.findElement(contactInfoBy).click();
			FireFoxOperator.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			By mailBy = By.xpath("//section[child::header[contains(.,'Email')]]/div/a[contains(@href,'mail')]");
			try {String mail = FireFoxOperator.driver.findElement(mailBy).getText().trim();
			System.out.println(mail);
			people.setEmail(mail);
			}catch (Exception e1) {;}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return writeToDb(people);
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
