/*
 * Created on 14.06.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.action;

/**
 * Klasse
 *
 */
public class ScoutAction extends Action{
	private int dir;
	public ScoutAction(/*int fighterID, */int dir) {
		super(/*fighterID*/);
		this.dir = dir;
	}
	
	public int getDirection() {
		return dir;
	}
}
