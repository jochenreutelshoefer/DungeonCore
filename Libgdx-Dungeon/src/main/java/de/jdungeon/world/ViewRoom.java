package de.jdungeon.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Sprite;
import dungeon.JDPoint;
import dungeon.RoomInfo;
import figure.Figure;
import figure.FigureInfo;
import graphics.GraphicObject;

import de.jdungeon.app.screen.GraphicObjectComparator;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 03.01.20.
 */
public class ViewRoom {

	private RoomInfo roomInfo;
	private Sprite sprite;

	private final List<GraphicObject> graphicObjectsForRoom = new ArrayList<>();
	private final List<GraphicObject> graphicObjectsForRoomTransport = new ArrayList<>();

	public final List<GraphicObject> backGroundObjects = new ArrayList<>();
	public final List<GraphicObject> backGroundObjectsTransport = new ArrayList<>();

	public final Map<Class<? extends Figure>, List<GraphicObject>> figureObjects = new HashMap<>();
	public final List<GraphicObject> figureObjectsTransport = new ArrayList<>();

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
		backGroundObjects.clear();
		figureObjects.clear();

		for (GraphicObject graphicObject : graphicObjectsForRoom) {
			Object clickableObject = graphicObject.getClickableObject();
			if (clickableObject instanceof FigureInfo) {
				Class<? extends Figure> figureClass = ((FigureInfo) clickableObject).getFigureClass();
				List<GraphicObject> figureList = figureObjects.get(figureClass);
				if (figureList == null) {
					figureList = new ArrayList<>();
					figureObjects.put(figureClass, figureList);
				}
				figureList.add(graphicObject);
			}
			else {
				backGroundObjects.add(graphicObject);
			}
		}
	}

	public Object findClickedObjectInRoom(JDPoint inGameLocation, int roomOffsetX, int roomOffsetY) {
		List<GraphicObject> allRoomObjects = new ArrayList<>(backGroundObjects);
		for (List<GraphicObject> figures : figureObjects.values()) {
			allRoomObjects.addAll(figures);
		}
		Collections.sort(allRoomObjects, new GraphicObjectComparator());
		Object clickedObject = null;
		for (GraphicObject graphicObject : allRoomObjects) {
			if (graphicObject.hasPoint(inGameLocation, roomOffsetX, roomOffsetY)) {
				clickedObject = graphicObject.getClickableObject();
				break;
			}
		}
		return clickedObject;
	}

	public List<GraphicObject> getFigureObjects(Class<? extends Figure> figureClass) {
		if (!figureObjects.containsKey(figureClass)) {
			return Collections.emptyList();
		}
		if (figureObjects.get(figureClass).isEmpty()) {
			return Collections.emptyList();
		}
		figureObjectsTransport.clear();
		List<GraphicObject> figureObjects = this.figureObjects.get(figureClass);
		figureObjectsTransport.addAll(figureObjects);
		return figureObjects;
	}

	public List<GraphicObject> getBackgroundObjectsForRoom() {
		// use transport list to overcome concurrent modification problem efficiently
		backGroundObjectsTransport.clear();
		backGroundObjectsTransport.addAll(backGroundObjects);
		return graphicObjectsForRoomTransport;
	}
}
