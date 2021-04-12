package de.jdungeon.gui.activity;

import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.action.result.ActionResult;

import de.jdungeon.app.gui.activity.SkillActivityProvider;
import de.jdungeon.world.PlayerController;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.02.20.
 */
public class DirectionScoutActivity extends ScoutActivity {

	private final RouteInstruction.Direction direction;

	public DirectionScoutActivity(PlayerController playerController, RouteInstruction.Direction direction) {
		super(playerController);
		this.direction = direction;
	}

	@Override
	public Object getObject() {
		return SkillActivityProvider.SCOUT;
	}

	@Override
	public ActivityPlan createExecutionPlan(boolean doIt, Object target) {
		return super.createExecutionPlan(doIt, direction);
	}

	@Override
	public ActionResult possible(Object target) {
		return super.possible(direction);
	}

}
