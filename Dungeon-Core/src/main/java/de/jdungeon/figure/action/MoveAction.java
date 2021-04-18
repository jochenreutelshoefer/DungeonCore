/*
 * Created on 07.04.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.figure.action;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.result.ActionResult;

/**
 * Bewegungsaktion. Enthaelt die Richtung der Bewegung.
 *
 *
 */
public class MoveAction extends AbstractExecutableAction {

	private final JDPoint startPoint;
	private final int directionIndex;
	private RouteInstruction.Direction direction = null;
	private RouteInstruction.Direction dir;
	private final Figure figure;

	public MoveAction(FigureInfo info, JDPoint startPoint, int dir) {
		super();
		this.startPoint = startPoint;
		directionIndex = dir;
		direction = RouteInstruction.Direction.fromInteger(directionIndex);
		figure = info.getVisMap().getDungeon().getFigureIndex().get(info.getFigureID());
	}

	public MoveAction(Figure fig, JDPoint startPoint, int dir) {
		super();
		this.startPoint = startPoint;
		directionIndex = dir;
		direction = RouteInstruction.Direction.fromInteger(directionIndex);
		figure = fig;
	}

	public MoveAction(FigureInfo info, JDPoint startPoint, RouteInstruction.Direction direction) {
		this.startPoint = startPoint;
		this.direction = direction;
		this.directionIndex = direction.getValue();
		figure = info.getVisMap().getDungeon().getFigureIndex().get(info.getFigureID());

	}

	public MoveAction(Figure fig, JDPoint startPoint, RouteInstruction.Direction direction) {
		this.startPoint = startPoint;
		this.direction = direction;
		this.directionIndex = direction.getValue();
		figure = fig;
	}


	/**
	 * @return Returns the directionIndex.
	 */
	@Deprecated // use getDirection instead
	public int getDirectionIndex() {
		return directionIndex;
	}

	public RouteInstruction.Direction getDirection() {
		return direction;
	}
	
	@Override
	public String toString() {
		return (this.getClass()+" :" + this.startPoint +" -> "+ RouteInstruction.Direction.fromInteger(directionIndex));
	}

	@Override
	public ActionResult handle(boolean doIt, int round) {
		if (figure.getActionPoints() < 1) {
			return ActionResult.NOAP;
		}
		int dir = getDirectionIndex();
		if (figure.getPos().getIndex() != Figure.getDirPos(dir)) {
			return ActionResult.POSITION;
		}
		if (figure.wayPassable(dir)) {
			if (doIt) {
				figure.walk(getDirection(), round);
				return ActionResult.DONE;
			}
			else {
				return ActionResult.POSSIBLE;
			}
		}
		else {
			return ActionResult.WRONG_TARGET;
		}
	}
}
