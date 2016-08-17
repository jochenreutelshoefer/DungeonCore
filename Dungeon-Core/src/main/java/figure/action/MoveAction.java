/*
 * Created on 07.04.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.action;

import dungeon.util.RouteInstruction;

/**
 * Bewegungsaktion. Enthaelt die Richtung der Bewegung.
 *
 *
 */
public class MoveAction extends Action {
	
	private int directionIndex;
	private RouteInstruction.Direction direction = null;

	private RouteInstruction.Direction dir;

	public MoveAction(int dir) {
		super();
		directionIndex = dir;
		direction = RouteInstruction.Direction.fromInteger(directionIndex);
	}

	public MoveAction(RouteInstruction.Direction direction) {
		this.direction = direction;
		this.directionIndex = direction.getValue();
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
		return (this.getClass()+" :"+ directionIndex);
	}
}
