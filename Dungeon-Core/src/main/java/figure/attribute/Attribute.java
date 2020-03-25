package figure.attribute;

//import java.util.*;

import java.io.Serializable;

/**
 * Attributwert einer Figur. Ein Attribut stellt das bezifferte Ma� fuer eine
 * Eigenschaft oder Faehigkeit der Figur dar. Eine Attribut besteht aus 2
 * Werten. Dem Basiswert und dem aktuellen Wert. Der Basiswert gibt an wie stark
 * die Eigenschaft prinzipiell ausgepraegt ist und der aktuelle Wert stellt das
 * momentane auf die Umstaende angepasste Ma� dar.
 * 
 */
public class Attribute implements Serializable {

	private final int name;

	private double value;

	private double basic;

	public static final int STRENGTH = 1;

	public static final int DEXTERITY = 2;

	public static final int PSYCHO = 3;

	public static final int AXE = 4;

	public static final int CLUB = 5;

	public static final int LANCE = 6;

	public static final int SWORD = 7;

	public static final int WOLFKNIFE = 8;

	public static final int NATURE_KNOWLEDGE = 9;

	public static final int CREATURE_KNOWLEDGE = 10;

	public static final int UNDEAD_KNOWLEDGE = 11;

	public static final int SCOUT = 12;

	public static final int THREAT = 13;

	public static final int HEALTH = 14;

	public static final int DUST = 15;

	public static final int DUSTREG = 16;

	public static final int HEALTHREG = 17;

	public static final int BRAVE = 18;

	public static final int CHANCE_TO_HIT = 19;

	public static final int HIT_POINTS = 20;

	public static final int FOUNTAIN = 21;
	public static final int OXYGEN = 21;

	// todo : refactor to enum

	public Attribute(int name, int v) {
		this.name = name;
		basic = v;
		value = basic;
	}

	public Attribute(int name, double v) {
		this.name = name;
		basic = v;
		value = basic;
	}

	public double getValue() {
		return value;
	}
	
	public void addToMax(int v) {
		addToMax((double)v);
	}
	
	public void addToMax(double v) {
		if(value + v > basic) {
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

	public int getType() {
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

	/**
	 * Sets the basic.
	 * 
	 * @param basic
	 *            The basic to set
	 * 
	 */
	public void setBasic(double basic) {
		this.basic = basic;
	}

}
