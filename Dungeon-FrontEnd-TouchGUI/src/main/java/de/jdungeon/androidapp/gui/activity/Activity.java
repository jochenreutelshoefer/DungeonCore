package de.jdungeon.androidapp.gui.activity;

import game.RoomInfoEntity;
import gui.Paragraphable;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 25.01.18.
 */
public interface Activity extends Paragraphable {

	Object getObject();

	RoomInfoEntity getTarget();

}
