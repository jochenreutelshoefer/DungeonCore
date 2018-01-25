package de.jdungeon.androidapp.gui;

import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.androidapp.gui.smartcontrol.AnimatedSmartControlElement;
import de.jdungeon.androidapp.gui.smartcontrol.Drawable;
import de.jdungeon.androidapp.gui.smartcontrol.SmartControlRoomPanel;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.12.17.
 */
public class SubGUIElementAnimated extends AnimatedSmartControlElement {

	private final JDPoint posRelative;

	public SubGUIElementAnimated(final JDPoint posRelative, final JDDimension dimension, SmartControlRoomPanel smartControl) {
		super(posRelative, dimension, smartControl);
		this.posRelative = posRelative;

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
		g.fillRect(getX(), getY(), dimension.getWidth(), dimension.getHeight(), Colors.WHITE);
	}

}
