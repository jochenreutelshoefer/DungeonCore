package de.jdungeon.androidapp.gui;


public interface ItemWheelBindingSet {

	ItemWheelActivity getActivity(int index);

	void update(float time);

	ItemActivityItemProvider getProvider();
}
