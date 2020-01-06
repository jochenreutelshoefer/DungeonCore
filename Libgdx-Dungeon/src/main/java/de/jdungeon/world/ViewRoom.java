package de.jdungeon.world;

import java.util.ArrayList;
import java.util.Collections;
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

	private final List<GraphicObject> graphicObjectsForRoom = new ArrayList<>();
	private final List<GraphicObject> graphicObjectsForRoomTransport = new ArrayList<>();

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
		this.graphicObjectsForRoom.clear();
		this.graphicObjectsForRoom.addAll(graphicObjectsForRoom);
	}

	public List<GraphicObject> getGraphicObjectsForRoom() {
		// use transport list to overcome concurrent modification problem efficiently
		graphicObjectsForRoomTransport.clear();
		graphicObjectsForRoomTransport.addAll(graphicObjectsForRoom);
		return graphicObjectsForRoomTransport;
	}
}
