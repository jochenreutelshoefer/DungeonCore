package figure.attribute;

import gui.Texts;

//import java.util.*;

/**
 * Hilfsklasse zum Modifizieren von Attributwerten
 * Welches Attribut und um wieviel verändert werden soll. - Bei magischen Gegenständen
 */
public class ItemModification {

	private double value;

	private int attribute;

	public ItemModification(int key, int value) {

		this.value = value;
		this.attribute = key;
	}

	public ItemModification(int attribute, double value) {
		this.value = value;
		this.attribute = attribute;
	}

	public int getAttribute() {
		return attribute;
	}

	public double getValue() {
		return value;
	}

	public String getText() {
		return Texts.getAttributeName(attribute) + " +" + value;
	}

}
