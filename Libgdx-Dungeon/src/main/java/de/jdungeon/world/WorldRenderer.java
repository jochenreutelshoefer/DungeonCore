package de.jdungeon.world;

import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;
import dungeon.JDPoint;
import figure.Figure;
import figure.FigureInfo;
import figure.hero.Hero;
import figure.monster.Orc;
import figure.monster.Skeleton;
import figure.monster.Wolf;
import graphics.GraphicObject;
import graphics.GraphicObjectRenderer;
import graphics.JDGraphicObject;
import graphics.JDImageLocated;
import graphics.util.DrawingRectangle;

import de.jdungeon.CameraHelper;
import de.jdungeon.Constants;
import de.jdungeon.app.gui.FocusManager;
import de.jdungeon.game.Color;
import de.jdungeon.util.Pair;

/**
 * Renders the dungeon world the screen using the given camera and view model.
 *
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 24.12.19.
 */
public class WorldRenderer implements Disposable {

	private static final String TAG = WorldRenderer.class.getName();

	private final ViewModel viewModel;
	private final OrthographicCamera camera;
	private final FocusManager focusManager;
	private final GraphicObjectRenderer dungeonObjectRenderer;
	private final CameraHelper cameraHelper;
	private SpriteBatch batch;
	public static final int ROOM_SIZE = 80;
	private final ShapeRenderer shapeRenderer = new ShapeRenderer();

	public WorldRenderer(GraphicObjectRenderer graphicObjectRenderer, ViewModel viewModel, OrthographicCamera camera, CameraHelper cameraHelper, FocusManager focusManager) {
		this.dungeonObjectRenderer = graphicObjectRenderer;
		this.cameraHelper = cameraHelper;
		this.viewModel = viewModel;
		this.camera = camera;
		this.focusManager = focusManager;
		init();
	}

	public static Pair<Float, Float> getPlayerRoomWorldPosition(FigureInfo figure) {
		JDPoint number = figure.getRoomInfo().getNumber();
		return new Pair<>((float) number.getX() * ROOM_SIZE + ROOM_SIZE / 2, (float) number.getY() * ROOM_SIZE + ROOM_SIZE / 2);
	}

	private void init() {
		batch = new SpriteBatch();
		viewModel.initGraphicObjects(dungeonObjectRenderer);

		cameraHelper.setZoom(1f);

		camera.update();
	}

	public void render() {
		cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		renderDungeon();
	}

	public void update(float deltaTime) {
		// nothing yet
	}

	private void renderDungeon() {
		batch.begin();
		renderDungeonBackgroundObjectsForAllRooms();
		renderFigureObjectsForAllRooms();
		batch.end();
	}

	private void highlight(DrawingRectangle rectangle, int roomOffsetX, int roomOffsetY) {
		int x1 = rectangle.getX(roomOffsetX);
		int y1 = rectangle.getY(roomOffsetY);
		int x2 = x1 + rectangle.getWidth();
		int y2 = y1 + rectangle.getHeight();
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.YELLOW);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.rect(x1, y1, x2 - x1, y2 - y1);
		shapeRenderer.end();
	}

	private void prepareDraw(Color color) {

	}

	private void renderDungeonBackgroundObjectsForAllRooms() {
		for (int x = 0; x < viewModel.getDungeonWidth(); x++) {
			for (int y = 0; y < viewModel.getDungeonHeight(); y++) {
				ViewRoom room = viewModel.getRoom(x, y);

				// fetch prepared render information
				List<Pair<GraphicObject, TextureAtlas.AtlasRegion>> renderInformation = room.getBackgroundObjectsForRoom();
				if (renderInformation == null || renderInformation.isEmpty()) {
					// no render information yet, we need to fetch object information about the room and update the ViewRoom with it
					List<GraphicObject> graphicObjectsForRoom = dungeonObjectRenderer.createGraphicObjectsForRoom(room.getRoomInfo(), x * WorldRenderer.ROOM_SIZE, y * WorldRenderer.ROOM_SIZE);
					room.setGraphicObjects(graphicObjectsForRoom);
					renderInformation = room.getBackgroundObjectsForRoom();
				}
				drawGraphicObjectsToSpritebatch(renderInformation, x, y);
			}
		}
	}

	private void renderFigureObjectsForAllRooms() {
		Class<? extends Figure>[] figureClasses = new Class[] { Hero.class, Orc.class, Wolf.class, Skeleton.class };

		// iterate first for figure classes to have less atlas switches as each figure has a distinct atlas
		for (Class<? extends Figure> figureClass : figureClasses) {
			for (int x = 0; x < viewModel.getDungeonWidth(); x++) {
				for (int y = 0; y < viewModel.getDungeonHeight(); y++) {
					ViewRoom room = viewModel.getRoom(x, y);
					List<Pair<GraphicObject, TextureAtlas.AtlasRegion>> graphicObjectsForRoom = room.getFigureObjects(figureClass);
					drawGraphicObjectsToSpritebatch(graphicObjectsForRoom, x, y);
				}
			}
		}
	}

	private void drawGraphicObjectsToSpritebatch(List<Pair<GraphicObject, TextureAtlas.AtlasRegion>> graphicObjectsForRoom, int x, int y) {
		GraphicObject highlightedObject = focusManager.getGraphicObject();
		for (Pair<GraphicObject, TextureAtlas.AtlasRegion> pair : graphicObjectsForRoom) {
			TextureAtlas.AtlasRegion atlasRegion = pair.getB();
			GraphicObject graphicObject = pair.getA();
			if (graphicObject instanceof JDGraphicObject) {
				if (atlasRegion != null) {
					JDGraphicObject object = ((JDGraphicObject) graphicObject);
					JDImageLocated locatedImage = object.getLocatedImage();
					int posX = locatedImage.getX(x * WorldRenderer.ROOM_SIZE);
					int posY = locatedImage.getY(y * WorldRenderer.ROOM_SIZE);
					batch.draw(atlasRegion, posX, posY, locatedImage.getWidth(), locatedImage.getHeight());
					// highlight focus object
					if (highlightedObject != null) {
						checkForHighlightedObject(highlightedObject, graphicObject, posX, posY, locatedImage.getWidth(), locatedImage
								.getHeight());
					}
				}
			}
			else {
				if (atlasRegion != null) {
					DrawingRectangle destinationRectangle = graphicObject.getRectangle();
					int posX = destinationRectangle.getX(x * WorldRenderer.ROOM_SIZE);
					int posY = destinationRectangle.getY(y * WorldRenderer.ROOM_SIZE);
					batch.draw(atlasRegion, posX, posY, destinationRectangle.getWidth(), destinationRectangle.getHeight());
					if (highlightedObject != null) {
						checkForHighlightedObject(highlightedObject, graphicObject, posX, posY, destinationRectangle.getWidth(), destinationRectangle
								.getHeight());
					}
				}
			}
		}
	}

	private void checkForHighlightedObject(GraphicObject highlightedObject, GraphicObject graphicObject, int posX, int posY, int width, int height) {
		/*
		if (graphicObject.equals(highlightedObject)) {
			batch.setProjectionMatrix(camera.combined);
			shapeRenderer.setProjectionMatrix(camera.combined);
			shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.YELLOW);
			shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			shapeRenderer.rect(posX, posY, width, height);
			shapeRenderer.end();
		}
		*/
	}

	public void resize(int width, int height) {
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
		camera.update();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
