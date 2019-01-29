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
import org.json.JSONObject;

public class ApiClient {
	private Preferences prefs;
	private String domain;
	private String api = "lin-api";
	String name;
	String limits;
	String usage;
	String expaired;
	
	public ApiClient() {
		prefs = Preferences.userRoot().node("db");
		this.domain = prefs.get("dataserver", "http://localhost");
	}

	public String userAuth(String email, String password) {
		String responseString = null;
		try {

			//URL url = new URL("http://localhost/lin-api/user/login.php");
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
		
		// extracting data from response 
		JSONObject obj = new JSONObject(responseString);
	    String res = obj.getString("response");
	    System.out.println(res);

	    if(res.equalsIgnoreCase("ok")) {
		    JSONArray user = obj.getJSONArray("user");
		    
		    for (int i = 0; i < user.length(); i++)
		    {
		        name = user.getJSONObject(i).getString("name");
		        limits = user.getJSONObject(i).getString("limits");
		        usage = user.getJSONObject(i).getString("usage");
		        expaired = user.getJSONObject(i).getString("expaired");
			    System.out.println(name + " : " + limits + " : " + usage + " : " + expaired);
		    }
	    }
	    else if(res.equalsIgnoreCase("failed")) return "User not found ";
	    else return "oops something went wrong";
	    	
	    if(!check(expaired)) return "Your license has expired";
	    else
	    	if(Integer.parseInt(usage) >= Integer.parseInt(limits)) return "Your limits has expired";
	  
		return "Welcome "+ name;
	}
	
	private boolean check(String dateDB){
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
