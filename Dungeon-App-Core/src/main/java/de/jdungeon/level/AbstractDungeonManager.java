package de.jdungeon.level;

import java.util.List;
import java.util.Map;

import de.jdungeon.dungeon.builder.DungeonFactory;

public abstract class AbstractDungeonManager implements DungeonManager{

	private final Map<Integer, List<DungeonFactory>> stages;

	public AbstractDungeonManager(Map<Integer, List<DungeonFactory>> stages) {
		this.stages = stages;
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
