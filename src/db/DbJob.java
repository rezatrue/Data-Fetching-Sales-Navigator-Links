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

import pojo.Job;
import pojo.People;


public class DbJob implements LocalDBHandler{
	
	private String TABLE_NAME = "job";
	private Connection conn = null;

	public DbJob() {
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
        System.out.println(" drop : " + status );
        return status;
	}
	
	public boolean createNewTable() {
		// Drop table if previous information exist
		if(!dropTable())
			return false;
        // SQL statement for creating a new table
        String sqlTCreate = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME + " ("
                + "	Job_Link text PRIMARY KEY,"
                + "	Job_Title text,"
                + "	Company text,"
                + "	Company_Link text,"
                + "	Location text,"
                + "	Job_Description text"
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
        
        return true;
    }
	
	public int countRecords() {
		int count = 0;
		String sql = "SELECT count(*) FROM " + TABLE_NAME;
		if(conn == null)
        	connect();
		Statement stmt = null;
		ResultSet rset = null;
		if(conn != null) {
			try {
				stmt = conn.createStatement();
				rset = stmt.executeQuery(sql);
				rset.next();
				count = rset.getInt(1);
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}finally {
				try {
					stmt.close();
					rset.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("total : " + count);
		return count;
	}
	
	public Job selectAtIndex(int num) {
		Job job = null;
		String sql = "SELECT * FROM " + TABLE_NAME + " LIMIT 1 OFFSET " + num;
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
					System.out.println(rs.getString("Job_Link"));
					job = new Job(
		                       rs.getString("Job_Link"),
		                       rs.getString("Job_Title"), 
		                       rs.getString("Company"),
		                       rs.getString("Company_Link"), 
		                       rs.getString("Location"),
		                       rs.getString("Job_Description")
					);
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
		return job;
	}
	public LinkedList<Job> selectRows(int num){
		LinkedList<Job> list  = new LinkedList<>();
		
        String sql = "SELECT * FROM " + TABLE_NAME + " LIMIT " + num;
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
					Job job = new Job(
				                       rs.getString("Job_Link"),
				                       rs.getString("Job_Title"), 
				                       rs.getString("Company"),
				                       rs.getString("Company_Link"), 
				                       rs.getString("Location"),
				                       rs.getString("Job_Description")
							);
					list.add(job);
					
				    System.out.println( 
				                       rs.getString("Job_Link") + "\t" +
				                       rs.getString("Job_Title") +  "\t" + 
				                       rs.getString("Company") + "\t" +
				                       rs.getString("Company_Link") +  "\t" + 
				                       rs.getString("Location") + "\t" +
				                       rs.getString("Job_Description"));
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

	public boolean insert(Object obj) {
		Job job = (Job)obj;
		String sql = "INSERT INTO " 
					+ TABLE_NAME 
					+ " (Job_Link, Job_Title, Company, Company_Link, Location,"
					+ " Job_Description) "
					+ " VALUES(?,?,?,?,?,?)";
		
		PreparedStatement pstmt = null;
        if(conn == null)
        	connect();
        if(conn != null) {
	        try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, job.getJobLink());
				pstmt.setString(2, job.getJobTitle());
				pstmt.setString(3, job.getCompany());
				pstmt.setString(4, job.getCompanyLink());
				pstmt.setString(5, job.getLocation());
				pstmt.setString(6, job.getJobDescription());
				pstmt.executeUpdate();
			} catch (SQLException e) {
				System.out.println("3"+e.getMessage());
				//e.printStackTrace();
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
	
	public boolean update(Object obj, String link) {
		Job job = (Job) obj;
		String sql = "UPDATE " 
					+ TABLE_NAME 
					+ " SET Job_Link = ?, Job_Title = ?, Company = ?, Company_Link = ?, Location = ?,"
					+ " Job_Description = ?"
					+ " WHERE Job_Link = ?";
		
		PreparedStatement pstmt = null;
        if(conn == null)
        	connect();
        if(conn != null) {
	        try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, job.getJobLink());
				pstmt.setString(2, job.getJobTitle());
				pstmt.setString(3, job.getCompany());
				pstmt.setString(4, job.getCompanyLink());
				pstmt.setString(5, job.getLocation());
				pstmt.setString(6, job.getJobDescription());
				pstmt.setString(7, link);
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
