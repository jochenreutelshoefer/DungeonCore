package spell;

import dungeon.Position;
import item.equipment.Armor;
import item.equipment.Helmet;
import fight.Slap;
import figure.Figure;
import figure.hero.Hero;
import game.JDEnv;

public class MightyStruck extends Spell implements TargetSpell{

	public static int[][] values = { { 1, 1, 10, 25, 1 }, { 15, 13, 12, 25, 2 } };
	
	public static final String suffix = "mighty_struck";
	
	private boolean isPossibleNormal;
	private boolean isPossibleInFight;
	
	public MightyStruck(int level) {
		super(level, values[level-1]);
		this.stepsNec = 2;
		isPossibleNormal = false;
		isPossibleInFight = true;
	}
	
	public MightyStruck(int level, int diffMin, int diff, int cost, int strength, int learnCost) {
		super(level, diffMin, diff, cost, strength,learnCost);
		this.stepsNec = 2;
		isPossibleNormal = false;
		isPossibleInFight = true;

	}
	
	public boolean distanceOkay(Figure mage, Object target) {
		return Spell.distanceMax(mage,target,2);
	}
	
	public boolean isPossibleFight() {
		return this.isPossibleInFight;
	}
	
	public boolean isPossibleNormal() {
		return this.isPossibleNormal;
	}
	
	public int getType() {
		return Spell.SPELL_MIGHTYSTRUCK;
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
			((Figure)target).getSlap(new Slap(mage,this.getStrength(),100,150));
			if(target instanceof Hero) {
				if(Math.random() > 0.5) {
					Armor a = ((Hero)target).getInventory().getArmor1();
					if(a != null) {
 					a.takeRelDamage(0.3);
					}
				}else {
					Helmet helmet = ((Hero)target).getInventory().getHelmet1();
					if(helmet != null) {
						((Hero)target).removeItem(helmet);
						((Hero)target).getRoom().addItem(helmet);
						helmet.takeRelDamage(0.2);
					}
				}
			}
		}

	}

public String getName() {
		
		return JDEnv.getString("spell_"+suffix+"_name");
	}

}

