package game;

import figure.action.Action;

/**
 * An ActionSpecifier provides actions. This interface can be used to create
 * control units for the figures of the game (, be it a GUI or an AI).
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
	public Action getAction();

}
