package figure.monster;


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
		//construcHelp(value);

	}
	
	public Ogre(int value,Dungeon d) {

		super(value,d );
		construcHelpOgre(value);
		//construcHelp(value);

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
		
		Mclass = getLvlName(value, lvl_names);
		this.lvl_names = lvl_names;

		name = (Texts.getName("ogre"));
		
		if(level >= 2) {
			spellbook.addSpell(new MightyStruck(1));
		}
	}
	
	protected int getSCATTER() {
		return this.SCATTER;
	}

	public static String SPECIAL_ATTACK_GUI_STATEMENT = " zimmert Dir so ein über den Kopf, dass Du bonommen umhertaumelst!";
	protected boolean makeSpecialAttack(Figure op) {
		//Fighter op = getTarget();
		
		this.half_bonus = true;
		this.makesgoldenHit = true;
		System.out.println("Ogre.makeSpecialAttack!");
		op.decFightAP(1);
		if (op instanceof Hero) {
			this.getRoom().distributePercept(new SpecialAttackPercept(Monster.GHUL,op,this));
			Inventory sachen = ((Hero) op).getInventory();
			Helmet helm = sachen.getHelmet1();
			if (helm != null) {
				helm.takeRelDamage(0.3);
			}
		}
		attack(op);
		

		this.specialAttackCounter = 50;
		return false;

	}
	
	public int getCHANCE_TO_HIT() {
		return CHANCE_TO_HIT;
	}
	
	protected int getHEALTH_DAMAGE_BALANCE() {
		return this.HEALTH_DAMAGE_BALANCE;
	}

//	public Action turnElse(int c) {
//		recover();
//		if (c == 0) {
//			if (spitted) {
//				return null;
//			}
//		} else {
//			////System.out.println("Oger geht vom Rudel weg");
//			boolean b = false;
//			while (!b) {
//				int a = (int) (Math.random() * 80) + 20;
//				if (a <= 20) {
//					return null;
//				} else if (a <= 40) {
//					return new ActionMove(RouteInstruction.SOUTH);
//					//goSouth(); //////System.out.println(name+" geht suedlich");
//				} else if (a <= 60) {
//					return new ActionMove(RouteInstruction.EAST);
//					//goEast();//////System.out.println(name+" get oestlich");
//				} else if (a <= 80) {
//					return new ActionMove(RouteInstruction.NORTH);
//					//goNorth();//////System.out.println(name+" get noerdlich");
//				} else {
//					return new ActionMove(RouteInstruction.WEST);
//					//goWest();//////System.out.println(name+" get westlich");
//				}
//			}
//		}
//		return null;
//	}

	public double getAntiFleeFactor() {
		return 0.3;
	}

	public int hunting() {
		return Monster.OGRE_HUNTING;
	}

}
