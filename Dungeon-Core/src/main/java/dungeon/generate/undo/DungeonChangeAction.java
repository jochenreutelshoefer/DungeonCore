package dungeon.generate.undo;

import dungeon.Dungeon;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 03.08.16.
 */
public interface DungeonChangeAction {

	void doAction();

	void undo();
}
