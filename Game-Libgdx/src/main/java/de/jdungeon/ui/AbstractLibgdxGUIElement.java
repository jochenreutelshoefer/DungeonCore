package de.jdungeon.ui;

import android.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	protected Game game;
	Texture bgTexture;


	public AbstractLibgdxGUIElement(JDPoint position, JDDimension dimension, Game game) {
		super();
		this.position = position;
		this.dimension = dimension;
		this.game = game;
		init();
	}

	public void init() {
		createBackgroundTexture();
	}

	private void createBackgroundTexture() {
		int width = getDimension().getWidth();
		int height = getDimension().getHeight();
		Pixmap bgPixmap = new Pixmap(width, height, Pixmap.Format.RGB888);
		bgPixmap.setColor(Color.WHITE);
		bgPixmap.fill();
		bgPixmap.setColor(com.badlogic.gdx.graphics.Color.WHITE);
		bgPixmap.drawRectangle(0, 0, width, height);
		this.bgTexture = new Texture(bgPixmap);
	}

	@Override
	public boolean handlePanEvent(float x, float y, float dx, float dy) {
		// do nothing usually
		return false;
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


	protected void drawBackground(SpriteBatch batch) {
		drawBackground(batch, position.getX(), position.getY());
	}

	protected void drawBackground(SpriteBatch batch, int currentX, int currentY) {
		batch.draw(bgTexture, currentX, currentY);
	}





}

