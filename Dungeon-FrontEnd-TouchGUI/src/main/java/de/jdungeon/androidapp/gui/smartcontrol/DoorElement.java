package de.jdungeon.androidapp.gui.smartcontrol;

import dungeon.JDPoint;
import event.ActionEvent;
import event.EventManager;
import figure.action.Action;
import util.JDDimension;

import de.jdungeon.androidapp.DrawUtils;
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
public class DoorElement extends SubGUIElement {

	private final boolean locked;
	private final boolean hasKey;
	private final Action action;

	public DoorElement(JDPoint position, JDDimension dimension, GUIElement parent, boolean locked, boolean hasKey, Action action) {
		super(position, dimension, parent);
		this.locked = locked;
		this.hasKey = hasKey;
		this.action = action;
	}

	@Override
	public void handleTouchEvent(Input.TouchEvent touch) {
		EventManager.getInstance().fireEvent(new ActionEvent(action));
	}


	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		JDPoint parentPosition = parent.getPositionOnScreen();
		JDPoint posRelative = this.getPositionOnScreen();
		JDPoint absolutePosition = new JDPoint(parentPosition.getX() + posRelative.getX(), parentPosition.getY() + posRelative.getY());
		JDDimension dimension = this.getDimension();
		Color borderColor = locked ? Colors.RED : Colors.GREEN;
		Color fillColor = hasKey ? Colors.GREEN : Colors.RED;
		DrawUtils.fillRectangle(g, borderColor, absolutePosition, dimension);
		DrawUtils.fillRectangle(g, fillColor, new JDPoint(absolutePosition.getX()+2, absolutePosition.getY()+2), new JDDimension(dimension.getWidth()-4, dimension.getHeight()-4));
	}
}
