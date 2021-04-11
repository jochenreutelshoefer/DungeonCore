package de.jdungeon.skill;

import de.jdungeon.dungeon.Position;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.figure.percept.FleePercept;
import de.jdungeon.figure.percept.Percept;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 10.04.20.
 */
public class FleeSkill extends SimpleSkill {

	@Override
	protected boolean isPossibleFight() {
		return true;
	}

	@Override
	protected boolean isPossibleNonFight() {
		return false;
	}

	@Override
	public ActionResult doExecute(SimpleSkillAction a, boolean doIt, int round) {
		Figure actor = a.getActor();
		Room room = actor.getRoom();
		if (room != null && room.fightRunning()) {

			Room oldRoom = actor.getRoom();
			if (actor.canPayActionPoints(1)) {
				boolean flees;
				RouteInstruction.Direction dir = actor.getPos().getPossibleFleeDirection();
				if (dir != null && actor.getRoom().getDoor(dir) != null
						&& actor.getRoom().getDoor(dir).isPassable(actor)) {
					if (doIt) {
						actor.setLookDir(dir.getValue());
						Position oldPos = actor.getPos();
						flees = actor.flee(dir, round);
						actor.payActionPoint(a, round);
						if (flees) {
							Percept p = new FleePercept(actor, oldPos, dir.getValue(), true, round);
							oldRoom.distributePercept(p);
							actor.getRoom().distributePercept(p);
						}
						else {
							Percept p = new FleePercept(actor, oldPos, dir.getValue(), false, round);
							oldRoom.distributePercept(p);
						}
						return ActionResult.DONE;
					}
					return ActionResult.POSSIBLE;
				}
				return ActionResult.POSITION;
			}
			return ActionResult.NOAP;
		}
		return ActionResult.MODE;
	}

}
