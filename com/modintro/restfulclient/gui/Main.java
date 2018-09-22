/**
 * 
 */
package com.modintro.restfulclient.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URI;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import com.modintro.restfulclient.model.TableModel;

/**
 * @author Craig Spencer <craigspencer@modintro.com>
 * 
 */
public class Main {

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
	
	private static JFrame frame = new JFrame("Restful Client Demo");
	
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
        	tmodel = new TableModel(xml);
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        }
        final JTable jTable = new JTable(tmodel);
		jTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
		jTable.setFillsViewportHeight(true);
		// jTable.getModel().addTableModelListener(this);		
		JScrollPane scrollPane = new JScrollPane(jTable);		
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
        // Add footer
        
        frame.setVisible(true);
        // frame.pack();
	}
	
	
	
	static class NewAction extends AbstractAction {
		
		public NewAction(String text, String desc, int mnemonic) {
			super(text);
			putValue(SHORT_DESCRIPTION, desc);
			putValue(MNEMONIC_KEY, mnemonic);
		}
		
		public void actionPerformed(ActionEvent e) {
			frame.getContentPane().setBackground(Color.RED);
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
	
	static String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" +
			"<notes>" +
			"<note>" + 
	   		"<to>Tove</to>" +
	   		"<from>Jani</from>" +
	   		"<heading>Reminder</heading>" +
	   		"<body>Don't forget me this weekend!</body>" +
	   		"</note>" +
	   		"<note>" + 
	   		"<to>Gia</to>" +
	   		"<from>Craig</from>" +
	   		"<heading>Helpful Tip</heading>" +
	   		"<body>Just swallow next time!</body>" +
	   		"</note>" +
	   		"<note>" + 
	   		"<to>Bean</to>" +
	   		"<from>Craig</from>" +
	   		"<heading>Bad Dog Notice</heading>" +
	   		"<body>Stop pooping on the floor!</body>" +
	   		"</note>" +
	   		"</notes>";

}
