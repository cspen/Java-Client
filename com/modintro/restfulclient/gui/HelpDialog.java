package com.modintro.restfulclient.gui;

/**
 * Display a help dialog window. Content is loaded from
 * help.html.
 * 
 * @author Craig Spencer <craigspencer@modintro.com>
 * Last Modified: 11/11/2018
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class HelpDialog extends JDialog implements ActionListener {

	public HelpDialog(JFrame frame) {
		super(frame, "Help", false);
		JEditorPane jpane = new JEditorPane();
		jpane.setEditable(false);
		jpane.setContentType(CONTENT_TYPE);
		
		InputStream is = getClass().getResourceAsStream(HELP_CONTENT_FILE);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		
		try {
			String allLines = null;
			String line = null;
		    while ((line = reader.readLine()) != null) {
		        allLines += line;
		    }
		    jpane.setText(allLines);
		} catch(Exception ex) {
			JOptionPane.showMessageDialog(this, 
					"Help could not be loaded");			
		}
		
		JScrollPane scrollPane = new JScrollPane(jpane);
		scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(250, 145));
		// scrollPane.setMinimumSize(new Dimension(100, 100));
				
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		pack();
	}
	
	public void actionPerformed(ActionEvent ae) {}
	
	private static final String CONTENT_TYPE = "text/html";
	private static final String HELP_CONTENT_FILE = "help.html";
}