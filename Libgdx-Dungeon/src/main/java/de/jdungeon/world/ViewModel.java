package de.jdungeon.world;

import java.util.List;

import com.badlogic.gdx.Gdx;
import dungeon.RoomInfo;
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

	private static final String TAG = ViewModel.class.getName();

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

	/**
	 * Returns the visibility status of the room with the given coordinates
	 *
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return the numerical value of the visibility state of the room at (x,y)
	 */
	public int getVisStatus(int x, int y) {
		RoomInfo roomInfo = figure.getRoomInfo(x, y);
		if(roomInfo == null) return 0;
		return roomInfo.getVisibilityStatus();
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
	 * Thread-blackboard put-method: here for a particular room of the world the render information
	 * is updated/prepared by the world thread (the world thread now when the world changes in a way
	 * that requires update of the render information for the UI-controlled figure.
	 *
	 * @param x x coordinate of the room to be updated
	 * @param y y coordinate of the room to be updated
	 */
	public void updateRoom(int x, int y) {
		Gdx.app.log(TAG,"Updating render information of room: "+x +" - "+ y);
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
