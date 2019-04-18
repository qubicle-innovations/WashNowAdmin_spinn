package com.washnow.admin.http;

import org.apache.http.impl.client.DefaultHttpClient;

public class MyHTTPClient {
	
	public static DefaultHttpClient instanCE;
	private static final String TAG = "&&----HTTPClient-----**";
	public static DefaultHttpClient getInstance(){
	    if(instanCE == null){
	    	CreateInstance();
	    }
	    return instanCE;
	}
	
	public static void CreateInstance(){
		 instanCE = new DefaultHttpClient();
	}

	
}
