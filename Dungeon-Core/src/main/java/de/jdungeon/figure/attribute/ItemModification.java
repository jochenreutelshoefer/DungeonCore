package de.jdungeon.figure.attribute;

import java.io.Serializable;

import de.jdungeon.gui.Texts;

//import java.de.jdungeon.util.*;

/**
 * Hilfsklasse zum Modifizieren von Attributwerten
 * Welches Attribut und um wieviel verändert werden soll. - Bei magischen Gegenständen
 */
public class ItemModification implements Serializable {

	private final double value;

	private final Attribute.Type attribute;

	public ItemModification(Attribute.Type key, int value) {

		this.value = value;
		this.attribute = key;
	}

	public ItemModification(Attribute.Type attribute, double value) {
		this.value = value;
		this.attribute = attribute;
	}

	public Attribute.Type getAttribute() {
		return attribute;
	}

	public double getValue() {
		return value;
	}

	public String getText() {
		return Texts.getAttributeName(attribute) + " +" + value;
	}

}
