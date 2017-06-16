package spell;

import figure.Figure;
import figure.FigureInfo;
import figure.hero.Hero;
import game.InfoEntity;
import game.JDEnv;
import item.Item;
import item.ItemValueComparator;
import item.equipment.weapon.Weapon;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Rust extends AbstractTargetSpell {

public static int[][] values = { { 1, 1, 8, 8, 1 }, { 15, 13, 12, 25, 2 } };
	
	public static final String suffix = "rust";
	
	private final boolean isPossibleNormal;
	private final boolean isPossibleInFight;
	
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
	
	@Override
	public int getType() {
		return AbstractSpell.SPELL_RUST;
	}
	
	@Override
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	
	@Override
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}

	@Override
	public String getText() {
		return JDEnv.getString("spell_"+suffix+"_text");
	}

	@Override
	public boolean isApplicable(Figure mage, Object target) {
		if(target instanceof Hero) {
			return true;
		}
		return false;
	}

	@Override
	public void sorcer(Figure mage, Object op) {
		if(op instanceof Hero) {
			List<Item> l = new LinkedList<Item>();
			if (op instanceof Hero) {
				l = ((Hero) op).getInventory().getWeaponList();
			}
			Collections.sort(l, new ItemValueComparator());
			if (l.size() > 0) {
				Weapon weap = ((Weapon) l.get(0));
				weap.takeRelDamage(0.25);
			}
		}

	}

	@Override
	public Class<? extends InfoEntity> getTargetClass() {
		return FigureInfo.class;
	}

@Override
public String getName() {
		
		return JDEnv.getString("spell_"+suffix+"_name");
	}
}
