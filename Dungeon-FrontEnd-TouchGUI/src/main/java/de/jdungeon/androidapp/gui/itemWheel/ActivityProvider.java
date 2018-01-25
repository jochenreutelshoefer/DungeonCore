package de.jdungeon.androidapp.gui.itemWheel;

import java.util.List;

import de.jdungeon.game.Image;

public interface ActivityProvider {

	List<Activity> getActivities();

	Image getActivityImage(Activity a);

	void activityPressed(Activity infoEntity);

}
