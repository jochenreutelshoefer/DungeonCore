package de.jdungeon.dungeon.builder;

import java.util.Collection;
import java.util.HashSet;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.builder.serialization.LevelDTO;
import de.jdungeon.level.DungeonFactory;

public abstract class AbstractASPDungeonFactory implements DungeonFactory {

	protected DungeonResult dungeonBuild;

	@Override
	public LevelDTO getDTO() {
		if(this.dungeonBuild == null) {
			throw new IllegalStateException("May not be called before 'create()' method!");
		}
		return this.dungeonBuild.getDungeonDTO();
	}

	@Override
	public Dungeon getDungeon() {
		return dungeonBuild.getDungeon();
	}

}
