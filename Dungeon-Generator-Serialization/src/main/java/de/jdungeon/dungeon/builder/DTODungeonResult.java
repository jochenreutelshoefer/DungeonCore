package de.jdungeon.dungeon.builder;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.builder.serialization.LevelDTO;

public class DTODungeonResult implements DungeonResult {

	private LevelDTO dungeon;

	private String description;

	private Dungeon createdDungeon = null;

	public DTODungeonResult(LevelDTO dungeon, String description) {
		this.dungeon = dungeon;
		this.description = description;
	}

	public LevelDTO getDungeonDTO() {
		return dungeon;
	}

	public Dungeon getDungeon() {
		if (createdDungeon == null) {
			createdDungeon = new DTODungeonFactory(dungeon).create();
		}
		return createdDungeon;
	}

	public JDPoint getStartPosition() {
		return dungeon.getStartPosition();
	}

	@Override
	public String getDescription() {
		return description;
	}
}
