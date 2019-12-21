package de.jdungeon.app.gui.activity;

import java.util.List;

import de.jdungeon.game.Image;

public interface ActivityProvider {

	List<Activity> getActivities();

	Image getActivityImage(Activity a);

	void activityPressed(Activity infoEntity);

	boolean isCurrentlyPossible(Activity infoEntity);

}
