package de.jdungeon.dungeon.level;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import com.sun.tools.javac.util.List;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.builder.AbstractASPDungeonFactory;
import de.jdungeon.dungeon.builder.ChestItemBuilder;
import de.jdungeon.dungeon.builder.DefaultDoorSpecification;
import de.jdungeon.dungeon.builder.DoorMarker;
import de.jdungeon.dungeon.builder.DungeonBuilderASP;
import de.jdungeon.dungeon.builder.DungeonGenerationException;
import de.jdungeon.dungeon.builder.DungeonResult;
import de.jdungeon.dungeon.builder.HallBuilder;
import de.jdungeon.dungeon.builder.KeyBuilder;
import de.jdungeon.dungeon.builder.LocationBuilder;
import de.jdungeon.dungeon.builder.StartLocationBuilder;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.monster.Spider;
import de.jdungeon.item.DustItem;
import de.jdungeon.item.HealPotion;
import de.jdungeon.item.Item;
import de.jdungeon.item.Items;
import de.jdungeon.item.OxygenPotion;
import de.jdungeon.item.map.AncientMapFragmentUtils;
import de.jdungeon.item.paper.ScrollMagic;
import de.jdungeon.level.generation.SimpleDungeonFiller;
import de.jdungeon.level.stageone.HadrianAI;
import de.jdungeon.location.HealthFountain;
import de.jdungeon.location.LevelExit;
import de.jdungeon.location.RevealMapShrine;
import de.jdungeon.location.ScoutShrine;
import de.jdungeon.location.defender.DefenderLocation;
import de.jdungeon.spell.conjuration.FirConjuration;
import de.jdungeon.spell.conjuration.LionessConjuration;

public class LevelSome extends AbstractASPDungeonFactory {

	private DungeonResult dungeonBuild;

	@Override
	public Dungeon createDungeon() throws DungeonGenerationException {
		JDPoint start = new JDPoint(4, 7);
		int hallUpperLeftCornerX = start.getX() - 1;
		int hallLowerLeftCornerY = start.getY() - 4;
		int hallWidth = 3;
		int hallHeight = 3;

		java.util.List<Item> gimmickPool = new ArrayList<>();
		gimmickPool.add(new OxygenPotion());
		gimmickPool.add(new HealPotion(35));
		gimmickPool.add(new ScrollMagic(new LionessConjuration(1)));
		gimmickPool.add(new ScrollMagic(new FirConjuration(1)));
		gimmickPool.add(new DustItem(7));

		Collection<Item> selectedGimmicks = Items.selectRandomN(gimmickPool, 3);
		Collection<ChestItemBuilder> chestItemBuilders = selectedGimmicks.stream()
				.map(item -> new ChestItemBuilder(item))
				.collect(Collectors.toList());

		DoorMarker doorHallNorth = DoorMarker.create(hallUpperLeftCornerX + 1, hallLowerLeftCornerY, RouteInstruction.Direction.North);
		DoorMarker doorHallSouth = DoorMarker.create(hallUpperLeftCornerX + 1, hallLowerLeftCornerY + hallHeight - 1, RouteInstruction.Direction.South);
		DefaultDoorSpecification centerHall = new HallBuilder(hallUpperLeftCornerX, hallLowerLeftCornerY, hallWidth, hallHeight)
				.createAllInternalDoors()
				// we need entrance and exit for our hall
				.removeWall(doorHallNorth)
				.removeWall(doorHallSouth)
				.build();

		centerHall.addDoor(doorHallNorth);

		List<DoorMarker> potentialOpenDoors = List.of(
				DoorMarker.create(hallUpperLeftCornerX, hallLowerLeftCornerY, RouteInstruction.Direction.North),
				DoorMarker.create(hallUpperLeftCornerX, hallLowerLeftCornerY, RouteInstruction.Direction.West),
				DoorMarker.create(hallUpperLeftCornerX, hallLowerLeftCornerY + 1, RouteInstruction.Direction.West),
				DoorMarker.create(hallUpperLeftCornerX + 2, hallLowerLeftCornerY, RouteInstruction.Direction.North),
				DoorMarker.create(hallUpperLeftCornerX + 2, hallLowerLeftCornerY, RouteInstruction.Direction.East),
				DoorMarker.create(hallUpperLeftCornerX + 2, hallLowerLeftCornerY + 1, RouteInstruction.Direction.East)
		);
		java.util.List<DoorMarker> potentialOpenDoorsBag = new ArrayList<DoorMarker>(potentialOpenDoors);
		int indexDoor1 = (int) (Math.random() * potentialOpenDoorsBag.size());
		DoorMarker openDoor1 = potentialOpenDoorsBag.remove(indexDoor1);
		centerHall.removeWall(openDoor1);
		centerHall.addDoor(openDoor1);

		int indexDoor2 = (int) (Math.random() * potentialOpenDoorsBag.size());
		DoorMarker openDoor2 = potentialOpenDoorsBag.remove(indexDoor2);
		centerHall.removeWall(openDoor2);
		centerHall.addDoor(openDoor2);

		Collection<JDPoint> possibleKeyPosition = List.of(
				new JDPoint(hallUpperLeftCornerX - 2, hallLowerLeftCornerY),
				new JDPoint(hallUpperLeftCornerX - 2, hallLowerLeftCornerY - 1),
				new JDPoint(hallUpperLeftCornerX - 2, hallLowerLeftCornerY + 1),
				new JDPoint(hallUpperLeftCornerX - 1, hallLowerLeftCornerY),
				new JDPoint(hallUpperLeftCornerX - 1, hallLowerLeftCornerY - 1),
				new JDPoint(hallUpperLeftCornerX - 1, hallLowerLeftCornerY + 1),
				new JDPoint(hallUpperLeftCornerX, hallLowerLeftCornerY - 1)
		);

		//JDPoint keyPosition = possibleKeyPosition.get((int) (Math.random() * possibleKeyPosition.size()));
		JDPoint exitP = new JDPoint(4, 0);
		LocationBuilder exit = new LocationBuilder(LevelExit.class, exitP.getX(), exitP.getY());
		StartLocationBuilder startL = new StartLocationBuilder(start, exitP);
		LocationBuilder defender = new LocationBuilder(DefenderLocation.class, start.getX(), start.getY() - 1);
		LocationBuilder scoutTower = new LocationBuilder(ScoutShrine.class, hallUpperLeftCornerX + 1, hallLowerLeftCornerY + 1);
		KeyBuilder keyBuilder = new KeyBuilder("Messing")
				.setLockDoor(DoorMarker.create(hallUpperLeftCornerX + 1, hallLowerLeftCornerY, RouteInstruction.Direction.North))
				.addNonReachableLocation(exit)
				.addReachableLocation(scoutTower)
				//.addReachableLocations(chestItemBuilders)
				;
		dungeonBuild = new DungeonBuilderASP()
				.gridSize(10, 10)
				.setStartingPoint(startL)
				.addLocation(exit)
				.addLocation(scoutTower)
				.addLocation(defender)
				.addLocation(new LocationBuilder(HealthFountain.class))
				.addLocationsShortestDistanceExactlyConstraint(startL, exit, 7)
				.addLocationsShortestDistanceExactlyConstraint(exit, scoutTower, 4)
				.addDoorSpecification(centerHall)
				.setMaxAmountOfDoors(42)
				.setMinAmountOfDoors(38)
				.setMaxDeadEnds(4)
				.addLocations(chestItemBuilders)
				.addKey(keyBuilder)
				.addLocationsShortestDistanceAtLeastConstraint(startL, keyBuilder, 6)
				.build();

		Dungeon dungeon = dungeonBuild.getDungeon();
		Room wolfRoom = dungeon.getRoom(hallUpperLeftCornerX + 1, hallLowerLeftCornerY);
		HadrianAI ai = new HadrianAI(hallUpperLeftCornerX, hallLowerLeftCornerY);
		Spider hadrian = new Spider(14000, ai, "Hadrian");
		wolfRoom.figureEnters(hadrian, RouteInstruction.Direction.North.getValue(), -1);
		ai.setFigure(FigureInfo.makeFigureInfo(hadrian, hadrian.getViwMap()));
		SimpleDungeonFiller.setAllFound(hadrian.getViwMap());

		return dungeon;
	}

	@Override
	public JDPoint getHeroEntryPoint() {
		return dungeonBuild.getStartPosition();
	}

	@Override
	public String icon() {
		return null;
	}

	@Override
	public String getName() {
		return "some level";
	}

	@Override
	public String getDescription() {
		return getName();
	}

	@Override
	public int getRoundScoringBaseValue() {
		return 100;
	}
}
