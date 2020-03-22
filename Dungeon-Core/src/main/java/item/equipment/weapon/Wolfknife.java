package item.equipment.weapon;


import game.JDEnv;

import java.util.*;
public class Wolfknife extends Weapon {
	
	
	
	private static int Bear_ModifierS = -20;
	private static int Ogre_ModifierS = -20;
	private static int Skeleton_ModifierS = -100;
	private static int Wolf_ModifierS = 100;
	private static int Orc_ModifierS = -20;
	private static int Ghul_ModifierS = -20;

	
    

    public Wolfknife (int worth, boolean magic) {
	
	super(worth, magic, getHitpoints(worth));
	tumbleBasicValue = 2;
	averageDamage = worth /(4 +(int)(Math.random() * 5));
	scatter = 2 + (int)(Math.random() * (averageDamage /4));
	//Min_Damage = Average_Damage - scatter;
	//Max_Damage = Average_Damage + scatter;
	chanceToHit = (int)(((float)worth / averageDamage) * 6);
	setModifiers();
	name = JDEnv.getResourceBundle().getString("wolfknife");

    }
    
    @Override
	public String getName() {
    	return name;
    }

    public Wolfknife (int Min_Damage, int Max_Damage, int Chance_to_hit, boolean magic, int hitpoints) {
    	super(((Min_Damage + Max_Damage) / 2) * Chance_to_hit, magic, hitpoints);
		tumbleBasicValue = 2;
    	this.averageDamage = (Max_Damage + Min_Damage) /2;
	this.scatter = (Max_Damage - Min_Damage) / 2;
	//this.Min_Damage = Min_Damage;
	//this.Max_Damage = Max_Damage;
	this.chanceToHit = Chance_to_hit;
	name = JDEnv.getResourceBundle().getString("wolfknife");

	setModifiers();
    }

	public Wolfknife(int value, LinkedList list) {
    	super(value, true, getHitpoints(value));
    	averageDamage = worth /(4 +(int)(Math.random() * 5));
		scatter = 2 + (int)(Math.random() * (averageDamage /4));
		//Min_Damage = Average_Damage - scatter;
		//Max_Damage = Average_Damage + scatter;
		chanceToHit = (int)(((float)worth / averageDamage) * 6);
		setModifiers();
		name = JDEnv.getResourceBundle().getString("wolfknife");
		
		modifications = list;
	
    	
    }
    
    
    private  static int getHitpoints(int value) {
		return value/3;
	}
	protected void setModifiers() {
    	bearModifier = Bear_ModifierS;
		ogreModifier = Ogre_ModifierS;
		skeletonModifier = Skeleton_ModifierS;
		wolfModifier = Wolf_ModifierS;
		orcModifier = Orc_ModifierS;
		ghulModifier = Ghul_ModifierS;
		
		RANGE_NEAR = 200;
		RANGE_FAR = 10;
		RANGE_MID = 70;

	}
	

	}
