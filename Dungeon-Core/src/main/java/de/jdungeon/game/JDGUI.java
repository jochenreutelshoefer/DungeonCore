/*
 * Created on 13.11.2005
 *
 */
package de.jdungeon.game;

import java.util.List;

import de.jdungeon.figure.ControlUnit;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.Action;

public interface JDGUI extends ControlUnit {

	/**
	 * Sets the de.jdungeon.figure's action which will be processed next by the de.jdungeon.game.
	 *
	 * @param a
	 */
	void plugAction(Action a);

	void plugActions(List<Action> actions);
	

	/**
	 * Handles the end of the de.jdungeon.game. GUIs for instance can show Game-Over screen.
	 */
	void gameOver();
	
	/**
	 * Tells the ControlUnit that its de.jdungeon.figure is on turn. Can be used for GUI
	 * rendering for instance.
	 */
	void onTurn();


	FigureInfo getFigure();
	
	/**
	 * A new de.jdungeon.game round in the de.jdungeon.game world has begun.
	 */
	void gameRoundEnded();

}
