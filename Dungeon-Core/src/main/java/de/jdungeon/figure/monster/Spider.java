package de.jdungeon.figure.monster;

import de.jdungeon.ai.AI;
import de.jdungeon.figure.APAgility;
import de.jdungeon.figure.FigurePresentation;
import de.jdungeon.figure.attribute.Attribute;
import de.jdungeon.gui.Texts;
import de.jdungeon.spell.Cobweb;

public class Spider extends NatureMonster {

	public static final int CHANCE_TO_HIT = 50;
	protected static final int HEALTH_DAMAGE_BALANCE = 8;
	protected static final int DAMAGE_VARIANCE = 2;

	public Spider(int value) {
		super(value);
		construcHelpBear();
	}

	public Spider(int value, AI ai, String name) {
		super(value, ai);
		this.strength = new Attribute(Attribute.Type.Strength, 5);
		this.dexterity = new Attribute(Attribute.Type.Dexterity, 11);
		this.name = name;
	}

	protected APAgility createAgility() {
		return new APAgility(12, 1.8);
	}

	@Override
	public FigurePresentation getFigurePresentation() {
		return FigurePresentation.Spider;
	}

	protected void construcHelpBear() {
		this.strength = new Attribute(Attribute.Type.Strength, 12);
		this.dexterity = new Attribute(Attribute.Type.Dexterity, 9);

		if (level >= 2) {
			this.spellbook.addSpell(new Cobweb(1));
		}

		name = Texts.getName("Spinne");
	}

	@Override
	protected int getDamageVariance() {
		return DAMAGE_VARIANCE;
	}

	@Override
	protected int getHealthDamageBalance() {
		return HEALTH_DAMAGE_BALANCE;
	}

	@Override
	public int getChangeToHit() {
		return CHANCE_TO_HIT;
	}

	@Override
	public double getAntiFleeFactor() {
		return 0.4;
	}

	@Override
	public int hunting() {
		return Monster.BEAR_HUNTING;
	}
}
