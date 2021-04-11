package de.jdungeon.item.equipment.weapon;

import de.jdungeon.game.JDEnv;

import java.util.*;
public class Sword extends Weapon {

	private static int BearModifierS = 0;
	private static int OgreModifierS = 0;
	private static int SkeletonModifierS = -20;
	private static int WolfModifierS = 0;
	private static int OrcModifierS = 30;
	private static int GhulModifierS = 0;
	
	 

	public Sword(int worth, boolean magic) {
		super(worth, magic, getHitpoints(worth));
		tumbleBasicValue = 15;
		this.worth = worth;
		averageDamage = worth / (4 + (int) (Math.random() * 3));
		scatter = 2 + (int) (Math.random() * (averageDamage / 3));
		//Min_Damage = average - scatter;
		//Max_Damage = average + scatter;
		chanceToHit = (int) (((float) worth / averageDamage) * 6);
		setModifiers();
		name = JDEnv.getResourceBundle().getString("sword");
	}

	public Sword(
		int Min_Damage,
		int Max_Damage,
		int Chance_to_hit,
		boolean magic,
		int hitpoints) {
		super(
			((Min_Damage + Max_Damage) / 2) * Chance_to_hit,
			magic,
			hitpoints);
			tumbleBasicValue = 15;
		this.averageDamage = (Max_Damage + Min_Damage) / 2;
		this.scatter = (Max_Damage - Min_Damage) / 2;
		//this.Min_Damage = Min_Damage;
		//this.Max_Damage = Max_Damage;
		this.chanceToHit = Chance_to_hit;
		name = JDEnv.getResourceBundle().getString("sword");
	}

	public Sword(int worth, LinkedList list) {
		super(worth, true, getHitpoints(worth));
		this.worth = worth;
		tumbleBasicValue = 15;
		averageDamage = worth / (4 + (int) (Math.random() * 3));
		scatter = 2 + (int) (Math.random() * (averageDamage / 3));
		//Min_Damage = average - scatter;
		//Max_Damage = average + scatter;
		chanceToHit = (int) (((float) worth / averageDamage) * 6);
		setModifiers();
		name = JDEnv.getResourceBundle().getString("sword");
		modifications = list;

	}

	private static int getHitpoints(int value) {
		return value / 2;
	}

	protected void setModifiers() {
		bearModifier = BearModifierS;
		ogreModifier = OgreModifierS;
		skeletonModifier = SkeletonModifierS;
		wolfModifier = WolfModifierS;
		orcModifier = OrcModifierS;
		ghulModifier = GhulModifierS;
		
		RANGE_NEAR = 100;
		RANGE_FAR = 50;
		RANGE_MID = 120;

	}

}
