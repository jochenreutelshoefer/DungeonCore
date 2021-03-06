package de.jdungeon.app.screen;

import java.util.Comparator;

import de.jdungeon.dungeon.ChestInfo;
import de.jdungeon.dungeon.DoorInfo;
import de.jdungeon.dungeon.PositionInRoomInfo;
import de.jdungeon.dungeon.RoomInfo;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.graphics.GraphicObject;
import de.jdungeon.item.ItemInfo;
import de.jdungeon.location.LocationInfo;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 18.01.18.
 */
public class GraphicObjectClickComparator implements Comparator<GraphicObject> {

	private static final int FLOOR = 0;
	private static final int SHRINE = 1;
	private static final int CHEST = 2;
	private static final int ITEM = 3;
	private static final int POSITION = 4;
	private static final int FIGURE_DEAD = 5;
	private static final int FIGURE = 6;
	private static final int DOOR = 7;
	//private static final int SPOT = 8;

	@Override
	public int compare(GraphicObject arg0, GraphicObject arg1) {
		if (arg0 == null) {
			return 1;
		}
		if (arg1 == null) {
			return -1;
		}
		Object clickedObject0 = arg0.getClickableObject();
		if (clickedObject0 == null) {
			return 1;
		}
		Object clickedObject1 = arg1.getClickableObject();
		if (clickedObject1 == null) {
			return -1;
		}

		int priority0 = getPriority(clickedObject0);
		int priority1 = getPriority(clickedObject1);

		return priority1 - priority0;
	}

	private int getPriority(Object o) {
		if (o instanceof RoomInfo) {
			return FLOOR;
		}
		if (o instanceof FigureInfo) {
			if(((FigureInfo)o).isDead()) {
				return FIGURE_DEAD;
			}
			return FIGURE;
		}
		if (o instanceof DoorInfo) {
			return DOOR;
		}
		if (o instanceof PositionInRoomInfo) {
			return POSITION;
		}
		if (o instanceof LocationInfo) {
			return SHRINE;
		}
		if (o instanceof ChestInfo) {
			return CHEST;
		}
		if (o instanceof ItemInfo) {
			return ITEM;
		}
		return -1;
	}

}
