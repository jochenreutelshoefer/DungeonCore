package de.jdungeon.androidapp.gui.itemWheel;

import java.util.List;

import de.jdungeon.game.Image;

public interface ItemWheelActivityProvider {

	List<ItemWheelActivity> getActivities();

	Image getActivityImage(ItemWheelActivity a);

	void activityPressed(ItemWheelActivity infoEntity);
}
