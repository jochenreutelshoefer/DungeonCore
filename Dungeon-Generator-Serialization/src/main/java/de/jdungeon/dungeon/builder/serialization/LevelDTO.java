package de.jdungeon.dungeon.builder.serialization;

import java.util.Objects;

import de.jdungeon.dungeon.JDPoint;

public class LevelDTO extends AbstractDTO {

	DungeonDTO dungeonDTO;
	String name;
	JDPoint startPosition;

	public LevelDTO(DungeonDTO dungeonDTO) {
		this.dungeonDTO = dungeonDTO;
	}

	/**
	 * Required for JSON serialization
	 */
	public LevelDTO() {
	}



	public DungeonDTO getDungeonDTO() {
		return dungeonDTO;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStartPosition(JDPoint startPosition) {
		this.startPosition = startPosition;
	}

	public String getName() {
		return name;
	}

	public JDPoint getStartPosition() {
		return startPosition;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LevelDTO levelDTO = (LevelDTO) o;
		return dungeonDTO.equals(levelDTO.dungeonDTO) && startPosition.equals(levelDTO.startPosition);
	}

	@Override
	public int hashCode() {
		return Objects.hash(dungeonDTO, startPosition);
	}
}
