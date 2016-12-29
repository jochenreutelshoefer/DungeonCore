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

	public PositionElement(JDPoint position, JDDimension dimension, GUIElement parent, Action action) {
		super(position, dimension, parent);
		this.action = action;
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
		int width = dimension.getWidth() / 2;
		int height = dimension.getHeight() / 2;
		int color = Color.WHITE;
		if(this.action instanceof AttackAction) {
			color = Color.RED;
		}
		g.drawOval(absolutePosition.getX() + width/2, absolutePosition.getY()+height/2, width, height, color);
	}
}
