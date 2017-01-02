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
	private final Image backGround;

	public ImageGUIElement(JDPoint position, JDDimension dimension, Image im) {
		this(position, dimension, im, null);
	}

	public ImageGUIElement(JDPoint position, JDDimension dimension, Image im, Image backGround) {
		super(position, dimension);
		this.im = im;
		this.backGround = backGround;
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		int x = this.position.getX();
		int y = this.position.getY();
		int width = this.dimension.getWidth();
		int height = this.dimension.getHeight();

		if(backGround != null) {
			g.drawScaledImage(backGround, x, y,
					width, height, 0, 0, backGround.getWidth(),
					backGround.getHeight());

			// actual image is drawn smaller within the background image
			x = x+width / 8;
			y = y+height / 8;
			width = width - width / 4;
			height =  height - height / 4;
		}

		g.drawScaledImage(im, x, y, width
				 ,height, 0, 0,
				im.getWidth(), im.getHeight());

	}
}
