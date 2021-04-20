package de.jdungeon.animation;

import com.badlogic.gdx.utils.Pool;

public class AnimationObjectPool extends Pool<AnimationFrame> {

    public static AnimationObjectPool framePool = new AnimationObjectPool();


    @Override
    protected AnimationFrame newObject() {
        return new AnimationFrame();
    }

    public void free(AnimationFrame frame) {
        super.free(frame);
    }
}
