package level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import level.stageone.PreliminaryStartLevelFactory;
import level.stageone.StartLevel;
import level.stageone.StartLevelOLD;
import level.stagetwo.EscortLevel2A;
import level.stagetwo.LevelTwoB;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 07.03.16.
 */
public class DefaultDungeonManager implements DungeonManager {

	private final Map<Integer, List<DungeonFactory>> stages = new HashMap<>();

	public DefaultDungeonManager() {

		List<DungeonFactory> stageZeroList = new ArrayList<>();
		stageZeroList.add(new StartLevel());
		stages.put(0 , stageZeroList);

		List<DungeonFactory> stageOneList = new ArrayList<>();
		stageOneList.add(new LevelTwoB());
		stageOneList.add(new EscortLevel2A());
		stages.put(1 , stageOneList);

		List<DungeonFactory> stageTwoList = new ArrayList<>();
		stageTwoList.add(new StartLevelOLD());
		stageTwoList.add(new StartLevelOLD());
		stageTwoList.add(new StartLevelOLD());
		stages.put(2 , stageTwoList);
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
