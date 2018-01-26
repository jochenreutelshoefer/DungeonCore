package de.jdungeon.androidapp.gui.activity;

import java.util.List;

import android.util.Log;
import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 25.01.18.
 */
public class SingleActivityPresenter extends ActivityPresenter {

	public SingleActivityPresenter(JDPoint position, JDDimension dimension, StandardScreen screen, Game game, ActivityProvider provider, Image itemBg, int tileSize) {
		super(position, dimension, screen, game, provider, itemBg, tileSize);
	}

	@Override
	public void highlightEntity(Object object) {

	}

	@Override
	public boolean isVisible() {
		ExecutableActivity activity = getActivity();
		return activity!= null && activity.isCurrentlyPossible();
	}

	@Override
	public boolean hasPoint(JDPoint p) {
		return isVisible() && super.hasPoint(p);
	}

	private ExecutableActivity getActivity() {
		List<Activity> activities = provider.getActivities();
		if(!activities.isEmpty()) {
			Activity activity = activities.get(0);
			if(activity instanceof ExecutableActivity) {
				return ((ExecutableActivity)activity);
			}
		}
		Log.w("warning", "activity is no executable activity");
		return null;
	}

	@Override
	public boolean handleTouchEvent(Input.TouchEvent touch) {
		ExecutableActivity activity = getActivity();
		if(activity != null) {
			activity.execute();
			return true;
		}
		return false;
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
			drawActivity(g, this.getPositionOnScreen().getX(), this.getPositionOnScreen().getY(), activity);
		}
		g.drawRect(this.getPositionOnScreen().getX(), this.getPositionOnScreen().getY(), this.dimension.getWidth(), this.getDimension().getHeight(), Colors.BLUE);
	}


}
