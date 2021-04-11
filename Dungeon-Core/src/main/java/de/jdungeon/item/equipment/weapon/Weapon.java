package de.jdungeon.item.equipment.weapon;

import java.util.LinkedList;

import de.jdungeon.dungeon.Position;
import de.jdungeon.figure.attribute.ItemModification;
import de.jdungeon.game.JDEnv;
import de.jdungeon.gui.Paragraph;
import de.jdungeon.item.equipment.EquipmentItem;
import de.jdungeon.util.JDColor;

/**
 * abstrakte Klasse weapon
 *
 * @author <a href="mailto:"></a>
 * @version 1.0
 */

public abstract class Weapon extends EquipmentItem {

	public final static int AXE = 0;
	public final static int CLUB = 1;
	public final static int LANCE = 2;
	public final static int SWORD = 3;
	public final static int WOLFKNIFE = 4;

	//attribute hit_points;
	private int hitsPerHP = 80;

	private int actualHits = 0;

	//String name;
	public Weapon(int value, boolean magic, int hitpoints) {
		super(value, magic);
	}

	public Weapon(int value, LinkedList mods, int hitpoints) {
		super(value, true);
		modifications = mods;
	}

	public int getDamage(int k) {
		actualHits++;
		if (actualHits == hitsPerHP) {
			actualHits = 0;
		}

		if (k == 0) {
			return getMin_Damage()
					+ ((int) (Math.random() * ((2 * scatter) + 1)));
		}
		else if (k == -1) {
			return getMin_Damage();
		}
		else if (k == 1) {
			return getMaxDamage();
		}
		else {
			return 0;
		}
	}

	protected int RANGE_NEAR;
	protected int RANGE_FAR;
	protected int RANGE_MID;

	public int getRangeCapability(int range) {
		int c = -1;
		if (range == Position.DIST_FAR) {
			c = RANGE_FAR;
		}
		if (range == Position.DIST_NEAR) {
			c = RANGE_NEAR;
		}
		if (range == Position.DIST_MID) {
			c = RANGE_MID;
		}
		return c;
	}

	protected int averageDamage;
	protected int scatter;

	protected int chanceToHit;

	protected int orcModifier;

	protected int ghulModifier;

	protected int wolfModifier;

	protected int bearModifier;

	protected int skeletonModifier;

	protected int ogreModifier;

	protected static int tumbleBasicValue;

	public static Weapon newRandomWeapon(double d, boolean magic) {
		int a = (int) (Math.random() * 100);
		int value = (int) d;
		if (a <= 15) {
			return new Lance(value, false);
		}
		else if (a <= 30) {
			return new Wolfknife(value, false);
		}
		else if (a <= 45) {
			return new Club(value, false);
		}
		else if (a <= 70) {
			return new Axe(value, false);
		}
		else {
			return new Sword(value, false);
		}
	}

	/**
	 * Describe <code>getMin_Damage</code> method here.
	 *
	 * @return an <code>int</code> value
	 */
	public int getMin_Damage() {
		int a = getAverageDamage() - scatter;
		if (a < 0) {
			return 0;
		}
		return a;
	}

	/**
	 * Describe <code>getMax_Damage</code> method here.
	 *
	 * @return an <code>int</code> value
	 */
	public int getMaxDamage() {
		return getAverageDamage() + scatter;
	}

	/**
	 * Describe <code>getChance_to_hit</code> method here.
	 *
	 * @return an <code>int</code> value
	 */
	public int getChanceToHit() {
		return chanceToHit;
	}

	public int getTumbleBasic() {
		return this.tumbleBasicValue;
	}

	private String getRangeValuationString(int value) {

		if (value < 35) {
			return "--";
		}
		if (value < 75) {
			return "-";
		}
		if (value < 125) {
			return "0";
		}
		if (value < 160) {
			return "+";
		}
		return "++";
	}

	@Override
	public Paragraph[] getParagraphs() {
		Paragraph[] p = new Paragraph[4];
		p[0] = new Paragraph(getClassName());
		p[0].setSize(24);
		p[0].setCentered();
		p[0].setColor(new JDColor(82, 82, 82));
		p[0].setBold();

		p[1] = new Paragraph(getClassName() + " "
				+ getMin_Damage()
				+ "-"
				+ getMaxDamage()
				+ " : "
				+ chanceToHit);
		p[1].setSize(16);
		p[1].setCentered();
		p[1].setColor(JDColor.black);
		p[1].setBold();

		String s = JDEnv.getResourceBundle().getString("damage") + ": "
				+ getMin_Damage()
				+ "-"
				+ getMaxDamage()
				+ "\n"
				+ JDEnv.getResourceBundle().getString("chance_to_hit") + ": "
				+ chanceToHit
				+ "%\n"
				+ JDEnv.getResourceBundle().getString("state");
		if (magic) {
			for (int i = 0; i < modifications.size(); i++) {
				ItemModification m = (ItemModification) modifications.get(i);
				s += m.getText() + "  ";
			}
		}

		p[2] = new Paragraph(s);
		p[2].setSize(14);
		p[2].setCentered();
		p[2].setColor(JDColor.black);

		int rangeNear = this.getRangeCapability(Position.DIST_NEAR);
		int rangeFar = this.getRangeCapability(Position.DIST_FAR);
		int rangeMid = this.getRangeCapability(Position.DIST_MID);

		String nearString = getRangeValuationString(rangeNear);
		String farString = getRangeValuationString(rangeFar);
		String midString = getRangeValuationString(rangeMid);

		String rangeString = JDEnv.getResourceBundle().getString("distance") + ": " + JDEnv.getResourceBundle()
				.getString("near") + ": " + nearString + " | " + JDEnv.getResourceBundle()
				.getString("mid") + ": " + midString + " | " + JDEnv.getResourceBundle().getString("far") + ": " +
				farString;

		p[3] = new Paragraph(rangeString);
		p[3].setSize(14);
		p[3].setCentered();
		p[3].setColor(JDColor.black);

		return p;
	}

	public int getAverageDamage() {
		return averageDamage;
	}

	public int getScatter() {
		return scatter;
	}

	/**
	 * Describe <code>getOrc_Modifier</code> method here.
	 *
	 * @return an <code>int</code> value
	 */
	public int getOrcModifier() {
		return orcModifier;
	}

	/**
	 * Describe <code>getOgre_Modifier</code> method here.
	 *
	 * @return an <code>int</code> value
	 */
	public int getOgreModifier() {
		return ogreModifier;
	}

	/**
	 * Describe <code>getWolf_Modifier</code> method here.
	 *
	 * @return an <code>int</code> value
	 */
	public int getWolfModifier() {
		return wolfModifier;
	}

	/**
	 * Describe <code>getBear_Modifier</code> method here.
	 *
	 * @return an <code>int</code> value
	 */
	public int getBearModifier() {
		return bearModifier;
	}

	/**
	 * Describe <code>getSkeleton_Modifier</code> method here.
	 *
	 * @return an <code>int</code> value
	 */
	public int getSkeletonModifier() {
		return skeletonModifier;
	}

	/**
	 * Describe <code>getGhul_Modifier</code> method here.
	 *
	 * @return an <code>int</code> value
	 */
	public int getGhulModifier() {
		return ghulModifier;
	}

	public void setName(String n) {
		name = n;
	}

	@Override
	public String toString() {
		//if(Max_Damage == 0) return ("keine");
		//else { 
		if (unique) {
			return getName();
		}
		String s = "";
//		s+="<html><font color =";
//		
//		String color = "black";
//		if (hit_points.perCent() <= 50) {
//				 color = "red";
//				} else if (hit_points.perCent() <= 70) {
//					color = "yellow";
//				} 
//		s += color+">";
//		
		s +=
				(getName()
						+ " "
						+ getMin_Damage()
						+ "-"
						+ getMaxDamage()
						+ " : "
						+ chanceToHit);
		if (magic) {
			if (unique) {
				s += "(u)";
			}
			else {
				s += "(m)";
			}
		}
//		s += "</font></html>";

		return s;
		//}
	}

	@Override
	public String getName() {
		//if (unique) {
		return name;
		//}
		//else return getClassName();

	}

	private String getClassName() {
		if (this instanceof Sword) {
			return JDEnv.getResourceBundle().getString("sword");
		}
		else if (this instanceof Club) {
			return JDEnv.getResourceBundle().getString("club");
		}
		else if (this instanceof Axe) {
			return JDEnv.getResourceBundle().getString("axe");
		}
		else if (this instanceof Lance) {
			return JDEnv.getResourceBundle().getString("lance");
		}
		else if (this instanceof Wolfknife) {
			return JDEnv.getResourceBundle().getString("wolfknife");
		}
		else {
			return "unbekannte Waffe";
		}
	}

	@Override
	public String getText() {
		String s =
				JDEnv.getResourceBundle().getString("damage") + ": "
						+ getMin_Damage()
						+ "-"
						+ getMaxDamage()
						+ "\n"
						+ JDEnv.getResourceBundle().getString("chance_to_hit") + ": "
						+ chanceToHit
						+ "%\n";
		if (magic) {
			for (int i = 0; i < modifications.size(); i++) {
				ItemModification m = (ItemModification) modifications.get(i);
				s += m.getText() + " ";
			}
		}

		return s;
	}

	protected static int getHitPoints(int worth) {
		return worth / 2;
	}

	// 	protected void setModifiers() {
	//    	Bear_Modifier = Bear_ModifierS;
	//		Ogre_Modifier = Ogre_ModifierS;
	//		Skeleton_Modifier = Skeleton_ModifierS;
	//		Wolf_Modifier = Wolf_ModifierS;
	//		Orc_Modifier = Orc_ModifierS;
	//		Ghul_Modifier = Ghul_ModifierS;
	//
	//	}

	/**
	 * Returns the actualHits.
	 *
	 * @return int
	 */
	public int getActualHits() {
		return actualHits;
	}

	/**
	 * Returns the hitsPerHP.
	 *
	 * @return int
	 */
	public int getHitsPerHP() {
		return hitsPerHP;
	}

	/**
	 * Sets the actualHits.
	 *
	 * @param actualHits The actualHits to set
	 */
	public void setActualHits(int actualHits) {
		this.actualHits = actualHits;
	}

	/**
	 * Sets the average_Damage.
	 *
	 * @param average_Damage The average_Damage to set
	 */
	public void setAverageDamage(int average_Damage) {
		averageDamage = average_Damage;
	}

	/**
	 * Sets the hitsPerHP.
	 *
	 * @param hitsPerHP The hitsPerHP to set
	 */
	public void setHitsPerHP(int hitsPerHP) {
		this.hitsPerHP = hitsPerHP;
	}

	/**
	 * Sets the scatter.
	 *
	 * @param scatter The scatter to set
	 */
	public void setScatter(int scatter) {
		this.scatter = scatter;
	}
}
