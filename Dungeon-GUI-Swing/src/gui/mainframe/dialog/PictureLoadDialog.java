package gui.mainframe.dialog;

import gui.AbstractStartWindow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;



public class PictureLoadDialog extends javax.swing.JDialog {
	
	String load = "Bild wird geladen: ";
	String pic;
	JLabel label = new JLabel(load);
	JLabel label2 = new JLabel("Bitte etwas Geduld.");
	public PictureLoadDialog(AbstractStartWindow  view) {
		super(view,"Bilder werden geladen");
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		this.setContentPane(panel);
		panel.setBackground(Color.red);
		panel.add(label2);
		panel.add(label);
		panel.setBorder(new EtchedBorder());
		
		
		this.setSize(300,200);
		positionieren();
		this.setVisible(true);
		
	}
	
	
	
	public void setPicName(String name) {
		
		label.setText(load+name);
	this.repaint();
	}
	
	
	public void positionieren() {
		Dimension dimension = new Dimension(getToolkit().getScreenSize());
		int screenWidth = (int) dimension.getWidth();
		int screenHeight = (int) dimension.getHeight();
		int width = this.getWidth();
		int height = this.getHeight();
		setLocation((screenWidth / 2) - (width / 2), (screenHeight / 2)
				- (height / 2));
	}

}
