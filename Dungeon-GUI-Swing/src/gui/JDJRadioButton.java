/*
 * Created on 06.08.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author Jochen
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.*;

public class JDJRadioButton extends JRadioButton{
	
	/**
	 * 
	 */
	public JDJRadioButton() {
		super();
		//this.setBackground(JDJPanel.bgColor);
		this.setOpaque(false);
		// TODO Auto-generated constructor stub
	}
	
	public JDJRadioButton(String s) {
			super(s);
			//this.setBackground(JDJPanel.bgColor);
			this.setOpaque(false);
			this.setMaximumSize(new Dimension(130,30));
			//this.setBackground(Color.TRANSLUCENT);
			this.setForeground(Color.black);
			// TODO Auto-generated constructor stub
		}
		
	public void paintComponent(Graphics g) {
		
		//g.drawImage(JDJPanel.getBackGroundImage(), 0,0,this.getWidth(),this.getHeight(),null);
		super.paintComponent(g);
	}
	public void setText(String s) {
		int k = 17;
		if(s.length() >= k) {
		
		//s = s.substring(0,k);
		}
		String erg = "<html><b><b><font size = \"3\" face=\"arial\" >";
				erg += s;
				erg += "</font></b></b></html>";
				super.setText(erg);
	}

}
