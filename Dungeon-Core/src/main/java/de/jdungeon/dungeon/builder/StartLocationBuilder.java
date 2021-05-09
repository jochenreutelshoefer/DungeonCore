package de.jdungeon.dungeon.builder;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Room;
import de.jdungeon.location.RevealMapShrine;
import de.jdungeon.log.Log;

public class StartLocationBuilder extends AbstractLocationBuilder {

	private JDPoint exitPosition;

	public StartLocationBuilder(JDPoint startPosition, JDPoint exitPosition) {
		super(startPosition);
		this.exitPosition = exitPosition;
	}



	@Override
	public String getIdentifier() {
		return "Start";
	}

	@Override
	public void insert(Dungeon dungeon, int x, int y) {
		Room room = dungeon.getRoom(x, y);
		if (room.getLocation() != null) {
			Log.severe("Location clash. Already location existing when creating start location: " + room.getLocation());
		}
		else {
			room.setLocation(new RevealMapShrine(dungeon.getRoom(exitPosition)));
		}
	}
}
