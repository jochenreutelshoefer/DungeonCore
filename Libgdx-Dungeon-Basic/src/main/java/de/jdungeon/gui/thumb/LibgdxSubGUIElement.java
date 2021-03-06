package de.jdungeon.gui.thumb;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.util.JDDimension;

import de.jdungeon.gui.AbstractLibgdxGUIElement;
import de.jdungeon.gui.LibgdxGUIElement;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.16.
 */
public abstract class LibgdxSubGUIElement extends AbstractLibgdxGUIElement {

	protected final LibgdxGUIElement parent;

	private final int x;

	private final int y;
	public LibgdxSubGUIElement(JDPoint position, JDDimension dimension, LibgdxGUIElement parent) {
		super(position, dimension);
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
