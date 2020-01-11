package de.jdungeon.app.gui;

import java.util.List;

import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.app.screen.StandardScreen;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input;
import de.jdungeon.game.ScrollMotion;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.16.
 */
public abstract class ContainerGUIElement extends AbstractGUIElement {

	public ContainerGUIElement(JDPoint position, JDDimension dimension, Game game) {
		super(position, dimension, game);
	}

	protected abstract List<? extends GUIElement> getAllSubElements();

	@Override
	public boolean handleTouchEvent(Input.TouchEvent touch) {
		JDPoint coordinates = new JDPoint(touch.x, touch.y);
		boolean handles = false;
		for (GUIElement guiElement : getAllSubElements()) {
			if (guiElement.hasPoint(coordinates) && guiElement.isVisible()) {
				boolean taken = guiElement.handleTouchEvent(touch);
				if(taken) {
					handles = true;
					break;
				}
			}
		}
		return handles;
	}

	@Override
	public boolean hasPoint(JDPoint p) {
		for (GUIElement guiElement : getAllSubElements()) {
			if (guiElement.hasPoint(p) && guiElement.isVisible()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean handleScrollEvent(ScrollMotion scrolling) {
		JDPoint coordinates = screen.normalizeRawCoordinates(scrolling.getStartEvent());
		boolean handles = false;
		for (GUIElement guiElement : getAllSubElements()) {
			if (guiElement.hasPoint(coordinates) && guiElement.isVisible()) {
				boolean taken = guiElement.handleScrollEvent(scrolling);
				if(taken) {
					handles = true;
					break;
				}
			}
		}
		return handles;
	}

	@Override
	public void update(float time) {
		for (GUIElement guiElement : getAllSubElements()) {
			guiElement.update(time);
		}
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		for (GUIElement guiElement : getAllSubElements()) {
			if (guiElement.isVisible()) {
				guiElement.paint(g, null);
			}
		}
	}


}
