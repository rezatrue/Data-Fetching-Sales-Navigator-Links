package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.prefs.Preferences;

public class DBHandler {
	private Connection conn;
	
//	private String url = "jdbc:mysql://192.168.1.72:3306/linkedin";
//	private String dbuser = "toolusers";
//	private String dbpass = "123";
//	private String table = "registered_user";
//	private String column = "code";

//	private String url = "jdbc:mysql://64.202.102.231:3306/ranimach_rztrue";
//	private String dbuser = "ranimach_rztrueu";
//	private String dbpass = "1W&LPtrue%a";
//	private String table = "registered_user";
//	private String column = "code";
	
	private String url;
	private String dbuser;
	private String dbpass;
	private String table;
	private String column;
	private Preferences prefs;

	public DBHandler() {
		prefs = Preferences.userRoot().node("db");
		this.url = "jdbc:mysql://" + prefs.get("dataserver", "");
		this.dbuser = prefs.get("dbuser", "");
		this.dbpass = prefs.get("dbpassword", "");
		this.table = prefs.get("tablename", "");
		this.column = prefs.get("columnname", "");
		}
	
	public DBHandler(String url, String table, String column, String dbuser, String dbpass) {
		this.url = url;
		this.dbuser = dbuser;
		this.dbpass = dbpass;
		this.table = table;
		this.column = column;

	}

	private boolean connecttoDB(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			this.conn = DriverManager.getConnection(url, dbuser, dbpass);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if(conn!=null) return true;
		else return false;
	}
	
	private boolean closeConn(){
		try {
			conn.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String userAuth(String user, String password){
		String returntxt = "";
		if (conn==null) if(!connecttoDB()) return "Unable to Connect";
		try {
			Statement stmt= conn.createStatement();  
			ResultSet rs = stmt.executeQuery("SELECT * FROM `"+ table +"` WHERE `user` = '"+user+"' and `password` = '"+password+"'");
			//ResultSet rs = stmt.executeQuery("SELECT * FROM `registered_user` WHERE `user` = 'reza' and `password` = '123456'");
			String s = "";
			while(rs.next()){  
			s = rs.getString(column);
			}
			if(s!=""){
			if(check(s)) returntxt = "Welcome "+ user ;
			else returntxt = "Your license has expired" ;
			}else{
				returntxt = "Please check you Username & Password" ;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			returntxt = "Unexpected SQL Exception";
		}
		if (conn!=null) if(!closeConn()) closeConn();
		
		return returntxt;
	}
	
	private boolean check(String s){
		DateFormat dateFormat = new SimpleDateFormat("MM");
		Calendar cal = Calendar.getInstance();
		String name = dateFormat.format(cal.getTime());
		if(sum(s.substring(3, 5))>=sum(name)) return true;
		else return false;
	}
	
	private int sum(String text){
		//System.out.println(text);
		int result=0;
		for (int i = 0; i < text.length(); i++) {
			result += Integer.parseInt((String)text.subSequence(i, i+1));
		}
		//System.out.println(result);
		return result;
	}
	
}
