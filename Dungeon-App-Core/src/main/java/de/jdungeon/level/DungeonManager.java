package de.jdungeon.level;

import java.util.List;

import de.jdungeon.user.DungeonFactory;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 07.03.16.
 */
public interface DungeonManager {

	/**
	 * Returns the list of dungeon level that can be selected of at the given stage
	 *
	 * @param stage current player stage
	 * @return list of dungeon factories
	 */
	List<DungeonFactory> getDungeonOptions(int stage);

	/**
	 * Returns the number of stages contained in this DungeonManager
	 *
	 * @return number of stages contained in this DungeonManager
	 */
	int getNumberOfStages();

}
