package de.jdungeon.dungeon.generate.undo;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 03.08.16.
 */
public interface DungeonChangeAction {

	boolean doAction();

	void undo();
}
