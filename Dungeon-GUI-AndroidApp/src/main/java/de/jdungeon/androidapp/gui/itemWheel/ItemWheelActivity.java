package de.jdungeon.androidapp.gui.itemWheel;

import gui.Paragraph;
import gui.Paragraphable;

public class ItemWheelActivity implements Paragraphable {

	private final Object object;

	public Object getObject() {
		return object;
	}

	public ItemWheelActivity(Object o) {
		super();
		this.object = o;
	}

	@Override
	public String toString() {
		return object.toString();
	}

	@Override
	public Paragraph[] getParagraphs() {
		if (object instanceof Paragraphable) {
			return ((Paragraphable) object).getParagraphs();
		} else {
			Paragraph[] p = new Paragraph[2];
			p[0] = new Paragraph(object.toString());
			p[1] = new Paragraph("Kosten: 0");
			return p;
		}
	}
}
