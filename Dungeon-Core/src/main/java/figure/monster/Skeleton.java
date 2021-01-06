package figure.monster;

import ai.AI;
import figure.FigurePresentation;
import figure.attribute.Attribute;
import game.JDEnv;
import gui.Texts;

public class Skeleton extends UndeadMonster {

	public static final int CHANCE_TO_HIT = 40;

	private static final int HEALTH_DAMAGE_BALANCE = 10;

	protected static final int VARIANCE = 3;

	public Skeleton(int value) {
		super(value);
		construcHelpSkeleton(value);
	}

	public Skeleton(int value, AI ai) {
		super(value, ai);
		construcHelpSkeleton(value);
	}

	public Skeleton(int value, boolean special_attack) {
		super(value);
		construcHelpSkeleton(value);
	}

	@Override
	public FigurePresentation getFigurePresentation() {
		return FigurePresentation.Skeleton;
	}

	protected void construcHelpSkeleton(int value) {
		String[] lvl_names = { JDEnv.getString("skel1"), JDEnv.getString("skel2"), JDEnv.getString("skel3"),
				JDEnv.getString("skel4"), JDEnv.getString("skel5"), JDEnv.getString("skel6") };
		this.lvl_names = lvl_names;
		this.strength = new Attribute(Attribute.Type.Strength, 7);
		this.dexterity = new Attribute(Attribute.Type.Dexterity, 9);
		name = (Texts.getName("skeleton"));
	}

	@Override
	protected int getDamageVariance() {
		return VARIANCE;
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
		return 0.9;
	}

	@Override
	public int hunting() {
		return Monster.SKELETON_HUNTING;
	}
}