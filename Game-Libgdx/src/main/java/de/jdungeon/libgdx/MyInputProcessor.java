package de.jdungeon.libgdx;

import com.badlogic.gdx.InputProcessor;

import de.jdungeon.game.Input;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.12.19.
 */
public class MyInputProcessor implements InputProcessor {

	private final LibgdxInput input;

	public MyInputProcessor(LibgdxInput input) {
		this.input = input;
	}

	@Override
	public boolean keyDown(int i) {
		return false;
	}

	@Override
	public boolean keyUp(int i) {
		return false;
	}

	@Override
	public boolean keyTyped(char c) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Input.TouchEvent event = new Input.TouchEvent();
		event.x = screenX;
		event.y = screenY;
		event.type = Input.TouchEvent.TOUCH_DOWN;
		event.pointer = pointer;
		input.addEvent(event);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Input.TouchEvent event = new Input.TouchEvent();
		event.x = screenX;
		event.y = screenY;
		event.type = Input.TouchEvent.TOUCH_UP;
		event.pointer = pointer;
		input.addEvent(event);
		return true;
	}

	@Override
	public boolean touchDragged(int i, int i1, int i2) {
		return false;
	}

	@Override
	public boolean mouseMoved(int i, int i1) {
		return false;
	}

	@Override
	public boolean scrolled(int i) {
		return false;
	}
}
