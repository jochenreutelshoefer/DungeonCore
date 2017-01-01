package de.jdungeon.androidapp.gui;

import dungeon.JDPoint;
import util.JDDimension;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.16.
 */
public abstract class SubGUIElement extends AbstractGUIElement {

	protected final GUIElement parent;

	public SubGUIElement(JDPoint position, JDDimension dimension,
						 GUIElement parent) {
		super(position, dimension);
		this.parent = parent;
	}


	@Override
	public boolean hasPoint(JDPoint p) {
		// check whether point p is in rectangle
		JDPoint absolutePosition = new JDPoint(parent.getPositionOnScreen().getX() + position.getX(),parent.getPositionOnScreen().getY() + position.getY());
		return p.getX() >= absolutePosition.getX()
				&& p.getX() <= absolutePosition.getX() + dimension.getWidth()
				&& p.getY() >= absolutePosition.getY()
				&& p.getY() <= absolutePosition.getY() + dimension.getHeight();
	}

}