package com.test.LoginTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class RestAPI {




	private int timeout = 3000;
	private HttpClient httpClient;
	private HttpPost httpPost;
	private HttpGet httpGet;
	private HttpResponse httpResponse;

	private String error;
	private String responseString = null;

	private ArrayList data;

	public RestAPI(){
		this.httpClient = new DefaultHttpClient();
		this.data = new ArrayList();
	}

	public void setTimeout(int timeout){
		this.timeout = timeout;
	}

	public void post(String url){
		this.post(url, null);
	}

	public void post(String url, ArrayList data){
		this.httpPost = new HttpPost(url);
		this.httpClient.getParams().setParameter("http.socket.timeout", this.timeout);
		try {
			this.data = data;
			if(data != null)
				this.httpPost.setEntity(new UrlEncodedFormEntity(this.data));
			this.httpResponse = this.httpClient.execute(this.httpPost);
//			System.out.println("url"+url);
//			System.out.println("Post parameters : " + this.httpPost.getEntity());
//			System.out.println("Response Code : " + this.httpResponse.getStatusLine().getStatusCode());
		} catch (UnsupportedEncodingException e) {
			this.error = e.getMessage();
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			this.error = e.getMessage();
			e.printStackTrace();
		} catch (IOException e) {
			this.error = e.getMessage();
			e.printStackTrace();
		}
	}

	public void get(String url){
		this.httpGet = new HttpGet(url);
		this.httpClient.getParams().setParameter("http.socket.timeout", this.timeout);
		try {
			this.httpResponse = this.httpClient.execute(this.httpGet);
		} catch (UnsupportedEncodingException e) {
			this.error = e.getMessage();
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			this.error = e.getMessage();
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e);
			this.error = e.getMessage();
			e.printStackTrace();
		}
	}	

	public String getError() {
		return this.error;
	}

	public String getResponseString(){
		if(this.httpResponse!=null){
			try {
				HttpEntity httpEntity = this.httpResponse.getEntity();
				InputStream is = httpEntity.getContent();
	            BufferedReader reader = new BufferedReader(
	            	new InputStreamReader(is, "euc-kr"), 8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	            is.close();
	            this.responseString = sb.toString();
	            return this.responseString;
	        } catch (Exception e) {
	        	this.error = e.getMessage();
	            Log.e("pnote", "Response string buffer error. " + e.getMessage());
	        }
		} 
		return null;
	}

	public JSONObject getResponseJSONObject(){
		String JSONString = this.getResponseString();
		if(JSONString != null)
			Log.i("pnote", JSONString);
		else Log.i("pnote", "JSONString is null");
		if(JSONString != null) {
			JSONObject jObj;
			try {
				jObj = new JSONObject(JSONString);
				return jObj;
			} catch (JSONException e) {
				this.error = e.getMessage();
				e.printStackTrace();
			}
		}
		return null;
	}

	public String getResponseText(){
		return this.responseString;
	}
}