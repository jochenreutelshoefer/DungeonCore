/*
 * Created on 13.11.2005
 *
 */
package game;



public interface JDGUI extends ControlUnit{
	
	

	/**
	 * Handles the end of the game. GUIs for instance can show Game-Over screen.
	 */
	public void gameOver();
	
	/**
	 * Tells the ControlUnit that its figure is on turn. Can be used for GUI
	 * rendering for instance.
	 */
	public void onTurn();


	
	/**
	 * A new game round in the game world has begun.
	 */
	public void gameRoundEnded();
}
