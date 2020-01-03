package de.jdungeon.world;

import figure.percept.Percept;
import game.PerceptHandler;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 03.01.20.
 */
public class GameScreenPerceptHandler implements PerceptHandler {

	private final GameScreen screen;

	public GameScreenPerceptHandler(GameScreen screen) {
		this.screen = screen;
	}

	@Override
	public void tellPercept(Percept p) {
		// TODO
	}
}
