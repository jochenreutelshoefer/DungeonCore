package figure.monster;




import spell.Cobweb;
import dungeon.Dungeon;
import item.equipment.Armor;
import item.equipment.Helmet;

import figure.Figure;


import figure.attribute.Attribute;
import figure.hero.Hero;
import figure.hero.Inventory;
import figure.percept.SpecialAttackPercept;

import game.JDEnv;
import gui.Texts;

public class Spider extends NatureMonster {

	public static final int CHANCE_TO_HIT = 25;
	private static final int HEALTH_DAMAGE_BALANCE = 10;
	protected static final int SCATTER = 3;
	
	
//	public Bear(int value, Dungeon d, JDPoint p) {
//		super();
//		//this.game = game;
//		location = p;
//		this.home = d;
//		String[] lvl_names =
//			{
//				"Junger B�r",
//				"Kleiner B�r",
//				"B�r",
//				"H�hlenb�r",
//				"Riesenb�r",
//				"Kriegsb�r" };
//		this.lvl_names = lvl_names;
//
//		name = Texts.getName("bear");
//		worth = value;
//		Mclass = getLvlName(worth, lvl_names);
//		//if(worth <= 1500){ Mclass = "B�r";}
//		//else{ Mclass = "H�hlenbar";}
//
//		value = (value / this.CHANCE_TO_HIT) * 6;
//		tumble_value = 10;
//
//		int HealthI = value / (10 + (int) (Math.random() * 10));
//
//		value = value / HealthI;
//		Health = new Attribute(Character.HEALTH, HealthI);
//		Psycho = new Attribute(Character.PSYCHO, 10);
//		int average = value;
//
//		int scatter = 1 + (int) (Math.random() * (average / 4));
//		Min_Damage = average - scatter;
//		Max_Damage = average + scatter;
//		// value = value / average;
//		Chance_to_hit = new Attribute(Character.CHANCE_TO_HIT, this.CHANCE_TO_HIT);
//		////System.out.println("Neuer :"+this.getClass());
//		////System.out.println("Health: "+HealthI);
//		////System.out.println("Damage: "+average);
//		////System.out.println("Chance: "+this.CHANCE_TO_HIT);
//	}

	public Spider(int value) {
		super(value);
		construcHelpBear(value);
	}
	
	protected void construcHelpBear(int value) {
		tumbleValue = 10;
		this.antiTumbleValue = 80;
		this.strength = new Attribute(Attribute.STRENGTH,12);
		this.dexterity = new Attribute(Attribute.DEXTERITY,9);
		
		if(level >= 2) {
			this.spellbook.addSpell(new Cobweb(1));
		}
		
		
		name = Texts.getName("bear");
	}
	
	@Override
	protected int getSCATTER() {
		return this.SCATTER;
	}
	
	protected int getHEALTH_DAMAGE_BALANCE() {
		return this.HEALTH_DAMAGE_BALANCE;
	}
	
	@Override
	public int getCHANCE_TO_HIT() {
		return CHANCE_TO_HIT;
	}

	public static String GUI_STATEMENT = ("Br�t Dir derma�en eine �ber, dass Deine R�stung in Fetzen von Dir f�llt1");
	
	
	@Override
	protected boolean makeSpecialAttack(Figure op) {
			//Fighter op = getTarget();
			if(op instanceof Hero) {
				Inventory sachen = ((Hero)op).getInventory();
				Helmet helm = sachen.getHelmet1();
				if(helm != null) {
					helm.takeRelDamage(0.5);
					sachen.layDown(helm);
					
				}
				Armor ruestung = sachen.getArmor1();
				if(ruestung != null) {
					ruestung.takeRelDamage(0.3);
				}
				
				getRoom().distributePercept(new SpecialAttackPercept(op,this, -1));
				
			}
			//attack(op);
			//attack(op);
			
			this.specialAttackCounter = 50;
		return false;
	}
	
//	public Action turnElse(int c) {
//		recover();
//		if (c == 0) {
//			if(spitted) {
//				
//			}
//		} else {
//			////System.out.println("Baer geht vom Rudel weg");
//
//			boolean b = false;
//			while (!b) {
//				int a = (int) (Math.random() * 80) + 20;
//				if (a <= 20) {
//				} else if (a <= 40) {
//					return new ActionMove(RouteInstruction.SOUTH);
//					//goSouth(); //////System.out.println(name+" geht
//					// suedlich");
//				} else if (a <= 60) {
//					return new ActionMove(RouteInstruction.EAST);
//					//goEast();//////System.out.println(name+" get oestlich");
//				} else if (a <= 80) {
//					return new ActionMove(RouteInstruction.NORTH);
//					//goNorth();//////System.out.println(name+" get
//					// noerdlich");
//				} else {
//					return new ActionMove(RouteInstruction.WEST);
//					//goWest();//////System.out.println(name+" get westlich");
//				}
//
//			}
//
//		}
//		return null;
//
//	}
	
	@Override
	public double getAntiFleeFactor() {
		return 0.4;
	}
	
	@Override
	public int hunting() {
		return Monster.BEAR_HUNTING;	
	}

}
