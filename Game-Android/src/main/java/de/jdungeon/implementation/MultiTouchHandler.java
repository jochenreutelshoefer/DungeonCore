package de.jdungeon.implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import de.jdungeon.game.Input.TouchEvent;
import de.jdungeon.util.Pool;
import de.jdungeon.util.Pool.PoolObjectFactory;
import de.jdungeon.game.ScrollMotion;

public class MultiTouchHandler implements TouchHandler, OnGestureListener,
		GestureDetector.OnDoubleTapListener {
    private static final int MAX_TOUCHPOINTS = 10;
   
    boolean[] isTouched = new boolean[MAX_TOUCHPOINTS];
    int[] touchX = new int[MAX_TOUCHPOINTS];
    int[] touchY = new int[MAX_TOUCHPOINTS];
    int[] id = new int[MAX_TOUCHPOINTS];
    Pool<TouchEvent> touchEventPool;
    //List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
    List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
    float scaleX;
    float scaleY;
	float scaleFactor;
	MotionEvent doubleTap = null;
	MotionEvent longPress = null;

	private ScaleGestureDetector scaleGestureDetector;

	private AndroidScrollMotion scrollDist;

	private GestureDetectorCompat detectorCompat;

    public MultiTouchHandler(View view, float scaleX, float scaleY) {
        PoolObjectFactory<TouchEvent> factory = new PoolObjectFactory<TouchEvent>() {
            @Override
            public TouchEvent createObject() {
                return new TouchEvent();
            }
        };
        touchEventPool = new Pool<TouchEvent>(factory, 100);
        view.setOnTouchListener(this);

		detectorCompat = new GestureDetectorCompat(view.getContext(), this);
		detectorCompat.setOnDoubleTapListener(this);

		scaleGestureDetector = new ScaleGestureDetector(view.getContext(),
				new ScaleListener(this));
		


        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

	class ScaleListener extends
			ScaleGestureDetector.SimpleOnScaleGestureListener {

		MultiTouchHandler handler;

		public ScaleListener(MultiTouchHandler handler) {
			this.handler = handler;
		}


		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			float scaleFactor = detector.getScaleFactor();
			handler.setScaleFactor(scaleFactor);
			return true;
		}
	}

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        synchronized (this) {
			this.scaleGestureDetector.onTouchEvent(event);
			this.detectorCompat.onTouchEvent(event);
            int action = event.getAction() & MotionEvent.ACTION_MASK;
            int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT;
            int pointerCount = event.getPointerCount();
            TouchEvent touchEvent;
			for (int i = 0; i < MAX_TOUCHPOINTS; i++) {
                if (i >= pointerCount) {
                    isTouched[i] = false;
                    id[i] = -1;
                    continue;
                }
                int pointerId = event.getPointerId(i);
                if (event.getAction() != MotionEvent.ACTION_MOVE && i != pointerIndex) {
                    // if it's an up/down/cancel/out event, mask the id to see if we should process it for this touch
                    // point
                    continue;
                }
                switch (action) {
					//case MotionEvent.ACTION_POINTER_UP:
					//case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_DOWN:
					case MotionEvent.ACTION_POINTER_DOWN:
					touchEvent = touchEventPool.newObject();
					touchEvent.motionEvent = new AndroidMotionEvent(event);
					touchEvent.type = TouchEvent.TOUCH_DOWN;
					touchEvent.pointer = pointerId;
					touchEvent.x = touchX[i] = (int) (event.getX(i) * scaleX);
					touchEvent.y = touchY[i] = (int) (event.getY(i) * scaleY);
					isTouched[i] = true;
					id[i] = pointerId;
					touchEventsBuffer.add(touchEvent);
					break;
                case MotionEvent.ACTION_CANCEL:
                    touchEvent = touchEventPool.newObject();
					touchEvent.motionEvent = new AndroidMotionEvent(event);
                    touchEvent.type = TouchEvent.TOUCH_UP;
                    touchEvent.pointer = pointerId;
                    touchEvent.x = touchX[i] = (int) (event.getX(i) * scaleX);
                    touchEvent.y = touchY[i] = (int) (event.getY(i) * scaleY);
                    isTouched[i] = false;
                    id[i] = -1;
                    touchEventsBuffer.add(touchEvent);
                    break;

                case MotionEvent.ACTION_MOVE:
                    touchEvent = touchEventPool.newObject();
					touchEvent.motionEvent = new AndroidMotionEvent(event);
                    touchEvent.type = TouchEvent.TOUCH_DRAGGED;
                    touchEvent.pointer = pointerId;
                    touchEvent.x = touchX[i] = (int) (event.getX(i) * scaleX);
                    touchEvent.y = touchY[i] = (int) (event.getY(i) * scaleY);
                    isTouched[i] = true;
                    id[i] = pointerId;
                    touchEventsBuffer.add(touchEvent);
                    break;
                }
            }
            return true;
        }
    }

	public void setScaleFactor(float scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	public void setDoubleTap(MotionEvent event) {

	}

	public void setScrolling(MotionEvent e1, float x, float y) {
		this.scrollDist = new AndroidScrollMotion(e1, new ScrollMotion.FloatDimension(x, y));
	}

	@Override
    public boolean isTouchDown(int pointer) {
        synchronized (this) {
            int index = getIndex(pointer);
            if (index < 0 || index >= MAX_TOUCHPOINTS)
                return false;
            else
                return isTouched[index];
        }
    }

    @Override
    public int getTouchX(int pointer) {
        synchronized (this) {
            int index = getIndex(pointer);
            if (index < 0 || index >= MAX_TOUCHPOINTS)
                return 0;
            else
                return touchX[index];
        }
    }

    @Override
    public int getTouchY(int pointer) {
        synchronized (this) {
            int index = getIndex(pointer);
            if (index < 0 || index >= MAX_TOUCHPOINTS)
                return 0;
            else
                return touchY[index];
        }
    }

	private List<TouchEvent> touchEventsResult = new ArrayList<>();

    @Override
    public List<TouchEvent> getTouchEvents() {
        synchronized (this) {
            int len = touchEventsBuffer.size();
            if(len == 0) {
				return Collections.emptyList();
			}
			// recycle objects for further use (necessary ?)
			for (int i = 0; i < len; i++) {
				touchEventPool.free(touchEventsBuffer.get(i));
			}
			// clear result container
			touchEventsResult.clear();
			// fill result container
            touchEventsResult.addAll(touchEventsBuffer);
			// clear buffer
            touchEventsBuffer.clear();
			// return result container
            return touchEventsResult;
        }
    }
   
    // returns the index for a given pointerId or -1 if no index.
    private int getIndex(int pointerId) {
        for (int i = 0; i < MAX_TOUCHPOINTS; i++) {
            if (id[i] == pointerId) {
                return i;
            }
        }
        return -1;
    }

	@Override
	public float getScaleEvent() {
		synchronized (this) {
			float value = this.scaleFactor;
			this.scaleFactor = 1;
			return value;
		}
	}

	@Override
	public MotionEvent getDoubleTapEvent() {
		synchronized (this) {
			MotionEvent d = this.doubleTap;
			this.doubleTap = null;
			return d;
		}
	}

	@Override
	public MotionEvent getLongPressEvent() {
		synchronized (this) {
			MotionEvent d = this.longPress;
			this.longPress = null;
			return d;
		}
	}



	@Override
	public AndroidScrollMotion getScrollEvent() {
		synchronized (this) {
			AndroidScrollMotion d = this.scrollDist;
			this.scrollDist = null;
			return d;
		}
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		setScrolling(e1, distanceX, distanceY);
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		this.longPress = e;

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		this.doubleTap = e;
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		this.doubleTap = e;
		return true;
	}
}
