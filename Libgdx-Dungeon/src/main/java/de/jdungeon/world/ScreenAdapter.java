package de.jdungeon.world;

import de.jdungeon.app.screen.StandardScreen;
import de.jdungeon.game.Game;

/**
 * This is a convenience/compatibility class helping to mix up the old Screen framework with the new one
 * Once the old screen framework is disappeared, this class also may disappear.
 *
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 11.01.20.
 */
@Deprecated
public class ScreenAdapter extends StandardScreen {

	public ScreenAdapter(Game game) {
		super(game);
	}
}
