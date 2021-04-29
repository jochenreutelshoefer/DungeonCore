package de.jdungeon.figure;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

import de.jdungeon.figure.action.Action;
import de.jdungeon.figure.action.MoveAction;
import de.jdungeon.figure.action.StepAction;
import de.jdungeon.figure.attribute.Attribute;
import de.jdungeon.skill.attack.AttackSkill;
import de.jdungeon.skill.FleeSkill;
import de.jdungeon.skill.Skill;
import de.jdungeon.skill.SkillAction;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 25.03.20.
 */
public class APAgility implements Serializable {

	private final Attribute oxygen;
	private final APCounter apCounter;
	private final double recoveryRate;

	public APAgility() {
		this(12, 1);
	}

	public APAgility(int max, double recoveryRate) {
		oxygen = new Attribute(Attribute.Type.Oxygen, max);
		apCounter = new APCounter();
		this.recoveryRate = recoveryRate;
	}

	public void turn(int round) {
		// figure gets an AP only if there is at least a little amount of oxygen
		if(oxygen.getValue() >= 2) {
			apCounter.setCurrentAP(1, round);
		}

		// take a breath instead
		oxygen.addToMax(recoveryRate);

	}

	public Attribute getOxygen() {
		return oxygen;
	}

	private static final Collection<Class<? extends Action>> OXYGEN_ACTIONS = new HashSet<>();
	private static final Collection<Class<? extends Skill>> OXYGEN_SKILLS = new HashSet<>();

	static {
		// TODO: after completion of refactoring towards Skills, refactor this set to work on Skills, not on actions any more
		OXYGEN_ACTIONS.add(MoveAction.class);
		OXYGEN_ACTIONS.add(StepAction.class);
		OXYGEN_ACTIONS.add(AttackSkill.AttackSkillAction.class);
		OXYGEN_SKILLS.add(FleeSkill.class);
	}

	public void payActionPoint(Action action, int round) {
		apCounter.payActionpoint(round);
		if(action != null && (OXYGEN_ACTIONS.contains(action.getClass()) // todo: refactor : unify
				|| (action instanceof SkillAction && (OXYGEN_SKILLS.contains(((SkillAction)action).getSkill().getClass()))))) {
			oxygen.modValue(-2);
		}
	}

	public int getCurrentAP() {
		return this.apCounter.getCurrentAP();
	}

	public boolean canPayActionpoints(int k) {
		return this.apCounter.canPayActionpoints(k);
	}

	public void reset() {
		oxygen.setValue(oxygen.getBasic());
		this.apCounter.reset();
	}
}
