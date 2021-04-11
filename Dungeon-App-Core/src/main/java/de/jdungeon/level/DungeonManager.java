package de.jdungeon.level;

import java.util.List;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 07.03.16.
 */
public interface DungeonManager {

	List<DungeonFactory> getDungeonOptions(int stage);

	int getNumberOfStages();
}
