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
		this.strength = new Attribute(Attribute.STRENGTH,12);
		this.dexterity = new Attribute(Attribute.DEXTERITY,6);
		
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
			//Fighter op = getTarget();
			Poisoning p = new Poisoning(this,4,8);
			
			getRoom().distributePercept(new SpecialAttackPercept(Monster.GHUL,op,this));
			op.poison(p);
			this.specialAttackCounter = 50;
			return false;
	}

	protected int getHEALTH_DAMAGE_BALANCE() {
		return this.HEALTH_DAMAGE_BALANCE;
	}

//	public Action turnElse(int c) {
//		recover();
//		if (c == 0) {
//			if(spitted) {
//						return null;
//					}
//		} else {
//			////System.out.println("Ghul geht vom Rudel weg");
//			boolean b = false;
//			while (!b) {
//				int a = (int) (Math.random() * 80) + 20;
//				if (a <= 20) {
//				} else if (a <= 40) {
//					return new ActionMove(RouteInstruction.SOUTH);
//					//goSouth(); //////System.out.println(name+" geht suedlich");
//				} else if (a <= 60) {
//					return new ActionMove(RouteInstruction.EAST);
//					//goEast();//////System.out.println(name+" get oestlich");
//				} else if (a <= 80) {
//					return new ActionMove(RouteInstruction.NORTH);
//					//goNorth();//////System.out.println(name+" get noerdlich");
//				} else {
//					return new ActionMove(RouteInstruction.WEST);
//					//goWest();//////System.out.println(name+" get westlich");
//				}
//			}
//		}
//		return null;
//	}
	
	
	@Override
	public int hunting() {
		return Monster.GHUL_HUNTING;	
	}
	
	@Override
	public double getAntiFleeFactor() {
			return 0.2;
		}

}
