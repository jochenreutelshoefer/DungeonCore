import java.util.*;

import de.jdungeon.ai.SimpleHeroBehavior;
import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.Position;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.builder.DungeonGenerationException;
import de.jdungeon.figure.DungeonVisibilityMap;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.figure.hero.HeroInfo;
import de.jdungeon.figure.hero.HeroUtil;
import de.jdungeon.figure.hero.Profession;
import de.jdungeon.figure.hero.Zodiac;
import de.jdungeonx.DungeonGameLoop;
import de.jdungeon.game.JDEnv;
import de.jdungeon.util.MyResourceBundle;
import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;

import de.jdungeon.level.DefaultDungeonManager;
import de.jdungeon.level.DungeonFactory;
import de.jdungeon.level.DungeonManager;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 08.12.19.
 */
public class DungeonWorldTest  {


	@Test
	public void testDungeonWorld() throws DungeonGenerationException {
		JDEnv.init(new MyResourceBundle() {
			@Override
			public String get(String key) {
				return null;
			}

			@Override
			public String format(String key, String... inserts) {
				return null;
			}
		});
		DungeonManager manager = new DefaultDungeonManager();
		for (int i = 0; i < manager.getNumberOfStages(); i++) {
			List<DungeonFactory> dungeonFactories = manager.getDungeonOptions(i);
			for (DungeonFactory dungeonFactory : dungeonFactories) {
				dungeonFactory.create();
				Dungeon dungeon = dungeonFactory.getDungeon();
				Hero hero = HeroUtil.getBasicHero(Hero.HeroCategory.Warrior.getCode(), "Gisbert2", Zodiac.Aquarius,
						Profession.Lumberjack);

				DungeonWorldTestUpdater updater = new DungeonWorldTestUpdater(dungeon);
				DungeonGameLoop gameLoop = new DungeonGameLoop(dungeon, updater);


				hero.setActualDungeon(dungeon);

				SimpleHeroBehavior control = new SimpleHeroBehavior();
				hero.setControl(control);

				DungeonVisibilityMap heroVisMap = hero.getViwMap();
				HeroInfo heroInfo = new HeroInfo(hero, heroVisMap);
				dungeon.getRoomNr(dungeonFactory.getHeroEntryPoint().getX(), dungeonFactory.getHeroEntryPoint().getY()).figureEnters(hero, 0, -1);
				control.setFigure(heroInfo);
				dungeon.prepare();

				for (int round = 0; round < 500; round++) {
					gameLoop.worldTurn(round);
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
				assertEquals(factory.getName()+": Position of de.jdungeon.figure does not refer to de.jdungeon.figure: "+roomFigure.getPos(), posFigure, roomFigure);
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

			// check that every de.jdungeon.figure can only be located at ONE position
			for (Figure positionFigure : positionFigures) {

				// if it was already in the collection, it was from another de.jdungeon.location
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
