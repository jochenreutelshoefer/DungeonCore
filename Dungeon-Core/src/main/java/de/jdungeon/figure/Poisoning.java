package de.jdungeon.figure;

import java.io.Serializable;

import de.jdungeon.figure.Figure;
import de.jdungeon.skill.attack.Slap;
import de.jdungeon.skill.attack.SlapResult;

/**
 * Vergiftung, die die betroffene Figur eine Anzahl von Runden lang schwaecht.
 */
public class Poisoning implements Serializable {

	private int time = 0;

	private int strength = 0;

	//private boolean hero = false;

	private Figure actor;

	//private int fightRounds = 0;

	public Poisoning(Figure actor, int strength, int time) {
		this.time = time;
		this.strength = strength;
		this.actor = actor;

	}

	public int getTime() {
		return time;
	}

	public void sufferRound(Figure f, int round) {
		if (time > 0) {

			Slap s = new Slap(actor, Slap.POISON,strength, 150);
			

			SlapResult res = f.getSlap(s, round);
			actor.receiveSlapResult(res);
//			f.getRoom().distributePercept(
//					new TextPercept(JDEnv.getString("poison_damage")+": " + strength));
			time--;
		}
	}

	// public void sufferFight(Figure f) {
	// fightRounds++;
	// if(fightRounds == 3) {
	// fightRounds = 0;
	// time--;
	// }
	//		
	// Slap s = new Slap(actor,null, strength/3, Slap.POISON,0,0,-1);
	// f.tellPercept(new TextPercept("Giftschaden: "+ strength/3));
	// SlapResult res = f.getSlap(s);
	// actor.receiveSlapResult(res);
	// }

}
