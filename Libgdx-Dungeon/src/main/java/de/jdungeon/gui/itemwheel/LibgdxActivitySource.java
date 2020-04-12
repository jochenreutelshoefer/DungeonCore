package de.jdungeon.gui.itemwheel;

import de.jdungeon.gui.activity.Activity;
import de.jdungeon.gui.LibgdxActivityProvider;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 09.02.20.
 */
public interface LibgdxActivitySource {

	Activity getActivity(int index);

	int getNumberOfObjects();

	void update(float time);

	LibgdxActivityProvider getProvider();
}
