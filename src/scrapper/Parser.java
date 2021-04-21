package scrapper;

import java.util.LinkedList;

import pojo.People;

public interface Parser {

	public int parseList();
	
	public boolean parseData(int index);
	
	public int getTotalCounts();
	
	public int deleteAllData();
	
	public int writeToDb(LinkedList<?> list);
	

}
