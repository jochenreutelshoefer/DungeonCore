import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import ai.SimpleHeroBehavior;
import dungeon.Dungeon;
import dungeon.Position;
import dungeon.Room;
import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.hero.Hero;
import figure.hero.HeroInfo;
import figure.hero.HeroUtil;
import figure.hero.Profession;
import figure.hero.Zodiac;
import game.DungeonGameLoop;
import game.JDEnv;
import junit.framework.TestCase;
import level.DefaultDungeonManager;
import level.DungeonFactory;
import level.DungeonManager;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 08.12.19.
 */
public class DungeonWorldTest extends TestCase {

	public void testDungeonWorld() {
		JDEnv.init(ResourceBundle.getBundle("texts", Locale.GERMAN));
		DungeonManager manager = new DefaultDungeonManager();
		for (int i = 0; i < manager.getNumberOfStages(); i++) {
			List<DungeonFactory> dungeonFactories = manager.getDungeonOptions(i);
			for (DungeonFactory dungeonFactory : dungeonFactories) {
				Dungeon dungeon = dungeonFactory.createDungeon();
				Hero hero = HeroUtil.getBasicHero(Hero.HeroCategory.Warrior.getCode(), "Gisbert", Zodiac.Aquarius,
						Profession.Lumberjack);

				DungeonGameLoop dungeonGame = new DungeonGameLoop(dungeon);


				hero.setActualDungeon(dungeon);

				SimpleHeroBehavior control = new SimpleHeroBehavior();
				hero.setControl(control);

				DungeonVisibilityMap heroVisMap = hero.getRoomVisibility();
				HeroInfo heroInfo = new HeroInfo(hero, heroVisMap);
				dungeon.getRoomNr(dungeonFactory.getHeroEntryPoint().getX(), dungeonFactory.getHeroEntryPoint().getY()).figureEnters(hero, 0, -1);
				control.setFigure(heroInfo);
				dungeon.prepare();

				for (int round = 0; round < 500; round++) {
					dungeonGame.worldTurn(round);
					checkConsistentState(dungeon, dungeonFactory);
				}
			}
		}
	}

	private void checkConsistentState(Dungeon dungeon, DungeonFactory factory) {
		Set<Position> allPositions = new HashSet<>();
		Set<Figure> allFigures = new HashSet<>();
		Collection<Room> allRooms = dungeon.getAllRooms();
		for (Room room : allRooms) {
			List<Figure> roomFigures = room.getRoomFigures();
			Position[] positions = room.getPositions();


			// check that positions, room and figures are correctly associated
			for (Figure roomFigure : roomFigures) {
				Figure posFigure = roomFigure.getPos().getFigure();
				assertEquals(factory.getName()+": Position of figure does not refer to figure: "+roomFigure.getPos(), posFigure, roomFigure);
				assertEquals(roomFigure.getPos().getRoom(), roomFigure.getRoom());
			}


			//check that a position can only be in one room
			for (Position pos : positions) {
				assertFalse(allPositions.contains(pos));
			}
			allPositions.addAll(allPositions);

			// check that position figures and room figures match
			List<Figure> positionFigures = collect(positions);
			assertEquals(positionFigures.size(), roomFigures.size());
			assertEquals(new HashSet<>(roomFigures), new HashSet<>(positionFigures));

			// check that every figure can only be located at ONE position
			for (Figure positionFigure : positionFigures) {

				// if it was already in the collection, it was from another location
				assertFalse(allFigures.contains(positionFigure));
			}
			allFigures.addAll(positionFigures);
		}


	}

	private List<Figure> collect(Position[] positions) {
		List<Figure> positionFigures = new ArrayList<>();
		for (Position position : positions) {
			Figure figure = position.getFigure();
			if(figure != null) {
				positionFigures.add(figure);
			}
		}
		return positionFigures;
	}
}
