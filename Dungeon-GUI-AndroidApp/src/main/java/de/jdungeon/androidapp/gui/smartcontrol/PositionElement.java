package de.jdungeon.androidapp.gui.smartcontrol;

import android.graphics.Color;
import dungeon.JDPoint;
import dungeon.Position;
import event.ActionEvent;
import event.EventManager;
import event.WannaMoveEvent;
import event.WannaStepEvent;
import figure.action.Action;
import figure.action.AttackAction;
import util.JDDimension;

import de.jdungeon.androidapp.DrawUtils;
import de.jdungeon.androidapp.gui.AbstractGUIElement;
import de.jdungeon.androidapp.gui.GUIElement;
import de.jdungeon.androidapp.gui.SubGUIElement;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.16.
 */
public class PositionElement extends SubGUIElement {

	private final Action action;
	private final int ballWidth;
	private final int ballHeight;
	private final int ballOffsetX;
	private final int ballOffsetY;

	public PositionElement(JDPoint position, JDDimension dimension, GUIElement parent, Action action) {
		super(position, dimension, parent);
		this.action = action;
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
	public void handleTouchEvent(Input.TouchEvent touch) {
		EventManager.getInstance().fireEvent(new ActionEvent(action));
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		JDPoint parentPosition = parent.getPositionOnScreen();
		JDPoint posRelative = this.getPositionOnScreen();
		JDPoint absolutePosition = new JDPoint(parentPosition.getX() + posRelative.getX(), parentPosition.getY() + posRelative.getY());
		JDDimension dimension = this.getDimension();
		DrawUtils.drawRectangle(g, Color.WHITE, absolutePosition, dimension);

		int color = Color.WHITE;
		if(this.action instanceof AttackAction) {
			color = Color.RED;
		}
		g.drawOval(absolutePosition.getX() + ballOffsetX, absolutePosition.getY()+ ballOffsetY, ballWidth, ballHeight, color);
	}
}
