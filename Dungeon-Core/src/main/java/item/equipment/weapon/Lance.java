package item.equipment.weapon;

import game.JDEnv;

import java.util.*;
public class Lance extends Weapon {

    private static int BearModifierS = 30;
	private static int OgreModifierS = 60;
	private static int SkeletonModifierS = -50;
	private static int WolfModifierS = -100;
	private static int OrcModifierS = 0;
	private static int GhulModifierS = 0;
	
	

    public Lance (int worth, boolean magic) {
    	super(worth, magic, getHitpoints(worth));
		tumbleBasicValue = 5;
	averageDamage = worth /(2 +(int)(Math.random() * 4));
	scatter = 1 + (int)(Math.random() * (averageDamage /4));
	//Min_Damage = average - scatter;
	//Max_Damage = average + scatter;
	chanceToHit = (int)(((float)worth / averageDamage) * 6);
	name = JDEnv.getResourceBundle().getString("lance");

		setModifiers();
    }

    public Lance (int Min_Damage, int Max_Damage, int Chance_to_hit, boolean magic, int hitpoints) {
    	super(((Min_Damage + Max_Damage) / 2) * Chance_to_hit, magic, hitpoints);
		tumbleBasicValue = 5;
    	this.averageDamage = (Max_Damage + Min_Damage) /2;
	this.scatter = (Max_Damage - Min_Damage) / 2;
		//this.Min_Damage = Min_Damage;
		//this.Max_Damage = Max_Damage;
		this.chanceToHit = Chance_to_hit;
		name = JDEnv.getResourceBundle().getString("lance");
	
		setModifiers();
    }
    
    public Lance(int value, LinkedList list) {
    	super(value, true, getHitpoints(value));
    	averageDamage = worth /(2 +(int)(Math.random() * 4));
		scatter = 1 + (int)(Math.random() * (averageDamage /4));
		//Min_Damage = average - scatter;
		//Max_Damage = average + scatter;
		chanceToHit = (int)(((float)worth / averageDamage) * 6);
	setModifiers();
	name = JDEnv.getResourceBundle().getString("lance");
		
		modifications = list;
	
    	
    }

	private  static int getHitpoints(int value) {
		return 1 * value / 3;
	}
	
	protected void setModifiers() {
    	bearModifier = BearModifierS;
		ogreModifier = OgreModifierS;
		skeletonModifier = SkeletonModifierS;
		wolfModifier = WolfModifierS;
		orcModifier = OrcModifierS;
		ghulModifier = GhulModifierS;

		RANGE_NEAR = 30;
		RANGE_FAR = 180;
		RANGE_MID = 120;
	}
    

}
