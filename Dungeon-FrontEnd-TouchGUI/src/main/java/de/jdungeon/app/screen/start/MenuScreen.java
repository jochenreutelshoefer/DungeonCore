package de.jdungeon.app.screen.start;

import de.jdungeon.app.screen.StandardScreen;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 01.01.17.
 */
public abstract class MenuScreen extends StandardScreen {

	public static final int HEADER_HEIGHT = 85;

	public MenuScreen(Game game) {
		super(game);
	}

	protected void drawHeader(Graphics g, String headerString) {
		g.fillRect(0, 0, this.game.getScreenWidth(), HEADER_HEIGHT, Colors.BLACK);
		g.drawLine(0, HEADER_HEIGHT, this.game.getScreenWidth(), HEADER_HEIGHT, Colors.GRAY);
		g.drawString(headerString, this.game.getScreenWidth()/2, 60, g
				.getTextPaintWhite25());
	}

	protected abstract String getHeaderString();

	@Override
	public void paint(float deltaTime) {
		super.paint(deltaTime);

		Graphics g = game.getGraphics(null);

		String headerString = getHeaderString();
		drawHeader(g, headerString);
	}

}
