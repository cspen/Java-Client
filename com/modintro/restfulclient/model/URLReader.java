package com.modintro.restfulclient.model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLReader {
	
	public static void main(String[] args) {
		try {
			URLReader reader = new URLReader("http://localhost/GEM/rest/employees/");
			String data = "lname=Reys&fname=Bart";
			data += "&dept=Accounting&salary=20000&ftime=0&hdate=2018-12-31";
			String str = reader.postData(data);
			System.out.print(str);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public URLReader(String url) {
		this.url = url;
	}
	
	public String getData() throws Exception {
		return request("GET", null, null);
	}
	
	public String getData(int id) throws Exception {
		return request("GET", id + "", null);
	}
	
	public String getData(int page, int pagesize) throws Exception {
		String params = "?page=" + page + "&pagesize=" + pagesize;
		return request("GET", params, null);
	}
	
	public String getData(int page, int pageSize,
			String sortBy, Boolean sortOrder) throws Exception{
		String params = "?page=" + page + "&pagesize=" + pageSize
				+ "&sort=" + sortBy + "&order=" + sortOrder;
		return request("GET", params, null);
	}
	
	public String postData(String data) throws Exception {
		return request("POST", null, data);
	}
	
	public void deleteData(int id) throws Exception {
		request("DELETE", id + "", null);
	}
	
	public String putData(int id, String data) throws Exception {
		return request("PUT", id + "", data);
	}
	
	private String request(String httpVerb, String params, String data) throws Exception {
		URL url;
		if(params != null) {
			url = new URL(this.url + params);
		} else {
			url = new URL(this.url);
		} 
        HttpURLConnection URLConn = (HttpURLConnection)url.openConnection();
        URLConn.setRequestMethod(httpVerb);
        if(httpVerb.equals("POST") || httpVerb.equals("PUT")) {
        	URLConn.setDoOutput(true);
        	if(httpVerb.equals("POST")) {
        		URLConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        	}
        	try(DataOutputStream wr = new DataOutputStream(URLConn.getOutputStream())) {
        		wr.write(data.getBytes());
        	}
        } else {
        	URLConn.setRequestProperty("Accept", "application/xml");
        }
        BufferedReader in = new BufferedReader(
        		new InputStreamReader(URLConn.getInputStream()));

        String inputLine;
        StringBuilder input = new StringBuilder();
        while ((inputLine = in.readLine()) != null)
            input.append(inputLine);
        in.close();
        System.out.println(" *** " + URLConn.getHeaderField("Etag"));
		return input.toString();
	}	
	
	private String url;
}
