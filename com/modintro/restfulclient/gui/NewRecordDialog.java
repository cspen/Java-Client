package com.modintro.restfulclient.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NewRecordDialog extends JDialog implements ActionListener {
	
	public NewRecordDialog(JFrame window) {
		super(window, "New Record", true);		
		this.window = window;
		setSize(100, 100);
		
		JPanel form = new JPanel();
		JTextField fname = new JTextField();
		JTextField lname = new JTextField();
		form.add(fname);
		form.add(lname);
		getContentPane().add(form, BorderLayout.CENTER);
		
		JPanel buttonPane = new JPanel();
		buttonPane.add(ok = createButton("OK"));
		buttonPane.add(cancel = createButton("Cancel"));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);		
	}
	
	private JButton createButton(String label) {
		JButton button = new JButton(label);
		button.setPreferredSize(new Dimension(80, 20));
		button.addActionListener(this);
		return button;
	}
	
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if(source == ok) {
			// TO-DO: Code to add new record
			setVisible(false);
		} else if(source == cancel) {
			// TO-DO: Clear all inputs
			setVisible(false);
		}
	}
	
	private JFrame window;
	private JButton ok;
	private JButton cancel;
}
