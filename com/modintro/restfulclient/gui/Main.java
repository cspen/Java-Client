/**
 * 
 */
package com.modintro.restfulclient.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
        HowToAction howToAct = new HowToAction("How to...", "Help", new Integer(KeyEvent.VK_T));
        JMenu newMenuItem = new JMenu("New");
        newMenuItem.setMnemonic(KeyEvent.VK_N);
        
        JMenu deleteMenuItem = new JMenu("Delete");
        deleteMenuItem.setMnemonic(KeyEvent.VK_D);
        
        HowToAction howToAct = new HowToAction("How to...", "Help", new Integer(KeyEvent.VK_T));
        JMenu helpMenuItem = new JMenu("Help");
        JMenuItem howToMenuItem = new JMenuItem(howToAct);
        helpMenuItem.add(howToMenuItem);
        JMenuItem aboutMenuItem = new JMenuItem("About", KeyEvent.VK_A);
        helpMenuItem.add(aboutMenuItem);
        
        menuBar.add(newMenuItem);
        menuBar.add(deleteMenuItem);
        menuBar.add(helpMenuItem);
        frame.setJMenuBar(menuBar);
        
        // Add menu actions
        
        // Add JTable
        final JTable jTable = new JTable(new TableModel());
		jTable.setPreferredScrollableViewportSize(new Dimension(500, 200));
		jTable.setFillsViewportHeight(true);
		// jTable.getModel().addTableModelListener(this);		
		JScrollPane scrollPane = new JScrollPane(jTable);		
		// frame.add(scrollPane);
		
        // Add footer
        
        frame.setVisible(true);
        frame.pack();
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
}
