package de.jdungeon.game;

import java.util.List;



public interface Input {
   
    class TouchEvent {
        public static final int TOUCH_DOWN = 0;
        public static final int TOUCH_UP = 1;
        public static final int TOUCH_DRAGGED = 2;
        public static final int TOUCH_HOLD = 3;

        public int type;
        public int x, y;
        public int pointer;
		public MotionEvent motionEvent;


    }


	boolean isTouchDown(int pointer);

	int getTouchX(int pointer);

	int getTouchY(int pointer);

	List<TouchEvent> getTouchEvents();

	float getScaleEvent();

	ScrollMotion getScrollEvent();

	MotionEvent getDoubleTapEvent();

	MotionEvent getLongPressEvent();
}
