package de.jdungeon.dungeon.builder.serialization;

import java.util.Map;
import java.util.Objects;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.builder.LocatedEntityBuilder;
import de.jdungeon.dungeon.builder.LocationBuilder;
import de.jdungeon.location.LevelExit;
import de.jdungeon.log.Log;

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

	public JDPoint getExitPosition() {
		Map<JDPoint, LocatedEntityBuilder> locations = this.dungeonDTO.getLocations();
		for (JDPoint point : locations.keySet()) {
			LocatedEntityBuilder locatedEntityBuilder = locations.get(point);
			if(locatedEntityBuilder instanceof LocationBuilder) {
				if(((LocationBuilder)locatedEntityBuilder).getClazz().equals(LevelExit.class)) {
					return point;
				}
			}
		}
		Log.severe("No exit found in LevelDTO: "+this.name );
		return null;
	}
}
