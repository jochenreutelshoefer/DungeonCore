package dungeon.util;

import dungeon.Dungeon;
import dungeon.JDPoint;
import figure.DungeonVisibilityMap;
import figure.hero.Hero;
import figure.hero.HeroInfo;
import game.DungeonGame;

public class DungeonManager {

	/**
	 * The hero enters the dungeon at the specified point. Creates a FigureInfo
	 * object with a corresponding VisbilityMap.
	 * 
	 * @param h
	 * @param derDungeon
	 * @return
	 */
	public static HeroInfo enterDungeon(Hero h, Dungeon derDungeon, JDPoint p) {
		DungeonGame dungeonGame = DungeonGame.getInstance();

		dungeonGame.setDungeon(derDungeon);
		h.setActualDungeon(derDungeon);
		h.move(derDungeon.getRoomNr(p.getX(), p.getY()));
		DungeonVisibilityMap heroVisMap = h.getRoomVisibility();
		HeroInfo figureInfo = new HeroInfo(h, heroVisMap);

		return figureInfo;
	}

}
