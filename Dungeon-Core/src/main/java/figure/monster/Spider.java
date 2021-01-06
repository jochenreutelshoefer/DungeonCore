package figure.monster;

import figure.Figure;
import figure.FigurePresentation;
import figure.attribute.Attribute;
import figure.hero.Hero;
import figure.hero.Inventory;
import figure.percept.SpecialAttackPercept;
import gui.Texts;
import item.equipment.Armor;
import item.equipment.Helmet;
import spell.Cobweb;

public class Spider extends NatureMonster {

	private static final int CHANCE_TO_HIT = 25;
	private static final int HEALTH_DAMAGE_BALANCE = 10;
	private static final int SPIDER_DAMAGE_VARIANCE = 3;

	public Spider(int value) {
		super(value);
		construcHelpBear();
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
		return SPIDER_DAMAGE_VARIANCE;
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
