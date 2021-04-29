/*
 * Created on 18.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.figure;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.figure.action.Action;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.location.LevelExit;

/**
 * A control unit controls one de.jdungeon.figure within the dungeon de.jdungeon.game. Each de.jdungeon.figure
 * within the de.jdungeon.game is controlled by one control unit.
 * 
 * There are two major categories of control units: User Interfaces and AIs.
 * 
 * @author Jochen
 * 
 */
public interface ControlUnit extends ActionSpecifier, PerceptHandler {
	

	/**
	 * Sets the de.jdungeon.figure to be controlled by this ControlUnit. Should be used for
	 * initialization only.
	 * 
	 * @param f
	 */
	void setFigure(FigureInfo f);


	default boolean isUI() {
		return false;
	}

	/**
	 * Tells the ControlUnit that its figure is on turn. Can be used for GUI
	 * rendering for instance.
	 */
	void onTurn();

	/**
	 * Handles the end of the GUIs for instance can show Game-Over screen.
	 */
	void gameOver();

	/**
	 * Provides feedback about an executed action. In particular, if an invalid
	 * action had been specified a corresponding ActionResult is provided.
	 * (ActionResult)
	 *  @param action
	 *            The action that was (tried to be) executed by this de.jdungeon.figure
	 * @param res
	 * @param round
	 */
	void actionProcessed(Action action, ActionResult res, int round);
	
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
	 * (Can be used to highlight that fact prominently to the de.jdungeon.user.
	 *
	 * @param p
	 */
	void notifyVisibilityStatusIncrease(JDPoint p);

	void exitUsed(LevelExit exit, Figure f);
}
