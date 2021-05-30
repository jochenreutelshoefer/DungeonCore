package de.jdungeon.level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.jdungeon.dungeon.builder.DungeonFactory;
import de.jdungeon.level.stageone.HadrianLevel;
import de.jdungeon.level.stagethree.StartLevelOLD;
import de.jdungeon.level.stagetwo.EscortLevel2A;
import de.jdungeon.level.stagetwo.MoonRuneChase;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 07.03.16.
 */
public class DefaultDungeonManager extends AbstractDungeonManager {

	static final Map<Integer, List<DungeonFactory>> stages = new HashMap<>();

	{
		List<DungeonFactory> stageZeroList = new ArrayList<>();
		stageZeroList.add(new HadrianLevel());
		stages.put(0, stageZeroList);

		List<DungeonFactory> stageOneList = new ArrayList<>();
		//stageOneList.add(new LevelTwoB());
		stageOneList.add(new MoonRuneChase());
		stageOneList.add(new EscortLevel2A());
		stages.put(1, stageOneList);

		List<DungeonFactory> stageTwoList = new ArrayList<>();
		stageTwoList.add(new StartLevelOLD());
		stages.put(2, stageTwoList);
	}

	public DefaultDungeonManager() {
		super(stages);
	}

	@Override
	public List<DungeonFactory> getDungeonOptions(int stage) {
		return stages.get(stage);
	}

	@Override
	public int getNumberOfStages() {
		return stages.size();
	}
}
