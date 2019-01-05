package de.jdungeon.androidapp.gui;

import util.JDDimension;

import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Configuration;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input.TouchEvent;
import de.jdungeon.game.MotionEvent;
import de.jdungeon.game.ScrollMotion;

import dungeon.JDPoint;

public abstract class AbstractGUIElement implements GUIElement {

	protected JDPoint position;
	protected final JDDimension dimension;
	// TODO: remove
	private GUIElement parent;
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

	public AbstractGUIElement(JDPoint position, JDDimension dimension, Game game) {
		super();
		this.position = position;
		this.dimension = dimension;
		this.game = game;
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
	public boolean needsRepaint() {
		return true;
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
	public boolean handleTouchEvent(TouchEvent touch) {
		return false;
	}

	@Override
	public void update(float time) {
			// TODO Auto-generated method stub
	}

	@Override
	public boolean handleScrollEvent(ScrollMotion scrolling) {
		return false;
	}

	@Override
	public Game getGame() {
		return getScreen().getGame();
	}

	public void setGame(Game game) {
		this.game = game;
	}

	protected void drawBackground(Graphics g) {
		drawBackground(g, position.getX(), position.getY());
	}

	protected void drawBackground(Graphics g, int currentX, int currentY) {
		final Configuration.GUIStyle guiStyle = game.getConfiguration().getGUIStyle();
		if(guiStyle == Configuration.GUIStyle.retro) {
			GUIUtils.drawBackgroundRetro(g, currentX, currentY, dimension);
		} else {
			GUIUtils.drawBackgroundSimple(g, currentX, currentY, dimension);
		}
	}


	protected void drawBorder(Graphics g, int currentX, int currentY) {
		final Configuration.GUIStyle guiStyle = game.getConfiguration().getGUIStyle();
		if(guiStyle == Configuration.GUIStyle.retro) {
			GUIUtils.drawDoubleBorderRetro(g, currentX, currentY, dimension, 20);
		} else {
			GUIUtils.drawBorderSimple(g, currentX, currentY, this.dimension);
		}
	}

	protected void drawBorder(Graphics g) {
		drawBorder(g, position.getX(), position.getY());
	}


}
