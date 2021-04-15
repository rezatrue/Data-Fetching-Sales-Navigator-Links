package scrapper;

import java.util.LinkedList;

public interface Parser {

	public LinkedList<?> parse(String html);
	
	public int parse();
	
	public int deleteAllData();
	

}
