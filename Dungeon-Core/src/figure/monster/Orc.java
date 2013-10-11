package figure.monster;

import item.ItemValueComparator;
import item.Item;

import java.util.*;

import spell.StealOrc;

import dungeon.Dungeon;
import dungeon.JDPoint;



import figure.Figure;


import figure.attribute.Attribute;
import figure.hero.Hero;
import figure.hero.Inventory;
import figure.percept.TextPercept;

import game.JDEnv;
import gui.Texts;

public class Orc extends CreatureMonster {

	public static final int CHANCE_TO_HIT = 25;
	private static final int HEALTH_DAMAGE_BALANCE = 12;
	protected static final int SCATTER = 2;
	
	
	public Orc(int value, Dungeon d) {
		super(value,d);	
		construcHelpOrc(value);
	}
	public Orc(int value) {
		
		super(value);
		construcHelpOrc(value);

	}

	public Orc(int value, boolean special_attack) {

		super(value);
		this.specialAttacking = special_attack;
		construcHelpOrc(value);

	}
	
	protected void construcHelpOrc(int value) {
		this.strength = new Attribute(Attribute.STRENGTH,7);
		this.dexterity = new Attribute(Attribute.DEXTERITY,8);
		String[] lvl_names =
		{
				JDEnv.getString("orc1"),
				JDEnv.getString("orc2"),
				JDEnv.getString("orc3"),
				JDEnv.getString("orc4"),
				JDEnv.getString("orc5"),
				JDEnv.getString("orc6") };
	Mclass = getLvlName(value, lvl_names);
	this.lvl_names = lvl_names;

	name = (Texts.getName("orc"));
	tumbleValue = 2;
	this.antiTumbleValue = 30;
	
	if(level >= 2) {
		spellbook.addSpell(new StealOrc(1));
	}
	
	
	
	}
	
	protected int getSCATTER() {
		return this.SCATTER;
	}


	public int getCHANCE_TO_HIT() {
		return CHANCE_TO_HIT;
	}
	
	protected int getHEALTH_DAMAGE_BALANCE() {
		return this.HEALTH_DAMAGE_BALANCE;
	}


	protected boolean makeSpecialAttack(Figure op) {
		//Fighter op = getTarget();
		if(op instanceof Hero) {
			Inventory sachen = ((Hero)op).getInventory();
			LinkedList heroItems = sachen.getUnusedItems();
			Collections.sort(heroItems,new ItemValueComparator());
			LinkedList stolen = new LinkedList();
			op.tellPercept(new TextPercept(getName()+" klaut Dir: "));
			while(Item.calcValueSum(stolen) < 30) {
				if(heroItems.size() == 0) {
					break;
				}
				Item toGive = ((Item)heroItems.removeFirst());
				stolen.add(toGive);
				op.tellPercept(new TextPercept(toGive.toString()));
				sachen.giveAwayItem(toGive,this);
				
			}
			//getGame().newStatement(" ...und verschwindet durch die Tür. ",2,1);
			op.tellPercept(new TextPercept(getName()+" beklaut Dich und verschwindet durch die Tür.. "));
			
		}
		//attack(op);
		int dir = this.getFleeDir();
		flee(dir);
		this.specialAttackCounter = 50;
		//attack(op);
		//System.out.println("Orc making SpecialAttack returning true");
		return true;

	}
	
	public boolean isAbleToTakeItemInFight() {
		return true;
}
	
	
	public int hunting() {
		return Monster.ORC_HUNTING;
	}
	public double getAntiFleeFactor() {
		return 0.7;
	}
}
