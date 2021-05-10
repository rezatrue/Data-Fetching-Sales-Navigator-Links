package scrapper;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import application.MainController;
import db.DbPeople;
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
		int num = MainController.prefs.getInt("unUpdatedListCount", 0);
		MainController.prefs.putInt("unUpdatedListCount", (num + 1));
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
		
		
		//a[child::span[contains(.,'Contact info')]] --- click
		//close button
		//div[@id='artdeco-modal-outlet']/div[@aria-hidden="false"]//button[contains(@class,'dismiss')]
		//button[contains(@class,'dismiss')]
		//section[child::header[contains(.,'Email')]]/div/a[contains(@href,'mail')]
		// website sugession can be taken
		//By contactInfoBy = By.xpath("//a[child::span[contains(.,'Contact info')]]");
		By contactInfoBy = By.xpath("//a[contains(.,'Contact info')]");
		try {
			FireFoxOperator.driver.findElement(contactInfoBy).click();
			
			//FireFoxOperator.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			//By mailBy = By.xpath("//section[child::header[contains(.,'Email')]]/div/a[contains(@href,'mail')]");
			
			By headingBy = By.xpath("//h2[contains(.,'Contact Info')]");
			FireFoxOperator.waitUntillVisible(headingBy);
			By mailBy = By.xpath("//a[contains(@href,'mailto')]");
			try {String mail = FireFoxOperator.driver.findElement(mailBy).getText().trim();
			System.out.println(mail);
			people.setEmail(mail);
			By dismissPopupBy = By.xpath("//button[contains(@class,'dismiss')]");
			FireFoxOperator.driver.findElement(dismissPopupBy).click();
			}catch (Exception e1) {;}
			
		} catch (Exception e) {
			e.printStackTrace();
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
			people.setCurrentJobTitle(cleanWhiteSpace(jobTitle));
			}catch (Exception e1) {;}
			
			By conpamyBy = By.xpath(".//p[contains(@class,'pv-entity__secondary-title')]");
			try {String conpamy = experienceWE.findElement(conpamyBy).getText().trim();
			people.setCurrentCompany(conpamy);
			}catch (Exception e1) {;}
			By serviceRangeBy = By.xpath(".//h4[contains(@class,'pv-entity__date-range')]/span[2]");
			try {String serviceRange = experienceWE.findElement(serviceRangeBy).getText().trim();
			people.setServiceRange(serviceRange);
			}catch (Exception e1) {;}
			By companyLocationBy = By.xpath(".//h4[contains(@class,'pv-entity__location')]/span[2]");
			try {String companyLocation = experienceWE.findElement(companyLocationBy).getText().trim();
			people.setCompanyLocation(companyLocation);
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
			people.setInstitute(institute);
			}catch (Exception e1) {;}
			By degreeNameBy = By.xpath("./p[contains(@class,'degree-name')]/span[2]");
			try {String degreeName = educationWE.findElement(degreeNameBy).getText().trim();
			people.setDegreeName(degreeName);
			}catch (Exception e1) {;}
			By fosBy = By.xpath("./p[contains(@class,'fos')]/span[2]");
			try {String fos = educationWE.findElement(fosBy).getText().trim();
			people.setFos(fos);
			}catch (Exception e1) {;}
			By datesBy = By.xpath("./following-sibling::p[contains(@class,'dates')]/span[2]");
			try {String dates = educationWE.findElement(datesBy).getText().trim();
			people.setDates(dates);
			}catch (Exception e1) {;}
		}

		return writeToDb(people);
	}
	
	@Override
	public int parseList(){
		return 0;
	}

	@Override
	public int writeToDb(LinkedList<?> list) {
		LinkedList<People> leadList = (LinkedList<People>) list;
		int count = 0;
		Iterator<People> it = leadList.iterator();
		localDb = new DbPeople();
			while(it.hasNext()) {
				People people = (People) it.next();
				if(localDb.insert(people)) count++;
			}
		return count;
	}


	private String cleanWhiteSpace(String txt) {
		return txt.replaceAll("[\\r\\n|\\r|\\n|\\s|\\t]", " ").trim();
	}

	
	
}
