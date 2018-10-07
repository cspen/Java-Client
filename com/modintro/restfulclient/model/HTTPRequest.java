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
	
	
	public static void main(String[] args) {
		try {
			HTTPRequest reader = new HTTPRequest("http://localhost/GEM/rest/employees/");
			// String data = "lname=Reys&fname=Bart";
			// data += "&dept=Accounting&salary=20000&ftime=0&hdate=2018-12-31";
			
			String json = "{\"lastname\":\"Brida\", \"firstname\":\"Elmer\"," +
					"\"department\":\"Sales\", \"fulltime\":\"0\", \"hiredate\":\"2018-09-02\"," +
					"\"salary\":\"75000\" }";
			// String str = reader.putData(70, json, "7647966b7343c29048673252e", null);
			String str = reader.getData(70, "ac627ab1ccbdb62ec96e702f07f6425b", "Sat, 03 Oct 2018 04:26:25 GMT");
			System.out.print("END " + str);
		} catch(Exception e) {
			System.out.println("CHEERS : " + e.getMessage());
			e.printStackTrace();
		}
	}
	
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
			String sortBy, Boolean sortOrder) throws Exception{
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
        	URLConn.setRequestProperty("If-None-Match", etag);
        if(lastModified != null)
        	URLConn.setRequestProperty("If-Modified-Since", lastModified);
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
       	URLConn.setRequestProperty("Etag", etag);
       	URLConn.setRequestProperty("Last-Modified", lastModified);
        
	}
	
	public String putData(int id, String data, String etag, String lastModified) throws Exception {
		URL url = new URL(this.url + id);
		HttpURLConnection URLConn = (HttpURLConnection)url.openConnection();
		URLConn.setDoOutput(true);
        URLConn.setRequestMethod("PUT");
        URLConn.setRequestProperty("Content-Type", "application/json");
        if(etag != null)
        	URLConn.setRequestProperty("Etag", etag);
        if(lastModified != null)
        	URLConn.setRequestProperty("Last-Modified", lastModified);
    	
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
        System.out.println("RESPONSE : " + this.responseCode);
    	BufferedReader in = new BufferedReader(
        	new InputStreamReader(URLConn.getInputStream()));
    	
    	String inputLine;
        StringBuilder input = new StringBuilder();
        while ((inputLine = in.readLine()) != null)
            input.append(inputLine);
        in.close();
        
        
        System.out.println("Last-Modified : " + URLConn.getHeaderField("Last-Modified"));
        
        return input.toString();
    }
    
    public int responseCode() {
    	return this.responseCode;
    }
    
    public String getMessage() {
    	return this.responseMessage;
    }
    
    public String etag() {
    	return this.etag();
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
