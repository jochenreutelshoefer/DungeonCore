package de.jdungeon.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.asset.Assets;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.ui.AbstractLibgdxGUIElement;
import de.jdungeon.ui.LibgdxGUIElement;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 07.02.20.
 */
public abstract class ImageLibgdxGUIElement extends AbstractLibgdxGUIElement {

	private final String filename;
	private final String backGround = null;
	private int relativeOffsetX = 0;
	private int relativeOffsetY = 0;

	public ImageLibgdxGUIElement(JDPoint position, JDDimension dimension, String im) {
		super(position, dimension);
		this.filename = im;
	}

	@Override
	public void paint(ShapeRenderer shapeRenderer) {
		// do nothing
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void paint(SpriteBatch batch) {
		int x = this.position.getX() + relativeOffsetX;
		int y = this.position.getY() + relativeOffsetY;
		int width = this.dimension.getWidth();
		int height = this.dimension.getHeight();


		// draw background
		TextureAtlas.AtlasRegion backGroundRegion = null;
		if(backGround != null) {
			backGroundRegion = Assets.instance.getAtlasRegion(backGround, Assets.instance.getGuiAtlas());
		}


		if(backGroundRegion != null) {
			batch.draw(backGroundRegion, x, y, width, height);
		}

		// actual image is drawn smaller within the background image
		x = x + width / 8;
		y = y + height / 8;
		width = width - width / 4;
		height = height - height / 4;

		// draw foreground
		TextureAtlas.AtlasRegion imageRegion = Assets.instance.getAtlasRegion(filename, Assets.instance.getGuiAtlas());
		batch.draw(imageRegion,  x, y, width, height);


	}

	public void setRelativeOffsetY(int relativeOffsetY) {
		this.relativeOffsetY = relativeOffsetY;
	}

	public void setRelativeOffsetX(int relativeOffsetX) {
		this.relativeOffsetX = relativeOffsetX;
	}
}
