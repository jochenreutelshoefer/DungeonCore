package de.jdungeon.dungeon.level;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.builder.ChestItemBuilder;
import de.jdungeon.dungeon.builder.DefaultDoorSpecification;
import de.jdungeon.dungeon.builder.DungeonBuilderFactory;
import de.jdungeon.dungeon.builder.DungeonGenerationException;
import de.jdungeon.dungeon.builder.Hall;
import de.jdungeon.dungeon.builder.HallBuilder;
import de.jdungeon.dungeon.builder.LocationBuilder;
import de.jdungeon.dungeon.builder.RescueCharacterLocationBuilder;
import de.jdungeon.dungeon.builder.StartLocationBuilder;
import de.jdungeon.dungeon.builder.serialization.DungeonDTO;
import de.jdungeon.dungeon.builder.serialization.ItemDTO;
import de.jdungeon.dungeon.builder.serialization.LevelDTO;
import de.jdungeon.dungeon.builder.serialization.ScrollItemDTO;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.item.DustItem;
import de.jdungeon.item.HealPotion;
import de.jdungeon.item.OxygenPotion;
import de.jdungeon.item.VisibilityCheatBall;
import de.jdungeon.location.HealthFountain;
import de.jdungeon.location.LevelExit;
import de.jdungeon.location.ScoutShrine;
import de.jdungeon.log.Log;
import de.jdungeon.spell.conjuration.FirConjuration;
import de.jdungeon.spell.conjuration.LionessConjuration;

public class LevelRescue extends AbstractLevel {

	public LevelRescue(DungeonBuilderFactory builderFactory) {
		super(builderFactory, 11, 11);
	}

	@Override
	protected void doGenerate() throws DungeonGenerationException {
		/**
		 * we need:
		 * - starting point
		 * - hostage position
		 * - exit position
		 * - blocker monster
		 * - patrol monster
		 * - support stuff
		 *
		 */

		JDPoint start = getRandomPointWestBorderNonCorner(3);

		// the start is in the west and we dice out 2 of the remaining three directions for rescueChar and exit
		List<RouteInstruction.Direction> directionCandidates = new ArrayList<>();
		directionCandidates.add(RouteInstruction.Direction.East);
		directionCandidates.add(RouteInstruction.Direction.North);
		directionCandidates.add(RouteInstruction.Direction.South);


		LocationBuilder exit = new LocationBuilder(LevelExit.class);
		RescueCharacterLocationBuilder rescueChar = new RescueCharacterLocationBuilder(exit);
		LocationBuilder scoutTower = new LocationBuilder(ScoutShrine.class, 1);


		// create exit hall
		int randomExit = (int) (Math.random() * directionCandidates.size());
		RouteInstruction.Direction exitDir = directionCandidates.remove(randomExit);
		JDPoint exitHallPoint = getRandomHallStartPoint(exitDir);
		Hall exitHall = createExitHall(exitHallPoint, exit);

		// create rescue hall
		int randomRescue = (int) (Math.random() * directionCandidates.size());
		RouteInstruction.Direction rescueDir = directionCandidates.remove(randomRescue);
		JDPoint rescueHallPoint = getRandomHallStartPoint(rescueDir);
		Hall rescueHall = createRescueHall(rescueHallPoint, rescueChar);


		StartLocationBuilder startL = new StartLocationBuilder(start, exit.getRoomPosition());

		Collection<ChestItemBuilder> chestItemBuilders = createChestBuilders(3,
				new ItemDTO(OxygenPotion.class),
				new ItemDTO(HealPotion.class, 35),
				new ScrollItemDTO(LionessConjuration.class),
				new ScrollItemDTO(FirConjuration.class),
				new ItemDTO(DustItem.class, 7)
		);

		HallBuilder centerHall = getCenterHall();

		dungeonBuild = createBuilder()
				.setStartingPoint(startL)
				.addLocation(exit)
				.addHall(exitHall)
				.addHall(rescueHall)
				.addLocation(scoutTower)
				//.addLocation(scoutTower2)
				.addLocation(new LocationBuilder(HealthFountain.class))
				//.addLocationsShortestDistanceAtLeastConstraint(startL, exit, 16)
				.addLocationsShortestDistanceAtLeastConstraint(startL, rescueChar, 9)
				.addHall(centerHall.build())
				.setMinAmountOfDoors(140)
				.setMaxDeadEnds(4)
				.addLocations(chestItemBuilders)
				//.addKey(keyBuilder)
				//.addLocationsShortestDistanceAtLeastConstraint(startL, keyBuilder, 6)
				.build();
	}

	private Hall createExitHall(JDPoint exitHallPoint, LocationBuilder exitLocation) {
		int centerHallUpperLeftCornerX = exitHallPoint.getX();
		int centerHallUpperLeftCornerY = exitHallPoint.getY();

		HallBuilder exitHall = getHallDoorSpecification3x3(centerHallUpperLeftCornerX, centerHallUpperLeftCornerY);

		JDPoint exitPoint = new JDPoint(centerHallUpperLeftCornerX + 1, centerHallUpperLeftCornerY + 1);
		exitLocation.setPosition(exitPoint);
		return new Hall(exitHall.build(), exitLocation);
	}

	private HallBuilder getHallDoorSpecification3x3(int centerHallUpperLeftCornerX, int centerHallUpperLeftCornerY) {
		int hallWidth = 3;
		int hallHeight = 3;
		return new HallBuilder(centerHallUpperLeftCornerX, centerHallUpperLeftCornerY, hallWidth, hallHeight, getWidth(), getHeight())
				.createAllInternalDoors()
				.removeWallIfPossible(centerHallUpperLeftCornerX + 1, centerHallUpperLeftCornerY, RouteInstruction.Direction.North)
				.removeWallIfPossible(centerHallUpperLeftCornerX + 1, centerHallUpperLeftCornerY + hallHeight - 1, RouteInstruction.Direction.South)
				.removeWallIfPossible(centerHallUpperLeftCornerX, centerHallUpperLeftCornerY + 1, RouteInstruction.Direction.West)
				.removeWallIfPossible(centerHallUpperLeftCornerX + hallWidth - 1, centerHallUpperLeftCornerY + 1, RouteInstruction.Direction.East);
	}

	private Hall createRescueHall(JDPoint point, RescueCharacterLocationBuilder rescueCharacterLocationBuilder) {
		int centerHallUpperLeftCornerX = point.getX();
		int centerHallUpperLeftCornerY = point.getY();

		HallBuilder rescue = getHallDoorSpecification3x3(centerHallUpperLeftCornerX, centerHallUpperLeftCornerY);
		JDPoint rescueCharPoint = new JDPoint(centerHallUpperLeftCornerX + 1, centerHallUpperLeftCornerY + 1);
		rescueCharacterLocationBuilder.setPosition(rescueCharPoint);
		LocationBuilder scoutTower2 = new LocationBuilder(ScoutShrine.class, 2);
		rescue.addNonPositionedLocation(scoutTower2);
		rescue.addLocation(rescueCharacterLocationBuilder, rescueCharPoint);
		return rescue.build();
	}

	private JDPoint getRandomHallStartPoint(RouteInstruction.Direction dir) {
		if (dir == RouteInstruction.Direction.North) {
			return getRandomPointIn(new JDPoint(1, 0), getWidth() - 4, 1);
		}
		if (dir == RouteInstruction.Direction.East) {
			return getRandomPointIn(new JDPoint(getWidth() - 3, 1), 1, getHeight() - 4);
		}

		if (dir == RouteInstruction.Direction.South) {
			return getRandomPointIn(new JDPoint(1, getHeight() - 3), getWidth() - 4, 1);
		}

		Log.severe("Invalid Direction: " + dir);
		return null;
	}

	private HallBuilder getCenterHall() {

		int centerHallUpperLeftCornerX = 4;
		int centerHallUpperLeftCornerY = 4;
		HallBuilder centerHall = getHallDoorSpecification3x3(centerHallUpperLeftCornerX, centerHallUpperLeftCornerY);
		return centerHall;
	}

	@Override
	public Dungeon getDungeon() {
		Dungeon dungeon = dungeonBuild.getDungeon();
		LevelDTO level = dungeonBuild.getDungeonDTO();
		DungeonDTO dungeonDTO = level.getDungeonDTO();
		JDPoint locationPosition = dungeonDTO.getLocationPosition(RescueCharacterLocationBuilder.class);
		addPatrolSpider(locationPosition.getX()-1, locationPosition.getY() -1, dungeon.getRoom(new JDPoint(locationPosition.getX(), locationPosition.getY()-1)));
		JDPoint exitPosition = getExitPosition();
		addPatrolSpider(exitPosition.getX()-1, exitPosition.getY() -1, dungeon.getRoom(new JDPoint(exitPosition.getX(), exitPosition.getY()-1)));
		dungeon.getRoom(this.getHeroEntryPoint()).addItem(new VisibilityCheatBall());
		return dungeon;
	}

	@Override
	public String icon() {
		return null;
	}

	@Override
	public String getName() {
		return "Rescue1";
	}

	@Override
	public String getDescription() {
		return getName();
	}

	@Override
	public int getRoundScoringBaseValue() {
		return 1000;
	}
}
