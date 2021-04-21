package de.jdungeon.gui.thumb;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.util.JDDimension;

import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.gui.activity.Activity;
import de.jdungeon.asset.Assets;
import de.jdungeon.gui.LibgdxGUIElement;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 07.02.20.
 */
public class LibgdxActivityControlElement extends LibgdxAnimatedSmartControlElement {

	private final Activity activity;
	private final String skillImage;

	public LibgdxActivityControlElement(JDPoint position, final JDDimension dimension, LibgdxGUIElement parent, Activity activity, final String skillImage) {
		super(position, dimension, parent);
		this.skillImage = skillImage;
		this.activity = activity;

		for (int i = 0; i < buttonAnimationSizeFactors.length; i++) {
			final int finalI = i;
			animationShapes[i] = (batch, deltaTime) -> {
				TextureAtlas.AtlasRegion atlasRegion = Assets.instance.getAtlasRegion(skillImage, Assets.instance.getGuiAtlas());
				JDPoint parentPosition = parent.getPositionOnScreen();
				int scaledWidth = (int) (dimension.getWidth() * buttonAnimationSizeFactors[finalI]);
				int scaledHeight = (int) (dimension.getHeight() * buttonAnimationSizeFactors[finalI]);

				batch.draw(atlasRegion,
						parentPosition.getX() + getPositionOnScreen().getX() - ((scaledWidth - dimension.getWidth()) / 2),
						parentPosition.getY() + getPositionOnScreen().getY() - ((scaledHeight - dimension.getHeight()) / 2),
						scaledWidth,
						scaledHeight);

			};
		}
	}

	@Override
	public boolean isVisible() {
		return activity.isCurrentlyPossible(null).getSituation() == ActionResult.Situation.possible;
	}

	@Override
	public boolean handleClickEvent(int x, int y) {
		super.handleClickEvent(x, y);
		if(activity.isCurrentlyPossible(null).getSituation() == ActionResult.Situation.possible) {
			AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
			activity.plugToController(null);
		}
		return true;
	}


	// TODO: something wrong here - should not overwrite animation render paint call
	@Override
	public void paint(SpriteBatch batch, float deltaTime) {
		super.paint(batch, deltaTime);
		int width = (int) (dimension.getWidth() * 0.8);
		int height = (int)(dimension.getHeight() * 0.8);
		TextureAtlas.AtlasRegion atlasRegion = Assets.instance.getAtlasRegion(skillImage, Assets.instance.getGuiAtlas());
		batch.draw(atlasRegion,  getX() + (int)(dimension.getWidth() * 0.1), getY()+ (int)(dimension.getHeight() * 0.1), width, height);

	}

}
