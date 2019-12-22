package de.jdungeon.adapter.input;

import java.util.Collections;
import java.util.List;

import de.jdungeon.game.Input;
import de.jdungeon.game.MotionEvent;
import de.jdungeon.game.ScrollMotion;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.12.19.
 */
public class LibgdxInput implements Input {
	@Override
	public boolean isTouchDown(int pointer) {
		return false;
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
		return Collections.emptyList();
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
