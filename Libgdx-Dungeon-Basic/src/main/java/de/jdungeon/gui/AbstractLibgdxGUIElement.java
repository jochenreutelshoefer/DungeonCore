package de.jdungeon.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.util.JDDimension;

import de.jdungeon.asset.Assets;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 07.02.20.
 */

public abstract class AbstractLibgdxGUIElement implements LibgdxGUIElement {

	protected JDPoint position;
	protected final JDDimension dimension;
	TextureRegion bgTexture;
	protected TextureAtlas.AtlasRegion window_badge = Assets.instance.getAtlasRegion("window_badge", Assets.instance.getGuiAtlas());

	private static final String DEFAULT_BACKGROUND_NAME = "window_border5";
	private String backgroundName = DEFAULT_BACKGROUND_NAME;

	public AbstractLibgdxGUIElement(JDPoint position, JDDimension dimension) {
		super();
		this.position = position;
		this.dimension = dimension;
		init();
	}

	public AbstractLibgdxGUIElement(JDPoint position, JDDimension dimension, String backgroundName) {
		super();
		this.position = position;
		this.dimension = dimension;
		this.backgroundName = backgroundName;
		init();
	}

	public void init() {
		bgTexture = Assets.instance.getAtlasRegion(backgroundName, Assets.instance.getGuiAtlas());
	}

	@Override
	public boolean handlePanEvent(float x, float y, float dx, float dy) {
		// do nothing usually
		return false;
	}

	@Override
	public boolean isAnimated() {
		return false;
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
	public void update(float time, int round) {
		// TODO Auto-generated method stub
	}

	protected void drawBackground(SpriteBatch batch, int currentX, int currentY) {
		if (bgTexture != null) {
			batch.draw(bgTexture, currentX, currentY, this.getDimension().getWidth(), this.dimension.getHeight());
		}
	}
}

