package de.jdungeon.ui;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.game.Game;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 07.02.20.
 */

public abstract class AbstractLibgdxGUIElement implements LibgdxGUIElement {

	protected JDPoint position;
	protected final JDDimension dimension;
	// TODO: remove
	private LibgdxGUIElement parent;
	protected Game game;


	public AbstractLibgdxGUIElement(JDPoint position, JDDimension dimension, Game game) {
		super();
		this.position = position;
		this.dimension = dimension;
		this.game = game;
	}

	public AbstractLibgdxGUIElement(JDPoint position, JDDimension dimension, Screen screen, Game game) {
		super();
		this.position = position;
		this.dimension = dimension;
		this.game = game;
	}

	// TODO: remove this constructor
	public AbstractLibgdxGUIElement(JDPoint position, JDDimension dimension,
									LibgdxGUIElement parent) {
		super();
		this.position = position;
		this.dimension = dimension;
		this.parent = parent;
	}

	@Override
	public void paint(ShapeRenderer shapeRenderer) {
		// do nothing
	}

	@Override
	public boolean needsRepaint() {
		return true;
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
	public JDPoint getPositionOnScreen() {
		return position;
	}

	@Override
	public JDDimension getDimension() {
		return dimension;
	}

	@Override
	public void update(float time) {
		// TODO Auto-generated method stub
	}

	@Override
	public Game getGame() {
		return this.game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	/*
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
	*/


}

