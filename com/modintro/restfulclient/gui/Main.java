/**
 * GUI for JTable application.
 * 
 * @author Craig Spencer <craigspencer@modintro.com>
 * Last Modified: 11/1/2018
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
import java.net.URI;
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
		//Create and set up the window.
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
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(newAct);        
        fileMenu.add(deleteAct);
               
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
        	// URL theService = new URL(APP_URL);
        	httpReq = new HTTPRequest(APP_URL);
        	String data = httpReq.getData();
        	ServerResponseParser xmlp = new ServerResponseParser(data);
        	
        	tmodel = new TableModel(xmlp.getData(), xmlp.getColumnNames());
        	tmodel.setHTTPRequest(httpReq);
        	tmodel.addUpdateListener(new Update());
        	
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        	String msg = "\nClosing Application";
        	JOptionPane.showMessageDialog(frame, e.getMessage() + msg,
        			"Error", JOptionPane.ERROR_MESSAGE);
        	System.exit(0);
        }
        
        jTable = new JTable(tmodel);
        tmodel.addTableModelListener(new TableListener());
        jTable.setRowSorter(new TableRowSorter<TableModel>(tmodel));
        
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
        
		jTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
		jTable.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(jTable);		
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		// Add dialog boxes
		newRecDialog = new NewRecordDialog(frame, tmodel);
        
        frame.setVisible(true);
        // frame.pack();
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
			
			// TableModel model = (TableModel)te.getSource();
			// String columnName = model.getColumnName(column);
			// Object data = model.getValueAt(row, column);
			
			// System.out.println("DATACHANGE: r: " + row + " c: " + column + " d: " + data + "\n");
		}
	}
	
	static class Update implements UpdateListener {
		public void update(Object value, int row, int col) {
			// Update server before updating local model
			String data = createDataString(value, row, col);
			Integer id = (Integer)tmodel.getValueAt(row, 0);
			String etag = (String)tmodel.getValueAt(row, 7);
			String lmod = (String)tmodel.getValueAt(row, 8);
			
			try {
				httpReq.putData(id, data, etag, lmod);
			
				// then make another call to get
				// new etag and last modified fields
				tmodel.updateValueAt(value, row, col);
			} catch (Exception e) {
				
			}			
		}
		
		/*
		 * Create a JSON string from the data in the specified row.
		 * The value of the data at the cell with the specified row
		 * and column will be replaced by the specified value.
		 */
		private String createDataString(Object value, int row, int col) {
			String data = "";
			Integer id = (Integer)tmodel.getValueAt(row, 0);
			String fname = (String)tmodel.getValueAt(row, 1);
			String lname = (String)tmodel.getValueAt(row, 2);
			String dept = (String)tmodel.getValueAt(row, 3);
			Boolean ftime = (Boolean)tmodel.getValueAt(row, 4);
			String hdate = (String)tmodel.getValueAt(row, 5);
			Integer sal = (Integer)tmodel.getValueAt(row, 6);
			
			// Update edited column
			switch(col) {
				case 1: fname = (String)value;
				case 2: lname = (String)value;
				case 3: dept = (String)value;
				case 4: ftime = (Boolean)value;
				case 6:	sal = (Integer)value;	
			}
			
			data = "{ Employee: { \"lastname\":\"" + lname + "\", " +
				"\"firstname\":\"" + fname + "\", \"department\":\"" + dept + "\", " +
				"\"fulltime\":\"" + ftime + "\", \"hiredate\":\"" + hdate + "\", " +
				"\"salary\":\"" + sal + "\" }";
			
			return data;
		}
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
					Integer id = (Integer)tmodel.getValueAt(row, 0);
					String etag = (String)tmodel.getValueAt(row, 7);
					String lastMod = (String)tmodel.getValueAt(row, 8);
					
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
	
	static class HowToAction extends AbstractAction {
		
		public HowToAction(String text, String desc, int mnemonic) {
			super(text);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}
		
		public void actionPerformed(ActionEvent e) {
			frame.getContentPane().setBackground(Color.GREEN);
		}
	}
	
	static class AboutAction extends AbstractAction {
		
		public AboutAction(String text, String desc, int mnemonic) {
			super(text);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}
		
		// Open web page in human's default browser
		public void actionPerformed(ActionEvent e) {
			if (Desktop.isDesktopSupported()) {
				try {
					Desktop.getDesktop().browse(new URI("http://www.yahoo.com"));
				} catch(Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
			frame.getContentPane().setBackground(Color.BLUE);
		}
	}
	
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
	
	private static JFrame frame = new JFrame("Restful Client Demo");
	private static final String DEPT_LIST_URL = "http://localhost/GEM/rest/departments/";
	private static NewRecordDialog newRecDialog;
	private static JTable jTable;
	private static TableModel tmodel;
	private static HTTPRequest httpReq;
}