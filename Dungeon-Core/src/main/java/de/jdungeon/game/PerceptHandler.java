package de.jdungeon.game;

import de.jdungeon.figure.percept.Percept;

public interface PerceptHandler {
	/**
	 * Processes a percept that the de.jdungeon.figure observed from the world.
	 * 
	 * @param p
	 */
	void tellPercept(Percept p);

}
