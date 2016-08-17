package game;

import figure.percept.Percept;

public interface PerceptHandler {
	/**
	 * Processes a percept that the figure observed from the world.
	 * 
	 * @param p
	 */
	public void tellPercept(Percept p);

}
