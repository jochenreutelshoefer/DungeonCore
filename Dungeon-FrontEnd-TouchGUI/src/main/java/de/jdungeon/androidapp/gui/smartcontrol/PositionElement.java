package de.jdungeon.androidapp.gui.smartcontrol;

import dungeon.JDPoint;
import event.ActionEvent;
import event.EventManager;
import figure.FigureInfo;
import figure.action.Action;
import game.InfoEntity;
import game.RoomEntity;
import util.JDDimension;

import de.jdungeon.androidapp.event.InfoObjectClickedEvent;
import de.jdungeon.androidapp.gui.GUIElement;
import de.jdungeon.game.Color;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.16.
 */
public class PositionElement extends AnimatedSmartControlElement {

	private final Action action;
	private final Color color;
	private final RoomEntity clickableObject;
	private final int ballWidth;
	private final int ballHeight;
	private final int ballOffsetX;
	private final int ballOffsetY;
	private final int x;
	private final int y;

	public PositionElement(JDPoint position, JDDimension dimension, final GUIElement parent, Action action, final Color color, RoomEntity clickableObject) {
		super(position, dimension, parent);
		this.action = action;
		this.color = color;
		this.clickableObject = clickableObject;
		ballWidth = (int) (dimension.getWidth() / 1.5);
		ballHeight = (int) (dimension.getHeight() / 1.5);
		ballOffsetX = (dimension.getWidth() - ballWidth) / 2;
		ballOffsetY = (dimension.getHeight() - ballHeight) / 2;

		JDPoint parentPosition = parent.getPositionOnScreen();
		JDPoint posRelative = this.getPositionOnScreen();
		JDPoint absolutePosition = new JDPoint(parentPosition.getX() + posRelative.getX(), parentPosition.getY() + posRelative
				.getY());
		x = absolutePosition.getX() + ballOffsetX;
		y = absolutePosition.getY() + ballOffsetY;

		final JDPoint positionOnScreen = getPositionOnScreen();

		// prepare highlight animation drawables
		if (action != null) {
			for (int i = 0; i < animationShapes.length; i++) {
				final int finalI = i;
				animationShapes[i] = new Drawable() {
					@Override
					public void paint(Graphics g, JDPoint viewportPosition) {
						JDPoint parentPosition = parent.getPositionOnScreen();
						JDPoint absolutePosition = new JDPoint(parentPosition.getX() + positionOnScreen.getX(), parentPosition
								.getY() + positionOnScreen
								.getY());
						int x = absolutePosition.getX() + ballOffsetX;
						int y = absolutePosition.getY() + ballOffsetY;

						int scaledSizeX = (int) (ballWidth * buttonAnimationSizes[finalI]);
						int scaledSizeY = (int) (ballHeight * buttonAnimationSizes[finalI]);
						g.drawOval(x - ((scaledSizeX - ballWidth) / 2), y - ((scaledSizeY - ballHeight) / 2), scaledSizeX, scaledSizeY, color);
					}
				};
			}
		}
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public boolean handleTouchEvent(Input.TouchEvent touch) {
		super.handleTouchEvent(touch);
		if (action != null) {
			EventManager.getInstance().fireEvent(new ActionEvent(action));
			if (clickableObject != null) {
				EventManager.getInstance().fireEvent(new InfoObjectClickedEvent(clickableObject));
			}
		}
		return true;
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		super.paint(g, viewportPosition);
		if (clickableObject instanceof FigureInfo) {
			g.fillOval(x, y, ballWidth, ballHeight, color);
		}
		else {
			g.drawOval(x, y, ballWidth, ballHeight, color);
		}

	}
}
