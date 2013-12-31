package com.test.LoginTest;

import android.os.AsyncTask;
import android.widget.Toast;

public class ThreadForRestAPI extends Thread{
	String url;
	public ThreadForRestAPI(String url){
		this.url=url;
	}
	public void run(){
		RestAPI r = new RestAPI();
    	r.get("http://google.com");
    	r.getResponseString();
    	String textRetrieved = r.getResponseText();
    	//Toast.makeText(Login.this,textRetrieved.substring(0, 10), Toast.LENGTH_SHORT).show();
	}

}
