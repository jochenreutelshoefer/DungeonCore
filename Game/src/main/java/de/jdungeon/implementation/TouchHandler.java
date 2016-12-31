package de.jdungeon.implementation;

import java.util.List;

import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import de.jdungeon.game.Input.TouchEvent;
import de.jdungeon.util.ScrollMotion;


public interface TouchHandler extends OnTouchListener {
	boolean isTouchDown(int pointer);
   
	int getTouchX(int pointer);
   
	int getTouchY(int pointer);
   
	List<TouchEvent> getTouchEvents();

	float getScaleEvent();

	ScrollMotion getScrollEvent();
	
	MotionEvent getDoubleTapEvent();
	
	MotionEvent getLongPressEvent();
}
