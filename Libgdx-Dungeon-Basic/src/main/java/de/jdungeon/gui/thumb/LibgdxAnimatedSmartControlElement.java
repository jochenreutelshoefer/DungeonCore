package de.jdungeon.gui.thumb;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.util.JDDimension;

import de.jdungeon.gui.LibgdxGUIElement;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 13.01.18.
 */
public abstract class LibgdxAnimatedSmartControlElement extends LibgdxSubGUIElement {

    private long buttonAnimationStart;
    private static final int buttonAnimationStepTime = 80;

    public static float[] buttonAnimationSizeFactors = new float[]{2, 3, 4};
    protected LibgdxDrawable[] animationShapes;

    public LibgdxAnimatedSmartControlElement(JDPoint position, JDDimension dimension, LibgdxGUIElement parent) {
        super(position, dimension, parent);
        animationShapes = new LibgdxDrawable[buttonAnimationSizeFactors.length];
    }

    @Override
    public boolean handleClickEvent(int x, int y) {
        startAnimation();
        return false;
    }

    public void startAnimation() {
        buttonAnimationStart = System.currentTimeMillis();
    }

    @Override
    public void paint(SpriteBatch batch, float deltaTime) {
        long elapsedTime = System.currentTimeMillis() - buttonAnimationStart;
        if (elapsedTime < buttonAnimationSizeFactors.length * buttonAnimationStepTime) {
            int stage = (int) (elapsedTime / buttonAnimationStepTime);
            LibgdxDrawable animationShape = animationShapes[stage];
            if (animationShape != null) {
                animationShape.paint(batch, deltaTime);
            }
        }
    }
}
