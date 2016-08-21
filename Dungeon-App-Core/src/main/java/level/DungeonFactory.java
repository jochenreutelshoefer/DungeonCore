package level;

import dungeon.Dungeon;
import dungeon.JDPoint;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 07.03.16.
 */
public interface DungeonFactory {

	Dungeon createDungeon();

	JDPoint getHeroEntryPoint();

	String icon();

	String getName() ;

	String getDescription();
}
