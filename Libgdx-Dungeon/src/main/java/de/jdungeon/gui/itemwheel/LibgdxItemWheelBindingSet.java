package de.jdungeon.gui.itemwheel;

import de.jdungeon.app.gui.activity.Activity;
import de.jdungeon.app.gui.activity.ActivitySource;

public interface LibgdxItemWheelBindingSet extends LibgdxActivitySource {


	int getBindingSize();

	Activity getAndClearLastAdded();
}
