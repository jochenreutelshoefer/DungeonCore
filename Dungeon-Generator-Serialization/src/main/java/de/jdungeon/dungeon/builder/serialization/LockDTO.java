package de.jdungeon.dungeon.builder.serialization;

import java.util.Objects;

import de.jdungeon.dungeon.builder.DoorMarker;

public class LockDTO extends AbstractDTO {

	String key;
	DoorMarker door;

	public LockDTO(String key, DoorMarker d) {
		this.key = key;
		this.door = d;
	}

	/**
	 * Required for JSON serialization
	 */
	public LockDTO() {
	}

	public DoorMarker getDoor() {
		return door;
	}

	public void setDoor(DoorMarker door) {
		this.door = door;
	}

	public String getKey() {
		return key;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LockDTO lockDTO = (LockDTO) o;
		return key.equals(lockDTO.key);
	}

	@Override
	public int hashCode() {
		return Objects.hash(key);
	}
}
