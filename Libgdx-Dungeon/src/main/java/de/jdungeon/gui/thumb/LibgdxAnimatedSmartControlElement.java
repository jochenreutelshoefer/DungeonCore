package de.jdungeon.gui.thumb;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.ui.LibgdxGUIElement;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 13.01.18.
 */
public abstract class LibgdxAnimatedSmartControlElement extends LibgdxSubGUIElement {

	private long buttonAnimationStart;
	private static final int buttonAnimationStepTime = 80;

	public static float[] buttonAnimationSizes = new float[] {2, 3, 4};
	protected LibgdxDrawable[] animationShapes;

	public LibgdxAnimatedSmartControlElement(JDPoint position, JDDimension dimension, LibgdxGUIElement parent) {
		super(position, dimension, parent);
		animationShapes = new LibgdxDrawable[buttonAnimationSizes.length];
	}

	@Override
	public boolean handleClickEvent(int x, int y) {
		buttonAnimationStart = System.currentTimeMillis();
		return false;
	}

	@Override
	public void paint(ShapeRenderer renderer) {
		long elapsedTime = System.currentTimeMillis() - buttonAnimationStart;
		if(elapsedTime < buttonAnimationSizes.length * buttonAnimationStepTime) {
			int stage = (int)(elapsedTime / buttonAnimationStepTime);
			LibgdxDrawable animationShape = animationShapes[stage];
			if(animationShape != null) {
				animationShape.paint(renderer);
			}
		}
	}

	@Override
	public void paint(SpriteBatch batch) {
		long elapsedTime = System.currentTimeMillis() - buttonAnimationStart;
		if(elapsedTime < buttonAnimationSizes.length * buttonAnimationStepTime) {
			int stage = (int)(elapsedTime / buttonAnimationStepTime);
			LibgdxDrawable animationShape = animationShapes[stage];
			if(animationShape != null) {
				animationShape.paint(batch);
			}
		}
	}
}
