package com.modintro.restfulclient.model;

/**
 * Table model for JTable.
 * 
 * The addUpdateListener and nofityUpdateListener methods are for
 * intercepting a table row update and allowing processing of the
 * input to take place before the change is committed to the table
 * and the original value is lost.
 * 
 * @author Craig Spencer <craigspencer@modintro.com>
 * Last Modified: 11/09/2018
 */

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel implements Constants {
	
	public TableModel(Object[][] data, Object[] columnNames) {
        setDataVector(data, columnNames);
    }
	
	public void setDataVector(Object[][] dataList, Object[] columnIdentifiers) {
        setDataVector(convertToVector(dataList), convertToVector(columnIdentifiers));
    }
	
	public void setDataVector(Vector<Vector<Object>> dataList, Vector<Object> columnIdentifiers) {
		if(dataList != null) {
			this.dataList = dataList;
		} else {
			this.dataList = new Vector<Vector<Object>>();
		}
		
		if(columnIdentifiers != null) {
			this.columnIdentifiers = columnIdentifiers;
		} else {
			this.columnIdentifiers = new Vector<Object>();
		}
        fireTableStructureChanged();
    }
	
	public int getColumnCount() {
		return columnIdentifiers.size();
	}
	
	public int getRowCount() {
		return dataList.size();
	}
	
	public String getColumnName(int col) {
		return columnIdentifiers.get(col).toString();		
	}
	
	public Object getValueAt(int row, int col) {
		Vector<Object> rowList = (Vector<Object>)dataList.get(row);
		return rowList.get(col);
	}
	
	public Class<?> getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}
	
	// Implement if table is editable
	public boolean isCellEditable(int row, int col) {
		if(col == 0 || col == 5 ) {
			return false;
		}
		return true;
	}
	
	// Implement if table's data can change
	@Override
	public void setValueAt(Object value, int row, int col) {
		Vector<Object> rowVector = (Vector<Object>)dataList.elementAt(row);
		Object obj = rowVector.get(col);
		
		// Prevent unnecessary updates - conserve bandwidth
		if(!value.equals(obj)) {
			if(!value.equals("")) { // Check for empty input				
				notifyUpdateListener(value, row, col);
			} 
		}
	}
	
	private void notifyUpdateListener(Object value, int row, int col) {
		this.updateListener.update(value, row, col);
	}
	
	public void addUpdateListener(UpdateListener ul) {
		this.updateListener = ul;
	}
	
	public void updateValueAt(Object value, int row, int col) {
		Vector<Object> rowVector = (Vector<Object>)dataList.elementAt(row);
		rowVector.setElementAt(value, col);
		fireTableCellUpdated(row, col);
	}
	
	public void removeRow(int row) {
		// Remove row from server
        dataList.removeElementAt(row);
        fireTableRowsDeleted(row, row);
    }
	
	public void insertRow(int row, Object[] rowData) {
		// Insert row on the server
        insertRow(row, convertToVector(rowData));
    }
	
	public void insertRow(int row, Vector<Object> rowData) {
        dataList.add(row, rowData);
        fireTableRowsInserted(row, row);
    }
	
	// Protected methods
	protected Vector<Object> convertToVector(Object[] anArray) {
		if(anArray == null) {
			return null;
		}
		
		Vector<Object> a = new Vector<Object>(anArray.length);
		for(Object o : anArray ) {
			a.add(o);
		}
		return a;
	}
	
	protected Vector<Vector<Object>> convertToVector(Object[][] anArray) {
		if(anArray == null) {
			return null;
		}
		
		Vector<Vector<Object>> a = new Vector<Vector<Object>>(anArray.length);
		for(Object[] o : anArray) {
			a.add(convertToVector(o));
		}
		return a;
	}
		
	private Vector<Vector<Object>> dataList;
	private Vector<Object> columnIdentifiers;
	private UpdateListener updateListener = null;
}