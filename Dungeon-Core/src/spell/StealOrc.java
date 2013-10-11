package spell;

import java.util.Collections;
import java.util.List;

import dungeon.Position;

import item.Item;
import item.ItemValueComparator;
import item.equipment.Armor;
import item.equipment.Helmet;
import fight.Slap;
import figure.Figure;
import figure.hero.Hero;
import figure.hero.Inventory;
import figure.percept.TextPercept;
import game.JDEnv;

public class StealOrc extends Spell implements TargetSpell{

	public static int[][] values = { { 1, 1, 10, 25, 1 }, { 15, 13, 12, 25, 2 } };

	public static final String suffix = "steal_orc";

	public StealOrc(int level) {
		super(level, values[level - 1]);
		this.stepsNec = 2;
		isPossibleNormal = false;
		isPossibleInFight = true;
	}
	
	private boolean isPossibleNormal;
	private boolean isPossibleInFight;

	public StealOrc(int level, int diffMin, int diff, int cost, int strength,
			int learnCost) {
		super(level, diffMin, diff, cost, strength, learnCost);
		this.stepsNec = 2;
		isPossibleNormal = false;
		isPossibleInFight = true;

	}
	
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}
	
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	
	public boolean distanceOkay(Figure mage, Object target) {
		return Spell.distanceMax(mage,target,2);
	}

	public int getType() {
		return Spell.SPELL_STEALORC;
	}

	public String getText() {
		return JDEnv.getString("spell_" + suffix + "_text");
	}

	public boolean isApplicable(Figure mage, Object target) {
		if (target instanceof Figure) {
			return true;
		}
		return false;
	}
	
	private Item selectItem(List l) {
		if(l.size() == 0) {
			return null;
		}
		Collections.sort(l, new ItemValueComparator());
		if(Math.random() < 0.6 || l.size() == 1) {
			return (Item)l.get(0);
		}else {
			if(Math.random() < 0.6 || l.size() == 2) {
				return (Item)l.get(1);
			}else {
				return (Item)l.get(2);
			}
		}
	}

	public void sorcer(Figure mage, Object target) {
		if (target instanceof Figure) {
			List l = ((Figure) target).getAllItems();
			
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
				mage.takeItem(it,mage.getRoom());
				mage.getRoom().distributePercept(new TextPercept("Geklaut: "+it.toString()));
			}

		}

	}

	public String getName() {

		return JDEnv.getString("spell_" + suffix + "_name");
	}

}
