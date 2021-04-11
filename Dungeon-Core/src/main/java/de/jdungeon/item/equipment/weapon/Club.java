package de.jdungeon.item.equipment.weapon;

import de.jdungeon.game.JDEnv;

import java.util.*;

public class Club extends Weapon {

	private static int BearModifierS = -100;
	private static int OgreModifierS = -30;
	private static int SkeletonModifierS = 150;
	private static int WolfModifierS = -10;
	private static int OrcModifierS = -10;
	private static int GhulModifierS = -30;
	
	
	

    public Club (int worth, boolean magic) {
    	super(worth, magic, getHitpoints(worth));
		tumbleBasicValue = 50;
	averageDamage = worth /(4 +(int)(Math.random() * 5));
	scatter = 1 + (int)(Math.random() * (averageDamage /4));
	//Min_Damage = average - scatter;
	//Max_Damage = average + scatter;
	chanceToHit = (int)(((float)worth / averageDamage) * 6);
	setModifiers();
	name = JDEnv.getResourceBundle().getString("club");


    }

    public Club (int Min_Damage, int Max_Damage, int Chance_to_hit, boolean magic, int hitpoints) {
    	super(((Min_Damage + Max_Damage) / 2) * Chance_to_hit, magic, hitpoints);
		tumbleBasicValue = 50;
    	this.averageDamage = (Max_Damage + Min_Damage) /2;
	this.scatter = (Max_Damage - Min_Damage) / 2;
	//this.Min_Damage = Min_Damage;
	//this.Max_Damage = Max_Damage;
	this.chanceToHit = Chance_to_hit;
	name = JDEnv.getResourceBundle().getString("club");
	
	setModifiers();
    }
    
    public Club(int value, LinkedList mods) {
		
		super(value, true, getHitpoints(value));
		averageDamage = worth /(4 +(int)(Math.random() * 5));
		scatter = 1 + (int)(Math.random() * (averageDamage /4));
		//Min_Damage = average - scatter;
		//Max_Damage = average + scatter;
		chanceToHit = (int)(((float)worth / averageDamage) * 6);
		setModifiers();
		name = JDEnv.getResourceBundle().getString("club");
		
		modifications = mods;
	}

	private  static int getHitpoints(int value) {
		return value;
	}
    
    protected void setModifiers() {
    	bearModifier = BearModifierS;
		ogreModifier = OgreModifierS;
		skeletonModifier = SkeletonModifierS;
		wolfModifier = WolfModifierS;
		orcModifier = OrcModifierS;
		ghulModifier = GhulModifierS;
		
		RANGE_NEAR = 80;
		RANGE_FAR = 60;
		RANGE_MID = 150;

	}

}
