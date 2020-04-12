package de.jdungeon.gui;

import java.util.List;

import de.jdungeon.gui.activity.Activity;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 09.02.20.
 */
public interface LibgdxActivityProvider {
	List<Activity> getActivities();

	String getActivityImage(Activity a);

	void activityPressed(Activity infoEntity);


}
