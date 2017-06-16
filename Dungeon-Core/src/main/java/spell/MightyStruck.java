package spell;

import item.equipment.Armor;
import item.equipment.Helmet;
import fight.Slap;
import figure.Figure;
import figure.FigureInfo;
import figure.hero.Hero;
import game.InfoEntity;
import game.JDEnv;

public class MightyStruck extends AbstractTargetSpell implements TargetSpell{

	public static int[][] values = { { 1, 1, 10, 25, 1 }, { 15, 13, 12, 25, 2 } };
	
	public static final String suffix = "mighty_struck";
	
	private final boolean isPossibleNormal;
	private final boolean isPossibleInFight;
	
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
	
	@Override
	public boolean distanceOkay(Figure mage, Object target) {
		return AbstractSpell.distanceMax(mage,target,2);
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
	public int getType() {
		return AbstractSpell.SPELL_MIGHTYSTRUCK;
	}

	@Override
	public String getText() {
		return JDEnv.getString("spell_"+suffix+"_text");
	}

	@Override
	public boolean isApplicable(Figure mage, Object target) {
		if(target instanceof Figure) {
			return true;
		}
		return false;
	}

	@Override
	public Class<? extends InfoEntity> getTargetClass() {
		return FigureInfo.class;
	}

	@Override
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

@Override
public String getName() {
		
		return JDEnv.getString("spell_"+suffix+"_name");
	}

}

