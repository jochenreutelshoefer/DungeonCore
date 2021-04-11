package de.jdungeon.figure.monster;

import de.jdungeon.figure.APAgility;
import de.jdungeon.figure.FigurePresentation;
import de.jdungeon.figure.attribute.Attribute;
import de.jdungeon.game.JDEnv;
import de.jdungeon.gui.Texts;
import de.jdungeon.spell.MightyStruck;

public class Ogre extends CreatureMonster {

	private static final int CHANCE_TO_HIT = 12;
	private static final int HEALTH_DAMAGE_BALANCE = 20;
	private static final int OGRE_DAMAGE_VARIANCE = 5;

	public Ogre(int value) {

		super(value);
		construcHelpOgre();
	}

	protected void construcHelpOgre() {
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
