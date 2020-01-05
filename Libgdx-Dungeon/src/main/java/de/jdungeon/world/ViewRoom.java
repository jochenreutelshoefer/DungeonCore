package de.jdungeon.world;

import java.util.List;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import dungeon.RoomInfo;
import graphics.GraphicObject;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 03.01.20.
 */
public class ViewRoom {

	private RoomInfo roomInfo;
	private Sprite sprite;

	private List<GraphicObject> graphicObjectsForRoom;

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public void setRoomInfo(RoomInfo roomInfo) {
		this.roomInfo = roomInfo;
	}

	public RoomInfo getRoomInfo() {
		return roomInfo;
	}

	public void setGraphicObjects(List<GraphicObject> graphicObjectsForRoom) {
		this.graphicObjectsForRoom = graphicObjectsForRoom;
	}

	public List<GraphicObject> getGraphicObjectsForRoom() {
		return graphicObjectsForRoom;
	}
}
