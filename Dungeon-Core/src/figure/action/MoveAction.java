/*
 * Created on 07.04.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.action;


/**
 * Bewegungsaktion. Enthaelt die Richtung der Bewegung.
 *
 *
 */
public class MoveAction extends Action {
	
	private final int direction;
	
	public MoveAction(int dir) {
		super();
		direction = dir;
	}

	/**
	 * @return Returns the direction.
	 */
	public int getDirection() {
		return direction;
	}
	
	@Override
	public String toString() {
		return (this.getClass()+" :"+direction);
	}
}
