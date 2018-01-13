package de.jdungeon.androidapp.gui.smartcontrol;

import android.util.Log;
import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.androidapp.gui.GUIElement;
import de.jdungeon.androidapp.gui.SubGUIElement;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 13.01.18.
 */
public abstract class AnimatedSmartControlElement extends SubGUIElement {

	private long buttonAnimationStart;
	private final int buttonAnimationStage = 0;
	private static final int buttonAnimationStepTime = 200;

	public static float[] buttonAnimationSizes = new float[] {4, 5, 6};
	protected Drawable[] animationShapes;

	public AnimatedSmartControlElement(JDPoint position, JDDimension dimension, GUIElement parent) {
		super(position, dimension, parent);
		animationShapes = new Drawable[buttonAnimationSizes.length];
	}

	@Override
	public boolean handleTouchEvent(Input.TouchEvent touch) {
		buttonAnimationStart = System.currentTimeMillis();
		return false;
	}


	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		long elapsedTime = System.currentTimeMillis() - buttonAnimationStart;
		if(elapsedTime < buttonAnimationSizes.length * buttonAnimationStepTime) {
			int stage = (int)(elapsedTime / buttonAnimationStepTime);
			// TODO: for some reason paint here is always only called for stage 0, why?
			Drawable animationShape = animationShapes[stage];
			if(animationShape != null) {
				animationShape.paint(g, viewportPosition);
			}
		}
	}
}
