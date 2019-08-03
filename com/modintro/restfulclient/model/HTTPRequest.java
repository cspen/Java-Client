package com.modintro.restfulclient.model;

/**
 * The HTTPRequest class provides HTTP requests
 * to a web server. Each function returns the server response
 * as a String.
 * 
 * @author Craig Spencer <craigspencer@modintro.com>
 * Last-Modified: 9/30/2018
 */
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPRequest {
	
	public HTTPRequest(String url) {
		this.url = url;
		responseCode = 0;
		responseMessage = null;
		etag = null;
		lastModified = null;
	}
	
	public String getData() throws Exception {
		return request("GET", null);
	}
		
	public String getData(int page, int pagesize) throws Exception {
		String params = "?page=" + page + "&pagesize=" + pagesize;
		return request("GET", params);
	}
	
	public String getData(int page, int pageSize,
			String sortBy, Boolean sortOrder) throws Exception {
		String params = "?page=" + page + "&pagesize=" + pageSize
				+ "&sort=" + sortBy + "&order=" + sortOrder;
		return request("GET", params);
	}	

	public String getData(int id, String etag, String lastModified) throws Exception {
		URL url = new URL(this.url + id);
        	HttpURLConnection URLConn = (HttpURLConnection)url.openConnection();
        	URLConn.setRequestMethod("GET");
        	URLConn.setRequestProperty("Accept", "application/xml");
        	if(etag != null)
        		URLConn.setRequestProperty("If-Match", etag);
        	if(lastModified != null)
        		URLConn.setRequestProperty("If-Unmodified-Since", lastModified);
        	URLConn.connect();
		return urlResponseReader(URLConn);
	}
	
	public String postData(String data) throws Exception {
		URL url = new URL(this.url);
		HttpURLConnection URLConn = (HttpURLConnection)url.openConnection();
		URLConn.setDoOutput(true);
        	URLConn.setRequestMethod("POST");
        	URLConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    	
        	try(DataOutputStream wr = new DataOutputStream(URLConn.getOutputStream())) {
    			wr.write(data.getBytes());
    		}        
		return urlResponseReader(URLConn);
	}
	
	public void deleteData(int id, String etag, String lastModified) throws Exception {
		URL url = new URL(this.url + id);
        	HttpURLConnection URLConn = (HttpURLConnection)url.openConnection();
        	URLConn.setRequestMethod("DELETE");
        	URLConn.setRequestProperty("If-Match", etag);
       		URLConn.setRequestProperty("If-Unmodified-Since", lastModified);
       		URLConn.connect();
       		urlResponseReader(URLConn);
	}
	
	public String putData(int id, String data, String etag, String lastModified) throws Exception {
		URL url = new URL(this.url + id);
		HttpURLConnection URLConn = (HttpURLConnection)url.openConnection();
		URLConn.setDoOutput(true);
        URLConn.setRequestMethod("PUT");
        URLConn.setRequestProperty("Content-Type", "application/json");
        
        if(etag != null)
        	URLConn.setRequestProperty("If-Match", etag);
        if(lastModified != null)
        	URLConn.setRequestProperty("If-Unmodified-Since", lastModified);
        
       	try(DataOutputStream wr = new DataOutputStream(URLConn.getOutputStream())) {
    		wr.write(data.getBytes());
    	}		
		return urlResponseReader(URLConn);
	}	
	
	private String request(String httpVerb, String params) throws Exception {
		URL url;
		if(params != null) {
			url = new URL(this.url + params);
		} else {
			url = new URL(this.url);
		} 
        	HttpURLConnection URLConn = (HttpURLConnection)url.openConnection();
       		URLConn.setRequestMethod(httpVerb);
       		URLConn.setRequestProperty("Accept", "application/xml");
        
        	return urlResponseReader(URLConn);
	}     
        
    private String urlResponseReader(HttpURLConnection URLConn) throws Exception {
    	this.responseCode = URLConn.getResponseCode();
    	this.responseMessage = URLConn.getResponseMessage();
    	BufferedReader in = new BufferedReader(
       		new InputStreamReader(URLConn.getInputStream()));
    	
    	String inputLine;
       	StringBuilder input = new StringBuilder();
       	while ((inputLine = in.readLine()) != null)
           		input.append(inputLine);
       	in.close();
                
       	return input.toString();
    }
    
    	public int responseCode() {
    		return this.responseCode;
    	}
    
    	public String responseMessage() {
    		return this.responseMessage;
    	}
    
    	public String etag() {
    		return this.etag;
    	}
    
    	public String lastModified() {
    		return this.lastModified;
    	}
	
	private String url;
	private int responseCode;
	private String responseMessage;
	private String etag;
	private String lastModified;
}