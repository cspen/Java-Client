package com.modintro.restfulclient.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class StatusBar extends JPanel {
	
	public StatusBar() {
		setLayout(new FlowLayout(FlowLayout.LEFT, 10, 3));
		setBackground(Color.lightGray);
		setBorder(BorderFactory.createLineBorder(Color.gray));
		setPagePane(Color.red);
		add(pagePane);
	}
	
	private void setPagePane(Color color) {}
	
	private StatusPane pagePane = new StatusPane("Employees");
	
	class StatusPane extends JLabel {
		
		public StatusPane(String text) {
			setBackground(Color.lightGray);
			setForeground(Color.black);
			setHorizontalAlignment(CENTER);
			setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			setPreferredSize(new Dimension(200, 20));
			setText(text);
		}
	}
}
