package com.modintro.restfulclient.gui;

/**
 * Display a help dialog window. Content are loaded from
 * help.html.
 * 
 * @author Craig Spencer <craigspencer@modintro.com>
 * Last Modified: 11/11/2018
 */

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
		
		File helpFile = new File(getClass().getResource(HELP_CONTENT_FILE).getFile());
		Path path = helpFile.toPath();	
		try {
			byte[] bArr = Files.readAllBytes(path);
			jpane.setText(new String(bArr));
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
	
	public void actionPerformed(ActionEvent ae) {
		
	}
	
	private static final String CONTENT_TYPE = "text/html";
	private static final String HELP_CONTENT_FILE = "help.html";

}
