package com.braedonaschmidt.weathernotif;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

public final class QueryUtils {
	private QueryUtils() {} //Private constructor to keep a QueryUtils object from being created
	
	//Main method that sends data, the rest are just needed for this one
	public static Weather retrieveWeather(String urlStr) {
		URL url = createURL(urlStr);
		String jsonResponse = null;
		
		try {
			jsonResponse = makeHttpRequest(url);
		}
		catch (IOException e) {
			//todo: handle this
			//Error closing input stream
		}
		
		return extractFromJson(jsonResponse);
	}
	
	//converting String-form url to URL object; handle try/catch
	private static URL createURL(String url) {
		URL result = null;
		
		try {
			result = new URL(url);
		}
		catch (MalformedURLException e) {
			//todo: handle this
			//Error creating URL
		}
		
		return result;
	}
	
	private static String makeHttpRequest(URL url) throws IOException {
		String jsonResponse = "";
		if (url == null) return jsonResponse; //return nothing if bad URL
		
		HttpURLConnection urlConnection = null;
		InputStream inputStream = null;
		
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setReadTimeout(10000);
			urlConnection.setConnectTimeout(15000);
			urlConnection.setRequestMethod("GET");
			urlConnection.connect();
			
			if (urlConnection.getResponseCode() == 200) {
				inputStream = urlConnection.getInputStream();
				jsonResponse = readFromStream(inputStream);
			}
			else {
				//todo: handle this
				//Error response code: urlConnection.getResponseCode()
			}
		}
		catch (IOException e) {
			//todo: handle this
			//Problem retrieving the earthquake JSON results
		}
		finally {
			if (urlConnection != null)
				urlConnection.disconnect();
			if (inputStream != null)
				inputStream.close();
		}
		
		return jsonResponse;
	}
	
	private static String readFromStream(InputStream stream) throws IOException {
		StringBuilder output = new StringBuilder(); //faster to use this rather than String because it's mutable (doesn't recreate itself every time)
		
		if (stream != null) {
			InputStreamReader isReader = new InputStreamReader(stream, Charset.forName("UTF-8"));
			BufferedReader reader = new BufferedReader(isReader);
			String line = reader.readLine();
			while (line != null) {
				output.append(line);
				line = reader.readLine();
			}
		}
		
		return output.toString();
	}
	
	private static Weather extractFromJson(String weatherJson) {
		if (TextUtils.isEmpty(weatherJson))
			return null;
		
		Weather weather = new Weather(0, 0, "null");
		
		try {
			JSONObject jsonObject = new JSONObject(weatherJson);
			JSONObject main = jsonObject.getJSONObject("main");
			
			weather.setHigh(main.getDouble("temp_max"));
			weather.setLow(main.getDouble("temp_min"));
			weather.setDesc(jsonObject.getJSONArray("weather").getJSONObject(0).getString("description"));
		}
		catch (JSONException e) {
			//todo: handle this
			//Problem parsing the earthquake JSON results
		}
		
		return weather;
	}
}