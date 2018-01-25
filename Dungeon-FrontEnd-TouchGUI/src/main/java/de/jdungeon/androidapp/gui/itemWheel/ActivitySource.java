package de.jdungeon.androidapp.gui.itemWheel;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 25.01.18.
 */
public interface ActivitySource {

	Activity getActivity(int index);

	int getNumberOfObjects();

	void update(float time);

	ActivityProvider getProvider();
}
