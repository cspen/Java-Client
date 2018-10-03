package com.modintro.restfulclient.gui;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
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
		c.insets = new Insets(5, 5, 5, 5);
		JLabel fnameLabel = new JLabel("First Name:");
		form.add(fnameLabel, c);
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0.1;
		c.insets = new Insets(5, 5, 5, 5);
		JTextField fname = new JTextField();
		form.add(fname, c);
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 5, 5);
		JLabel lnameLabel = new JLabel("Last Name:");
		form.add(lnameLabel, c);
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 0.1;
		c.insets = new Insets(5, 5, 5, 5);
		JTextField lname = new JTextField();
		form.add(lname, c);
		
		JLabel deptLabel = new JLabel("Department");
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(5, 5, 5, 5);
		form.add(deptLabel, c);		
		
		JComboBox deptCombo = createDeptCombo();
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(5, 5, 5, 5);
		form.add(deptCombo, c);
		
		/*
		JLabel ftLabel = new JLabel("Full Time: ");
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 5, 5);
		form.add(ftLabel, c); */
		
		JCheckBox ftCheck = new JCheckBox("Full Time: ");
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 5, 5);
		form.add(ftCheck, c);
		
		JLabel hireDate = new JLabel("Hire Date (y/m/d)");
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		c.insets = new Insets(5, 5, 5, 5);
		form.add(hireDate, c);
		
		int year = Calendar.getInstance().get(Calendar.YEAR);
		JComboBox<Integer> hireYear = createNumericCombo(1900, year, year);
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 4;
		c.weightx = 0.1;
		c.insets = new Insets(5, 5, 5, 5);
		form.add(hireYear, c);
		
		int month = Calendar.getInstance().get(Calendar.MONTH);
		JComboBox<Integer> hireMonth = createNumericCombo(1, 12, month);
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 4;
		c.weightx = 0.1;
		c.insets = new Insets(5, 5, 5, 5);
		form.add(hireMonth, c);
		
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		JComboBox<Integer> hireDay = createNumericCombo(1, 31, day);
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 4;
		c.insets = new Insets(5, 5, 5, 5);
		form.add(hireDay, c);
		
		JLabel salLabel = new JLabel("Salary: ");
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 5;
		c.insets = new Insets(5, 5, 5, 5);
		form.add(salLabel, c);
		
		JTextField salary = new JTextField();
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 5;
		c.insets = new Insets(5, 5, 5, 5);
		form.add(salary, c);
		
		
		
		
		
		getContentPane().add(form, BorderLayout.CENTER);
		
		JPanel buttonPane = new JPanel();
		buttonPane.add(ok = createButton("OK"));
		buttonPane.add(cancel = createButton("Cancel"));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);		
		pack();
	}
	
	private JButton createButton(String label) {
		JButton button = new JButton(label);
		button.setPreferredSize(new Dimension(80, 20));
		button.addActionListener(this);
		return button;
	}
	
	private static JComboBox createDeptCombo() {
		return new JComboBox();
	}
	
	private static JComboBox<Integer> createNumericCombo(int start, int end, int set) {
		JComboBox<Integer> jcombo = new JComboBox<Integer>();
		for(int i = start; i <= end; i++) {
			jcombo.addItem(i);
			if(i == set) {
				jcombo.setSelectedItem(i);
			}
		}
		return jcombo;
	}
	
	// For ActionListener interface
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
