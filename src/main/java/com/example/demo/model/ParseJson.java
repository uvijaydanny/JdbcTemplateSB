package com.example.demo.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.parser.*;
import org.json.simple.JSONObject;


public class ParseJson {

	private static final String USER_AGENT = "Mozilla/5.0";

	/*private static final String GET_URL = "http://localhost:9090/SpringMVCExample";*/

	private static final String POST_URL = "https://smba.trafficmanager.net/apis/v3/conversations/29:1nyFt9K9wtG4IcxwC0dqJSPqNcgpAuKUPATVws8E5vVk/activities/1517835664677";

	private static final String POST_PARAMS = "{ " +
    "\"type\": \"message\"," + 
    "\"from\": { " +
       " \"id\": \"28:32f1fd35-57ae-4ea7-b9ff-0b1e21334f9a\", " +
       " \"name\": \"DannyChatSkype\" " + 
    "}," +
    "\"conversation\": { " +
       " \"id\": \"29:1nyFt9K9wtG4IcxwC0dqJSPqNcgpAuKUPATVws8E5vVk\" " +
   "}, " +
   "\"recipient\": { " +
        "\"id\": \"29:1nyFt9K9wtG4IcxwC0dqJSPqNcgpAuKUPATVws8E5vVk\", " +
        "\"name\": \"Vijay Danny\" " +
    "}, " +
    "\"text\": \"Hi Danny\"," +
    "\"replyToId\": \"1517835664677\" " +
"}";
	public static final String PWD = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Ino0NHdNZEh1OHdLc3VtcmJmYUs5OHF4czVZSSIsImtpZCI6Ino0NHdNZEh1OHdLc3VtcmJmYUs5OHF4czVZSSJ9.eyJhdWQiOiJodHRwczovL2FwaS5ib3RmcmFtZXdvcmsuY29tIiwiaXNzIjoiaHR0cHM6Ly9zdHMud2luZG93cy5uZXQvZDZkNDk0MjAtZjM5Yi00ZGY3LWExZGMtZDU5YTkzNTg3MWRiLyIsImlhdCI6MTUxNzg5MTUxMiwibmJmIjoxNTE3ODkxNTEyLCJleHAiOjE1MTc4OTU0MTIsImFpbyI6IlkyTmdZSml3cWpQbFdkNzYxZVVUbUNQYm1iOVBBUUE9IiwiYXBwaWQiOiIzMmYxZmQzNS01N2FlLTRlYTctYjlmZi0wYjFlMjEzMzRmOWEiLCJhcHBpZGFjciI6IjEiLCJpZHAiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC9kNmQ0OTQyMC1mMzliLTRkZjctYTFkYy1kNTlhOTM1ODcxZGIvIiwidGlkIjoiZDZkNDk0MjAtZjM5Yi00ZGY3LWExZGMtZDU5YTkzNTg3MWRiIiwidXRpIjoicmJDUkthUTJVVWVsMEowMUprTVpBQSIsInZlciI6IjEuMCJ9.OVoRN7JICrOkiJ3dGfVJ0R2CUr5oayO1I5wYNNQbj2fc9UNLjli9hnKSSfh5qkbaLzogjCpJWOmrzsN58R3A0zGvQ9zGHsjLKXlX586_x_CeaicsPuzEX85V9DQlrWcaDSTazqAQET61FWjxiJVTCfbgU_jxe8WyiJNAPwVbPWxr1ioNUkFz2N7DJLc1lMGoG27dywhvfVr5ko7go_nQN-UpDQnc-2uN1DZnr4eG3TEjIwezPDqCPU-vB4BGBetn_g5TLP7u6en9VYh1JdRXbneFSG0hc8WJo6zfN0DL3BnQ94aovVkvFaziITkbErqsFkmlvf9B9V69Pzn9v8bTLg";
	public static final String BEARER_TOKEN = "grant_type=client_credentials&client_id=32f1fd35-57ae-4ea7-b9ff-0b1e21334f9a&client_secret=augPPOYTA91|$?yfsdG032!&scope=https%3A%2F%2Fapi.botframework.com%2F.default";
	public static String access_Token;
	
	public static void main(String[] args) throws IOException, ParseException {

		GetBearer();
		System.out.println("GET DONE");
		
		/*sendPOST();
		System.out.println("POST DONE");*/
	}

	private static void sendPOST() throws IOException {
		URL obj = new URL(POST_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Authorization", PWD);

		// For POST only - START
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(POST_PARAMS.getBytes());
		os.flush();
		os.close();
		// For POST only - END

		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
		} else {
			System.out.println("POST request not worked");
		}
	}
	/* Get the unique Bearer Token  which should be used when posting the data to Skype*/
	private static void GetBearer() throws IOException, ParseException 
	{
		URL obj = new URL("https://login.microsoftonline.com/botframework.com/oauth2/v2.0/token");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("Host", "login.microsoftonline.com");

		// For POST only - START
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(BEARER_TOKEN.getBytes());
		os.flush();
		os.close();
		// For POST only - END

		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
			String str = response.toString();
			Object ob = new JSONParser().parse(str);
			JSONObject jo = (JSONObject) ob;
			access_Token = (String) jo.get("access_token");
			System.out.println(access_Token);
			
		} else {
			System.out.println("POST request not worked");
		}
	}
}
