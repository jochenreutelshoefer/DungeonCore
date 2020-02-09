package de.jdungeon.gui.thumb;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dungeon.DoorInfo;
import dungeon.JDPoint;
import event.EventManager;
import figure.action.Action;
import util.JDDimension;

import de.jdungeon.app.ActionAssembler;
import de.jdungeon.app.event.InfoObjectClickedEvent;
import de.jdungeon.gui.LibgdxDrawUtils;
import de.jdungeon.ui.LibgdxGUIElement;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.16.
 */
public class LibgdxDoorElement extends LibgdxAnimatedSmartControlElement {

	private final boolean locked;
	private final boolean hasKey;
	private final Action action;
	private final DoorInfo clickableObject;
	private final ActionAssembler guiControl;

	public LibgdxDoorElement(JDPoint position, final JDDimension dimension, final LibgdxGUIElement parent, final boolean locked, final boolean hasKey, Action action, DoorInfo clickableObject, ActionAssembler guiControl) {
		super(position, dimension, parent);
		this.locked = locked;
		this.hasKey = hasKey;
		this.action = action;
		this.clickableObject = clickableObject;
		this.guiControl = guiControl;

		// prepare highlight animation drawables
		for (int i = 0; i < animationShapes.length; i++) {
			final int finalI = i;
			animationShapes[i] = new LibgdxDrawable() {
				@Override
				public void paint(ShapeRenderer renderer) {
					JDPoint parentPosition = parent.getPositionOnScreen();
					JDPoint posRelative = getPositionOnScreen();
					JDPoint absolutePosition = new JDPoint(parentPosition.getX() + posRelative.getX(), parentPosition.getY() + posRelative.getY());
					int width = (int) ((dimension.getWidth()) * buttonAnimationSizes[finalI]);
					int height = (int)((dimension.getHeight()) * buttonAnimationSizes[finalI]);
					int x = absolutePosition.getX() - ((width - dimension.getWidth())/2);
					int y = absolutePosition.getY() - ((height - dimension.getHeight())/2);
					renderer.set(ShapeRenderer.ShapeType.Line);
					renderer.setColor(Color.WHITE);
					renderer.rect(x, y, width, height);
				}

				@Override
				public void paint(SpriteBatch batch) {

				}
			};
		}
	}

	@Override
	public boolean handleClickEvent(int x, int y) {
		super.handleClickEvent(x, y);
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
	public void paint(SpriteBatch batch) {

	}

	@Override
	public void paint(ShapeRenderer renderer) {
		super.paint(renderer);
		JDPoint parentPosition = parent.getPositionOnScreen();
		JDPoint posRelative = this.getPositionOnScreen();
		JDPoint absolutePosition = new JDPoint(parentPosition.getX() + posRelative.getX(), parentPosition.getY() + posRelative.getY());
		JDDimension dimension = this.getDimension();
		Color borderColor = locked ? Color.RED : Color.GREEN;
		Color fillColor = hasKey ? Color.GREEN : Color.RED;
		LibgdxDrawUtils.fillRectangle(renderer, borderColor, absolutePosition, dimension);
		LibgdxDrawUtils.fillRectangle(renderer, fillColor, new JDPoint(absolutePosition.getX()+2, absolutePosition.getY()+2), new JDDimension(dimension.getWidth()-4, dimension.getHeight()-4));
	}
}
