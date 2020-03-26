package figure.monster;


import figure.APAgility;
import spell.MightyStruck;
import dungeon.Dungeon;
import item.equipment.Helmet;
import figure.Figure;


import figure.attribute.Attribute;
import figure.hero.Hero;
import figure.hero.Inventory;
import figure.percept.SpecialAttackPercept;

import game.JDEnv;
import gui.Texts;

public class Ogre extends CreatureMonster {

	public static final int CHANCE_TO_HIT = 12;
	private static final int HEALTH_DAMAGE_BALANCE = 20;
	protected static final int SCATTER = 5;

	public Ogre(int value) {

		super(value);
		construcHelpOgre(value);

	}
	

	protected void construcHelpOgre(int value) {
		
		tumbleValue = 30;
		this.antiTumbleValue = 80;
		this.strength = new Attribute(Attribute.STRENGTH,12);
		this.dexterity = new Attribute(Attribute.DEXTERITY,6);
		String[] lvl_names =
			{
				JDEnv.getString("ogre1"),
				JDEnv.getString("ogre2"),
				JDEnv.getString("ogre3"),
				JDEnv.getString("ogre4"),
				JDEnv.getString("ogre5"),
				JDEnv.getString("ogre6") };
		
		this.lvl_names = lvl_names;

		name = (Texts.getName("ogre"));
		
		if(level >= 2) {
			spellbook.addSpell(new MightyStruck(1));
		}
	}

	@Override
	protected APAgility createAgility() {
		return new APAgility(8, 0.7);
	}
	
	@Override
	protected int getSCATTER() {
		return this.SCATTER;
	}

	@Override
	protected boolean makeSpecialAttack(Figure op) {
		//Fighter op = getTarget();

		// TODO: re-implement as a skill using oxygen reduction

		/*
		this.half_bonus = true;
		this.makesgoldenHit = true;
		System.out.println("Ogre.makeSpecialAttack!");
		op.decActionPoints(1, -1);
		if (op instanceof Hero) {
			this.getRoom().distributePercept(new SpecialAttackPercept(op,this, -1));
			Inventory sachen = ((Hero) op).getInventory();
			Helmet helm = sachen.getHelmet1();
			if (helm != null) {
				helm.takeRelDamage(0.3);
			}
		}
		attack(op, -1);
		

		this.specialAttackCounter = 50;
		return false;

		*/
		return false;
	}
	
	@Override
	public int getCHANCE_TO_HIT() {
		return CHANCE_TO_HIT;
	}
	
	protected int getHEALTH_DAMAGE_BALANCE() {
		return this.HEALTH_DAMAGE_BALANCE;
	}

	@Override
	public double getAntiFleeFactor() {
		return 0.3;
	}

	@Override
	public int hunting() {
		return Monster.OGRE_HUNTING;
	}

}
