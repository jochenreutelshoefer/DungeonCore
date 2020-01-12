package de.jdungeon.world;

import java.util.List;

import figure.FigureInfo;
import graphics.GraphicObject;
import graphics.GraphicObjectRenderer;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 03.01.20.
 */
public class ViewModel {

	private final FigureInfo figure;
	private final ViewRoom [][] roomViews;
	private GraphicObjectRenderer renderer;

	public ViewModel(FigureInfo figure, int sizeX, int sizeY) {
		this.figure = figure;
		roomViews = new ViewRoom[sizeX][sizeY];
		init();
	}

	public void init() {
		for(int x = 0; x < roomViews.length; x++) {
			for(int y = 0; y < roomViews[0].length; y++) {
				roomViews[x][y] = new ViewRoom();
				roomViews[x][y].setRoomInfo(figure.getRoomInfo(x, y));
			}
		}
	}

	public void initGraphicObjects(GraphicObjectRenderer renderer) {
		this.renderer = renderer;
		for (int x = 0; x < roomViews.length; x++) {
			for (int y = 0; y < roomViews[0].length; y++) {
				ViewRoom currentViewRoom = roomViews[x][y];
				List<GraphicObject> graphicObjectsForRoom = renderer.createGraphicObjectsForRoom(currentViewRoom.getRoomInfo(), x * WorldRenderer.roomSize, y * WorldRenderer.roomSize);
				currentViewRoom.setGraphicObjects(graphicObjectsForRoom);
			}
		}
	}

	public void updateRoom(int x, int y) {
		ViewRoom currentViewRoom = roomViews[x][y];
		List<GraphicObject> graphicObjectsForRoom = renderer.createGraphicObjectsForRoom(currentViewRoom.getRoomInfo(), x * WorldRenderer.roomSize, y * WorldRenderer.roomSize);
		currentViewRoom.setGraphicObjects(graphicObjectsForRoom);
	}

	public int getDungeonWidth() {
		return roomViews.length;
	}

	public int getDungeonHeight() {
		return roomViews[0].length;
	}

	public ViewRoom getRoom(int x, int y) {
		if(x < 0 || y < 0 || x >= roomViews.length || y >= roomViews[0].length) return null;
		return roomViews[x][y];
	}
}
