package de.jdungeon.gui.thumb;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.app.gui.activity.ExecutableActivity;
import de.jdungeon.asset.Assets;
import de.jdungeon.ui.LibgdxGUIElement;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 07.02.20.
 */
public class LibgdxActivityControlElement  extends LibgdxAnimatedSmartControlElement {

	private final ExecutableActivity activity;
	private final String skillImage;

	public LibgdxActivityControlElement(JDPoint position, final JDDimension dimension, LibgdxGUIElement parent,  ExecutableActivity activity, final String skillImage) {
		super(position, dimension, parent);
		this.skillImage = skillImage;
		this.activity = activity;

		for (int i = 0; i < buttonAnimationSizes.length; i++) {
			final int finalI = i;
			animationShapes[i] = new LibgdxDrawable() {
				@Override
				public void paint(ShapeRenderer shapeRenderer) {
					int sizeX = (int) (dimension.getWidth() * buttonAnimationSizes[finalI]);
					int sizeY = (int) (dimension.getHeight() * buttonAnimationSizes[finalI]);
					int diffSizeX = sizeX - getDimension().getWidth();
					int diffSizeY = sizeY - getDimension().getHeight();
					shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
					shapeRenderer.setColor(Color.WHITE);
					shapeRenderer.rect(getX() - diffSizeX / 2, getY() - diffSizeY / 2, sizeX, sizeY);
				}

				@Override
				public void paint(SpriteBatch batch) {
					int sizeX = (int) (dimension.getWidth() * buttonAnimationSizes[finalI]);
					int sizeY = (int) (dimension.getHeight() * buttonAnimationSizes[finalI]);
					int diffSizeX = sizeX - getDimension().getWidth();
					int diffSizeY = sizeY - getDimension().getHeight();
					TextureAtlas.AtlasRegion atlasRegion = Assets.instance.getAtlasRegion(skillImage, Assets.instance.getGuiAtlas());
					batch.draw(atlasRegion, getX() - diffSizeX / 2, getY() - diffSizeY / 2, sizeX, sizeY );

				}

			};
		}
	}

	@Override
	public boolean isVisible() {
		return activity.isCurrentlyPossible();
	}

	@Override
	public boolean handleClickEvent(int x, int y) {
		super.handleClickEvent(x, y);
		if(activity.isCurrentlyPossible()) {
			activity.execute();
		}
		return true;
	}


	// TODO: something wrong here - should not overwrite animation render paint call
	@Override
	public void paint(SpriteBatch batch) {
		int width = (int) (dimension.getWidth() * 0.8);
		int height = (int)(dimension.getHeight() * 0.8);
		TextureAtlas.AtlasRegion atlasRegion = Assets.instance.getAtlasRegion(skillImage, Assets.instance.getGuiAtlas());
		batch.draw(atlasRegion,  getX() + (int)(dimension.getWidth() * 0.1), getY()+ (int)(dimension.getHeight() * 0.1), width, height);

	}

}
