package de.jdungeon.androidapp.gui;

import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.androidapp.screen.GameScreen;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.03.16.
 */
public abstract class ImageGUIElement extends AbstractGUIElement {

	protected Image im;
	protected Game game;


	public ImageGUIElement(JDPoint position, JDDimension dimension, Image im, Game g) {
		super(position, dimension);
		this.im = im;
		this.game = g;
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		g.drawScaledImage(im, this.position.getX(), this.position.getY(),
				this.dimension.getWidth(), this.dimension.getHeight(), 0, 0,
				im.getHeight(), im.getWidth());

	}
}
