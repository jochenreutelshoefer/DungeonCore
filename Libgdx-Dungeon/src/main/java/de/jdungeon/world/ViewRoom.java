package de.jdungeon.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import dungeon.JDPoint;
import dungeon.RoomInfo;
import figure.Figure;
import figure.FigureInfo;
import graphics.GraphicObject;

import de.jdungeon.app.screen.GraphicObjectComparator;
import de.jdungeon.util.Pair;

/**
 *
 * A ViewRoom prepares and provides the information required to render a room on the screen.
 * It is created/updated if the world change (that is if world changes are perceived).
 * The actual rendering loop can fetch lists of prepared Pairs for each game objects.
 *
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 03.01.20.
 */
public class ViewRoom {

	private RoomInfo roomInfo;

	private final GraphicObjectRenderCollection backGroundObjects = new GraphicObjectRenderCollection();

	public final Map<Class<? extends Figure>, GraphicObjectRenderCollection> figureObjects = new HashMap<>();
	public final List<Pair<GraphicObject, TextureAtlas.AtlasRegion>> figureObjectsTransport = new ArrayList<>();

	public void setRoomInfo(RoomInfo roomInfo) {
		this.roomInfo = roomInfo;
	}

	public RoomInfo getRoomInfo() {
		return roomInfo;
	}

	public void setGraphicObjects(List<GraphicObject> graphicObjectsForRoom) {
		backGroundObjects.clear();

		synchronized (figureObjects) {
			figureObjects.clear();
			for (GraphicObject graphicObject : graphicObjectsForRoom) {
				Object clickableObject = graphicObject.getClickableObject();
				if (clickableObject instanceof FigureInfo) {
					Class<? extends Figure> figureClass = ((FigureInfo) clickableObject).getFigureClass();
					GraphicObjectRenderCollection figureList = figureObjects.get(figureClass);
					if (figureList == null) {
						figureList = new GraphicObjectRenderCollection();
						figureObjects.put(figureClass, figureList);
					}
					figureList.addObject(graphicObject);
				}
				else {
					backGroundObjects.addObject(graphicObject);
				}
			}
		}
	}

	public Object findClickedObjectInRoom(JDPoint inGameLocation, int roomOffsetX, int roomOffsetY) {
		List<GraphicObject> allRoomObjects = new ArrayList<>(backGroundObjects.getGraphicObjects());
		for (GraphicObjectRenderCollection figures : figureObjects.values()) {
			allRoomObjects.addAll(figures.getGraphicObjects());
		}
		Collections.sort(allRoomObjects, new GraphicObjectComparator());
		Object clickedObject = null;
		for (GraphicObject object : allRoomObjects) {
			if (object.hasPoint(inGameLocation, roomOffsetX, roomOffsetY)) {
				clickedObject = object.getClickableObject();
				break;
			}
		}
		return clickedObject;
	}

	public List<Pair<GraphicObject, TextureAtlas.AtlasRegion>> getFigureObjects(Class<? extends Figure> figureClass) {
		synchronized (figureObjects) {
			if (!figureObjects.containsKey(figureClass)) {
				return Collections.emptyList();
			}
			if (figureObjects.get(figureClass).getGraphicObjects().isEmpty()) {
				return Collections.emptyList();
			}
			return figureObjects.get(figureClass).getRenderInformation();
		}
	}

	public List<Pair<GraphicObject, TextureAtlas.AtlasRegion>> getBackgroundObjectsForRoom() {
		return backGroundObjects.getRenderInformation();
	}

}
