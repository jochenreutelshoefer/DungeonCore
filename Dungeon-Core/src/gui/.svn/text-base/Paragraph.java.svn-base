/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

package gui;
import javax.swing.text.*;
import java.awt.Color;

public class Paragraph {

	SimpleAttributeSet style;

	
	String text;

	
	boolean Centered;

	
	Color c;

	
	String f;

	
	int size;

	
	public Paragraph(SimpleAttributeSet set, String txt) {
		style = set;
		text = txt;
	}
	
	public Paragraph(String txt) {
		text = txt;
		style = new SimpleAttributeSet();
	}
	
	public static boolean areEqual(Paragraph[] a, Paragraph [] b) {
		if(a == null && b == null) {
			return true;
		}
		if(a == null || b == null) {
			return false;
		}
		
		if(a.length != b.length) {
			return false;
		
			
		}
		for (int i = 0; i < b.length; i++) {
			if(!a[i].equals(b[i])) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean equals(Object o) {
		if(o instanceof Paragraph) {
			Paragraph other = (Paragraph)o;
			String s1 = other.text;
			String s2 = this.text;
			
			if(other.text.equals(this.text)) {
				
				return true;
			}
		}
		
		return false;
		
	}
	
	public void setCentered() {
		Centered = true;
		StyleConstants.setAlignment(style, StyleConstants.ALIGN_CENTER);
	}
	
	public void setJustified() {
		StyleConstants.setAlignment(style, StyleConstants.ALIGN_JUSTIFIED);
	}
	
	public void setColor(Color c) {
		this.c = c;
		StyleConstants.setForeground(style, c);
	}

	/**
	 * 
	 * @uml.property name="f"
	 */
	public void setFont(String font) {
		this.f = font;
		if (font != null) {

			StyleConstants.setFontFamily(style, font);
		}
	}

	/**
	 * 
	 * @uml.property name="size"
	 */
	public void setSize(int size) {
		this.size = size;
		StyleConstants.setFontSize(style, size);
	}

	
	public void setBold() {
		StyleConstants.setBold(style,true);
	}
	
	public String toString() {
		return text;
	}

	/**
	 * Returns the style.
	 * @return SimpleAttributeSet
	 * 
	 * @uml.property name="style"
	 */
	public SimpleAttributeSet getStyle() {
		return style;
	}

	/**
	 * Returns the text.
	 * @return String
	 * 
	 * @uml.property name="text"
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return
	 * 
	 * @uml.property name="centered"
	 */
	public boolean isCentered() {
		return Centered;
	}

	/**
	 * @return
	 * 
	 * @uml.property name="f"
	 */
	public String getF() {
		return f;
	}

	/**
	 * @param color
	 * 
	 * @uml.property name="c"
	 */
	public void setC(Color color) {
		c = color;
	}

	/**
	 * @return Returns the size.
	 * 
	 * @uml.property name="size"
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param text The text to set.
	 * 
	 * @uml.property name="text"
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return Returns the c.
	 * 
	 * @uml.property name="c"
	 */
	public Color getC() {
		return c;
	}

}
