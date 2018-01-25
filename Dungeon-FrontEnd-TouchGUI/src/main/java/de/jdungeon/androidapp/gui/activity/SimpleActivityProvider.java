package de.jdungeon.androidapp.gui.activity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 25.01.18.
 */
public class SimpleActivityProvider {

	private final List<Activity> activities;

	public SimpleActivityProvider(List<Activity> activities) {
		this.activities = activities;
	}

	public SimpleActivityProvider(Activity activity) {
		activities = new ArrayList<>();
		activities.add(activity);
	}
}
