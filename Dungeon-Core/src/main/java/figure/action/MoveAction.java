/*
 * Created on 07.04.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.action;

import dungeon.JDPoint;
import dungeon.util.RouteInstruction;

/**
 * Bewegungsaktion. Enthaelt die Richtung der Bewegung.
 *
 *
 */
public class MoveAction extends Action {

	private final JDPoint startPoint;
	private final int directionIndex;
	private RouteInstruction.Direction direction = null;
	private RouteInstruction.Direction dir;

	public MoveAction(JDPoint startPoint, int dir) {
		super();
		this.startPoint = startPoint;
		directionIndex = dir;
		direction = RouteInstruction.Direction.fromInteger(directionIndex);
	}

	public MoveAction(JDPoint startPoint, RouteInstruction.Direction direction) {
		this.startPoint = startPoint;
		this.direction = direction;
		this.directionIndex = direction.getValue();
	}

	public JDPoint getStartPoint() {
		return startPoint;
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
}
