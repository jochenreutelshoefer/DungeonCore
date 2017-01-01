package de.jdungeon.androidapp.gui;

import util.JDDimension;

import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input.TouchEvent;
import de.jdungeon.game.MotionEvent;
import de.jdungeon.game.ScrollMotion;

import dungeon.JDPoint;

public abstract class AbstractGUIElement implements GUIElement {

	protected final JDPoint position;
	protected final JDDimension dimension;
	// TODO: remove
	private GUIElement parent;
	//protected GUIElement parent;
	protected StandardScreen screen = null;
	protected Game game;


	public AbstractGUIElement(JDPoint position, JDDimension dimension,
			StandardScreen screen, Game game) {
		super();
		this.position = position;
		this.dimension = dimension;
		this.screen = screen;
		this.game = game;
	}

	public AbstractGUIElement(JDPoint position, JDDimension dimension) {
		super();
		this.position = position;
		this.dimension = dimension;
	}

	// TODO: remove this constructor
	public AbstractGUIElement(JDPoint position, JDDimension dimension,
			GUIElement parent) {
		super();
		this.position = position;
		this.dimension = dimension;
		this.parent = parent;
		this.screen = parent.getScreen();
	}

	@Override
	public StandardScreen getScreen() {
		return screen;
	}

	@Override
	public boolean hasPoint(JDPoint p) {
		// check whether point p is in rectangle
		return p.getX() >= position.getX()
				&& p.getX() <= position.getX() + dimension.getWidth()
				&& p.getY() >= position.getY()
				&& p.getY() <= position.getY() + dimension.getHeight();
	}

	@Override
	public void handleDoubleTapEvent(MotionEvent touch) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleLongPressEvent(MotionEvent touch) {
		// TODO Auto-generated method stub

	}

	@Override
	public JDPoint getPositionOnScreen() {
		return position;
	}

	@Override
	public JDDimension getDimension() {
		return dimension;
	}

	@Override
	public void handleTouchEvent(TouchEvent touch) {
		// TODO Auto-generated method stub
	}

	@Override
	public void update(float time) {

			// TODO Auto-generated method stub
	}

	@Override
	public void handleScrollEvent(ScrollMotion scrolling) {

	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	protected void drawBorder(Graphics g) {
		GUIUtils.drawDoubleBorder(g, position.getX(), position.getY(), dimension, 20);

	}

	protected void drawBackground(Graphics g) {
		GUIUtils.drawBackground(g,  position.getX(), position.getY(), dimension);
	}
}
