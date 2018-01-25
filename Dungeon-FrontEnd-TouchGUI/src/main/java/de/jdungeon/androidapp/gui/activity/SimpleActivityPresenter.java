package de.jdungeon.androidapp.gui.activity;

import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 25.01.18.
 */
public class SimpleActivityPresenter extends ActivityPresenter {

	public SimpleActivityPresenter(JDPoint position, JDDimension dimension, StandardScreen screen, Game game, ActivityProvider provider) {
		super(position, dimension, screen, game, provider);
	}

	@Override
	public void highlightEntity(Object object) {

	}

	@Override
	public Object highlightFirst() {
		return null;
	}

	@Override
	protected void centerOnIndex(Activity activity) {

	}

	@Override
	protected void iconTouched(Activity activity) {

	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {

	}
}
