package com.modintro.restfulclient.model;

import java.io.StringReader;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import test.jaxb.Employees;
import test.jaxb.Employees.Employee;

public class TableModel extends AbstractTableModel implements Constants {

	public TableModel(String xml) throws Exception {
		// ServerResponseParser srp = new ServerResponseParser(xml);
		
		dataList = new ArrayList<ArrayList<Object>>();
		
		HTTPRequest httpReq = new HTTPRequest(DEPT_URL);
		String rows = null;
		try {
			rows = httpReq.getData();
			JAXBContext jbc = JAXBContext.newInstance("test.jaxb");
			Unmarshaller u = jbc.createUnmarshaller();
			
			Employees emp = (Employees)u.unmarshal(new StreamSource(new StringReader(rows)));
		    ArrayList<Employee> list = (ArrayList<Employee>) emp.getEmployee();
		    int length = list.size();
		    for(int i = 0; i < length; i++) {
		    	// dataTable.get(i).add(0, list.get)
		    }		   			
		} catch(Exception e) {
			System.out.println(e.getMessage());	
		}
		
		// columnIdentifiers = srp.getColumnNames();
		// data = srp.getData();
	}
	
	public TableModel(Object[][] data, Object[] columnNames) {
        setDataVector(data, columnNames);
    }
	
	public void setDataVector(Object[][] dataList, Object[] columnIdentifiers) {
        setDataVector(convertToArrayList(dataList), convertToArrayList(columnIdentifiers));
    }
	
	public void setDataVector(ArrayList<ArrayList<Object>> dataList, ArrayList<Object> columnIdentifiers) {
		if(dataList != null) {
			this.dataList = dataList;
		} else {
			this.dataList = new ArrayList<ArrayList<Object>>();
		}
		
		if(columnIdentifiers != null) {
			this.columnIdentifiers = columnIdentifiers;
		} else {
			this.columnIdentifiers = new ArrayList<Object>();
		}
       fireTableStructureChanged();
    }
	
	public int getColumnCount() {
		return columnIdentifiers.size();
	}
	
	public int getRowCount() {
		return data.length;
	}
	
	public String getColumnName(int col) {
		return (String)columnIdentifiers.get(col);		
	}
	
	public Object getValueAt(int row, int col) {
		ArrayList<Object> rowList = (ArrayList<Object>)dataList.get(row);
        return rowList.get(col);
	}
	
	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}
	
	// Implement if table is editable
	public boolean isCellEditable(int row, int col) {
		if(col == 0 || col == 5 )
			return false;
		return true;
	}
	
	// Implement if table's data can change
	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		dataList.get(row).add(col, (String)value);
		fireTableCellUpdated(row, col);
	}
	
	public void removeRow(int row) {
        dataList.remove(row);
        fireTableRowsDeleted(row, row);
    }
	
	public void insertRow(int row, Object[] rowData) {
        insertRow(row, convertToArrayList(rowData));
    }
	
	public void insertRow(int row, ArrayList<Object> rowData) {
        dataList.add(row, rowData);
        fireTableRowsInserted(row, row);
    }
	
	// Protected methods
	protected ArrayList<Object> convertToArrayList(Object[] anArray) {
		if(anArray == null) {
			return null;
		}
		
		ArrayList<Object> a = new ArrayList<Object>(anArray.length);
		for(Object o : anArray ) {
			a.add(o);
		}
		return a;
	}
	
	protected ArrayList<ArrayList<Object>> convertToArrayList(Object[][] anArray) {
		if(anArray == null) {
			return null;
		}
		
		ArrayList<ArrayList<Object>> a = new ArrayList<ArrayList<Object>>(anArray.length);
		for(Object[] o : anArray) {
			a.add(convertToArrayList(o));
		}
		return a;
	}
		
	private Object[][] data;
	private ArrayList<ArrayList<Object>> dataList;
	private ArrayList<Object> columnIdentifiers;
}
