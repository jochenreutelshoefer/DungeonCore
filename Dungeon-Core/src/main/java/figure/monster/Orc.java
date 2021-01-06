package figure.monster;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import figure.Figure;
import figure.FigurePresentation;
import figure.attribute.Attribute;
import figure.hero.Hero;
import figure.hero.Inventory;
import figure.percept.TextPercept;
import game.JDEnv;
import gui.Texts;
import item.Item;
import item.ItemValueComparator;
import spell.StealOrc;

public class Orc extends CreatureMonster {

	private static final int CHANCE_TO_HIT = 25;
	private static final int HEALTH_DAMAGE_BALANCE = 12;
	private static final int SCATTER = 2;

	public Orc(int value) {

		super(value);
		construcHelpOrc();
	}

	public Orc(int value, boolean special_attack) {

		super(value);
		construcHelpOrc();
	}

	protected void construcHelpOrc() {
		this.strength = new Attribute(Attribute.Type.Strength, 7);
		this.dexterity = new Attribute(Attribute.Type.Dexterity, 8);
		this.lvl_names = new String[] {
				JDEnv.getString("orc1"),
				JDEnv.getString("orc2"),
				JDEnv.getString("orc3"),
				JDEnv.getString("orc4"),
				JDEnv.getString("orc5"),
				JDEnv.getString("orc6") };

		name = (Texts.getName("orc"));

		if (level >= 2) {
			spellbook.addSpell(new StealOrc(1));
		}
	}

	@Override
	public FigurePresentation getFigurePresentation() {
		return FigurePresentation.Orc;
	}

	@Override
	protected int getDamageVariance() {
		return Orc.SCATTER;
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
	public int hunting() {
		return Monster.ORC_HUNTING;
	}

	@Override
	public double getAntiFleeFactor() {
		return 0.7;
	}
}
