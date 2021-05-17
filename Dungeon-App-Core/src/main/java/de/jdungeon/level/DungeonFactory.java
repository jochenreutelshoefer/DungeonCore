package de.jdungeon.level;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.builder.DungeonGenerationException;
import de.jdungeon.dungeon.builder.serialization.LevelDTO;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 07.03.16.
 */
public interface DungeonFactory {

	void create() throws DungeonGenerationException;

	Dungeon getDungeon();

	LevelDTO getDTO();

	JDPoint getHeroEntryPoint();

	String icon();

	String getName() ;

	String getDescription();

	int getRoundScoringBaseValue();
}
