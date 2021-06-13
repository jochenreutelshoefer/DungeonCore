package de.jdungeon.dungeon.builder;

public class DungeonBuilderFactoryASPGenerate implements DungeonBuilderFactory {
	@Override
	public DungeonBuilder create() {
		return new DungeonBuilderASP();
	}

	@Override
	public DungeonGenerationMode getMode() {
		return DungeonGenerationMode.Generate;
	}
}
