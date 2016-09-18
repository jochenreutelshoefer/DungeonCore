/*
 * Created on 13.02.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.monster;

import figure.Figure;
import figure.action.Action;
import figure.attribute.Attribute;


/**
 * Zwerg Alberich, Hüter des Schatzes. Soll nur einmal vorkommen.
 */
public class Dwarf extends Monster {
	
	private static final int HEALTH_DAMAGE_BALANCE = 10;
	public static final int CHANCE_TO_HIT = 30;
	protected static final int SCATTER = 5;
	
	public  Dwarf() {
		super(7000);
		tumbleValue = 0;
		this.strength = new Attribute(Attribute.STRENGTH,12);
		this.dexterity = new Attribute(Attribute.DEXTERITY,5);
		// TODO Auto-generated constructor stub
		name = "Alberich";
//		int value = 4000;
//		int HealthI = value / (50 + (int) (Math.random() * 10));
//
//		value = value / HealthI;
//		health = new Attribute(Attribute.HEALTH, HealthI);
//		psycho = new Attribute(Attribute.PSYCHO, 20);
//		int average = value / (3 + (int) (Math.random() * 4));
//		int scatter = 1 + (int) (Math.random() * (average / 4));
//		minDamage = average - scatter;
//		maxDamage = average + scatter;
//		//	value = value / average;
//		chanceToHit =
//			new Attribute(
//					Attribute.CHANCE_TO_HIT,
//				(int) (((float) value / average) * 6));
//		//(100));

		
	}
	
	public double getAntiFleeFactor() {
		return 0;
	}
	
	protected int getHEALTH_DAMAGE_BALANCE() {
		return this.HEALTH_DAMAGE_BALANCE;
	}

	public int getCHANCE_TO_HIT() {
		return CHANCE_TO_HIT;
	}
	
	protected int getSCATTER() {
		return this.SCATTER;
	}
	
	
	public boolean makeSpecialAttack(Figure op) {
		//Fighter op = getTarget();
		attack(op);
		attack(op);
		
		this.specialAttackCounter = 50;
		
		return false;
	}
	
	public int hunting() {
		return 0;
	}
	
	public Action turnElse(int a) {
		return null;
	}

}


