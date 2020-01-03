package de.jdungeon.app.gui.smartcontrol;

import dungeon.DoorInfo;
import dungeon.JDPoint;
import event.EventManager;
import figure.action.Action;
import util.JDDimension;

import de.jdungeon.app.DrawUtils;
import de.jdungeon.app.ActionController;
import de.jdungeon.app.event.InfoObjectClickedEvent;
import de.jdungeon.app.gui.GUIElement;
import de.jdungeon.game.Color;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.16.
 */
public class DoorElement extends AnimatedSmartControlElement {

	private final boolean locked;
	private final boolean hasKey;
	private final Action action;
	private final DoorInfo clickableObject;
	private final ActionController guiControl;

	public DoorElement(JDPoint position, final JDDimension dimension, final GUIElement parent, final boolean locked, final boolean hasKey, Action action, DoorInfo clickableObject, ActionController guiControl) {
		super(position, dimension, parent);
		this.locked = locked;
		this.hasKey = hasKey;
		this.action = action;
		this.clickableObject = clickableObject;
		this.guiControl = guiControl;

		// prepare highlight animation drawables
		for (int i = 0; i < animationShapes.length; i++) {
			final int finalI = i;
			animationShapes[i] = new Drawable() {
				@Override
				public void paint(Graphics g, JDPoint viewportPosition) {
					JDPoint parentPosition = parent.getPositionOnScreen();
					JDPoint posRelative = getPositionOnScreen();
					JDPoint absolutePosition = new JDPoint(parentPosition.getX() + posRelative.getX(), parentPosition.getY() + posRelative.getY());
					int width = (int) ((dimension.getWidth()) * buttonAnimationSizes[finalI]);
					int height = (int)((dimension.getHeight()) * buttonAnimationSizes[finalI]);
					int x = absolutePosition.getX() - ((width - dimension.getWidth())/2);
					int y = absolutePosition.getY() - ((height - dimension.getHeight())/2);
					DrawUtils.drawRectangle(g, Colors.WHITE, new JDPoint(x, y), new JDDimension(width, height));
				}
			};
		}
	}

	@Override
	public boolean handleTouchEvent(Input.TouchEvent touch) {
		super.handleTouchEvent(touch);
		guiControl.plugActions(guiControl.getActionAssembler().wannaLockDoor(clickableObject));
		if(clickableObject != null) {
			EventManager.getInstance().fireEvent(new InfoObjectClickedEvent(clickableObject));
		}
		return true;
	}


	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		super.paint(g, viewportPosition);
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
