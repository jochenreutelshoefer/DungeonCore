package de.jdungeon.androidapp.screen.start;

import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 01.01.17.
 */
public abstract class MenuScreen extends StandardScreen {

	public MenuScreen(Game game) {
		super(game);
	}

	protected void drawHeader(Graphics g, String headerString) {
		g.fillRect(0, 0, this.game.getScreenWidth(), 100, Colors.BLACK);
		g.drawLine(0, 100, this.game.getScreenWidth(), 100, Colors.GRAY);
		g.drawString(headerString, this.game.getScreenWidth()/2, 60, g
				.getDefaultPaint());
	}

	protected abstract String getHeaderString();

	@Override
	public void paint(float deltaTime) {
		super.paint(deltaTime);

		Graphics g = game.getGraphics();

		String headerString = getHeaderString();
		drawHeader(g, headerString);
	}

}
