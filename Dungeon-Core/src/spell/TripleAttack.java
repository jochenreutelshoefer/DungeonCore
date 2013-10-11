package spell;

import item.ItemValueComparator;
import item.equipment.weapon.Weapon;

import java.util.Collections;
import java.util.LinkedList;

import figure.Figure;
import figure.hero.Hero;
import game.JDEnv;

public class TripleAttack extends Spell {

public static int[][] values = { { 1, 1, 10, 8, 1 }, { 15, 13, 12, 25, 2 } };
	
	public static final String suffix = "triple_attack";
	
	private boolean isPossibleNormal;
	private boolean isPossibleInFight;
	
	public TripleAttack(int level) {
		super(level, values[level-1]);
		this.stepsNec = 2;
		isPossibleNormal = false;
		isPossibleInFight = true;
	}
	
	public TripleAttack(int level, int diffMin, int diff, int cost, int strength, int learnCost) {
		super(level, diffMin, diff, cost, strength,learnCost);
		this.stepsNec = 2;
		isPossibleNormal = false;
		isPossibleInFight = true;

	}
	
	public int getType() {
		return Spell.SPELL_TRIPLEATTACK;
	}
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	
	
	
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}

	public String getText() {
		return JDEnv.getString("spell_"+suffix+"_text");
	}

	public boolean isApplicable(Figure mage, Object target) {
		if(target instanceof Figure) {
			return true;
		}
		return false;
	}

	public void sorcer(Figure mage, Object target) {
		if(target instanceof Figure) {
			mage.attack((Figure)target);
			mage.attack((Figure)target);
			mage.attack((Figure)target);
		}

	}

public String getName() {
		
		return JDEnv.getString("spell_"+suffix+"_name");
	}

}
