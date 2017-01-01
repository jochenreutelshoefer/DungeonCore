package de.jdungeon.androidapp.gui;

import java.util.List;

import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.16.
 */
public abstract class ContainerGUIElement extends AbstractGUIElement {

	public ContainerGUIElement(JDPoint position, JDDimension dimension, StandardScreen screen, Game game) {
		super(position, dimension, screen, game);
	}

	protected abstract List<GUIElement> getAllSubElements();

	@Override
	public void handleTouchEvent(Input.TouchEvent touch) {
		JDPoint coordinates = new JDPoint(touch.x, touch.y);
		for (GUIElement guiElement : getAllSubElements()) {
			if (guiElement.hasPoint(coordinates) && guiElement.isVisible()) {
				guiElement.handleTouchEvent(touch);
			}
		}
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
