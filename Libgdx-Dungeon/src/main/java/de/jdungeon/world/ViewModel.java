package de.jdungeon.world;

import figure.FigureInfo;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 03.01.20.
 */
public class ViewModel {

	private final FigureInfo figure;
	private final ViewRoom [][] roomViews;

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

	public int getDungeonWidth() {
		return roomViews.length;
	}

	public int getDungeonHeight() {
		return roomViews[0].length;
	}

	public ViewRoom getRoom(int x, int y) {
		return roomViews[x][y];
	}
}
