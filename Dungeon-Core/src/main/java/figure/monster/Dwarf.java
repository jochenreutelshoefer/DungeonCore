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
	public static final int CHANCE_TO_HIT = 40;
	protected static final int SCATTER = 5;
	
	public Dwarf() {
		super(7000);
		tumbleValue = 0;
		this.strength = new Attribute(Attribute.Type.Strength,12);
		this.dexterity = new Attribute(Attribute.Type.Dexterity,5);
		// TODO Auto-generated constructor stub
		name = "Alberich";
	}
	
	@Override
	public double getAntiFleeFactor() {
		return 0;
	}
	
	@Override
	protected int getHEALTH_DAMAGE_BALANCE() {
		return HEALTH_DAMAGE_BALANCE;
	}

	@Override
	public int getCHANCE_TO_HIT() {
		return CHANCE_TO_HIT;
	}
	
	@Override
	protected int getSCATTER() {
		return SCATTER;
	}
	
	
	@Override
	public boolean makeSpecialAttack(Figure op) {
		//Fighter op = getTarget();
		attack(op, -1);
		attack(op, -1);
		
		this.specialAttackCounter = 50;
		
		return false;
	}
	
	@Override
	public int hunting() {
		return 0;
	}
	
	public Action turnElse(int a) {
		return null;
	}

}


