package com.modintro.restfulclient.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NewRecordDialog extends JDialog implements ActionListener {
	
	public NewRecordDialog(JFrame window) {
		super(window, "New Record", true);		
		this.window = window;
		setSize(100, 100);
		
		JPanel form = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		JLabel fnameLabel = new JLabel("First Name:");
		form.add(fnameLabel, c);
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		JTextField fname = new JTextField();
		form.add(fname, c);
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		JLabel lnameLabel = new JLabel("Last Name:");
		form.add(lnameLabel, c);
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		JTextField lname = new JTextField();
		form.add(lname, c);
		
		
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
