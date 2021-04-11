package de.jdungeon.skill;

import java.io.Serializable;

import de.jdungeon.dungeon.Room;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.result.ActionResult;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 10.04.20.
 */
public abstract class Skill<ACTION extends SkillAction> implements Serializable {

	private int dustCosts = 0;

	public Skill() {
	}

	public Skill(int dustCosts) {
		this.dustCosts = dustCosts;
	}

	public ActionResult execute(ACTION action, boolean doIt, int round) {
		Figure actor = action.getActor();
		if(actor == null) return ActionResult.OTHER; // hero death problem
		Room room = actor.getRoom();
		if(room == null) return ActionResult.OTHER; // exit dungeon problem
		boolean fightRunning = room.fightRunning();
		if(!isPossibleFight() && Boolean.TRUE.equals(fightRunning)) {
			return ActionResult.MODE;
		}
		if(!isPossibleNonFight() && Boolean.FALSE.equals(fightRunning)) {
			return ActionResult.MODE;
		}
		if(! actor.canPayDust(dustCosts)) {
			return ActionResult.DUST;
		}
		if(! actor.canPayActionPoints(1)) {
			return ActionResult.NOAP;
		}
		if(!checkPositionOk(action)) {
			return ActionResult.POSITION;
		}
		if(!checkDistanceOk(action)) {
			return ActionResult.DISTANCE;
		}

		// do action
		ActionResult actionResult = doExecute(action, doIt, round);


		// if done, pay dust costs
		if(doIt && actionResult.getSituation() == ActionResult.Situation.done) {
			actor.payDust(dustCosts);
			actor.payActionPoint(action, round);
		}
		return actionResult;
	}

	protected abstract boolean checkPositionOk(ACTION action);

	protected abstract boolean checkDistanceOk(ACTION action);

	protected abstract boolean isPossibleFight();

	protected abstract boolean isPossibleNonFight();

	public abstract ActionResult doExecute(ACTION action, boolean doIt, int round);

	public abstract <BUILDER extends ActionBuilder<?, ACTION>> BUILDER newActionFor(FigureInfo actor);

	public int getDustCosts() {
		return dustCosts;
	}
}
