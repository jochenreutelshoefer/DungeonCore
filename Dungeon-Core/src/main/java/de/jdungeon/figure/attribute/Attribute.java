package de.jdungeon.figure.attribute;

//import java.de.jdungeon.util.*;

import java.io.Serializable;

/**
 * Attributwert einer Figur. Ein Attribut stellt das bezifferte Ma� fuer eine
 * Eigenschaft oder Faehigkeit der Figur dar. Eine Attribut besteht aus 2
 * Werten. Dem Basiswert und dem aktuellen Wert. Der Basiswert gibt an wie stark
 * die Eigenschaft prinzipiell ausgepraegt ist und der aktuelle Wert stellt das
 * momentane auf die Umstaende angepasste Maß dar.
 */
public class Attribute implements Serializable {

	public enum Type {
		Strength(1),
		Dexterity(2),
		Psycho(3),
		Health(4),
		HealthReg(5),
		Dust(6),
		DustReg(7),
		Fountain(8),
		Oxygen(9),
		@Deprecated
		OtherDeprecatedAttributeType(666);

		private final int number;

		Type(int number) {
			this.number = number;
		}

		public static Type fromValue(int value) {
			for (Type type : Type.values()) {
				if (value == type.getNumber()) {
					return type;
				}
			}
			// unknown value
			return null;
		}

		public int getNumber() {
			return number;
		}
	}

	private final Type name;

	private double value;

	private double basic;

	public Attribute(Type name, int v) {
		this.name = name;
		basic = v;
		value = basic;
	}

	public Attribute(Type name, double v) {
		this.name = name;
		basic = v;
		value = basic;
	}

	public double getValue() {
		return value;
	}

	public void addToMax(int v) {
		addToMax((double) v);
	}

	public void addToMax(double v) {
		if (value + v > basic) {
			value = basic;
		}
		else {
			value += v;
		}
	}

	@Override
	public String toString() {
		return new String("" + getValue() + "/" + getBasic());
	}

	public double getBasic() {
		return basic;
	}

	public Type getType() {
		return name;
	}

	public void incBasic() {
		basic++;
		value++;
	}

	public void incBasic(double incr) {
		basic += incr;
		value += incr;
	}

	public void modValue(double k) {
		int x = (int) (k * 100);
		k = ((double) x) / 100;
		value += k;
	}

	public void modBasic(double k) {
		basic += k;
	}

	public void setValue(int k) {
		value = k;
	}

	public void setValue(double k) {
		value = k;
	}

	public int perCent() {
		return (int) (((float) getValue() / (float) getBasic()) * 100);
	}

	public double relValue() {
		return ((float) getValue() / (float) getBasic());
	}
}
