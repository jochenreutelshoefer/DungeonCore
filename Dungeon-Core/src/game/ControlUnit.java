/*
 * Created on 18.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package game;

import dungeon.JDPoint;
import figure.FigureInfo;
import figure.action.Action;
import figure.action.result.ActionResult;
import figure.percept.Percept;



public interface ControlUnit extends ActionSpecifier{
	

	
	public void actionDone(Action a, ActionResult res);

	
	public void tellPercept(Percept p);
	
	public void onTurn();
	
	public void gameOver();
	
	public void setFigure(FigureInfo f);


	public boolean isHostileTo(FigureInfo f);
	
	public void resetingRoomVisibility(JDPoint p);

}
