package de.jdungeon.androidapp.gui;

import dungeon.JDPoint;
import graphics.JDImageLocated;
import graphics.JDImageProxy;
import util.JDDimension;

import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.03.16.
 */
public abstract class ImageGUIElement extends AbstractGUIElement {

	private JDImageProxy jdImage;
	protected Image im;
	private final Image backGround;
	private int relativeOffsetX = 0;
	private int relativeOffsetY = 0;

	public ImageGUIElement(JDPoint position, JDDimension dimension, Image im) {
		this(position, dimension, im, null);
	}

	public ImageGUIElement(JDPoint position, JDDimension dimension, Image im, Image backGround) {
		super(position, dimension);
		this.im = im;
		this.backGround = backGround;
	}

	public ImageGUIElement(JDPoint position, JDDimension dimension, JDImageProxy jdImage) {
		this(position, dimension, (Image) jdImage.getImage(), null);
		this.jdImage = jdImage;
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	public void paint(Graphics g, JDPoint viewportPosition, JDImageLocated image) {
		g.drawScaledImage((Image) image.getImage().getImage(), image.getX(0), image.getY(0), image.getWidth()
				, image.getHeight(), 0, 0, im.getWidth(), im.getHeight());
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		int x = this.position.getX() + relativeOffsetX;
		int y = this.position.getY() + relativeOffsetY;
		int width = this.dimension.getWidth();
		int height = this.dimension.getHeight();
		if (jdImage != null) {
			paint(g, viewportPosition, new JDImageLocated(jdImage, x, y, width, height));
		}
		else {
			if (backGround != null) {
				g.drawScaledImage(backGround, x, y,
						width, height, 0, 0, backGround.getWidth(),
						backGround.getHeight());

				// actual image is drawn smaller within the background image
				x = x + width / 8;
				y = y + height / 8;
				width = width - width / 4;
				height = height - height / 4;
			}

			g.drawScaledImage(im, x, y, width, height, 0, 0,
					im.getWidth(), im.getHeight());

		}
	}

	public void setRelativeOffsetY(int relativeOffsetY) {
		this.relativeOffsetY = relativeOffsetY;
	}

	public void setRelativeOffsetX(int relativeOffsetX) {
		this.relativeOffsetX = relativeOffsetX;
	}
}
