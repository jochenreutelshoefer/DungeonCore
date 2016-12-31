package de.jdungeon.implementation;

import java.util.List;

import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import de.jdungeon.game.Input.TouchEvent;

public interface TouchHandler extends OnTouchListener {
	boolean isTouchDown(int pointer);
   
	int getTouchX(int pointer);
   
	int getTouchY(int pointer);
   
	List<TouchEvent> getTouchEvents();

	float getScaleEvent();

	AndroidScrollMotion getScrollEvent();
	
	MotionEvent getDoubleTapEvent();
	
	MotionEvent getLongPressEvent();
}
