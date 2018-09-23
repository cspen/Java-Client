package com.modintro.restfulclient.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class URLReader {

	public URLReader(String url) throws MalformedURLException {
		this.url = url;
	}
	
	public String getData() {
		return "";
	}
	
	public String postData(String data) {
		return "";
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
	
	private String request(String httpVerb, String params, String data) throws Exception {
		URL url;
		if(params != null) {
			url = new URL(this.url + params);
		} else {
			url = new URL(this.url);
		}
        HttpURLConnection URLConn = (HttpURLConnection)url.openConnection();
        URLConn.setRequestMethod(httpVerb);
        URLConn.setRequestProperty("Accept", "application/xml");
        BufferedReader in = new BufferedReader(
        		new InputStreamReader(URLConn.getInputStream()));

        String inputLine;
        StringBuilder input = new StringBuilder();
        while ((inputLine = in.readLine()) != null)
            input.append(inputLine);
        in.close();
		return "";
	}	
	
	private String url;
}
