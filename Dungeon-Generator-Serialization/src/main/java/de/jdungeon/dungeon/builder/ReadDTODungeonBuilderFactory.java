package de.jdungeon.dungeon.builder;

public class ReadDTODungeonBuilderFactory implements DungeonBuilderFactory {

	@Override
	public DungeonBuilder create() {
		// return a dummy dungeon builder that will never actually be used
		return new ReadDTODungeonBuilder();
	}

	@Override
	public DungeonGenerationMode getMode() {
		return DungeonGenerationMode.Read;
	}
}
