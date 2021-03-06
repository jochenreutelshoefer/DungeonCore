package de.jdungeon.skill.attack;

import de.jdungeon.dungeon.Position;
import de.jdungeon.figure.Figure;

/**
 * Ein Schlag. Enthaelt Staerke, Typ und Akteur.
 *
 */
public class Slap {
	
	public static final int STANDARD = 0;
	public static final int FIRE = 1;
	public static final int LIGHTNING = 2;
	public static final int MAGIC = 3;
	public static final int MIXED = 4;
	public static final int POISON = 5;

	private Figure actor = null;

	private final Figure target = null;

	private final float precision;

	private int type;

	private int valueStandard = 0;

	private int valueMagic = 0;

	private int valueFire = 0;

	private int valueLightning = 0;

	private int valuePoison = 0;


	
	public String toString() {
		String s = "";
		s+= "Attacker: "+actor.toString()+"\n";
		s+= "Wert: "+valueStandard +"\n";
		s+= "Precision: "+precision+"\n";
		s+= "Fire: "+valueFire+"\n";
		s+= "---\n";
		return s;
	}
	
	public Slap(Figure attacker, int value, float precision) {
		this.actor = attacker;
		this.valueStandard = value;
		this.precision = precision;
	}
	
	public Slap(Figure attacker, int type, int value, int precision) {
		this.actor = attacker;
		this.precision = precision;
		this.type = type;
		if(type == Slap.FIRE) {
			valueFire = value;
		}
		if(type == Slap.LIGHTNING) {
			valueLightning = value;
		}
		if(type == Slap.MAGIC) {
			valueMagic = value;
		}
		if(type == Slap.POISON) {
			valuePoison = value;
		}
	}

	/**
	 * Returns the magic.
	 * @return boolean
	 * 
	 */
	public boolean isMagic() {
		return false;
	}

	/**
	 * Returns the value_fire.
	 * @return int
	 * 
	 */
	public int getValueFire() {
		return valueFire;
	}

	/**
	 * Returns the value_lightning.
	 * @return int
	 * 
	 */
	public int getValueLightning() {
		return valueLightning;
	}

	/**
	 * Returns the value_magic.
	 * @return int
	 * 
	 */
	public int getValueMagic() {
		return valueMagic;
	}

	/**
	 * Returns the value_standard.
	 * @return int
	 * 
	 */
	public int getValueStandard() {
		int dist = 1;
		if(actor != null && target != null) {
			dist = actor.getPos().getDistanceTo(target.getPos());
		}

		if(dist == Position.DIST_FAR) {
			return ((valueStandard * 40) /100);
		}
		if(dist == Position.DIST_MID) {
			return ((valueStandard * 75) /100);
		}
		if(dist == Position.DIST_NEAR) {
			return ((valueStandard * 100) /100);
		}
		return 0;
	}

	/**
	 * Returns the type.
	 * @return int
	 * 
	 */
	public int getType() {
		return type;
	}

	/**
	 * Returns the actor.
	 * @return fighter
	 * 
	 */
	public Figure getActor() {
		return actor;
	}

	/**
	 * @return
	 * 
	 */
	public int getValue_poison() {
		return valuePoison;
	}

	/**
	 * @return
	 * 
 */

	public float getPrecision() {
		return precision;
	}

	public void setValueFire(int valueFire) {
		this.valueFire = valueFire;
	}

	public void setValueLightning(int valueLightning) {
		this.valueLightning = valueLightning;
	}

	public void setValueMagic(int valueMagic) {
		this.valueMagic = valueMagic;
	}

	public void setValuePoison(int valuePoison) {
		this.valuePoison = valuePoison;
	}

	public void setValueStandard(int valueStandard) {
		this.valueStandard = valueStandard;
	}


}
