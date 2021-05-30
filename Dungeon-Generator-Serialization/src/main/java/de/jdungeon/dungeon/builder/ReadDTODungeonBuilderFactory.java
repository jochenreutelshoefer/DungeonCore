package de.jdungeon.dungeon.builder;

public class ReadDTODungeonBuilderFactory implements DungeonBuilderFactory {

	@Override
	public DungeonBuilder create() {
		// return a dummy dungeon builder that will never actually be used
		return new ReadDTODungeonBuilder();
	}

	@Override
	public DungeonFactory.Mode getMode() {
		return DungeonFactory.Mode.Read;
	}
}
