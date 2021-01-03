package figure.monster;

import figure.APAgility;
import figure.Figure;
import figure.FigurePresentation;
import figure.attribute.Attribute;
import game.JDEnv;
import gui.Texts;
import spell.MightyStruck;

public class Ogre extends CreatureMonster {

	private static final int CHANCE_TO_HIT = 12;
	private static final int HEALTH_DAMAGE_BALANCE = 20;
	private static final int OGRE_DAMAGE_VARIANCE = 5;

	public Ogre(int value) {

		super(value);
		construcHelpOgre();
	}

	protected void construcHelpOgre() {
		tumbleValue = 30;
		this.strength = new Attribute(Attribute.Type.Strength, 12);
		this.dexterity = new Attribute(Attribute.Type.Dexterity, 6);
		String[] lvl_names =
				{
						JDEnv.getString("ogre1"),
						JDEnv.getString("ogre2"),
						JDEnv.getString("ogre3"),
						JDEnv.getString("ogre4"),
						JDEnv.getString("ogre5"),
						JDEnv.getString("ogre6") };

		this.lvl_names = lvl_names;

		name = (Texts.getName("ogre"));

		if (level >= 2) {
			spellbook.addSpell(new MightyStruck(1));
		}
	}

	@Override
	public FigurePresentation getFigurePresentation() {
		return FigurePresentation.Troll;
	}

	@Override
	protected APAgility createAgility() {
		return new APAgility(8, 0.7);
	}

	@Override
	protected int getDamageVariance() {
		return OGRE_DAMAGE_VARIANCE;
	}

	@Override
	protected boolean makeSpecialAttack(Figure op) {
		//Fighter op = getTarget();

		// TODO: re-implement as a skill using oxygen reduction

		/*
		this.half_bonus = true;
		this.makesgoldenHit = true;
		System.out.println("Ogre.makeSpecialAttack!");
		op.decActionPoints(1, -1);
		if (op instanceof Hero) {
			this.getRoom().distributePercept(new SpecialAttackPercept(op,this, -1));
			Inventory sachen = ((Hero) op).getInventory();
			Helmet helm = sachen.getHelmet1();
			if (helm != null) {
				helm.takeRelDamage(0.3);
			}
		}
		attack(op, -1);
		

		this.specialAttackCounter = 50;
		return false;

		*/
		return false;
	}

	@Override
	public int getChangeToHit() {
		return CHANCE_TO_HIT;
	}

	@Override
	protected int getHealthDamageBalance() {
		return HEALTH_DAMAGE_BALANCE;
	}

	@Override
	public double getAntiFleeFactor() {
		return 0.3;
	}

	@Override
	public int hunting() {
		return Monster.OGRE_HUNTING;
	}
}
