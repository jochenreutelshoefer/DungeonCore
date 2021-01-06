package de.jdungeon.gui;

import java.util.List;

import de.jdungeon.gui.activity.Activity;
import de.jdungeon.world.PlayerController;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 09.02.20.
 */
public interface LibgdxActivityProvider {

	List<Activity> getActivities();

	String getActivityImage(Activity a);

	void activityPressed(Activity infoEntity);

	PlayerController getPlayerController();


}
