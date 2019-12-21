package de.jdungeon.app.gui;

import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.app.gui.smartcontrol.AnimatedSmartControlElement;
import de.jdungeon.app.gui.smartcontrol.Drawable;
import de.jdungeon.app.gui.smartcontrol.SmartControlRoomPanel;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.12.17.
 */
public class SubGUIElementAnimated extends AnimatedSmartControlElement {

	private final JDPoint posRelative;
	private final Image image;

	public SubGUIElementAnimated(final JDPoint posRelative, final JDDimension dimension, SmartControlRoomPanel smartControl, Image image) {
		super(posRelative, dimension, smartControl);
		this.posRelative = posRelative;
		this.image = image;

		// prepare highlight animation drawables
		for (int i = 0; i < animationShapes.length; i++) {
			final int finalI = i;
			animationShapes[i] = new Drawable() {
				@Override
				public void paint(Graphics g, JDPoint viewportPosition) {
					JDPoint parentPosition = parent.getPositionOnScreen();
					JDPoint absolutePosition = new JDPoint(parentPosition.getX() + posRelative.getX(), parentPosition.getY() + posRelative
							.getY());
					int scaledWidth = (int) (dimension.getWidth() * buttonAnimationSizes[finalI]);
					int scaledHeight = (int) (dimension.getHeight() * buttonAnimationSizes[finalI]);
					g.drawRect(absolutePosition.getX() - ((scaledWidth - dimension.getWidth())/2), absolutePosition.getY()- ((scaledHeight - dimension.getHeight())/2), scaledWidth, scaledHeight, Colors.WHITE);
				}
			};
		}
	}

	@Override
	public boolean handleTouchEvent(Input.TouchEvent touch) {
		return super.handleTouchEvent(touch);
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		super.paint(g, viewportPosition);
		if(image != null) {
			g.drawScaledImage(image, getX(), getY(),dimension.getWidth(), dimension.getHeight(), 0, 0, image.getWidth(), image.getHeight());
		} else {
			g.fillRect(getX(), getY(), dimension.getWidth(), dimension.getHeight(), Colors.WHITE);
		}
	}

}
