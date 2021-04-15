package de.jdungeon;

import com.badlogic.gdx.InputAdapter;
import de.jdungeon.game.AbstractScreen;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 12.01.20.
 */
public class DefaultGuiInputController extends InputAdapter {

	private final AbstractScreen screen;

	public DefaultGuiInputController(AbstractScreen screen) {
		this.screen = screen;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return screen.clicked(screenX, screenY, pointer, button);
	}

	public void update(float deltaTime) {
		//handleControlEvents(deltaTime);
		//cameraHelper.update(deltaTime);
	}

}
