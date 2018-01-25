package de.jdungeon.androidapp.gui.activity;

import java.util.List;

import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 25.01.18.
 */
public class SingleActivityPresenter extends ActivityPresenter {

	public SingleActivityPresenter(JDPoint position, JDDimension dimension, StandardScreen screen, Game game, ActivityProvider provider, Image itemBg) {
		super(position, dimension, screen, game, provider, itemBg);
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
	public void paint(Graphics g, JDPoint viewportPosition) {
		List<Activity> activities = provider.getActivities();
		if(!activities.isEmpty()) {
			Activity activity = activities.iterator().next();
			drawActivityLarge(g, this.getPositionOnScreen().getX(), this.getPositionOnScreen().getY(), activity);
		}
	}


}
