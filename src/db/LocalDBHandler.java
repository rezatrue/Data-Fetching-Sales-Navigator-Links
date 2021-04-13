package db;
/*
 * Sqlite DB for keeping data in local machine
 * http://www.sqlitetutorial.net/sqlite-java/sqlite-jdbc-driver/
 */

import java.util.LinkedList;


public interface LocalDBHandler {
	
	final String DB_NAME = "llf.db";
	

//	public LocalDBHandler() {
//	}
//	
	public void connect();// { }

	public void closeConnection(); // {}
	public boolean dropTable(); // {return false;}
	
	public int countRecords(); // { return 0;}
	
	public String selectAtIndex(int num); // {return "";}
	
	
	public LinkedList<?> selectRows(int num); //{ return new LinkedList<>();}
	
	public boolean insert(Object obj); // {return false;}
	
	public boolean update(Object obj, String salesLink); // {return false;}

	public boolean createNewTable(); //{return false;}
	
	
}
