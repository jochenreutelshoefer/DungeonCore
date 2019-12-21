package de.jdungeon.app.gui.itemWheel;

import de.jdungeon.app.gui.activity.Activity;
import de.jdungeon.app.gui.activity.ActivitySource;

public interface ItemWheelBindingSet extends ActivitySource {



	int getBindingSize();

	Activity getAndClearLastAdded();
}
