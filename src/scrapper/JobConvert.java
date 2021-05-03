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
import db.DbJob;
import pojo.Job;
import webhandler.FireFoxOperator;

public class JobConvert implements Parser {

	public String baseUrl = "https://www.linkedin.com/";	
	DbJob localDb;
	
	public JobConvert() {
		localDb = new DbJob();
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
		//return localDb.update(obj, ((Job) obj).getJobLink());
		return false;
	}
	
	@Override
	public boolean parseData(int index) {
		Job job = new Job();
		return writeToDb(job);
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
