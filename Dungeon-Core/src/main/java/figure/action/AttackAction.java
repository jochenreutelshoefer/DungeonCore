/*
 * Created on 05.04.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.action;


/**
 * Angriffsaktion. Enthaelt das Ziel.
 */
public class AttackAction extends Action {
	
	private int target;
	public AttackAction(int f) {
		super();
		target = f;
		
	}

	/**
	 * @return Returns the target.
	 */
	public int getTarget() {
		return target;
	}
	
	public String toString() {
		return (this.getClass()+" :"+target);
	}
}
