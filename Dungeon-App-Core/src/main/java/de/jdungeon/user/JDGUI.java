/*
 * Created on 13.11.2005
 *
 */
package de.jdungeon.user;

import java.util.List;

import de.jdungeon.figure.ControlUnit;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.Action;
import de.jdungeon.game.Configuration;

public interface JDGUI extends ControlUnit {

	/**
	 * Sets the de.jdungeon.figure's action which will be processed next by the de.jdungeon.game.
	 *
	 * @param a
	 */
	void plugAction(Action a);

	void plugActions(List<Action> actions);


	@Override
	default boolean isUI(){
		return true;
	}

	Configuration getConfiguration();

	FigureInfo getFigure();
	
}
