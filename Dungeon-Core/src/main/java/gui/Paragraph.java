/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

package gui;

import util.JDColor;


public class Paragraph {

	// private final SimpleAttributeSet style;

	
	private String text;

	
	private boolean centered;

	private boolean justified;
	
	private boolean bold;

	private JDColor c;

	
	private String font;

	
	private int size;

	

	public Paragraph(String txt) {
		text = txt;
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
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Paragraph) {
			Paragraph other = (Paragraph)o;
			if(other.text.equals(this.text)) {
				return true;
			}
		}
		
		return false;
		
	}
	
	public void setCentered() {
		centered = true;
	}
	
	public void setJustified() {
		this.justified = true;
	}
	
	public void setColor(JDColor c) {
		this.c = c;
		//StyleConstants.setForeground(style, c);
	}

	/**
	 * 
	 * @uml.property name="f"
	 */
	public void setFont(String font) {
		this.font = font;
	}

	/**
	 * 
	 * @uml.property name="size"
	 */
	public void setSize(int size) {
		this.size = size;
	}

	
	public void setBold() {
		bold = true;
	}
	
	@Override
	public String toString() {
		return text;
	}

	public boolean isJustified() {
		return justified;
	}

	public boolean isBold() {
		return bold;
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
		return centered;
	}

	/**
	 * @return
	 * 
	 * @uml.property name="f"
	 */
	public String getFont() {
		return font;
	}

	/**
	 * @param color
	 * 
	 * @uml.property name="c"
	 */
	public void setC(JDColor color) {
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
	public JDColor getColor() {
		return c;
	}

}
