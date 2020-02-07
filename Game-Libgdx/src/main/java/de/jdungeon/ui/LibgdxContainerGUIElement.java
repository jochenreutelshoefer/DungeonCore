package de.jdungeon.ui;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.game.Game;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.16.
 */
public abstract class LibgdxContainerGUIElement extends AbstractLibgdxGUIElement {

	public LibgdxContainerGUIElement(JDPoint position, JDDimension dimension, Game game) {
		super(position, dimension, game);
	}

	protected abstract List<? extends LibgdxGUIElement> getAllSubElements();

	@Override
	public boolean hasPoint(JDPoint p) {
		for (LibgdxGUIElement guiElement : getAllSubElements()) {
			if (guiElement.hasPoint(p) && guiElement.isVisible()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void update(float time) {
		for (LibgdxGUIElement guiElement : getAllSubElements()) {
			guiElement.update(time);
		}
	}

	@Override
	public void paint(SpriteBatch batch) {
		for (LibgdxGUIElement guiElement : getAllSubElements()) {
			if (guiElement.isVisible()) {
				guiElement.paint(batch);
			}
		}
	}


}
