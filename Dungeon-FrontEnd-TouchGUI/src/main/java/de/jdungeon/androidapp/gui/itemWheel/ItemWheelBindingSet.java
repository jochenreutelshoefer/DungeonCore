package de.jdungeon.androidapp.gui.itemWheel;

import de.jdungeon.androidapp.gui.activity.Activity;
import de.jdungeon.androidapp.gui.activity.ActivitySource;

public interface ItemWheelBindingSet extends ActivitySource {



	int getBindingSize();

	Activity getAndClearLastAdded();
}
