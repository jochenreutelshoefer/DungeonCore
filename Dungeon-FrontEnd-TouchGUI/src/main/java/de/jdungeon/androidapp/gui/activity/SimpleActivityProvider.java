package de.jdungeon.androidapp.gui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.jdungeon.game.Image;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 25.01.18.
 */
public class SimpleActivityProvider implements ActivityProvider{

	private final Map<Activity, Image> activities;

	public SimpleActivityProvider(Map<Activity, Image> activities) {
		this.activities = activities;
	}

	public SimpleActivityProvider(Activity activity, Image image) {
		activities = new HashMap<>();
		activities.put(activity, image);
	}

	@Override
	public List<Activity> getActivities() {
		return new ArrayList<>(activities.keySet());
	}

	@Override
	public Image getActivityImage(Activity a) {
		return activities.get(a);
	}

	@Override
	public void activityPressed(Activity infoEntity) {

	}

	@Override
	public boolean isCurrentlyPossible(Activity infoEntity) {
		return false;
	}
}
