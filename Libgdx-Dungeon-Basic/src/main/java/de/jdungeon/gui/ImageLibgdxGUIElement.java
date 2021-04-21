package de.jdungeon.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.util.JDDimension;

import de.jdungeon.asset.Assets;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 07.02.20.
 */
public abstract class ImageLibgdxGUIElement extends AbstractLibgdxGUIElement {

	private final String filename;
	protected final String backGround;
	protected int relativeOffsetX = 0;
	protected int relativeOffsetY = 0;

	public ImageLibgdxGUIElement(JDPoint position, JDDimension dimension, String im) {
		this(position, dimension, im, null);
	}

	public ImageLibgdxGUIElement(JDPoint position, JDDimension dimension, String im, String bg) {
		super(position, dimension);
		this.filename = im;
		this.backGround = bg;
	}


	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void paint(SpriteBatch batch, float deltaTime) {
		int x = this.position.getX() + relativeOffsetX;
		int y = this.position.getY() + relativeOffsetY;
		int width = this.dimension.getWidth();
		int height = this.dimension.getHeight();

		drawBackground(batch, x, y, width, height);

		// actual image is drawn smaller within the background image
		x = x + width / 8;
		y = y + height / 8;
		width = width - width / 4;
		height = height - height / 4;

		// draw foreground
		TextureAtlas.AtlasRegion imageRegion = Assets.instance.getAtlasRegion(filename, Assets.instance.getGuiAtlas());
		if (imageRegion != null) {
			batch.draw(imageRegion, x, y, width, height);
		}
	}

	private void drawBackground(SpriteBatch batch, int x, int y, int width, int height) {
		// draw background
		TextureAtlas.AtlasRegion backGroundRegion = null;
		if (backGround != null) {
			backGroundRegion = Assets.instance.getAtlasRegion(getBackgroundImage(), Assets.instance.getGuiAtlas());
		}

		if (backGroundRegion != null) {
			batch.draw(backGroundRegion, x, y, width, height);
		}
	}

	protected String getBackgroundImage() {
		return backGround;
	}
}
