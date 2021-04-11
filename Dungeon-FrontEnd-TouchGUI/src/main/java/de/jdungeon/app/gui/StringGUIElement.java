package de.jdungeon.app.gui;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.util.JDDimension;

import de.jdungeon.app.screen.StandardScreen;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.util.PaintBuilder;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 19.01.18.
 */
public class StringGUIElement extends AbstractGUIElement {

	private final String text;
	private final PaintBuilder paint;

	public StringGUIElement(JDPoint position, JDDimension dimension, StandardScreen screen, Game game, String text) {
		super(position, dimension, game);
		this.text = text;
		paint = new PaintBuilder();
		paint.setColor(Colors.WHITE);
		paint.setFontSize(20);

	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		g.drawString(text, this.position.getX(), this.position.getY(), paint);
	}
}
