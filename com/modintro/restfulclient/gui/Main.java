/**
 * 
 */
package com.modintro.restfulclient.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;

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
	
	private static void createAndShowGUI() {
		//Create and set up the window.
        JFrame frame = new JFrame("Restful Client Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Center the window
        Toolkit tKit = frame.getToolkit();
        Dimension wndSize = tKit.getScreenSize();
        frame.setBounds(wndSize.width/4, wndSize.height/4,	// Position
        		wndSize.width/2, wndSize.height/2);      	// Size
        
        // Add menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu newMenuItem = new JMenu("New");
        JMenu deleteMenuItem = new JMenu("Delete");
        JMenu helpMenuItem = new JMenu("Help");
        
        menuBar.add(newMenuItem);
        menuBar.add(deleteMenuItem);
        menuBar.add(helpMenuItem);
        frame.setJMenuBar(menuBar);
        
        // Add JTable
        final JTable jTable = new JTable(new TableModel());
		jTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
		jTable.setFillsViewportHeight(true);
		// jTable.getModel().addTableModelListener(this);		
		JScrollPane scrollPane = new JScrollPane(jTable);		
		frame.add(scrollPane);
		
        // Add footer
        
        frame.setVisible(true);
	}

}
