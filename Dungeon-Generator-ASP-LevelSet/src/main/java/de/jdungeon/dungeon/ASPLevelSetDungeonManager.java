package de.jdungeon.dungeon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.jdungeon.dungeon.builder.AbstractASPDungeonFactory;
import de.jdungeon.dungeon.level.LevelSome;
import de.jdungeon.dungeon.level.LevelX;
import de.jdungeon.dungeon.level.LevelY;
import de.jdungeon.dungeon.level.LevelZ;
import de.jdungeon.level.AbstractDungeonManager;
import de.jdungeon.level.DungeonFactory;
import de.jdungeon.level.DungeonManager;
import de.jdungeon.level.stageone.HadrianLevel;
import de.jdungeon.level.stagethree.StartLevelOLD;
import de.jdungeon.level.stagetwo.EscortLevel2A;
import de.jdungeon.level.stagetwo.MoonRuneChase;

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
		stageTwoList.add(new LevelZ());
		stages.put(2, stageTwoList);
	}

	public ASPLevelSetDungeonManager() {
		super(stages);
	}
}
