package figure.monster;


import ai.AI;
import spell.TripleAttack;
import dungeon.Dungeon;
import dungeon.JDPoint;
import figure.*;
import figure.attribute.Attribute;



import game.JDEnv;
import gui.Texts;

public class Wolf extends NatureMonster {

	public static final int CHANCE_TO_HIT = 30;
	protected static final int  HEALTH_DAMAGE_BALANCE = 15;
	protected static final int SCATTER = 5;
	

	/**
	 * @return Returns the cHANCE_TO_HIT.
	 */
	@Override
	public int getCHANCE_TO_HIT() {
		return CHANCE_TO_HIT;
	}

	public static Wolf buildCustomWolf(int chance_to_hit, int damage, int health,int scatter, double healthRec, String name) {
		int value = (int) (((double)chance_to_hit * damage * health) / 5.8);
		Wolf w = new Wolf(value);
		w.health = new Attribute(Attribute.HEALTH,health);
		w.chanceToHit = new Attribute(Attribute.CHANCE_TO_HIT, chance_to_hit);
		w.minDamage = damage - scatter;
		w.maxDamage = damage + scatter;
		w.name = name;
		w.healthRecover = (int)healthRec;
		return w;
	}
	
	public Wolf(int value) {
		super(value);
		contrucHelpWolf(value);

	}

	public Wolf(int value, AI ai) {
		super(value, ai);
		contrucHelpWolf(value);

	}
	

	public Wolf(int value, boolean special_attack) {
		super(value);
		this.specialAttacking = special_attack;
		contrucHelpWolf(value);

		
	}

	protected void contrucHelpWolf(int value) {
		
		antiTumbleValue = 10;
		this.strength = new Attribute(Attribute.STRENGTH,5);
		this.dexterity = new Attribute(Attribute.DEXTERITY,11);
		String[] lvl_names = { JDEnv.getString("wolf1"), JDEnv.getString("wolf2"), JDEnv.getString("wolf3"),
				JDEnv.getString("wolf4"), JDEnv.getString("wolf5"), JDEnv.getString("wolf6") };
		this.lvl_names = lvl_names;

		name = (Texts.getName("wolf"));
		
		if(level >= 2) {
			spellbook.addSpell(new TripleAttack(1));
		}
	}
	
	@Override
	protected int getSCATTER() {
		return this.SCATTER;
	}

	@Override
	protected boolean makeSpecialAttack(Figure op) {
		//Fighter op = getTarget();
		attack(op);
		attack(op);
		attack(op);
		this.specialAttackCounter = 50;
		return false;
	}
	
	protected int getHEALTH_DAMAGE_BALANCE() {
		return this.HEALTH_DAMAGE_BALANCE;
	}

	@Override
	public double getAntiFleeFactor() {
		return 1.0;
	}

	@Override
	public int hunting() {
		return Monster.WOLF_HUNTING;
	}
}