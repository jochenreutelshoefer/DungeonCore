package de.jdungeon.dungeon.builder;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 07.03.16.
 */
public interface DungeonFactory {

	enum Mode {
		/**
		Generate new level on the fly
		 */
		Generate,
		/**
		Read pre-generated level from file system
		 */
		Read;
	}

	void create() throws DungeonGenerationException;

	Dungeon getDungeon();

	JDPoint getHeroEntryPoint();

	String icon();

	String getName();

	String getDescription();

	int getRoundScoringBaseValue();
}
