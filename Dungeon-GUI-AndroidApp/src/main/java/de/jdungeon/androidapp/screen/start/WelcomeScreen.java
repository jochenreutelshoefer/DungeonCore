package de.jdungeon.androidapp.screen.start;

import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.12.16.
 */
public class WelcomeScreen extends StandardScreen {

	public WelcomeScreen(Game game) {
		super(game);
	}

	@Override
	public void paint(float deltaTime) {
		Graphics gr = game.getGraphics();

		super.paint(deltaTime);

		gr.drawARGB(155, 0, 0, 0);
		gr.drawString("Welcome to Untitled Dungeon Game", 165, 165, defaultPaint);
	}
}
