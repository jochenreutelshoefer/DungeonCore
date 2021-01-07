/*
 * Created on 09.02.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package text;

import org.jetbrains.annotations.NotNull;

public class Statement {

	private final String text;
	private final int format;
	private final int round;

	public Statement(@NotNull String t, int i, int round) {
		text = t.trim();
		format = i;
		this.round = round;
	}

	public int getRound() {
		return round;
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
