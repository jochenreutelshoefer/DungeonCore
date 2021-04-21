package de.jdungeon.gui.thumb;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.util.JDDimension;

import de.jdungeon.asset.Assets;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.12.17.
 */
public class LibgdxSubGUIElementAnimated extends LibgdxAnimatedSmartControlElement {

	protected final String image;

	public LibgdxSubGUIElementAnimated(final JDPoint posRelative, final JDDimension dimension, SmartControlPanel smartControl, String image) {
		super(posRelative, dimension, smartControl);
		this.image = image;

		// prepare highlight animation drawables
		for (int i = 0; i < animationShapes.length; i++) {
			final int finalI = i;
			animationShapes[i] = (batch, deltaTime) -> {

				if (image != null) {

					JDPoint parentPosition = parent.getPositionOnScreen();
					JDPoint absolutePosition = new JDPoint(parentPosition.getX() + posRelative.getX(), parentPosition
							.getY() + posRelative
							.getY());
					int scaledWidth = (int) (dimension.getWidth() * buttonAnimationSizeFactors[finalI]);
					int scaledHeight = (int) (dimension.getHeight() * buttonAnimationSizeFactors[finalI]);

					TextureAtlas.AtlasRegion atlasRegion = Assets.instance.getAtlasRegion(image, Assets.instance.getGuiAtlas());
					batch.draw(atlasRegion,
							absolutePosition.getX() - ((scaledWidth - dimension.getWidth()) / 2),
							absolutePosition.getY() - ((scaledHeight - dimension.getHeight()) / 2),
							scaledWidth,
							scaledHeight);
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
	public void paint(SpriteBatch batch, float deltaTime) {
		super.paint(batch, deltaTime);
		if (image != null) {
			TextureAtlas.AtlasRegion atlasRegion = Assets.instance.getAtlasRegion(image, Assets.instance.getGuiAtlas());
			batch.draw(atlasRegion, getX(), getY(), dimension.getWidth(), dimension.getHeight());
		}
	}

}
