package de.jdungeon.level;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SingleDungeonManager extends AbstractDungeonManager {

	public SingleDungeonManager(Map<Integer, List<DungeonFactory>> stages) {
		super(stages);
	}

	public static SingleDungeonManager create(DungeonFactory level) {
		Map<Integer, List<DungeonFactory>> stages = new HashMap<>();
		stages.put(0, Collections.singletonList(level));
		return new SingleDungeonManager(stages);
	}
}
