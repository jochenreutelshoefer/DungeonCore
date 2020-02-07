package de.jdungeon.gui.thumb;

import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.app.gui.AbstractGUIElement;
import de.jdungeon.app.gui.GUIElement;
import de.jdungeon.game.Graphics;
import de.jdungeon.ui.AbstractLibgdxGUIElement;
import de.jdungeon.ui.LibgdxGUIElement;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.16.
 */
public abstract class LibgdxSubGUIElement extends AbstractLibgdxGUIElement {

	protected final LibgdxGUIElement parent;

	private final int x;

	private final int y;
	public LibgdxSubGUIElement(JDPoint position, JDDimension dimension,
							   LibgdxGUIElement parent) {
		super(position, dimension, parent.getGame());
		this.parent = parent;

		y  = parent.getPositionOnScreen().getY() + position.getY();
		x = parent.getPositionOnScreen().getX() + position.getX();
	}



	public LibgdxGUIElement getParent() {
		return parent;
	}


	@Override
	public boolean hasPoint(JDPoint p) {
		// check whether point p is in rectangle
		JDPoint absolutePosition = new JDPoint( getX(), getY());
		return p.getX() >= absolutePosition.getX()
				&& p.getX() <= absolutePosition.getX() + dimension.getWidth()
				&& p.getY() >= absolutePosition.getY()
				&& p.getY() <= absolutePosition.getY() + dimension.getHeight();
	}

	protected int getY() {
		return y;
	}

	protected int getX() {
		return x;
	}

}
