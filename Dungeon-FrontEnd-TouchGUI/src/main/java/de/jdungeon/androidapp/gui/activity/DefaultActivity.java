package de.jdungeon.androidapp.gui.activity;

import game.RoomInfoEntity;
import gui.Paragraph;
import gui.Paragraphable;

public class DefaultActivity implements Activity {

	private final Object object;
	private RoomInfoEntity target;

	public DefaultActivity(Object o) {
		super();
		this.object = o;
	}

	public DefaultActivity(Object o, RoomInfoEntity target) {
		super();
		this.object = o;
		this.target = target;

	}

	@Override
	public Object getObject() {
		return object;
	}

	@Override
	public RoomInfoEntity getTarget() {
		return target;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DefaultActivity activity = (DefaultActivity) o;

		if (object != null ? !object.equals(activity.object) : activity.object != null) return false;
		return !(target != null ? !target.equals(activity.target) : activity.target != null);

	}

	@Override
	public int hashCode() {
		int result = object != null ? object.hashCode() : 0;
		result = 31 * result + (target != null ? target.hashCode() : 0);
		return result;
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
