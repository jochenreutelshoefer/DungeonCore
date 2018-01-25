package de.jdungeon.androidapp.gui;

import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.game.Graphics;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.12.16.
 */
public abstract class SubGUIElement extends AbstractGUIElement {

	protected final GUIElement parent;

	private final int x;
	private final int y;

	public SubGUIElement(JDPoint position, JDDimension dimension,
						 GUIElement parent) {
		super(position, dimension);
		this.parent = parent;

		y  = parent.getPositionOnScreen().getY() + position.getY();
		x = parent.getPositionOnScreen().getX() + position.getX();
	}


	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		// for development only
		/*
		g.drawRect(parent.getPositionOnScreen().getX() +position.getX(), parent.getPositionOnScreen().getY() + position.getY(), dimension.getWidth(), dimension.getHeight(), Colors.BLUE);
		*/
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
