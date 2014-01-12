package de.jdungeon.androidapp.gui;

import game.InfoEntity;

public interface ItemWheelBindingSet<T extends InfoEntity> {

	T getInfoEntity(int index);

	void update(float time);
}
