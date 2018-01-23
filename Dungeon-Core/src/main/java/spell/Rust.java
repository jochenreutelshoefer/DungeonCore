package spell;

import dungeon.RoomEntity;
import figure.Figure;
import figure.FigureInfo;
import figure.hero.Hero;
import game.JDEnv;
import game.RoomInfoEntity;
import item.Item;
import item.ItemValueComparator;
import item.equipment.weapon.Weapon;

import java.util.Collections;
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
	public boolean distanceOkay(Figure mage, RoomEntity target) {
		return true;
	}

	@Override
	public boolean isApplicable(Figure mage, RoomEntity target) {
		if(target instanceof Hero) {
			return true;
		}
		return false;
	}

	@Override
	public void sorcer(Figure mage, RoomEntity op) {
		if(op instanceof Hero) {
			List<Item> l;
			l = ((Hero) op).getInventory().getWeaponList();
			Collections.sort(l, new ItemValueComparator());
			if (!l.isEmpty()) {
				Weapon weap = ((Weapon) l.get(0));
				weap.takeRelDamage(0.25);
			}
		}

	}

	@Override
	public Class<? extends RoomInfoEntity> getTargetClass() {
		return FigureInfo.class;
	}

@Override
public String getName() {
		
		return JDEnv.getString("spell_"+suffix+"_name");
	}
}
