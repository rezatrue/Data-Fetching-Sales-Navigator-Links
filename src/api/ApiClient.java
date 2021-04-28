package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.prefs.Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiClient {
	private Preferences prefs;
	private String domain;
	private String api = "api/lin";
	private String name;
	private int limits;
	private int usage;
	private String expaired;
	
	public ApiClient() {
		prefs = Preferences.userRoot().node("db");
		this.domain = prefs.get("dataserver", "http://localhost");
	}

	public int getLimits() {
		return limits;
	}

	public int getUsage() {
		return usage;
	}



	public String userAuth(String email, String password) {
		String responseString = null;
		try {

			//URL url = new URL("http://localhost/api/lin/user/login.php");
			URL url = new URL(domain + "/" + api + "/user/login.php");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			String namekey = "user_email";
			String nameValue =  email ;
			String passwordkey = "user_password";
			String passwordValue =  password ;
			
			String input = "{\"" + namekey + "\":\""+ nameValue + "\",\""+ passwordkey + "\":\""+ passwordValue + "\"}";
//			String input = "{\"user_email\":\"reza@mail.com\",\"user_password\":\"123\"}";

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			/*
			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}
			*/
			
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			StringBuilder sb = new StringBuilder();
			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				//System.out.println(output);
				sb.append(output);
			}
			responseString = sb.toString();
			conn.disconnect();

		} catch (MalformedURLException e) {
			return e.getMessage();
		} catch (IOException e) {
			return e.getMessage();
		}
		System.out.println(responseString);
		// extracting data from response 
		JSONObject obj;
		try {
			obj = new JSONObject(responseString);
		} catch (JSONException e) {
			// Server errors 
			return e.getMessage();
		}
	    String res = obj.getString("response");
	    System.out.println(res);

	    if(res.equalsIgnoreCase("ok")) {
		    JSONArray user = obj.getJSONArray("user");
		    
		    for (int i = 0; i < user.length(); i++)
		    {
		        name = user.getJSONObject(i).getString("name");
		        limits = Integer.parseInt(user.getJSONObject(i).getString("limits"));
		        usage = Integer.parseInt(user.getJSONObject(i).getString("usage"));
		        expaired = user.getJSONObject(i).getString("expaired");
			    System.out.println(name + " : " + limits + " : " + usage + " : " + expaired);
		    }
	    }
	    else if(res.equalsIgnoreCase("failed")) return "User not found ";
	    else return "oops something went wrong";
	    	
	    if(!checkExpairedDate(expaired)) return "Your license has expired";
	    if(usage >= limits) return "Your limits has expired";
	  
		return "Welcome "+ name;
	}
	
	public int updateUseage(int newUse) {
		System.out.println(newUse + "-");
		String email = prefs.get("user", ""); 
		String password = prefs.get("password", "");
		
		String responseString = null;
		try {

			//URL url = new URL("http://localhost/api/lin/user/updateusage.php");
			URL url = new URL(domain + "/" + api + "/user/updateusage.php");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");

			String namekey = "user_email";
			String nameValue =  email ;
			String passwordkey = "user_password";
			String passwordValue =  password ;
			String useagekey = "new_usage";
			int useageValue =  newUse ;
			
			String input = "{\"" + namekey + "\":\""+ nameValue + "\",\""+ passwordkey + "\":\""+ passwordValue + "\",\""+ useagekey + "\":\""+ useageValue + "\"}";
//			String input = "{\"user_email\":\"reza@mail.com\",\"user_password\":\"123\",\""+ useagekey + "\":\""+ useageValue + "\"}";

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			/*
			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}
			*/
			
			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			StringBuilder sb = new StringBuilder();
			String output;
			System.out.println("Output from Server (useage update).... \n");
			while ((output = br.readLine()) != null) {
				//System.out.println(output);
				sb.append(output);
			}
			responseString = sb.toString();
			conn.disconnect();

		} catch (MalformedURLException e) {
			System.err.println(e.getMessage());
			return -1;
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return -1;
		}
		
		// extracting data from response 
		JSONObject obj = new JSONObject(responseString);
	    String res = obj.getString("response");
	    System.out.println(res);

	    if(res.equalsIgnoreCase("ok")) {
		    JSONArray user = obj.getJSONArray("user");
		    
		    for (int i = 0; i < user.length(); i++)
		    {
		        name = user.getJSONObject(i).getString("name");
		        limits = Integer.parseInt(user.getJSONObject(i).getString("limits"));
		        usage = Integer.parseInt(user.getJSONObject(i).getString("usage"));
		        expaired = user.getJSONObject(i).getString("expaired");
			    System.out.println(name + " : " + limits + " : " + usage + " : " + expaired);
		    }
	    }else {
	    	System.err.println(res.toString());
	    	return -1;
	    }
	    // bypass error code , if limit accessed by one
	    int returnCode = limits - usage;
		return returnCode == -1 ? 0 : returnCode;
	}

	
	
	
	private boolean checkExpairedDate(String dateDB){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		Date dbDate = null;
		try {
			dbDate = dateFormat.parse(dateDB);
			Date timeNow = cal.getTime();
			if(dbDate.compareTo(timeNow) > 0) return true;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
}
