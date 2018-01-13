package de.jdungeon.androidapp.gui.itemWheel;


public interface ItemWheelBindingSet {

	ItemWheelActivity getActivity(int index);

	void update(float time);

	ItemWheelActivityProvider getProvider();

	int getSize();

	ItemWheelActivity getAndClearLastAdded();
}
