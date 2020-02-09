package de.jdungeon.gui.thumb;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.asset.Assets;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.12.17.
 */
public class LibgdxSubGUIElementAnimated extends LibgdxAnimatedSmartControlElement {

	private final String image;

	public LibgdxSubGUIElementAnimated(final JDPoint posRelative, final JDDimension dimension, SmartControlPanel smartControl, String image) {
		super(posRelative, dimension, smartControl);
		this.image = image;

		// prepare highlight animation drawables
		for (int i = 0; i < animationShapes.length; i++) {
			final int finalI = i;
			animationShapes[i] = new LibgdxDrawable() {
				@Override
				public void paint(ShapeRenderer shapeRenderer) {
					if (image == null) {
						JDPoint parentPosition = parent.getPositionOnScreen();
						JDPoint absolutePosition = new JDPoint(
								parentPosition.getX() + posRelative.getX(),
								parentPosition.getY() + posRelative.getY());
						int scaledWidth = (int) (dimension.getWidth() * buttonAnimationSizes[finalI]);
						int scaledHeight = (int) (dimension.getHeight() * buttonAnimationSizes[finalI]);
						shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
						shapeRenderer.setColor(Color.WHITE);
						shapeRenderer.rect(
								absolutePosition.getX() - ((scaledWidth - dimension.getWidth()) / 2),
								absolutePosition.getY() - ((scaledHeight - dimension.getHeight()) / 2),
								scaledWidth,
								scaledHeight);
					}
				}

				@Override
				public void paint(SpriteBatch batch) {

					if (image != null) {

						JDPoint parentPosition = parent.getPositionOnScreen();
						JDPoint absolutePosition = new JDPoint(parentPosition.getX() + posRelative.getX(), parentPosition
								.getY() + posRelative
								.getY());
						int scaledWidth = (int) (dimension.getWidth() * buttonAnimationSizes[finalI]);
						int scaledHeight = (int) (dimension.getHeight() * buttonAnimationSizes[finalI]);

						TextureAtlas.AtlasRegion atlasRegion = Assets.instance.getAtlasRegion(image, Assets.instance.getGuiAtlas());
						batch.draw(atlasRegion,
								absolutePosition.getX() - ((scaledWidth - dimension.getWidth()) / 2),
								absolutePosition.getY() - ((scaledHeight - dimension.getHeight()) / 2),
								scaledWidth,
								scaledHeight);
					}

				}
			};
		}
	}

	@Override
	public boolean handleClickEvent(int x, int y) {
		return super.handleClickEvent(x, y);
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void paint(SpriteBatch batch) {
		super.paint(batch);
		if (image != null) {
			TextureAtlas.AtlasRegion atlasRegion = Assets.instance.getAtlasRegion(image, Assets.instance.getGuiAtlas());
			batch.draw(atlasRegion, getX(), getY(), dimension.getWidth(), dimension.getHeight());
		}
	}

	@Override
	public void paint(ShapeRenderer renderer) {
		super.paint(renderer);
		if (image == null) {
			renderer.set(ShapeRenderer.ShapeType.Filled);
			renderer.setColor(Color.WHITE);
			renderer.rect(getX(), getY(), dimension.getWidth(), dimension.getHeight());
		}
	}
}
