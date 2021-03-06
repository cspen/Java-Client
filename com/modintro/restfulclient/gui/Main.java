/**
 * GUI for JTable application.
 * 
 * @author Craig Spencer <craigspencer@modintro.com>
 * Last Modified: 11/8/2018
 */
package com.modintro.restfulclient.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.StringReader;
import java.text.Format;
import java.text.NumberFormat;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import com.modintro.restfulclient.model.Constants;
import com.modintro.restfulclient.model.HTTPRequest;
import com.modintro.restfulclient.model.TableModel;
import com.modintro.restfulclient.model.UpdateListener;

import com.modintro.restfulclient.model.ServerResponseParser; 

public class Main implements Constants {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                createAndShowGUI();
	            }
	        });
	}
	
	private static void createAndShowGUI() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Center the window
        Toolkit tKit = frame.getToolkit();
        Dimension wndSize = tKit.getScreenSize();
        frame.setBounds(wndSize.width/4, wndSize.height/4,	// Position
        		wndSize.width/2, wndSize.height/3);      	// Size
        
        // Add menu bar
        JMenuBar menuBar = new JMenuBar();
        NewAction newAct = new NewAction("New", "Create a new record", new Integer(KeyEvent.VK_N));
        DeleteAction deleteAct = new DeleteAction("Delete", "Delete a record", new Integer(KeyEvent.VK_D));
        RefreshAction refAct = new RefreshAction("Refresh", "Refresh the table", new Integer(KeyEvent.VK_R));
        ExitAction extAct = new ExitAction("Exit", new Integer(KeyEvent.VK_E));
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(newAct);        
        fileMenu.add(deleteAct);
        fileMenu.addSeparator();
        fileMenu.add(refAct);
        fileMenu.addSeparator();
        fileMenu.add(extAct);
               
        JMenu helpMenu = new JMenu("Help");
        HowToAction howToAct = new HowToAction("How to...", "Help", new Integer(KeyEvent.VK_T));
        helpMenu.add(howToAct);
        AboutAction aboutAct = new AboutAction("About", "", KeyEvent.VK_A);
        helpMenu.add(aboutAct);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        frame.setJMenuBar(menuBar);
        
        StatusBar statusBar = new StatusBar();
        frame.getContentPane().add(statusBar, BorderLayout.SOUTH);
        
        // Add JTable
        try {
        	tmodel = getTableModel();        	
        } catch (Exception e) {
        	// System.out.println(e.getMessage());
        	String msg = "\nClosing Application";
        	JOptionPane.showMessageDialog(frame,  e.toString() + " " + msg,
        			"Error", JOptionPane.ERROR_MESSAGE);
        	
        	System.exit(0);
        }
        
        jTable = new JTable(tmodel);
        jTable.setRowSorter(new TableRowSorter<TableModel>(tmodel));        
        
        initTable();
        
		jTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
		jTable.setFillsViewportHeight(true);
		scrollPane = new JScrollPane(jTable);		
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		// Add dialog boxes
		newRecDialog = new NewRecordDialog(frame, tmodel);
		helpDialog = new HelpDialog(frame);
        
        frame.setVisible(true);
        // frame.pack();
	}
	
	public static void initTable() {
		JComboBox<String> deptCombo = NewRecordDialog.createDeptCombo();
        TableColumnModel colModel = jTable.getColumnModel();
        
        // Set combo box for department column
        colModel.getColumn(3).setCellEditor(new DefaultCellEditor(deptCombo));
        
        // Set formatting for salary column
        colModel.getColumn(6).setCellRenderer(NumberRenderer.getCurrencyRenderer());
        
        // Set formatting for text columns
        colModel.getColumn(1).setCellEditor(new TextVerifier());
        colModel.getColumn(2).setCellEditor(new TextVerifier());     
        		
		// Hide etag and last-modified columns
        jTable.getColumnModel().getColumn(7).setMinWidth(0);
        jTable.getColumnModel().getColumn(8).setMinWidth(0);
        jTable.getColumnModel().getColumn(7).setMaxWidth(0);
        jTable.getColumnModel().getColumn(8).setMaxWidth(0);
	}
	
	public static TableModel getTableModel() throws Exception {
		httpReq = new HTTPRequest(APP_URL);
    	String data = httpReq.getData();
    	
    	TableModel tm;
    	if(data != null || !data.equals("")) {
    		ServerResponseParser xmlp = new ServerResponseParser(data);
    	  	tm = new TableModel(xmlp.getData(), xmlp.getColumnNames());
    	} else {
    		tm = new TableModel(10, colNames);
    	}
    	tm.addTableModelListener(new TableListener());
    	tm.addUpdateListener(new Update());
    	return tm;
	}
	
	/*
	 * A class to receive and process table cell changes.
	 */
	static class Update implements UpdateListener {
		public void update(Object value, int row, int col) {
			// Update server before updating local model
			String data = createJSONString(value, row, col);
			Integer id = (Integer)tmodel.getValueAt(row, Constants.ID_COL);
			String etag = (String)tmodel.getValueAt(row, Constants.ETAG_COL);
			String lmod = (String)tmodel.getValueAt(row, Constants.LAST_MOD_COL);
			
			try {
				httpReq.putData(id, data, etag, lmod);
				int code = httpReq.responseCode();
				if(code == 204) {
					// Need to get etag and last modified fields
					// from the response headers then
					// create an XML string containing the updated
					// row data and pass it to the updateRow function
					
					String newEtag = httpReq.etag();
					String newLmod = httpReq.lastModified();
					
					/** JOptionPane.showMessageDialog(frame,
							"N: " + newEtag + " O: " + etag,
							"Test Title",
							JOptionPane.ERROR_MESSAGE); **/
					
					// String newData = createXMLDataString(newEtag, newLmod, row); // Need to construct new data string
					updateRow(value, newEtag, newLmod, row, col);
				} else {
					JOptionPane.showMessageDialog(frame,
							"Message",
							"Title",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception e) {
				// For some reason a 412 response throws an exception
				// in the URLConnection class
				if(httpReq.responseCode() == 412) { // Precondition failed
					String errStr = "Unable to update. Server contained more recent record." +
							"Local record updated from server.";							
					JOptionPane.showMessageDialog(frame,
							errStr,
							"Error",
							JOptionPane.ERROR_MESSAGE);
					
					// Get "fresh" data from server
					try {
						String newData = httpReq.getData(id, null, null);
						updateRow(newData, row);
					} catch(Exception ex) {
						JOptionPane.showMessageDialog(frame,
								"The operation could not be completed",
								"Oops!",
								JOptionPane.ERROR_MESSAGE);
					}					
				} else {
					JOptionPane.showMessageDialog(frame,
							// "The operation could not be completed due to server error.",
							"E: " + e.getMessage(),
							"Oops! * " + httpReq.responseCode(),
							JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}			
		}
		
		/*
		 * Create a JSON string from the data in the specified row.
		 * The value of the data at the cell with the specified row
		 * and column will be replaced by the specified value.
		 */
		private String createJSONString(Object value, int row, int col) {
			String data = "";
			Integer id = (Integer)tmodel.getValueAt(row, Constants.ID_COL);
			String lname = (String)tmodel.getValueAt(row, Constants.LAST_NAME_COL);
			String fname = (String)tmodel.getValueAt(row, Constants.FIRST_NAME_COL);
			String dept = (String)tmodel.getValueAt(row, Constants.DEPARTMENT_COL);
			
			String ftime = "0";
			if(((Boolean)tmodel.getValueAt(row, Constants.FULL_TIME_COL)).booleanValue()) {
				ftime = "1";
			};
			String hdate = (String)tmodel.getValueAt(row, Constants.HIRE_DATE_COL);
			Integer sal = (Integer)tmodel.getValueAt(row, Constants.SALARY_COL);
			
			// Update edited column
			switch(col) {
				case 1: lname = (String)value; break;
				case 2: fname = (String)value; break;
				case 3: dept = (String)value; break;
				case 4: if(((Boolean)value).booleanValue()) {
							ftime = "1"; 
						} else {
							ftime = "0";
						}
						break;
				case 6:	sal = (Integer)value; break;
			}
			
			data = "{ \"lastname\":\"" + lname + "\", " +
				"\"firstname\":\"" + fname + "\", \"department\":\"" + dept + "\", " +
				"\"fulltime\":\"" + ftime + "\", \"hiredate\":\"" + hdate + "\", " +
				"\"salary\":\"" + sal + "\" }";
			
			return data;
		}
		
		public String createXMLDataString(String etag, String lmod, int row) {
			StringBuilder str = new StringBuilder();
			System.out.println("Jj: " + (String)jTable.getValueAt(row, 1));
			str.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
			str.append("<Employee>");
			str.append("<employeeID>" + (Integer)jTable.getValueAt(row, Constants.ID_COL) + "</employeeID>");
			str.append("<last_name>" + (String)jTable.getValueAt(row, Constants.LAST_NAME_COL) + "</last_name>");
			str.append("<first_name>" + (String)jTable.getValueAt(row, Constants.FIRST_NAME_COL) + "</first_name>");
			str.append("<department>" + (String)jTable.getValueAt(row, Constants.DEPARTMENT_COL) + "</department>");
			str.append("<full_time>" + (Boolean)jTable.getValueAt(row, Constants.FULL_TIME_COL) + "</full_time>");
			str.append("<hire_date>" + (String)jTable.getValueAt(row, Constants.HIRE_DATE_COL) + "</hire_date>");
			str.append("<salary>" + (Integer)jTable.getValueAt(row, Constants.SALARY_COL) + "</salary>");
			str.append("<etag>" + etag + "</etag>");
			str.append("<last_modified>" + lmod + "</last_modified>");
			str.append("</Employee>");
			
			return str.toString();
		}
		
		// Update a row in the JTable
		private void updateRow(String newData, int row) throws Exception {
			JAXBContext jbc = JAXBContext.newInstance("test.jaxb");
			Unmarshaller u = jbc.createUnmarshaller();
			
			// System.out.println("ND: " + newData);
			test.jaxb.Employee emp = (test.jaxb.Employee)u.unmarshal(
					new StreamSource(new StringReader(newData)));
			
			Object newLastMod = emp.getLastModified();
			
			// Update the table row
			tmodel.updateValueAt(emp.getEmployeeID(), row, Constants.ID_COL);
			tmodel.updateValueAt(emp.getLastName(), row, Constants.LAST_NAME_COL);
			tmodel.updateValueAt(emp.getFirstName(), row, Constants.FIRST_NAME_COL);
			tmodel.updateValueAt(emp.getDepartment(), row, Constants.DEPARTMENT_COL);
			tmodel.updateValueAt(emp.isFullTime(), row, Constants.FULL_TIME_COL);
			tmodel.updateValueAt(emp.getHireDate(), row, Constants.HIRE_DATE_COL);
			tmodel.updateValueAt(emp.getSalary(), row, Constants.SALARY_COL);
			tmodel.updateValueAt(emp.getEtag(), row, Constants.ETAG_COL);
			tmodel.updateValueAt(newLastMod, row, Constants.LAST_MOD_COL);
		}
		
		private void updateRow(Object newValue, String etag, String lmod, 
				int row, int col) {
			
			if(col == Constants.SALARY_COL) { // Salary
				tmodel.updateValueAt((Integer)newValue, row, col);
			} else if(col == Constants.FULL_TIME_COL) {
				tmodel.updateValueAt((Boolean)newValue, row, col);
			} else {
				tmodel.updateValueAt((String)newValue, row, col);
			}
			
			tmodel.updateValueAt(etag, row, Constants.ETAG_COL);
			tmodel.updateValueAt(lmod, row, Constants.LAST_MOD_COL);
		}
		
		/*
		// Only one int column
		private void updateRow(int newData, int row) {
			
		}
		
		// Only one boolean column
		private void updateRow(boolean newData, int row) {
			
		}
		*/
	}
	
	static class NewAction extends AbstractAction {
		
		public NewAction(String text, String desc, int mnemonic) {
			super(text);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}
		
		public void actionPerformed(ActionEvent e) {
			Rectangle bounds = frame.getBounds();
			newRecDialog.setLocation(bounds.x + bounds.width/3,
					bounds.y + bounds.height/3);
			newRecDialog.setVisible(true);			
		}
	}
	
	static class DeleteAction extends AbstractAction {
		
		public DeleteAction(String text, String desc, int mnemonic) {
			super(text);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}
		
		public void actionPerformed(ActionEvent e) {
			int n = JOptionPane.showConfirmDialog(frame,
					"Are you sure you want to delete the current row?",
					"Delete row",
					JOptionPane.YES_NO_OPTION); 
			
			if(n == 0) {
				// Delete the row				
				int row = jTable.getSelectedRow();
				if(row == -1) {
					JOptionPane.showMessageDialog(frame,
					    "No row selected",
					    "Message",
					    JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
				try {
					// Get data from table
					// Need EmployeeID, etag, and lastModified
					Integer id = (Integer)tmodel.getValueAt(row, Constants.ID_COL);
					String etag = (String)tmodel.getValueAt(row, Constants.ETAG_COL);
					String lastMod = (String)tmodel.getValueAt(row, Constants.LAST_MOD_COL);
					
					// Send request to server
					httpReq.deleteData(id, etag, lastMod);
					
					// Check server response to be sure
					// the record was actually deleted
					if(httpReq.responseCode() == 204) {
						tmodel.removeRow(row);
					} else {
						JOptionPane.showMessageDialog(frame,
								"The operation could not be completed.",
								"Oops!",
								JOptionPane.ERROR_MESSAGE);
					}
					
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(frame,
						    "The operation could not be completed",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
				} 
			}
		}
	}
	
	// Refresh the JTable from server
	static class RefreshAction extends AbstractAction {
		public RefreshAction(String text, String desc, int mnemonic) {
			super(text);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}
		
		public void actionPerformed(ActionEvent e) {
			try {
				tmodel = getTableModel();
				jTable = new JTable();
				jTable.setModel(tmodel);
				initTable();				
				// Must create new JTable and TableModel
				// then load them into the JScrollPane
				// to update view.
				scrollPane.setViewportView(jTable);
			} catch(Exception ex) {
				JOptionPane.showMessageDialog(frame, "Unable to refresh");
			}
		}
	}
	
	// Close the application
	static class ExitAction extends AbstractAction {
		
		public ExitAction(String text, int mnemonic) {
			super(text);
			putValue(MNEMONIC_KEY, mnemonic);
		}
		
		public void actionPerformed(ActionEvent e) {
			frame.dispose();
			System.exit(0);
		}
	}
	
	// Open help menu
	static class HowToAction extends AbstractAction {
		
		public HowToAction(String text, String desc, int mnemonic) {
			super(text);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}
		
		public void actionPerformed(ActionEvent e) {
			helpDialog.setVisible(true);
		}
	}
	
	// Open about page
	static class AboutAction extends AbstractAction {
		
		public AboutAction(String text, String desc, int mnemonic) {
			super(text);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}
		
		public void actionPerformed(ActionEvent e) {
			if (Desktop.isDesktopSupported()) {
				try {
					String aboutMsg = "<html><h3>Java Client Application</h3>"
							+ "<p>Created by Craig Spencer</p>"
							+ "<p>December 2018</p>"
							+ "</html>";
					JOptionPane.showMessageDialog(frame,
							aboutMsg,
						    "About",
						    JOptionPane.PLAIN_MESSAGE);
					// Desktop.getDesktop().browse(new URI("http://github.com/cspen"));
				} catch(Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
			frame.getContentPane().setBackground(Color.BLUE);
		}
	}
	
	
	//
	// Classes below are for the JTable and not the JFrame
	// 
	public static class TextVerifier extends DefaultCellEditor {
		JFormattedTextField tf;
		
		public TextVerifier() {
			super(new JFormattedTextField());
			tf = (JFormattedTextField)getComponent();			
		}
		
		@Override
		public Component getTableCellEditorComponent(JTable table,
	            Object value, boolean isSelected,
	            int row, int column) {
			JFormattedTextField ftf =
		            (JFormattedTextField)super.getTableCellEditorComponent(
		                table, value, isSelected, row, column);
		        ftf.setValue(value);
		        return ftf;
		}		
		
		@Override
		public boolean stopCellEditing() {
	        JFormattedTextField ftf = (JFormattedTextField)getComponent();
	        String s = (String)ftf.getValue();
	        if (s.matches("^[A-Za-z]+ ?[A-Za-z]*$")) {
	        	try {
	                ftf.commitEdit();
	            } catch (java.text.ParseException exc) { }
		    
	        } else { //text is invalid
	            return false; //don't let the editor go away
		    } 
	        return super.stopCellEditing();
	    }		
	}
	
	public static class NumberRenderer extends DefaultTableCellRenderer {
		private Format formatter;
		/*
		 *  Use the specified number formatter and right align the text
		 */
		public NumberRenderer(NumberFormat formatter) {
			this.formatter = formatter;
			setHorizontalAlignment( SwingConstants.RIGHT );
		}

		/*
		 *  Use the default currency formatter for the default locale
		 */
		public static NumberRenderer getCurrencyRenderer() {
			return new NumberRenderer( NumberFormat.getCurrencyInstance() );
		}
		
		@Override
		public void setValue(Object value) {
			//  Format the Object before setting its value in the renderer
			try {
				if (value != null)
					value = formatter.format(value);
			}
			catch(IllegalArgumentException e) {}
			super.setValue(value);
		}
	}
	
	static public class MyRenderer extends DefaultTableCellRenderer {
	    @Override
	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
	        setBorder(noFocusBorder);
	        return this;
	    }
	}
	
	static class TableListener implements TableModelListener {		
		// Methods for TableModelInterface
		@Override
		public void tableChanged(TableModelEvent te) {
			// This design doesn't utilize this method
			// because this method isn't called until
			// after the table has been changed.
			
			/*
			int row = te.getFirstRow();
			int column = te.getColumn();
			int type = te.getType();
			
			String msg = "";
			if(type == TableModelEvent.DELETE) {
				msg = "DELETE ROW " + row;
			} else if(type == TableModelEvent.INSERT) {
				msg = "INSERT NEW ROW";
			} else if(type == TableModelEvent.UPDATE) {
				msg = "UPDATE ROW " + row;
			}			
			System.out.println(msg);
			*/			
		}
	}
	
	private static JFrame frame = new JFrame("Restful Client Demo");
	private static JScrollPane scrollPane;
	private static NewRecordDialog newRecDialog;
	private static HelpDialog helpDialog;
	private static JTable jTable;
	private static TableModel tmodel;
	private static HTTPRequest httpReq;
}