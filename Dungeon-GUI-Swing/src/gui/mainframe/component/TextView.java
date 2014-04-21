package gui.mainframe.component;
//import JDGuiJDJPanel;

import gui.JDGUISwing;
import gui.JDJPanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
//import java.util.*;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class TextView extends JDJPanel {
	
	String newline = "\n";

	
	//DefaultStyledDocument doc = new DefaultStyledDocument();

	
	JTextPane verlauftxt2;

	
	JScrollPane scrollPane;

	
	//agame game;
	
	public TextView(int x, int y, JDGUISwing gui) {
		super(gui);
		verlauftxt2 = new JTextPane(new DefaultStyledDocument());
		verlauftxt2.setEditable(false);
		verlauftxt2.setMargin(new Insets(5, 5, 5, 5));
		verlauftxt2.setBackground(new Color(220,220,220));	
		
		scrollPane =
			new JScrollPane(
				verlauftxt2,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		scrollPane.setPreferredSize(new Dimension(x,y-10));
		scrollPane.setMaximumSize(new Dimension(x, y-10));
		scrollPane.setSize(x,y-10);
		
		scrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
		JScrollBar bar = scrollPane.getVerticalScrollBar();
		bar.setBackground(JDJPanel.bgColor);
		if(bar.getComponentCount() > 1) {
			Component c = bar.getComponent(0);
			c.setBackground(JDJPanel.bgColor);
			Component c2 = bar.getComponent(1);
			c2.setBackground(JDJPanel.bgColor);
		}
		
		this.add(scrollPane);
		this.setSize(x,y);
		SimpleAttributeSet set = new SimpleAttributeSet();
		try {
			verlauftxt2.getDocument().insertString(verlauftxt2.getDocument().getLength(), cursor, set);
		}catch (Exception e) {
		}
		
	}
	
	private static final String cursor = ":-)";
	
	public void cls() {
		try {
			verlauftxt2.getDocument().remove(0,verlauftxt2.getDocument().getLength());
		}catch (Exception e) {
		}	
	}
	
	

	public static final int STYLE_SANSSERIF = 0;
	public static final int STYLE_TIMES_RED = 1;
	public static final int STYLE_SERIF_GREEN = 2;
	public static final int STYLE_COURIER_BLUE = 3;
	public static final int STYLE_COURIER_ORANGE = 4;
	
	
	
	public void newStatement(String s, int styleCode) {
		//if(true)return;
		SimpleAttributeSet set = new SimpleAttributeSet();
		//try {
		//helpStatement(s);

		if (styleCode == 0) {
			SimpleAttributeSet attr0 = new SimpleAttributeSet();
			StyleConstants.setFontFamily(attr0, "SansSerif");
			StyleConstants.setFontSize(attr0, 14);
			StyleConstants.setBold(attr0, false);
			StyleConstants.setAlignment(attr0, StyleConstants.ALIGN_CENTER);

			set = attr0;
		} else if (styleCode == 1) {
			SimpleAttributeSet attr1 = new SimpleAttributeSet();
			StyleConstants.setFontFamily(attr1, "TimesRoman");
			StyleConstants.setFontSize(attr1, 14);
			StyleConstants.setBold(attr1, true);
			StyleConstants.setForeground(attr1, Color.red);
			StyleConstants.setAlignment(attr1, StyleConstants.ALIGN_CENTER);
			set = attr1;
		} else if (styleCode == 2) {
			SimpleAttributeSet attr2 = new SimpleAttributeSet();
			StyleConstants.setFontFamily(attr2, "Serif");
			StyleConstants.setFontSize(attr2, 14);
			StyleConstants.setBold(attr2, true);
			StyleConstants.setForeground(attr2, new Color(20, 200, 20));
			StyleConstants.setAlignment(attr2, StyleConstants.ALIGN_CENTER);
			set = attr2;
		} else if (styleCode == 3) {
			SimpleAttributeSet attr3 = new SimpleAttributeSet();
			StyleConstants.setFontFamily(attr3, "Courier");
			StyleConstants.setFontSize(attr3, 14);
			StyleConstants.setBold(attr3, false);
			StyleConstants.setForeground(attr3, Color.blue);
			set = attr3;
		} else if (styleCode == 4) {
			SimpleAttributeSet attr2 = new SimpleAttributeSet();
			StyleConstants.setFontFamily(attr2, "Courier");
			StyleConstants.setFontSize(attr2, 12);
			StyleConstants.setBold(attr2, false);
			StyleConstants.setForeground(attr2, Color.orange);
			set = attr2;
		}

		String n = "              ";
		if (styleCode == 4) {
			s = n + s;
		}

		try {
			if ((styleCode != 4) ) {
				//int pos = doc.getText(0,doc.getLength()).lastIndexOf(cursor);
				verlauftxt2.getDocument().insertString(verlauftxt2.getDocument().getLength(), s + newline, set);
			}
		} catch (BadLocationException ble) {
			System.err.println("Couldn't insert initial text.");
		}
		int end = verlauftxt2.getDocument().getEndPosition().getOffset();
		//int end2 = doc.getLength();
		//verlauftxt2.
		//doc.getEndPosition().
		//scrollPane.
		Rectangle rect = new Rectangle(new Point(0,verlauftxt2.getHeight()-scrollPane.getHeight()), new Dimension(scrollPane.getWidth(),scrollPane.getWidth()));
		//System.out.println("scrolling to: "+rect.toString());
		//scrollPane.scrollRectToVisible(rect);
		
		JViewport port = scrollPane.getViewport();
		//port.scrollRectToVisible(rect);
		int height = verlauftxt2.getSize().height;
//		
//		//scrollPane.getVerticalScrollBar().;
		Point p = (new Point(0,height-this.HEIGHT));
////		try {
		port.setViewPosition(p);
		
//		}catch (Exception e) {
//			System.out.println("setViewPosition-Exception!");
//		}
		
		

	}

}
