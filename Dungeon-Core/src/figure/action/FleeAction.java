/*
 * Created on 05.04.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.action;


/**
 * Flieh-Aktion. Enthaelt die Fluchtrichtung
 */
public class FleeAction extends Action {
	
	//int direction;
	private boolean panic = false;
	
	public FleeAction(/*int fighterIndex,int dir,*/ boolean panic) {
		super(/*fighterIndex*/);
	    this.panic = panic;
		//direction = dir;
		
	}
	
//	FleeAction(int dir) {
//		direction = dir;
//	}

	/**
	 * @return Returns the direction.
	 */
//	public int getDirection() {
//		return direction;
//	}
	
//	public String toString() {
//		return (this.getClass()+" :"+direction);
//	}
}
