package com.modintro.restfulclient.gui;

/**
 * Dialog window containing a form to enter information
 * for a new record in the application.
 * 
 * @author Craig Spencer <craigspencer@modintro.com>
 * Last Modified: 11/09/2018
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import com.modintro.restfulclient.model.Constants;
import com.modintro.restfulclient.model.HTTPRequest;
import com.modintro.restfulclient.model.TableModel;

import test.jaxb.Departments;

public class NewRecordDialog extends JDialog
		implements ActionListener, Constants, WindowListener {
	
	public NewRecordDialog(JFrame window, TableModel tmodel) {
		super(window, "New Employee", true);
		addWindowListener(this);
		this.tmodel = tmodel;
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
		
		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
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
		
		salary = new JTextField();
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
	
	public static JComboBox<String> createDeptCombo() {		
		HTTPRequest httpReq = new HTTPRequest(DEPT_URL);
		JComboBox<String> deptCombo = new JComboBox<String>();
		String depts = null;
		try {
			depts = httpReq.getData();
			JAXBContext jbc = JAXBContext.newInstance("test.jaxb");
			Unmarshaller u = jbc.createUnmarshaller();
			
			Departments d = (Departments)u.unmarshal(new StreamSource(new StringReader(depts)));
		    ArrayList<String> list = (ArrayList<String>) d.getDepartment();
		    int length = list.size();
		    for(int i = 0; i < length; i++) {
		    	deptCombo.addItem(list.get(i));
		    }
		   			
		} catch(Exception e) {
			System.out.println(e.getMessage());			
		}		
		return deptCombo;
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
			String lastName =  lname.getText();
			String firstName = fname.getText();
			String department = (String)deptCombo.getSelectedItem();
			Boolean fullTime = ftCheck.isSelected();
			Integer year = (Integer)hireYear.getSelectedItem();
			Integer  month = (Integer)hireMonth.getSelectedItem();
			Integer day = (Integer)hireDay.getSelectedItem();
			String sal = salary.getText();	
			
			if(validateNewEmployee(lastName, firstName, year, month, day, sal)) {
				int ft = 0;
				if(fullTime) {
					ft = 1;
				}
				try {
					HTTPRequest httpReq = new HTTPRequest(APP_URL);
					String data = "lname=" + lastName + "&fname=" + firstName;
					data += "&dept=" + department + "&salary=" + sal;
					data += "&ftime=" + ft;
					data += "&hdate=" + year + "-" + month + "-" + day;
					String response = httpReq.postData(data);
					
					if(httpReq.responseCode() == 201) {
						// Alert human (need better message);
						JOptionPane.showMessageDialog(this, "Success");
						
						// Need to call server again to get
						// etag and last modified columns
						String newRec = httpReq.responseMessage();
						String[] parts = response.split("/");
						Integer newRecId = new Integer(parts[parts.length-1]);
						httpReq = new HTTPRequest(APP_URL + newRecId);
						response = httpReq.getData();
						
						if(httpReq.responseCode() == 200) {
							JAXBContext jbc = JAXBContext.newInstance("test.jaxb");
							Unmarshaller u = jbc.createUnmarshaller();
							
							test.jaxb.Employee emp = (test.jaxb.Employee)u.unmarshal(
									new StreamSource(new StringReader(response)));
							
							Object[] newRow = new Object[9];
							newRow[0] = emp.getEmployeeID();
							newRow[1] = emp.getLastName();
							newRow[2] = emp.getFirstName();
							newRow[3] = emp.getDepartment();
							newRow[4] = emp.isFullTime();
							newRow[5] = emp.getHireDate();
							newRow[6] = emp.getSalary();
							newRow[7] = emp.getEtag();
							newRow[8] = emp.getLastModified();
							
							tmodel.insertRow(0, newRow);									
							reset();
							setVisible(false);						    
						} else {
							errorDialog();
						}						
					} else {
						errorDialog();
					}
				} catch(Exception ex) {
					JOptionPane.showMessageDialog(this, "BAD NEWS - Something went wrong");
					// ex.printStackTrace();
				}
			}
		} else { // Cancel clicked
			reset();
			setVisible(false);
		}
	}
	
	private void errorDialog() {
		JOptionPane.showMessageDialog(this,
				"The operation could not be completed");
	}
	
	private Boolean validateNewEmployee(String ln, String fn,
			Integer year, Integer month, Integer day, String pay) {
		String regEx = "^[A-Za-z ]+$";
		String errMsg = "";
		if(!ln.matches(regEx)) {
			errMsg += "Last name must contain only letters and spaces";
		}
		if(!fn.matches(regEx)) {
			errMsg += "\nFirst name must contain only letters and spaces";
		}
		if(!validateDate(year, month, day)) {
			errMsg += "\nThe date is not valid";
		}
		if(!pay.matches("^[0-9]+$") || pay.equals("")) {
			errMsg += "\nSalary must be a numeric value";
		}

		if(!errMsg.equals("")) {
			JOptionPane.showMessageDialog(this, errMsg, "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}		
		return true;
	}
	
	private Boolean validateDate(Integer year, Integer month, Integer day) {
		if(year.intValue() < 1900 || year.intValue() > Calendar.getInstance().get(Calendar.YEAR)) {
			return false;
		}
		if(month.intValue() < 1 || month.intValue() > 12) {
			return false;
		}
		
		int[] daysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	    // Adjust for leap years
	    if(year.intValue() % 400 == 0 || (year.intValue() % 100 != 0 && year.intValue() % 4 == 0))
	        daysInMonth[1] = 29;
	    
	    if(day < 1 || day > daysInMonth[month-1]) {
	    	return false;
	    }
	    
	    return true;
	}
	
	private void reset() {
		fname.setText(null);
		lname.setText(null);
		salary.setText(null);
		deptCombo.setSelectedIndex(0);
		ftCheck.setSelected(false);
		hireYear.setSelectedItem(Calendar.getInstance().get(Calendar.YEAR));
		hireMonth.setSelectedItem(Calendar.getInstance().get(Calendar.MONTH) + 1);
		hireDay.setSelectedItem(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
	}
	
	// For WindowListener Interface
	public void windowActivated(WindowEvent e) {}
	public void windowClosing(WindowEvent e) {
		reset();
	}
	public void windowClosed(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	
	private TableModel tmodel;
	private JButton ok;
	private JButton cancel;
	
	private JTextField fname;
	private JTextField lname;
	private JComboBox<String> deptCombo;
	private JCheckBox ftCheck;
	private JComboBox<Integer> hireYear;
	private JComboBox<Integer> hireMonth;
	private JComboBox<Integer> hireDay;
	private JTextField salary;
}