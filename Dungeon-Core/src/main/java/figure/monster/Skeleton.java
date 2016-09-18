package figure.monster;

import ai.AI;
import figure.Figure;
import figure.attribute.Attribute;
import figure.hero.Hero;
import figure.percept.SpecialAttackPercept;
import game.JDEnv;
import gui.Texts;
import item.Item;
import item.ItemValueComparator;
import item.equipment.weapon.Weapon;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import spell.Rust;
import dungeon.Dungeon;

public class Skeleton extends UndeadMonster {

	public static final int CHANCE_TO_HIT = 40;

	private static final int HEALTH_DAMAGE_BALANCE = 10;

	protected static final int SCATTER = 3;

	public Skeleton(int value) {
		super(value);
		construcHelpSkeleton(value);
	}

	public Skeleton(int value, AI ai) {
		super(value, ai);
		construcHelpSkeleton(value);
	}


	public Skeleton(int value, boolean special_attack) {

		super(value);

		this.specialAttacking = special_attack;
		construcHelpSkeleton(value);
		//construcHelp(value);

	}

	protected void construcHelpSkeleton(int value) {
		String[] lvl_names = { JDEnv.getString("skel1"), JDEnv.getString("skel2"), JDEnv.getString("skel3"),
				JDEnv.getString("skel4"), JDEnv.getString("skel5"), JDEnv.getString("skel6") };
		this.lvl_names = lvl_names;
		this.antiTumbleValue = 20;
		this.strength = new Attribute(Attribute.STRENGTH,7);
		this.dexterity = new Attribute(Attribute.DEXTERITY,9);
		tumbleValue = 0;
		name = (Texts.getName("skeleton"));

		if(level >= 2) {
			spellbook.addSpell(new Rust(1));
		}
	}

	@Override
	protected int getSCATTER() {
		return this.SCATTER;
	}

	//	protected void construcHelp(int value) {
	//		
	//	
	//		worth = value;
	//	
	//		
	//	
	//		int HealthI = value / (40 + (int)(Math.random()*10));
	//		value = value / HealthI;
	//		Health = new Attribute(Character.HEALTH, HealthI);
	//		Psycho = new Attribute(Character.PSYCHO, 10);
	//		int average = value /(3 +(int)(Math.random() * 5));
	//		int scatter = 1 + (int)(Math.random() * (average /4));
	//		Min_Damage = average - scatter;
	//		Max_Damage = average + scatter;
	//		
	//		Chance_to_hit = new Attribute (Character.CHANCE_TO_HIT,(int)
	// (((float)value /
	// average )* 6));
	//
	//	}

	public static String SPECIAL_ATTACK_GUI_STATEMENT = " beschw�rt Rost und Verwesung �ber Deine Waffe!";
	
	
	@Override
	protected boolean makeSpecialAttack(Figure op) {
		//Fighter op = getTarget();
		List<Item> l = new LinkedList<Item>();
		if (op instanceof Hero) {
			l = ((Hero) op).getInventory().getWeaponList();
		}
		Collections.sort(l, new ItemValueComparator());
		if (l.size() > 0) {
			Weapon weap = ((Weapon) l.get(0));
			weap.takeRelDamage(0.3);
		}
		getRoom().distributePercept(new SpecialAttackPercept(Monster.SKELETON,op,this));
		this.specialAttackCounter = 50;
		return false;

	}

	@Override
	public int getCHANCE_TO_HIT() {
		return CHANCE_TO_HIT;
	}

	@Override
	protected int getHEALTH_DAMAGE_BALANCE() {
		return this.HEALTH_DAMAGE_BALANCE;
	}

//	public Action turnElse(int c) {
//		recover();
//		int a = (int) (Math.random() * 100);
//		if (c == 1) { //damit er auf jeden Fall weggeht
//			a = (int) (Math.random() * 20) + 80;
//		}
//		if (a <= 80) {
//			return null;
//		} else if (a <= 85) {
//			return new ActionMove(RouteInstruction.SOUTH);
//			//goSouth(); //////System.out.println(name+" geht suedlich");
//		} else if (a <= 90) {
//			return new ActionMove(RouteInstruction.EAST);
//			//goEast();//////System.out.println(name+" get oestlich");
//		} else if (a <= 95) {
//			return new ActionMove(RouteInstruction.NORTH);
//			//goNorth();//////System.out.println(name+" get noerdlich");
//		} else {
//			return new ActionMove(RouteInstruction.WEST);
//			//goWest();//////System.out.println(name+" get westlich");
//		}
//	}

	@Override
	public double getAntiFleeFactor() {
		return 0.9;
	}

	@Override
	public int hunting() {
		return Monster.SKELETON_HUNTING;
	}
}