package de.jdungeon.dungeon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.jdungeon.dungeon.level.LevelSome;
import de.jdungeon.dungeon.level.LevelX;
import de.jdungeon.dungeon.level.LevelY;
import de.jdungeon.dungeon.level.Level16x16;
import de.jdungeon.level.AbstractDungeonManager;
import de.jdungeon.level.DungeonFactory;

public class ASPLevelSetDungeonManager extends AbstractDungeonManager {

	static Map<Integer, List<DungeonFactory>> stages = new HashMap<>();

	static {
		List<DungeonFactory> stageZeroList = new ArrayList<>();
		stageZeroList.add(new LevelSome());
		stages.put(0, stageZeroList);

		List<DungeonFactory> stageOneList = new ArrayList<>();
		stageOneList.add(new LevelX());
		stageOneList.add(new LevelY());
		stages.put(1, stageOneList);

		List<DungeonFactory> stageTwoList = new ArrayList<>();
		stageTwoList.add(new Level16x16());
		stages.put(2, stageTwoList);
	}

	public ASPLevelSetDungeonManager() {
		super(stages);
	}
}
