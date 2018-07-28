package db;
/*
 * Sqlite DB for keeping data in local machine
 * http://www.sqlitetutorial.net/sqlite-java/sqlite-jdbc-driver/
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import scrapper.Info;

public class LocalDBHandler {
	
	private final String DB_NAME = "llf.db";
	private String TABLE_NAME = "profile";
	private Connection conn = null;

	public LocalDBHandler() {
		createNewTable();
	}
		
	public void connect() {
	        try {
	            String url = "jdbc:sqlite:sqlite/db/" + DB_NAME;
	            conn = DriverManager.getConnection(url);
	            
	            System.out.println("Connection to SQLite has been established.");
	            
	        } catch (SQLException e) {
	            System.out.println("0"+e.getMessage());
	        }
	    }
	
	public void closeConnection() {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println("10"+ex.getMessage());
            }
	}
	
	public boolean dropTable() {
		boolean status = true;
		// Drop table if previous information exist
		String sqlTDrop = "drop table IF EXISTS " + TABLE_NAME;
        
        if(conn==null)
        	connect();
        
        Statement stmt = null;
        
        try {
			if(conn!=null) {
				stmt = conn.createStatement();
			    stmt.execute(sqlTDrop);
			}
		} catch (SQLException e) {
			status = false;
			System.out.println("1"+e.getMessage());
		}finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				System.out.println("2"+e.getMessage());
			}
		}
        return status;
	}
	
	private void createNewTable() {
		// Drop table if previous information exist
		dropTable();
        // SQL statement for creating a new table
        String sqlTCreate = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME + " ("
                + "	Linkedin_Profile_URL text PRIMARY KEY,"
                + "	First_Name text,"
                + "	Last_Name text,"
                + "	Email_ID text,"
                + "	Contact_Number text,"
                + "	Location text,"
                + "	Industry text,"
                + "	Designation text,"
                + "	Company_Name text,"
                + "	Company_Size text"
                + ");";
        
        if(conn==null)
        	connect();
        
        Statement stmt = null;
        
        try {
			if(conn!=null) {
				stmt = conn.createStatement();
			    stmt.execute(sqlTCreate);
			}
		} catch (SQLException e) {
			System.out.println("1"+e.getMessage());
		}finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				System.out.println("2"+e.getMessage());
			}
		}
    }
	
	public LinkedList<Info> selectAll(){
		LinkedList<Info> list  = new LinkedList<>();
		
        String sql = "SELECT * FROM " + TABLE_NAME;
        if(conn == null)
        	connect();
        
        Statement stmt = null;
        ResultSet rs = null;
        if(conn!=null) {
        	
        	try {
				stmt  = conn.createStatement();
				rs    = stmt.executeQuery(sql);
				// loop through the result set
				while (rs.next()) {
					Info info = new Info(
				                       rs.getString("Linkedin_Profile_URL"),
				                       rs.getString("First_Name"), 
				                       rs.getString("Last_Name"),
				                       rs.getString("Email_ID"), 
				                       rs.getString("Contact_Number"),
				                       rs.getString("Location"), 
				                       rs.getString("Industry"),
				                       rs.getString("Designation"),
				                       rs.getString("Company_Name"),
				                       rs.getString("Company_Size")
							);
					list.add(info);
					
				    System.out.println( 
				                       rs.getString("Linkedin_Profile_URL") + "\t" +
				                       rs.getString("First_Name") +  "\t" + 
				                       rs.getString("Last_Name") + "\t" +
				                       rs.getString("Email_ID") +  "\t" + 
				                       rs.getString("Contact_Number") + "\t" +
				                       rs.getString("Location") +  "\t" + 
				                       rs.getString("Industry") + "\t" +
				                       rs.getString("Designation") +  "\t" + 
				                       rs.getString("Company_Name") + "\t" +
				                       rs.getString("Company_Size"));
				}
			} catch (SQLException e) {
				System.out.println("5"+e.getMessage());
			}finally {
				try {
					rs.close();
					stmt.close();
				} catch (SQLException e) {
					System.out.println("6"+e.getMessage());
				}
				
			}
			
        }
        return list;
	}

	public boolean insert(Info info) {
		String sql = "INSERT INTO " 
					+ TABLE_NAME 
					+ " (Linkedin_Profile_URL, First_Name, Last_Name, Email_ID, Contact_Number,"
					+ " Location, Industry, Designation, Company_Name, Company_Size) "
					+ " VALUES(?,?,?,?,?,?,?,?,?,?)";
		
		PreparedStatement pstmt = null;
        if(conn == null)
        	connect();
        if(conn != null) {
	        try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, info.getLink());
				pstmt.setString(2, info.getFirstName());
				pstmt.setString(3, info.getSecondName());
				pstmt.setString(4, info.getEmail());
				pstmt.setString(5, info.getPhone());
				pstmt.setString(6, info.getLocation());
				pstmt.setString(7, info.getIndustry());
				pstmt.setString(8, info.getCurrentJobTitle());
				pstmt.setString(9, info.getCurrentCompany());
				pstmt.setString(10, info.getCompanySize());
				pstmt.executeUpdate();
			} catch (SQLException e) {
				System.out.println("3"+e.getMessage());
				return false;
			}finally {
				try {
					pstmt.close();
				} catch (SQLException e) {
					System.out.println("4"+e.getMessage());
				}
			}
        }
        return true;
	}
	
}
