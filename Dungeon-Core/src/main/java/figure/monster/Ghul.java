package figure.monster;

import fight.Poisoning;
import figure.Figure;
import figure.FigurePresentation;
import figure.attribute.Attribute;
import figure.percept.SpecialAttackPercept;
import gui.Texts;
import spell.Poison;

public class Ghul extends UndeadMonster {
	private static final int CHANCE_TO_HIT = 15;
	private static final int HEALTH_DAMAGE_BALANCE = 16;
	private static final int SCATTER = 4;

	protected void construcHelpGhul() {
		this.strength = new Attribute(Attribute.Type.Strength, 12);
		this.dexterity = new Attribute(Attribute.Type.Dexterity, 6);

		if (level >= 2) {
			this.spellbook.addSpell(new Poison(1));
		}
		name = (Texts.getName("ghul"));
	}

	public Ghul(int value) {
		super(value);
		construcHelpGhul();
	}

	@Override
	public FigurePresentation getFigurePresentation() {
		return FigurePresentation.Ghul;
	}

	@Override
	public int getChangeToHit() {
		return CHANCE_TO_HIT;
	}

	@Override
	protected int getDamageVariance() {
		return SCATTER;
	}

	@Override
	protected int getHealthDamageBalance() {
		return HEALTH_DAMAGE_BALANCE;
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
