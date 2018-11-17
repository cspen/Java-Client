package com.modintro.restfulclient.model;

/**
 * This ServerResponseParser is specifically for parser
 * an XML response from the server located at
 * http://modintro.com/employees/
 * 
 * The XML from the server is parsed and its data
 * is stored into two arrays to be passed to a 
 * DefaulTableModel constructor for use in a JTable.
 * A one dimensional array stores the XML tags as
 * column headers and a two dimensional array holds
 * the data.
 * 
 * @author Craig Spencer <craigspencer@modintro.com>
 * Last modified: 10/18/2018
 */

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class ServerResponseParser {
	
	public ServerResponseParser() {}
	
	public ServerResponseParser(String xml) throws Exception {
		 parseXML(xml);		
	}
	
	private void parseXML(String xml) throws Exception {
		Document doc = loadXMLFromString(xml);
			
		// Get column names
		Node node = doc.getFirstChild(); 
		Node nextNode = node.getFirstChild().getFirstChild();
		
		ArrayList<Object> list = new ArrayList<Object>();
		while(nextNode != null) {
			StringBuffer nodeName = new StringBuffer(nextNode.getNodeName());
			if(!nodeName.toString().equals("#text")) {	
				nodeName.setCharAt(0, Character.toUpperCase(nodeName.charAt(0)));
				list.add(nodeName.toString());
			} else {
				if(nextNode.getNodeValue() != null && nextNode.getNodeValue().trim().length() > 0) {
					list.add(nextNode.getNodeValue());
				} 
			}
			nextNode = nextNode.getNextSibling();
		}
		
		// Move column names from list to array
		int listSize = list.size();
		columnNames = new String[listSize];
		for(int i = 0; i < listSize; i++) {
			columnNames[i] = list.get(i);
		}
			
		// Get data
		list = new ArrayList<Object>();
		NodeList nodelist = doc.getElementsByTagName("*");
		for(int i = 0; i < nodelist.getLength(); i++) {
			Node currentNode = nodelist.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				Node temp = currentNode.getFirstChild();
				if(temp.getNodeType() == Node.TEXT_NODE) {
					if(temp.getNodeValue().trim().length() > 0) {
						String val = temp.getNodeValue();
						if(val.matches("^[0-9]+$")) { // is numeric?
							if(val.equals("0")) {
								list.add(new Boolean("false"));
							} else if(val.equals("1") 
									&& temp.getParentNode().getNodeName().equals("full_time")) {
								list.add(new Boolean("true"));
							} else {
								list.add(new Integer(val));
							}
						} else {
							list.add(temp.getNodeValue());
						}
					}
				}
			}
		}
		
		// Move data to 2D array, if necessary
		if(columnNames.length == 0) return;
		listSize = list.size()/columnNames.length;
		int index = 0;
		data = new Object[listSize][columnNames.length];
		for(int i = 0; i < listSize; i++) {
			for(int j = 0; j < columnNames.length; j++) {
				data[i][j] = list.get(index);
				index++;
			}
		}
			
		// Check
		/*
		for(int i = 0; i < columnNames.length; i++)
			System.out.print(columnNames[i] + " ");
		System.out.println("CHECK");
		for(int i = 0; i < data.length; i++) {
			for(int j = 0; j < data[0].length; j++) {
				System.out.print(data[i][j] + " ");
			}
			System.out.println();
		}
		*/
	}	   
	
	public static Document loadXMLFromString(String xml) throws Exception
	{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    InputSource is = new InputSource(new StringReader(xml));
	    return builder.parse(is);
	}
	
	public Object[] getColumnNames() {
		return columnNames;
	}
	
	public Object[][] getData() {
		return data;
	}
	
	private Object[] columnNames;
	private Object[][] data;
}