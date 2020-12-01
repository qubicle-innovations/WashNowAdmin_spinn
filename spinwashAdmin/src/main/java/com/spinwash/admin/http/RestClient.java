package com.spinwash.admin.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.spinwash.admin.exception.*;


import android.util.Log;


public class RestClient {

	// private String result="";
	// private ResponseHandler <String> res=new BasicResponseHandler();
	// private List<NameValuePair> nameValuePairs = new
	// ArrayList<NameValuePair>(6);

	private List<NameValuePair> params;
	private static final String TAG = "RestClient";
	private String responseString = null;
	private boolean JSONEncode =false;
	private StatusLine statusLine;
	private String url;
	private int statusCode;
	private JSONObject jobject ;
	private InputStream inputStream;
	private boolean returnInputStream=false;
	private static final int TIMEOUT = 30000;
	

	public enum RequestMethod {
		GET, POST
	}

	public RestClient(String url) {
		this.url = url;
		Log.v(TAG, this.url);
		this.params = new ArrayList<NameValuePair>();
	}


	public RestClient(String url, boolean JSONEncode) {
		this.url = url;
		this.JSONEncode = JSONEncode;
		Log.v(TAG, this.url);
		jobject = new JSONObject();
		
	}

	public void execute(RequestMethod method) throws WashNowHttpException {
		if (method == RequestMethod.GET) {
			this.doGet();
		} else if (method == RequestMethod.POST) {
			this.doPost();
		}

	}
	
	public void executeForInputStream(RequestMethod method) throws WashNowHttpException {
		if (method == RequestMethod.GET) {
			this.doGet();
		} else if (method == RequestMethod.POST) {
			this.doPost();
		}

	}

	public String getResponseString() {
		//Log.v("Response", responseString);
		return responseString;

	}

	public void setResponseString(String responseString) {
		this.responseString = responseString;
	}

	public int getStatusCode() {
		return statusCode;
	}
	
	public StatusLine getStatusLine(){
		return statusLine;
	}
	
	

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	public InputStream getContent(){
		return inputStream;
	}
	
	public void setRequestForInputStream(boolean requireInputStream){
		returnInputStream=requireInputStream;
	}

	private void doPost() throws WashNowHttpException {
		HttpClient client = MyHTTPClient.getInstance();
		HttpPost post = new HttpPost(this.url);
		HttpParams httpParams = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT);

		try {
			if(!this.params.isEmpty()) {
				post.setEntity(new UrlEncodedFormEntity(this.params, HTTP.UTF_8));
				for (NameValuePair nvp : this.params) {
					Log.e(TAG, "Setting " + nvp.getName()
							+ " in the request body with value " + nvp.getValue());
				}
			}
			if(null!=jobject) {
				String message = jobject.toString();
				post.setEntity(new StringEntity(message, "UTF8"));
				post.setHeader("Content-type", "application/json");
			        
			}
			HttpResponse response = client.execute(post);
			  statusLine = response.getStatusLine();
			  Log.v("wtf", statusLine.getStatusCode()+"'");
			Log.e(TAG,
					statusLine.getStatusCode() + " : "
							+ statusLine.getReasonPhrase() + " : "
							+ statusLine.getProtocolVersion()); 
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				if(returnInputStream){
					inputStream=response.getEntity().getContent();
				}else{
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
					Log.i(TAG, "ResponseCode: " + statusCode);
					Log.i(TAG, "Response: " + responseString);
				}
				
			
			} else {
				// Closes the connection.
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				responseString = out.toString();
				Log.i(TAG, "ResponseCode: " + statusCode);
				Log.i(TAG, "Response: " + responseString);
				response.getEntity().getContent().close();
				/*
				 * MyLog.e("Response:", "> " + statusLine.getReasonPhrase() +
				 * "###" + response.getEntity().getContent());
				 */
				throw new WashNowHttpException(statusLine.getStatusCode(),
						"Server error");
			}

		} catch (ClientProtocolException exception) {
			Log.e(TAG + "1", exception.getMessage());
			throw new WashNowHttpException(
					Utils.CLIENT_PROTOCOL_EXCEPTION_STATUS_CODE,
					"ClientProtocolException error");
		} catch (IOException e) {
			Log.e(TAG + "2", e.getMessage());
			throw new WashNowHttpException(
					Utils.CONNECTION_TIME_OUT_STATUS_CODE, "Network error");
		}

	}
	
	

	private void doGet() throws WashNowHttpException {
		HttpClient httpclient = MyHTTPClient.getInstance();
		HttpResponse response;
		try {

			response = httpclient.execute(new HttpGet(url));
			statusLine = response.getStatusLine();
			
			statusCode =statusLine.getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				
				if(returnInputStream){
					inputStream=response.getEntity().getContent();
				}else{
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
					Log.i(TAG, "ResponseCode: " + statusCode);
					Log.i(TAG, "Response: " + responseString);
				}
			
			} else {
				// Closes the connection.
				//response.getEntity().getContent().close();
				// throw new IOException(statusLine.getReasonPhrase());
				throw new WashNowHttpException(statusCode, "Server error");
			}
		} catch (ClientProtocolException exception) {
			Log.e(TAG + "1", exception.getMessage());
			throw new WashNowHttpException(
					Utils.CLIENT_PROTOCOL_EXCEPTION_STATUS_CODE,
					"ClientProtocolException error");
		} catch (IOException e) {
			Log.e(TAG + "2", e.getMessage());
			throw new WashNowHttpException(
					Utils.CONNECTION_TIME_OUT_STATUS_CODE, "Network error");
		}

	}

	public void addParam(String name, String value) {
		if(!JSONEncode) {
			this.params.add(new BasicNameValuePair(name, value));
			Log.i("added", "added");
		}else {
			try {
				this.jobject.put(name, value);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	

}
