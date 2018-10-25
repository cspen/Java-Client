/**
 * 
 */
package com.modintro.restfulclient.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import com.modintro.restfulclient.model.Constants;
import com.modintro.restfulclient.model.HTTPRequest;
import com.modintro.restfulclient.model.TableModel;
import com.modintro.restfulclient.model.ServerResponseParser; 

/**
 * @author Craig Spencer <craigspencer@modintro.com>
 * 
 */
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
        TableModel tmodel = null;
        try {
        	// URL theService = new URL(APP_URL);
        	HTTPRequest httpReq = new HTTPRequest(APP_URL);
        	String data = httpReq.getData();
        	ServerResponseParser xmlp = new ServerResponseParser(data);
        	
        	tmodel = new TableModel(xmlp.getData(), xmlp.getColumnNames());
        	
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        	String msg = "\nClosing Application";
        	JOptionPane.showMessageDialog(frame, e.getMessage() + msg,
        			"Error", JOptionPane.ERROR_MESSAGE);
        	System.exit(0);
        }
        
        jTable = new JTable(tmodel);
        tmodel.addTableModelListener(new TableListener());
        jTable.addMouseListener(new MouseInput());
        jTable.setRowSorter(new TableRowSorter<TableModel>(tmodel));
        
        JComboBox<String> deptCombo = NewRecordDialog.createDeptCombo();
        TableColumn deptCol = jTable.getColumnModel().getColumn(3);
        deptCol.setCellEditor(new DefaultCellEditor(deptCombo));
        		
        // Hide etag and last-modified columns
        jTable.getColumnModel().getColumn(7).setMinWidth(0);
        jTable.getColumnModel().getColumn(8).setMinWidth(0);
        jTable.getColumnModel().getColumn(7).setMaxWidth(0);
        jTable.getColumnModel().getColumn(8).setMaxWidth(0);
        
		jTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
		jTable.setFillsViewportHeight(true);
		// jTable.getModel().addTableModelListener(this);		
		JScrollPane scrollPane = new JScrollPane(jTable);		
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
        // Add footer
		
		// Add dialog boxes
		newRecDialog = new NewRecordDialog(frame);
        
        frame.setVisible(true);
        // frame.pack();
	}	
	
	static class TableListener implements TableModelListener {
	
		// Methods for TableModelInterface
		@Override
		public void tableChanged(TableModelEvent te) {
			int row = te.getFirstRow();
			int column = te.getColumn();
			TableModel model = (TableModel)te.getSource();
			String columnName = model.getColumnName(column);
			Object data = model.getValueAt(row, column);
			
			if(!data.equals(currentVal) && data != null ) {
				if(data instanceof String) {
					String temp = (String)data;
					if(!temp.equals("")) {
						// System.out.println("NOT AN EMPTY STRING");
						// currentVal = null;
						// Validate and update server
					}
				} else if(data instanceof Boolean) {
					// System.out.println("CHECKBOX CLICKED");
					// currentVal = null;
				} else if(column == 3) {
					// System.out.println("COMBOBOX CLICKED");
					// currentVal = null;
				}
				System.out.println("DATACHANGE: r: " + row + " c: " + column + " d: " + data + "\n");
			}
		}
	}
	
	static class MouseInput extends MouseAdapter {
	    @Override
	    public void mouseClicked(MouseEvent evt) {
	        int row = jTable.rowAtPoint(evt.getPoint());
	        int col = jTable.columnAtPoint(evt.getPoint());
	        if (row >= 0 && col >= 0) {
	        	if(col != 4) {
	        		System.out.println("R: " + row + " C: " + col);
	        		// System.out.println("DATASELECTED: " + jTable.getValueAt(row, col) + "\n");
	        		// currentVal = jTable.getValueAt(row, col);
	        	}
	        }
	    }
	}
	
	static class NewAction extends AbstractAction {
		
		public NewAction(String text, String desc, int mnemonic) {
			super(text);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}
		
		public void actionPerformed(ActionEvent e) {
			// frame.getContentPane().setBackground(Color.RED);
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
			frame.getContentPane().setBackground(Color.BLACK);
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
	
	private static JFrame frame = new JFrame("Restful Client Demo");
	private static final String DEPT_LIST_URL = "http://localhost/GEM/rest/departments/";
	private static NewRecordDialog newRecDialog;
	private static JTable jTable;
	private static Object currentVal = null;
	 
}
