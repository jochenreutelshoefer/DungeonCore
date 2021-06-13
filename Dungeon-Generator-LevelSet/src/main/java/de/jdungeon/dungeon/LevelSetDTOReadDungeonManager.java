package de.jdungeon.dungeon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.jdungeon.dungeon.builder.ReadDTODungeonBuilderFactory;
import de.jdungeon.dungeon.level.LevelSome;
import de.jdungeon.dungeon.level.LevelX;
import de.jdungeon.dungeon.level.LevelY;
import de.jdungeon.dungeon.level.Level16x16;
import de.jdungeon.level.AbstractDungeonManager;
import de.jdungeon.user.DungeonFactory;

public class LevelSetDTOReadDungeonManager extends AbstractDungeonManager {

	static Map<Integer, List<DungeonFactory>> stages = new HashMap<>();

	static {
		List<DungeonFactory> stageZeroList = new ArrayList<>();
		ReadDTODungeonBuilderFactory builderFactory = new ReadDTODungeonBuilderFactory();
		stageZeroList.add(new LevelSome(builderFactory));
		stages.put(0, stageZeroList);

		List<DungeonFactory> stageOneList = new ArrayList<>();
		stageOneList.add(new LevelX(builderFactory));
		stageOneList.add(new LevelY(builderFactory));
		stages.put(1, stageOneList);

		List<DungeonFactory> stageTwoList = new ArrayList<>();
		stageTwoList.add(new Level16x16(builderFactory));
		stages.put(2, stageTwoList);
	}

	public LevelSetDTOReadDungeonManager() {
		super(stages);
	}
}
