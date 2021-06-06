package de.jdungeon.dungeon.level;

import java.util.Collection;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Room;
import de.jdungeon.dungeon.builder.ChestItemBuilder;
import de.jdungeon.dungeon.builder.DungeonBuilderFactory;
import de.jdungeon.dungeon.builder.DungeonGenerationException;
import de.jdungeon.dungeon.builder.LocationBuilder;
import de.jdungeon.dungeon.builder.RescueCharacterLocationBuilder;
import de.jdungeon.dungeon.builder.StartLocationBuilder;
import de.jdungeon.dungeon.builder.serialization.ItemDTO;
import de.jdungeon.dungeon.builder.serialization.ScrollItemDTO;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.monster.Spider;
import de.jdungeon.item.DustItem;
import de.jdungeon.item.HealPotion;
import de.jdungeon.item.OxygenPotion;
import de.jdungeon.item.VisibilityCheatBall;
import de.jdungeon.level.generation.SimpleDungeonFiller;
import de.jdungeon.level.stageone.HadrianAI;
import de.jdungeon.location.HealthFountain;
import de.jdungeon.location.LevelExit;
import de.jdungeon.location.ScoutShrine;
import de.jdungeon.spell.conjuration.FirConjuration;
import de.jdungeon.spell.conjuration.LionessConjuration;

public class LevelRescue extends AbstractLevel{

	JDPoint start = new JDPoint(4, 8);

	public LevelRescue(DungeonBuilderFactory builderFactory) {
		super(builderFactory);
	}

	@Override
	protected void doGenerate() throws DungeonGenerationException {
		/**
		 * we need:
		 * - starting point
		 * - hostage position
		 * - exist position
		 * - blocker monster
		 * - patrol monster
		 * - support stuff
		 *
		 */

		JDPoint exitP = new JDPoint(4, 0);
		LocationBuilder exit = new LocationBuilder(LevelExit.class, exitP.getX(), exitP.getY());
		StartLocationBuilder startL = new StartLocationBuilder(start, exitP);
		RescueCharacterLocationBuilder rescueChar = new RescueCharacterLocationBuilder(exit);
		LocationBuilder scoutTower = new LocationBuilder(ScoutShrine.class,1);
		LocationBuilder scoutTower2 = new LocationBuilder(ScoutShrine.class,2);

		Collection<ChestItemBuilder> chestItemBuilders = createChestBuilders(3,
				new ItemDTO(OxygenPotion.class),
				new ItemDTO(HealPotion.class, 35),
				new ScrollItemDTO(LionessConjuration.class),
				new ScrollItemDTO(FirConjuration.class),
				new ItemDTO(DustItem.class, 7)
		);


		dungeonBuild = createBuilder()
				.gridSize(10, 10)
				.setStartingPoint(startL)
				.addLocation(exit)
				.addLocation(scoutTower)
				.addLocation(scoutTower2)
				.addLocation(rescueChar)
				.addLocation(new LocationBuilder(HealthFountain.class))
				.addLocationsShortestDistanceExactlyConstraint(startL, exit, 16)
				.addLocationsShortestDistanceAtLeastConstraint(startL, rescueChar, 9)
				//.addDoorSpecification(centerHall)
				.setMinAmountOfDoors(120)
				.setMaxDeadEnds(4)
				.addLocations(chestItemBuilders)
				//.addKey(keyBuilder)
				//.addLocationsShortestDistanceAtLeastConstraint(startL, keyBuilder, 6)
				.build();
	}

	@Override
	public Dungeon getDungeon() {
		Dungeon dungeon = dungeonBuild.getDungeon();
		JDPoint exitPosition = getExitPosition();
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
