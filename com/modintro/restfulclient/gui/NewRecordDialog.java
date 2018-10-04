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
		
		JLabel fnameLabel = new JLabel("First Name: ");
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 5, 5);		
		form.add(fnameLabel, c);
		
		fname = new JTextField();
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0.1;
		c.insets = new Insets(5, 5, 5, 5);
		form.add(fname, c);
		
		JLabel lnameLabel = new JLabel("Last Name: ");
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 5, 5);
		form.add(lnameLabel, c);
		
		lname = new JTextField();
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 0.1;
		c.insets = new Insets(5, 5, 5, 5);
		form.add(lname, c);
		
		JLabel deptLabel = new JLabel("Department");
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(5, 5, 5, 5);
		form.add(deptLabel, c);		
		
		deptCombo = createDeptCombo();
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(5, 5, 5, 5);
		form.add(deptCombo, c);
		
		ftCheck = new JCheckBox("Full Time: ");
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
		hireYear = createNumericCombo(1900, year, year);
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 4;
		c.weightx = 0.1;
		c.insets = new Insets(5, 5, 5, 5);
		form.add(hireYear, c);
		
		int month = Calendar.getInstance().get(Calendar.MONTH);
		hireMonth = createNumericCombo(1, 12, month);
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 4;
		c.weightx = 0.1;
		c.insets = new Insets(5, 5, 5, 5);
		form.add(hireMonth, c);
		
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		hireDay = createNumericCombo(1, 31, day);
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
			System.out.println("Last Name: " + lname.getText());
			System.out.println("First Name: " + fname.getText());
			System.out.println("Department: " + deptCombo.getSelectedItem());
			System.out.println("Full Time: " + ftCheck.isSelected());
			System.out.println("Hired Date: " + hireYear.getSelectedItem() + "-" +
					hireMonth.getSelectedItem() + "-" + hireDay.getSelectedIndex());
			System.out.println("Salary: " + salary.getText());
			System.out.println();
			System.out.println();
			System.out.println();
			// setVisible(false);
		} else if(source == cancel) {
			// TO-DO: Clear all inputs
			setVisible(false);
		}
	}
	
	private JFrame window;
	private JButton ok;
	private JButton cancel;
	
	private JTextField fname;
	private JTextField lname;
	private JComboBox deptCombo;
	private JCheckBox ftCheck;
	private JComboBox<Integer> hireYear;
	private JComboBox<Integer> hireMonth;
	private JComboBox<Integer> hireDay;
	private JTextField salary;
}
