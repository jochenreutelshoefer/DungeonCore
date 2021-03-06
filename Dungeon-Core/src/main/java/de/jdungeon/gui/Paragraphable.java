/*
 * Created on 13.12.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.gui;

/**
 * @author Jochen
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface Paragraphable {

	Paragraph[] getParagraphs();

	default String getHeaderName() {
		return getParagraphs()[0].getText();
	}

	default String getRole() {
		if(getParagraphs().length > 1) {
			return getParagraphs()[1].getText();
		}
		return null;
	}

	default String getStatus() {
		return "";
	}

	default String getDescription() {
		return "";
	}
}
