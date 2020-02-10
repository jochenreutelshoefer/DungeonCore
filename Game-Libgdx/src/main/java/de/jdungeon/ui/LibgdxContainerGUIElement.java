package de.jdungeon.ui;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.game.Game;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.16.
 */
public abstract class LibgdxContainerGUIElement extends AbstractLibgdxGUIElement {

	public LibgdxContainerGUIElement(JDPoint position, JDDimension dimension) {
		super(position, dimension);
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
	public boolean handleClickEvent(int screenX, int screenY) {
		JDPoint coordinates = new JDPoint(screenX, screenY);
		boolean handles = false;
		for (LibgdxGUIElement guiElement : getAllSubElements()) {
			if (guiElement.hasPoint(coordinates) && guiElement.isVisible()) {
				boolean taken = guiElement.handleClickEvent(screenX, screenY);
				if(taken) {
					handles = true;
					break;
				}
			}
		}
		return handles;
	}

	@Override
	public void paint(ShapeRenderer shapeRenderer) {
		for (LibgdxGUIElement guiElement : getAllSubElements()) {
			if (guiElement.isVisible()) {
				guiElement.paint(shapeRenderer);
			}
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


	@Override
	public void update(float time) {
		for (LibgdxGUIElement guiElement : getAllSubElements()) {
			guiElement.update(time);
		}
	}



}
