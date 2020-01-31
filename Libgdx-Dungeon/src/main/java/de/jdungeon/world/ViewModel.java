package de.jdungeon.world;

import java.util.List;

import figure.FigureInfo;
import graphics.GraphicObject;
import graphics.GraphicObjectRenderer;

/**
 * Thread-blackboard data structure: the render information is delivered and prepared
 * by the world thread. The prepared information is fetched and consumed by the render thread.
 *
 * A ViewModel that allows the rendering loop quick access to information to be drawn - room by room.
 *
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 03.01.20.
 */
public class ViewModel {

	private final FigureInfo figure;
	public final ViewRoom [][] roomViews;
	public final int [][] roomOffSetsX;
	public final int [][] roomOffSetsY;
	private GraphicObjectRenderer renderer;

	public ViewModel(FigureInfo figure, int sizeX, int sizeY) {
		this.figure = figure;
		roomViews = new ViewRoom[sizeX][sizeY];
		roomOffSetsX = new int[sizeX][sizeY] ;
		roomOffSetsY = new int[sizeX][sizeY] ;
		init();
	}

	private void init() {
		for(int x = 0; x < roomViews.length; x++) {
			for(int y = 0; y < roomViews[0].length; y++) {
				roomViews[x][y] = new ViewRoom();
				roomViews[x][y].setRoomInfo(figure.getRoomInfo(x, y));

				roomOffSetsX[x][y] = x * WorldRenderer.ROOM_SIZE;;
				roomOffSetsY[x][y] = y * WorldRenderer.ROOM_SIZE;;
			}
		}
	}

	public void initGraphicObjects(GraphicObjectRenderer renderer) {
		this.renderer = renderer;
		for (int x = 0; x < roomViews.length; x++) {
			for (int y = 0; y < roomViews[0].length; y++) {
				ViewRoom currentViewRoom = roomViews[x][y];
				List<GraphicObject> graphicObjectsForRoom = renderer.createGraphicObjectsForRoom(currentViewRoom.getRoomInfo(), x * WorldRenderer.ROOM_SIZE, y * WorldRenderer.ROOM_SIZE);
				currentViewRoom.setGraphicObjects(graphicObjectsForRoom);
			}
		}
	}

	/**
	 * Thread-blackboard put-method: here for a particular room of the world the render infrormation
	 * is updated/prepared by the world thread (the world thread now when the world changes in a way
	 * that requires update of the render information for the UI-controlled figure.
	 *
	 * @param x x coordinate of the room to be updated
	 * @param y y coordinate of the room to be updated
	 */
	public void updateRoom(int x, int y) {
		ViewRoom currentViewRoom = roomViews[x][y];
		List<GraphicObject> graphicObjectsForRoom = renderer.createGraphicObjectsForRoom(currentViewRoom.getRoomInfo(), x * WorldRenderer.ROOM_SIZE, y * WorldRenderer.ROOM_SIZE);
		currentViewRoom.setGraphicObjects(graphicObjectsForRoom);
	}

	public int getDungeonWidth() {
		return roomViews.length;
	}

	public int getDungeonHeight() {
		return roomViews[0].length;
	}

	/**
	 * Thread blackboard fetch-method: here the prepared render information of a particular room
	 * is fetched by the render thread to be displayed on the UI.
	 *
	 * @param x x coordinate of the room to be rendered
	 * @param y y coordinate of the room to be rendered
	 * @return render information for room located at x, y
	 */
	public ViewRoom getRoom(int x, int y) {
		if(x < 0 || y < 0 || x >= roomViews.length || y >= roomViews[0].length) return null;
		return roomViews[x][y];
	}
}
