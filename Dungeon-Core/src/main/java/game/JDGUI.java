/*
 * Created on 13.11.2005
 *
 */
package game;

import java.util.List;

import figure.FigureInfo;
import figure.action.Action;
import item.ItemInfo;

public interface JDGUI extends ControlUnit{

	/**
	 * Sets the figure's action which will be processed next by the game.
	 *
	 * @param a
	 */
	void plugAction(Action a);

	void plugActions(List<Action> actions);
	

	/**
	 * Handles the end of the game. GUIs for instance can show Game-Over screen.
	 */
	void gameOver();
	
	/**
	 * Tells the ControlUnit that its figure is on turn. Can be used for GUI
	 * rendering for instance.
	 */
	void onTurn();


	FigureInfo getFigure();
	
	/**
	 * A new game round in the game world has begun.
	 */
	void gameRoundEnded();

}
