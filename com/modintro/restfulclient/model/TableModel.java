package com.modintro.restfulclient.model;

import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel {

	public TableModel() {
		ServerResponseParser srp = new ServerResponseParser();
		columnNames = srp.getColumnNames();
		data = srp.getData();
	}
	
	public int getColumnCount() {
		return columnNames.length;
	}
	
	public int getRowCount() {
		return data.length;
	}
	
	public String getColumnName(int col) {
		return columnNames[col];		
	}
	
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}
	
	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}
	
	// Implement if table is editable
	public boolean isCellEditable(int row, int col) {
		return true;
	}
	
	// Implement if table's data can change
	public void setValueAt(Object value, int row, int col) {
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}
	
	
	
	
	private Object[][] data;
	private String[] columnNames = {"ID", "First Name", "Last Name" };
}
