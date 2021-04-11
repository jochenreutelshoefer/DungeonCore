/*
 * Created on 14.06.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.figure.action;

import de.jdungeon.dungeon.Position;
import de.jdungeon.dungeon.Room;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.RoomObservationStatus;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.figure.percept.Percept;
import de.jdungeon.figure.percept.ScoutPercept;

public class ScoutAction extends AbstractExecutableAction{

	private final int dir;
	private final Figure figure;

	public ScoutAction(FigureInfo figureInfo, int dir) {
		super();
		this.dir = dir;
		figure = figureInfo.getMap().getDungeon().getFigureIndex().get(figureInfo.getFigureID());

	}
	
	public int getDirection() {
		return dir;
	}

	@Override
	public ActionResult handle(boolean doIt, int round) {
		Position position = figure.getPos();
		if (figure.getActionPoints() < 1) {
			return ActionResult.NOAP;
		}
		int dir = getDirection();
		if (position.getIndex() != Figure.getDirPos(dir)) {
			return ActionResult.POSITION;
		}
		Room toScout = figure.getRoom().getNeighbourRoom(dir);
		if (toScout == null) {
			return ActionResult.UNKNOWN;
		}
		Room scoutTarget = figure.getRoom().getNeighbourRoom(dir);
		if (figure.getRoomVisibility().getStatusObject(scoutTarget.getNumber()).getVisibilityStatus() >= RoomObservationStatus.VISIBILITY_FIGURES) {
			return ActionResult.UNKNOWN;
		}
		if (doIt) {
			figure.setLookDir(dir);
			ScoutResult result = figure.scout(this, round);
			figure.getRoomVisibility().addVisibilityModifier(toScout.getNumber(), result);
			Percept p = new ScoutPercept(figure, figure.getRoom(), dir, round);
			figure.getRoom().distributePercept(p);
			figure.payActionPoint(this, round);
			return ActionResult.DONE;
		}
		return ActionResult.POSSIBLE;
	}



}
