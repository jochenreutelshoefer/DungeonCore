package de.jdungeon.androidapp.gui.smartcontrol;

import dungeon.JDPoint;
import event.ActionEvent;
import event.EventManager;
import figure.FigureInfo;
import figure.action.Action;
import figure.action.AttackAction;
import game.InfoEntity;
import util.JDDimension;

import de.jdungeon.androidapp.DrawUtils;
import de.jdungeon.androidapp.event.InfoObjectClickedEvent;
import de.jdungeon.androidapp.gui.GUIElement;
import de.jdungeon.androidapp.gui.SubGUIElement;
import de.jdungeon.game.Color;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.16.
 */
public class PositionElement extends SubGUIElement {

	private final Action action;
	private final Color color;
	private final InfoEntity clickableObject;
	private final int ballWidth;
	private final int ballHeight;
	private final int ballOffsetX;
	private final int ballOffsetY;

	public PositionElement(JDPoint position, JDDimension dimension, GUIElement parent, Action action, Color color, InfoEntity clickableObject) {
		super(position, dimension, parent);
		this.action = action;
		this.color = color;
		this.clickableObject = clickableObject;
		ballWidth = (int) (dimension.getWidth() / 1.5);
		ballHeight = (int) (dimension.getHeight() / 1.5);
		ballOffsetX = (dimension.getWidth() - ballWidth) / 2;
		ballOffsetY = (dimension.getHeight() - ballHeight) / 2;
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public boolean handleTouchEvent(Input.TouchEvent touch) {
		if(action != null) {
			EventManager.getInstance().fireEvent(new ActionEvent(action));
			if(clickableObject != null) {
				EventManager.getInstance().fireEvent(new InfoObjectClickedEvent(clickableObject));
			}
		}
		return true;
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		super.paint(g, viewportPosition);
		JDPoint parentPosition = parent.getPositionOnScreen();
		JDPoint posRelative = this.getPositionOnScreen();
		JDPoint absolutePosition = new JDPoint(parentPosition.getX() + posRelative.getX(), parentPosition.getY() + posRelative.getY());
		JDDimension dimension = this.getDimension();
		//DrawUtils.drawRectangle(g, Colors.BLUE, absolutePosition, dimension);
		int x = absolutePosition.getX() + ballOffsetX;
		int y = absolutePosition.getY() + ballOffsetY;
		if(clickableObject instanceof FigureInfo) {
			g.fillOval(x, y, ballWidth, ballHeight, color);
		} else {
			g.drawOval(x, y, ballWidth, ballHeight, color);
		}

	}
}
