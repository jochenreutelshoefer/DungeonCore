/*
 * Created on 05.04.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.action;

import dungeon.Position;
import dungeon.Room;
import dungeon.util.RouteInstruction;
import figure.Figure;
import figure.FigureInfo;
import figure.action.result.ActionResult;
import figure.percept.FleePercept;
import figure.percept.Percept;
@Deprecated
public class FleeAction extends AbstractExecutableAction {

	private final Figure figure;

	public FleeAction(FigureInfo info) {
		figure = info.getMap().getDungeon().getFigureIndex().get(info.getFighterID());
	}


	public FleeAction(Figure figure) {
		this.figure = figure;
	}

	@Override
	public ActionResult handle(boolean doIt, int round) {
		Room room = figure.getRoom();
		if (room != null && room.fightRunning()) {

			Room oldRoom = figure.getRoom();
			if (figure.canPayActionPoints(1)) {

				if (figure.isPinnedToGround()) {
					return ActionResult.OTHER;
				}

				boolean flees;
				RouteInstruction.Direction dir = figure.getPos().getPossibleFleeDirection();
				if (dir != null && figure.getRoom().getDoor(dir) != null
						&& figure.getRoom().getDoor(dir).isPassable(figure)) {
					if (doIt) {
						figure.setLookDir(dir.getValue());
						Position oldPos = figure.getPos();
						flees = figure.flee(dir, round);
						figure.payActionPoint(this, round);
						if (flees) {
							Percept p = new FleePercept(figure, oldPos, dir.getValue(), true, round);
							oldRoom.distributePercept(p);
							figure.getRoom().distributePercept(p);
						}
						else {
							Percept p = new FleePercept(figure, oldPos, dir.getValue(), false, round);
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
