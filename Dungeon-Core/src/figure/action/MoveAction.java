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
	
	private int direction;
	
	public MoveAction(/*int fighterIndex,*/int dir) {
		
		super(/*fighterIndex*/);
		direction = dir;
		
	}

	/**
	 * @return Returns the direction.
	 */
	public int getDirection() {
		return direction;
	}
	
	public String toString() {
		return (this.getClass()+" :"+direction);
	}
}
