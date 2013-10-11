package spell;

import item.ItemValueComparator;
import item.equipment.weapon.Weapon;

import java.util.Collections;
import java.util.LinkedList;

import fight.Poisoning;
import figure.Figure;
import figure.hero.Hero;
import game.JDEnv;

public class Rust extends Spell {

public static int[][] values = { { 1, 1, 8, 8, 1 }, { 15, 13, 12, 25, 2 } };
	
	public static final String suffix = "rust";
	
	private boolean isPossibleNormal;
	private boolean isPossibleInFight;
	
	public Rust(int level) {
		super(level, values[level-1]);
		this.stepsNec = 2;
		isPossibleNormal = false;
		isPossibleInFight = true;
	}
	
	public Rust(int level, int diffMin, int diff, int cost, int strength, int learnCost) {
		super(level, diffMin, diff, cost, strength,learnCost);
		this.stepsNec = 2;
		isPossibleNormal = false;
		isPossibleInFight = true;

	}
	
	public int getType() {
		return Spell.SPELL_RUST;
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
		if(target instanceof Hero) {
			return true;
		}
		return false;
	}

	public void sorcer(Figure mage, Object op) {
		if(op instanceof Hero) {
			LinkedList l = new LinkedList();
			if (op instanceof Hero) {
				l = ((Hero) op).getInventory().getWeaponList();
			}
			Collections.sort(l, new ItemValueComparator());
			if (l.size() > 0) {
				Weapon weap = ((Weapon) l.getFirst());
				weap.takeRelDamage(0.25);
			}
		}

	}

public String getName() {
		
		return JDEnv.getString("spell_"+suffix+"_name");
	}
}
