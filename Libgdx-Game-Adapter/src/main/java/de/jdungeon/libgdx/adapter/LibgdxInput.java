package de.jdungeon.libgdx.adapter;

import java.util.List;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.View;

import de.jdungeon.game.Input;
import de.jdungeon.game.MotionEvent;

public class LibgdxInput implements Input {

    TouchHandler touchHandler;

    public LibgdxInput(Context context, View view, float scaleX, float scaleY) {
        if(Integer.parseInt(VERSION.SDK) < 5)
            touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
        else
            touchHandler = new MultiTouchHandler(view, scaleX, scaleY);        
    }


    @Override
    public boolean isTouchDown(int pointer) {
        return touchHandler.isTouchDown(pointer);
    }

    @Override
    public int getTouchX(int pointer) {
        return touchHandler.getTouchX(pointer);
    }

    @Override
    public int getTouchY(int pointer) {
        return touchHandler.getTouchY(pointer);
    }


    @Override
    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }

	@Override
	public float getScaleEvent() {
		return touchHandler.getScaleEvent();
	}

	@Override
	public LibgdxScrollMotion getScrollEvent() {
		return touchHandler.getScrollEvent();
	}

	@Override
	public MotionEvent getDoubleTapEvent() {
		android.view.MotionEvent doubleTapEvent = touchHandler.getDoubleTapEvent();
		if(doubleTapEvent != null) {
			return new LibgdxMotionEvent(doubleTapEvent);
		}
		return null;
	}

	@Override
	public MotionEvent getLongPressEvent() {

		android.view.MotionEvent longPressEvent = touchHandler.getLongPressEvent();
		if(longPressEvent != null) {
			return new LibgdxMotionEvent(longPressEvent);
		}
		return null;
	}
   
}