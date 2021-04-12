package de.jdungeon.figure;

import de.jdungeon.figure.action.Action;

/**
 * An ActionSpecifier provides actions. This interface can be used to create
 * control units for the figures of the de.jdungeon.game (, be it a GUI or an AI).
 * 
 * @author Jochen
 * 
 */
public interface ActionSpecifier {
	
	/**
	 * Determines an action.
	 * 
	 * @return
	 */
	Action getAction();

}
