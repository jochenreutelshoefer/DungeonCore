package de.jdungeon.gui;

import java.util.List;

import de.jdungeon.app.gui.activity.Activity;
import de.jdungeon.game.Image;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 09.02.20.
 */
public interface LibgdxActivityProvider {
	List<Activity> getActivities();

	String getActivityImage(Activity a);

	void activityPressed(Activity infoEntity);

	boolean isCurrentlyPossible(Activity infoEntity);

}
