package de.jdungeon.androidapp.gui.itemWheel;

import gui.Paragraph;
import gui.Paragraphable;

public class ItemWheelActivity implements Paragraphable {

	private final Object object;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ItemWheelActivity that = (ItemWheelActivity) o;

		return !(object != null ? !object.equals(that.object) : that.object != null);
	}

	@Override
	public int hashCode() {
		return object != null ? object.hashCode() : 0;
	}

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
