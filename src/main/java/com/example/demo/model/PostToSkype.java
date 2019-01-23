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

import com.seneca.TestWatson.WatsonConv;

import org.json.simple.JSONObject;


public class PostToSkype {

	private static final String USER_AGENT = "Mozilla/5.0";

	/*private static final String GET_URL = "http://localhost:9090/SpringMVCExample";*/

	private static final String POST_URL = "https://smba.trafficmanager.net/apis/v3/conversations/";
	public static final String PWD = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6Ino0NHdNZEh1OHdLc3VtcmJmYUs5OHF4czVZSSIsImtpZCI6Ino0NHdNZEh1OHdLc3VtcmJmYUs5OHF4czVZSSJ9.eyJhdWQiOiJodHRwczovL2FwaS5ib3RmcmFtZXdvcmsuY29tIiwiaXNzIjoiaHR0cHM6Ly9zdHMud2luZG93cy5uZXQvZDZkNDk0MjAtZjM5Yi00ZGY3LWExZGMtZDU5YTkzNTg3MWRiLyIsImlhdCI6MTUxNzg5MTUxMiwibmJmIjoxNTE3ODkxNTEyLCJleHAiOjE1MTc4OTU0MTIsImFpbyI6IlkyTmdZSml3cWpQbFdkNzYxZVVUbUNQYm1iOVBBUUE9IiwiYXBwaWQiOiIzMmYxZmQzNS01N2FlLTRlYTctYjlmZi0wYjFlMjEzMzRmOWEiLCJhcHBpZGFjciI6IjEiLCJpZHAiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC9kNmQ0OTQyMC1mMzliLTRkZjctYTFkYy1kNTlhOTM1ODcxZGIvIiwidGlkIjoiZDZkNDk0MjAtZjM5Yi00ZGY3LWExZGMtZDU5YTkzNTg3MWRiIiwidXRpIjoicmJDUkthUTJVVWVsMEowMUprTVpBQSIsInZlciI6IjEuMCJ9.OVoRN7JICrOkiJ3dGfVJ0R2CUr5oayO1I5wYNNQbj2fc9UNLjli9hnKSSfh5qkbaLzogjCpJWOmrzsN58R3A0zGvQ9zGHsjLKXlX586_x_CeaicsPuzEX85V9DQlrWcaDSTazqAQET61FWjxiJVTCfbgU_jxe8WyiJNAPwVbPWxr1ioNUkFz2N7DJLc1lMGoG27dywhvfVr5ko7go_nQN-UpDQnc-2uN1DZnr4eG3TEjIwezPDqCPU-vB4BGBetn_g5TLP7u6en9VYh1JdRXbneFSG0hc8WJo6zfN0DL3BnQ94aovVkvFaziITkbErqsFkmlvf9B9V69Pzn9v8bTLg";
	public static String BEARER_TOKEN = "grant_type=client_credentials&client_id=629afe20-87cc-41d1-8672-e5440e251ea8&client_secret=civoLMQF659]#fvmQJU59~^&scope=https%3A%2F%2Fapi.botframework.com%2F.default";
	public static String access_Token;
	
	public String recievePayload(String payload, String text) throws IOException, ParseException {

		String str = payload;
		Object ob = new JSONParser().parse(str);
		JSONObject jo = (JSONObject) ob;
		String type = (String) jo.get("type");
		System.out.println(type.equalsIgnoreCase("message"));
		if (type.equalsIgnoreCase("message")) {
		/*String text = (String) jo.get("text");*/
		String id = (String) jo.get("id");
		JSONObject Jfrom = (JSONObject) jo.get("from");
		String from_id = (String) Jfrom.get("id");
		String from_name = (String) Jfrom.get("name");
		JSONObject Jconversation = (JSONObject) jo.get("conversation");
		String conv_id = (String) Jconversation.get("id");
		JSONObject Jrecipient = (JSONObject) jo.get("recipient");
		String reci_id = (String) Jrecipient.get("id");
		System.out.println(id + " - " + from_id + " - " + conv_id + " - " + reci_id);
		
		GetBearer();
		System.out.println("GET DONE");
		
		sendPOST(text, id, from_id, from_name, conv_id, reci_id);
		/*sendPOST();
		System.out.println("POST DONE");*/
		}
		return "Done";
	}

	private static void sendPOST(String text, String id, String from_id, String from_name, String conv_id, String reci_id) throws IOException {
		String comp_url = POST_URL + conv_id + "/activities/" + id;
		URL obj = new URL(comp_url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		String password = "Bearer " +  access_Token;
		con.setRequestProperty("Authorization", password);
		String Msg = text;
		String post_data = "{ " +
			    "\"type\": \"message\"," + 
			    "\"from\": { " +
			       " \"id\": \"" + reci_id + "\", " +
			       " \"name\": \"DannyChatSkype\" " + 
			    "}," +
			    "\"conversation\": { " +
			       " \"id\": \"" + conv_id + "\" " +
			   "}, " +
			   "\"recipient\": { " +
			        "\"id\": \"" + from_id + "\", " +
			        "\"name\": \"" + from_name + "\" " +
			    "}, " +
			    "\"text\": \"" + Msg + "\"," +
			    "\"replyToId\": \"" + id + "\" " +
			"}";
		
		// For POST only - START
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(post_data.getBytes());
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
