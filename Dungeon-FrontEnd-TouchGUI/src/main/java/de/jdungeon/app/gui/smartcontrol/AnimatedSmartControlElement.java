package de.jdungeon.app.gui.smartcontrol;

import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.app.gui.GUIElement;
import de.jdungeon.app.gui.SubGUIElement;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 13.01.18.
 */
public abstract class AnimatedSmartControlElement extends SubGUIElement {

	private long buttonAnimationStart;
	private final int buttonAnimationStage = 0;
	private static final int buttonAnimationStepTime = 80;

	public static float[] buttonAnimationSizes = new float[] {2, 3, 4};
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

	public void startAnimation() {
		buttonAnimationStart = System.currentTimeMillis();
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		super.paint(g, viewportPosition);
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
