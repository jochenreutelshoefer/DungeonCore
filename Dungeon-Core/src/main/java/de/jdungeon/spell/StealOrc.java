package de.jdungeon.spell;

import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.dungeon.RoomInfoEntity;
import de.jdungeon.item.Item;
import de.jdungeon.item.ItemValueComparator;

import java.util.Collections;
import java.util.List;

import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.figure.hero.Inventory;
import de.jdungeon.figure.percept.TextPercept;
import de.jdungeon.game.JDEnv;

public class StealOrc extends AbstractTargetSpell implements TargetSpell{

	public static int[][] values = { { 1, 1, 10, 25, 1 }, { 15, 13, 12, 25, 2 } };

	public static final String suffix = "steal_orc";

	public StealOrc(int level) {
		super(level, values[level - 1]);
		this.stepsNec = 2;
		isPossibleNormal = false;
		isPossibleInFight = true;
	}
	
	private final boolean isPossibleNormal;
	private final boolean isPossibleInFight;

	public StealOrc(int level, int diffMin, int diff, int cost, int strength,
			int learnCost) {
		super(level, diffMin, diff, cost, strength, learnCost);
		this.stepsNec = 2;
		isPossibleNormal = false;
		isPossibleInFight = true;

	}
	
	@Override
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}
	
	@Override
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	
	@Override
	public boolean distanceOkay(Figure mage, RoomEntity target) {
		return AbstractSpell.distanceMax(mage,target,2);
	}

	@Override
	public int getType() {
		return AbstractSpell.SPELL_STEALORC;
	}

	@Override
	public String getText() {
		return JDEnv.getString("spell_" + suffix + "_text");
	}

	@Override
	public boolean isApplicable(Figure mage, RoomEntity target) {
		if (target instanceof Figure) {
			return true;
		}
		return false;
	}
	
	private Item selectItem(List<Item> l) {
		if(l.isEmpty()) {
			return null;
		}
		Collections.sort(l, new ItemValueComparator());
		if(Math.random() < 0.6 || l.size() == 1) {
			return l.get(0);
		}else {
			if(Math.random() < 0.6 || l.size() == 2) {
				return l.get(1);
			}else {
				return l.get(2);
			}
		}
	}

	@Override
	public void sorcer(Figure mage, RoomEntity target, int round) {
		if (target instanceof Figure) {
			List<Item> l = ((Figure) target).getAllItems();
			
			Item it = null;
			if (target instanceof Hero) {
				Inventory inv = ((Hero)target).getInventory();
				it = selectItem(l);
				int cnt = 0;
				while(it == inv.getArmor1() || it == inv.getHelmet1() || it == inv.getShield1() || it == inv.getWeapon1()) {
					cnt++;
					if(cnt > 50) {
						it = null;
						break;
					}
					it = selectItem(l);
					
				}
				
			} else {
				it = selectItem(l);
			}
			
			if(it != null) {
				((Figure)target).removeItem(it);
				mage.takeItem(it);
				mage.getRoom().distributePercept(new TextPercept("Geklaut: "+it.toString(), round));
			}

		}

	}

	@Override
	public Class<? extends RoomInfoEntity> getTargetClass() {
		return FigureInfo.class;
	}

	@Override
	public String getName() {

		return JDEnv.getString("spell_" + suffix + "_name");
	}

}
