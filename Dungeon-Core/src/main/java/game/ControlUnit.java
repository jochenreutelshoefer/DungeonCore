/*
 * Created on 18.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package game;

import dungeon.JDPoint;
import figure.Figure;
import figure.FigureInfo;
import figure.action.Action;
import figure.action.result.ActionResult;
import location.LevelExit;

/**
 * A control unit controls one figure within the dungeon game. Each figure
 * within the game is controlled by one control unit.
 * 
 * There are two major categories of control units: User Interfaces and AIs.
 * 
 * @author Jochen
 * 
 */
public interface ControlUnit extends ActionSpecifier, PerceptHandler {
	

	/**
	 * Sets the figure to be controlled by this ControlUnit. Should be used for
	 * initialization only.
	 * 
	 * @param f
	 */
	void setFigure(FigureInfo f);


	/**
	 * Provides feedback about an executed action. In particular, if an invalid
	 * action had been specified a corresponding ActionResult is provided.
	 * (ActionResult)
	 *  @param a
	 *            The action that was (tried to be) executed by this figure
	 * @param res
	 * @param round
	 */
	void actionProcessed(Action a, ActionResult res, int round);
	
	/**
	 * 
	 * 
	 * @param f
	 * @return
	 */
	boolean isHostileTo(FigureInfo f);
	
	/**
	 * Called when the visibility status of room number p has been decreased.
	 * (Can be used to store memory of latest available information about the
	 * room.)
	 * 
	 * @param p
	 */
	void notifyVisibilityStatusDecrease(JDPoint p);

	/**
	 * Called when the visibility status of room number p has been increased.
	 * (Can be used to highlight that fact prominently to the user.
	 *
	 * @param p
	 */
	void notifyVisibilityStatusIncrease(JDPoint p);

	void exitUsed(LevelExit exit, Figure f);
}
