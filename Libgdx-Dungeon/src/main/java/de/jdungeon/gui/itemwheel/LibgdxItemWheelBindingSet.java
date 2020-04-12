package de.jdungeon.gui.itemwheel;

import de.jdungeon.gui.activity.Activity;

public interface LibgdxItemWheelBindingSet extends LibgdxActivitySource {


	int getBindingSize();

	Activity getAndClearLastAdded();
}
