package figure.monster;



import spell.Poison;
import dungeon.Dungeon;
import fight.Poisoning;
import figure.Figure;
import figure.attribute.Attribute;
import figure.percept.SpecialAttackPercept;
import game.JDEnv;
import gui.Texts;

public class Ghul extends UndeadMonster {
	public static final int CHANCE_TO_HIT = 15;
	private static final int HEALTH_DAMAGE_BALANCE = 16;
	protected static final int SCATTER = 4;
	

	
	protected void construcHelpGhul(int value) {
		tumbleValue = 5;
		this.antiTumbleValue = 70;
		this.strength = new Attribute(Attribute.Type.Strength,12);
		this.dexterity = new Attribute(Attribute.Type.Dexterity,6);
		
		if(level >= 2) {
			this.spellbook.addSpell(new Poison(1));
		}
		
		this.lvl_names = lvl_names;
		name = (Texts.getName("ghul"));
	}

	public Ghul(int value) {
		super(value);
		construcHelpGhul(value);

	}
	
	@Override
	public int getCHANCE_TO_HIT() {
		return CHANCE_TO_HIT;
	}
	
	
	@Override
	protected int getSCATTER() {
		return this.SCATTER;
	}
	
	public static String GUI_STATEMENT = " umh�llt Dich mit einer vergifteten Verwesungswolke!";
	
	@Override
	protected boolean makeSpecialAttack(Figure op) {
			Poisoning p = new Poisoning(this,4,8);
			
			getRoom().distributePercept(new SpecialAttackPercept(op,this, -1));
			op.poison(p);
			this.specialAttackCounter = 50;
			return false;
	}

	protected int getHEALTH_DAMAGE_BALANCE() {
		return this.HEALTH_DAMAGE_BALANCE;
	}


	@Override
	public int hunting() {
		return Monster.GHUL_HUNTING;	
	}
	
	@Override
	public double getAntiFleeFactor() {
			return 0.2;
		}

}
