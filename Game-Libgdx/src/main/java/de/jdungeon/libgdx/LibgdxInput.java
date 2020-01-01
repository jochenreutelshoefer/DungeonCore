package de.jdungeon.libgdx;

import java.util.LinkedList;
import java.util.List;


import de.jdungeon.game.Input;
import de.jdungeon.game.MotionEvent;
import de.jdungeon.game.ScrollMotion;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.12.19.
 */
public class LibgdxInput implements Input {


	private final List<TouchEvent> eventQueue = new LinkedList<>();
	private final List<TouchEvent> transportQueue = new LinkedList<>();

	public void addEvent(TouchEvent event) {
		eventQueue.add(event);
	}

	@Override
	public boolean isTouchDown(int pointer) {
		return pointer == 0;
	}

	@Override
	public int getTouchX(int pointer) {
		return 0;
	}

	@Override
	public int getTouchY(int pointer) {
		return 0;
	}

	@Override
	public List<TouchEvent> getTouchEvents() {
		transportQueue.clear();
		transportQueue.addAll(eventQueue);
		eventQueue.clear();
		return transportQueue;
	}

	@Override
	public float getScaleEvent() {
		return 0;
	}

	@Override
	public ScrollMotion getScrollEvent() {
		return null;
	}

	@Override
	public MotionEvent getDoubleTapEvent() {
		return null;
	}

	@Override
	public MotionEvent getLongPressEvent() {
		return null;
	}
}
