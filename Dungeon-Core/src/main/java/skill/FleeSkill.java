package skill;

import dungeon.Position;
import dungeon.Room;
import dungeon.util.RouteInstruction;
import figure.Figure;
import figure.FigureInfo;
import figure.action.result.ActionResult;
import figure.percept.FleePercept;
import figure.percept.Percept;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 10.04.20.
 */
public class FleeSkill extends Skill<FleeSkill.FleeAction>{


	@Override
	public ActionResult execute(FleeAction a, boolean doIt, int round) {
		Room room = a.figure.getRoom();
		if (room != null && room.fightRunning()) {

			Room oldRoom = a.figure.getRoom();
			if (a.figure.canPayActionPoints(1)) {

				if (a.figure.isPinnedToGround()) {
					return ActionResult.OTHER;
				}

				boolean flees;
				RouteInstruction.Direction dir = a.figure.getPos().getPossibleFleeDirection();
				if (dir != null && a.figure.getRoom().getDoor(dir) != null
						&& a.figure.getRoom().getDoor(dir).isPassable(a.figure)) {
					if (doIt) {
						a.figure.setLookDir(dir.getValue());
						Position oldPos = a.figure.getPos();
						flees = a.figure.flee(dir, round);
						a.figure.payActionPoint(a, round);
						if (flees) {
							Percept p = new FleePercept(a.figure, oldPos, dir.getValue(), true, round);
							oldRoom.distributePercept(p);
							a.figure.getRoom().distributePercept(p);
						}
						else {
							Percept p = new FleePercept(a.figure, oldPos, dir.getValue(), false, round);
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

	@Override
	public FleeActionBuilder newAction() {
		return new FleeActionBuilder();
	}

	static class FleeAction extends SkillAction{

		private final Figure figure;

		public FleeAction(FleeSkill skill, FigureInfo info) {
			super(skill);
			figure = info.getMap().getDungeon().getFigureIndex().get(info.getFighterID());
		}
	}

	static class FleeActionBuilder extends ActionBuilder<FleeSkill.FleeAction> {
		private FleeSkill skill;
		private FigureInfo figure;

		FleeActionBuilder actor(FleeSkill skill, FigureInfo figure) {
			this.skill = skill;
			this.figure = figure;
			return this;
		}

		@Override
		public FleeAction get() {
			return new FleeAction(skill, figure);
		}
	}

}
