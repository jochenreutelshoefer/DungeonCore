package de.jdungeon.gui.activity;

import java.util.ArrayList;
import java.util.List;

import de.jdungeon.figure.action.Action;
import de.jdungeon.figure.action.SpellAction;
import de.jdungeon.skill.SkillAction;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 12.04.20.
 */
public class SimpleActivityPlan implements ActivityPlan {

	private final Activity activity;
	private final List<Action> actionSequence;

	public SimpleActivityPlan(Activity activity, List<Action> actionSequence) {
		this.activity = activity;
		this.actionSequence = actionSequence;
	}

	public SimpleActivityPlan(Activity activity, Action action) {
		this.activity = activity;
		this.actionSequence = new ArrayList<>();
		actionSequence.add(action);
	}

	@Override
	public Activity getActivity() {
		return activity;
	}

	@Override
	public Action getNextAction() {
		if(actionSequence.isEmpty()) return null;
		return actionSequence.remove(0);
	}

	@Override
	public boolean isCompleted() {
		return actionSequence.isEmpty();
	}

	@Override
	public int getLength() {
		return actionSequence.size();
	}

	@Override
	public int totalDustCosts() {
		// sum up all costs of all actions in the plan
		int costs = 0;
		for (Action action : actionSequence) {
			if(action instanceof SkillAction) {
				costs += ((SkillAction)action).getSkill().getDustCosts();
			}
			if(action instanceof SpellAction) {
				 costs += ((SpellAction) action).getSpell().getCost();
			}
		}
		return costs;
	}
}
