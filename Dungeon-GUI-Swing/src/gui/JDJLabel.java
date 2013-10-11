package gui;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JLabel;

/*
 * Created on 07.08.2004
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
public class JDJLabel extends JLabel {

	/**
	 * @param text
	 * @param icon
	 * @param horizontalAlignment
	 */
	public JDJLabel(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		this.setOpaque(false);
		// TODO Auto-generated constructor stub
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//g.drawImage(JDJPanel.getBackGroundImage(), 0,0,this.getWidth(),this.getHeight(),null);
	}

	/**
	 * @param text
	 * @param horizontalAlignment
	 */
	public JDJLabel(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param text
	 */
	public JDJLabel(String text) {
		super(text);
		this.setOpaque(false);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param image
	 * @param horizontalAlignment
	 */
	public JDJLabel(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param image
	 */
	public JDJLabel(Icon image) {
		super(image);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	public JDJLabel() {
		super();
		this.setOpaque(false);
		// TODO Auto-generated constructor stub
	}
	
	public void setText(String s) {
		String erg = "<html><font face=\"arial\" color=\"#000000\">";
		erg += s;
		erg += "</font></html>";
		super.setText(erg);
	}

}
