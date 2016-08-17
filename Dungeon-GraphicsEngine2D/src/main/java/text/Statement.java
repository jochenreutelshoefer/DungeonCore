/*
 * Created on 09.02.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package text;

public class Statement {
	
	private String text;
	private int format;
	
	public Statement(String t, int i) {
		text = t;
		format = i;
	}

	/**
	 * @return Returns the format.
	 */
	public int getFormat() {
		return format;
	}

	/**
	 * @return Returns the text.
	 */
	public String getText() {
		return text;
	}
	

}
