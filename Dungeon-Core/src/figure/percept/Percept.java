/*
 * Created on 08.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.percept;

import java.util.List;

import figure.Figure;
import game.JDEnv;

public abstract class Percept {
	
	protected Figure viewer;
	protected int round; 
	
	public Percept() {
		round = JDEnv.game.getRound();
	}
	
	public void perceivedBy(Figure f) {
		viewer = f;
	}
	
	public void setViewer(Figure f) {
		perceivedBy(f);
	}
	public int getRound() {
		return round;
	}
	
	public abstract List  getInvolvedFigures();
	
	
}
