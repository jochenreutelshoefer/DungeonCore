package figure.monster;

import ai.AI;
import figure.APAgility;
import figure.Figure;
import figure.FigurePresentation;
import figure.attribute.Attribute;
import game.JDEnv;
import gui.Texts;

public class Wolf extends NatureMonster {

	public static final int CHANCE_TO_HIT = 50;
	protected static final int HEALTH_DAMAGE_BALANCE = 8;
	protected static final int DAMAGE_VARIANCE = 2;

	/**
	 * @return Returns the cHANCE_TO_HIT.
	 */
	@Override
	public int getChangeToHit() {
		return CHANCE_TO_HIT;
	}

	public static Wolf buildCustomWolf(int chance_to_hit, int damage, int health, int scatter, double healthRec, String name) {
		int value = (int) (((double) chance_to_hit * damage * health) / 5.8);
		Wolf w = new Wolf(value);
		w.health = new Attribute(Attribute.Type.Health, health);
		w.minDamage = damage - scatter;
		w.maxDamage = damage + scatter;
		w.name = name;
		w.healthRecover = (int) healthRec;
		return w;
	}

	public Wolf(int value) {
		super(value);
		contrucHelpWolf();
	}

	@Override
	public FigurePresentation getFigurePresentation() {
		return FigurePresentation.WolfGrey;
	}

	public Wolf(int value, AI ai) {
		super(value, ai);
		contrucHelpWolf();
	}

	public Wolf(int value, AI ai, String name) {
		super(value, ai);
		contrucHelpWolf();
		this.name = name;
	}

	@Override
	protected APAgility createAgility() {
		return new APAgility(12, 1.8);
	}

	public Wolf(int value, boolean special_attack) {
		super(value);
		contrucHelpWolf();
	}

	protected void contrucHelpWolf() {
		this.strength = new Attribute(Attribute.Type.Strength, 5);
		this.dexterity = new Attribute(Attribute.Type.Dexterity, 11);
		this.lvl_names = new String[] { JDEnv.getString("wolf1"), JDEnv.getString("wolf2"), JDEnv.getString("wolf3"),
				JDEnv.getString("wolf4"), JDEnv.getString("wolf5"), JDEnv.getString("wolf6") };

		name = (Texts.getName("wolf"));
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
	public double getAntiFleeFactor() {
		return 1.0;
	}

	@Override
	public int hunting() {
		return Monster.WOLF_HUNTING;
	}
}