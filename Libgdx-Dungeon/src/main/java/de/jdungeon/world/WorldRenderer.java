package de.jdungeon.world;

import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import figure.Figure;
import figure.FigureInfo;
import figure.hero.Hero;
import figure.hero.HeroInfo;
import figure.monster.Orc;
import figure.monster.Skeleton;
import figure.monster.Wolf;
import graphics.GraphicObject;
import graphics.GraphicObjectRenderer;
import graphics.JDGraphicObject;
import graphics.JDImageLocated;
import graphics.JDImageProxy;
import graphics.util.DrawingRectangle;

import de.jdungeon.Constants;
import de.jdungeon.asset.Assets;
import de.jdungeon.util.Pair;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 24.12.19.
 */
public class WorldRenderer implements Disposable {

	private static final String TAG = WorldRenderer.class.getName();

	private final PlayerController playerController;
	private final ViewModel viewModel;
	private final OrthographicCamera camera;
	private SpriteBatch batch;
	private final GameScreenInputController worldController;
	private final GraphicObjectRenderer dungeonObjectRenderer;
	public static final int roomSize = 80;

	public WorldRenderer(GameScreenInputController worldController, PlayerController playerController, ViewModel viewModel, OrthographicCamera camera) {
		this.worldController = worldController;
		this.playerController = playerController;
		this.viewModel = viewModel;
		this.camera = camera;
		dungeonObjectRenderer = new GraphicObjectRenderer(roomSize, playerController);

		init();
	}

	private void init() {
		batch = new SpriteBatch();
		viewModel.initGraphicObjects(dungeonObjectRenderer);
		camera.update();
	}

	public void render() {
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		renderDungeon();
		//renderTestObjects();
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

	private void renderDungeonBackgroundObjectsForAllRooms() {
		for (int x = 0; x < viewModel.getDungeonWidth(); x++) {
			for (int y = 0; y < viewModel.getDungeonHeight(); y++) {
				ViewRoom room = viewModel.getRoom(x, y);

				// fetch prepared render information
				List<Pair<GraphicObject, TextureAtlas.AtlasRegion>> renderInformation = room.getBackgroundObjectsForRoom();
				if (renderInformation == null || renderInformation.isEmpty()) {
					// no render information yet, we need to fetch object information about the room and update the ViewRoom with it
					List<GraphicObject> graphicObjectsForRoom = dungeonObjectRenderer.createGraphicObjectsForRoom(room.getRoomInfo(), x * WorldRenderer.roomSize, y * WorldRenderer.roomSize);
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
		for (Pair<GraphicObject, TextureAtlas.AtlasRegion> pair : graphicObjectsForRoom) {
			TextureAtlas.AtlasRegion atlasRegion = pair.getB();
			GraphicObject graphicObject = pair.getA();
			if (graphicObject instanceof JDGraphicObject) {
				if (atlasRegion != null) {
					JDGraphicObject object = ((JDGraphicObject) graphicObject);
					JDImageLocated locatedImage = object.getLocatedImage();
					int posX = locatedImage.getX(x * WorldRenderer.roomSize);
					int posY = locatedImage.getY(y * WorldRenderer.roomSize);
					batch.draw(atlasRegion, posX, posY, locatedImage.getWidth(), locatedImage.getHeight());
				}
			}
			else {
				if (atlasRegion != null) {
					DrawingRectangle destinationRectangle = graphicObject.getRectangle();
					int posX = destinationRectangle.getX(x * WorldRenderer.roomSize);
					int posY = destinationRectangle.getY(y * WorldRenderer.roomSize);
					batch.draw(atlasRegion, posX, posY, destinationRectangle.getWidth(), destinationRectangle.getHeight());
				}
			}
		}
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
