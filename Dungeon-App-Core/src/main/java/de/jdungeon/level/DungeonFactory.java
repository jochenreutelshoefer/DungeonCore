package de.jdungeon.level;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.builder.DungeonGenerationException;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 07.03.16.
 */
public interface DungeonFactory {

	Dungeon createDungeon() throws DungeonGenerationException;

	JDPoint getHeroEntryPoint();

	String icon();

	String getName() ;

	String getDescription();

	int getRoundScoringBaseValue();
}
