package de.jdungeon.world;

import java.util.List;
import java.util.Objects;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import dungeon.RoomInfo;
import figure.Figure;
import figure.FigureInfo;
import figure.RoomObservationStatus;
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
	private final InputController worldController;
	private final GraphicObjectRenderer dungeonObjectRenderer;
	public static final int roomSize = 80;

	public WorldRenderer(InputController worldController, PlayerController playerController, ViewModel viewModel, OrthographicCamera camera) {
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
				/*
				Sprite sprite = room.getSprite();
				if (sprite == null) {
					sprite = createRoomSprite(room.getRoomInfo(), x, y);
					room.setSprite(sprite);
				}
				sprite.draw(batch);
				*/

				List<GraphicObject> graphicObjectsForRoom = room.getBackgroundObjectsForRoom();
				if (graphicObjectsForRoom == null || graphicObjectsForRoom.isEmpty()) {
					graphicObjectsForRoom = dungeonObjectRenderer.createGraphicObjectsForRoom(room.getRoomInfo(), x * WorldRenderer.roomSize, y * WorldRenderer.roomSize);
					room.setGraphicObjects(graphicObjectsForRoom);
				}
				drawGraphicObjectsToSpritebatch(graphicObjectsForRoom, x, y);
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
					List<GraphicObject> graphicObjectsForRoom = room.getFigureObjects(figureClass);
					drawGraphicObjectsToSpritebatch(graphicObjectsForRoom, x, y);
				}
			}
		}
	}

	private void drawGraphicObjectsToSpritebatch(List<GraphicObject> graphicObjectsForRoom, int x, int y) {
		for (GraphicObject graphicObject : graphicObjectsForRoom) {
			if (graphicObject instanceof JDGraphicObject) {
				JDGraphicObject object = ((JDGraphicObject) graphicObject);
				JDImageLocated locatedImage = object.getLocatedImage();
				TextureAtlas.AtlasRegion atlasRegion = findAtlasRegion(locatedImage.getImage(), graphicObject);
				if (atlasRegion != null) {
					int posX = locatedImage.getX(x * WorldRenderer.roomSize);
					int posY = locatedImage.getY(y * WorldRenderer.roomSize);
					batch.draw(atlasRegion, posX, posY, locatedImage.getWidth(), locatedImage.getHeight());
				}
			}
			else {
				DrawingRectangle destinationRectangle = graphicObject.getRectangle();
				JDImageProxy<?> image = graphicObject.getImage();
				TextureAtlas.AtlasRegion atlasRegion = findAtlasRegion(image, graphicObject);
				if (atlasRegion != null) {
					int posX = destinationRectangle.getX(x * WorldRenderer.roomSize);
					int posY = destinationRectangle.getY(y * WorldRenderer.roomSize);
					batch.draw(atlasRegion, posX, posY, destinationRectangle.getWidth(), destinationRectangle.getHeight());
				}
			}
		}
	}

	private TextureAtlas.AtlasRegion findAtlasRegion(JDImageProxy<?> image, GraphicObject object) {
		// TODO: The result of this method should be cached in view model too!

		Object clickableObject = object.getClickableObject();
		Class<? extends Figure> figureClass = null;
		if (clickableObject instanceof FigureInfo) {
			figureClass = ((FigureInfo) clickableObject).getFigureClass();
		}
		if (Hero.class.equals(figureClass)) {
			int heroCode = ((HeroInfo) clickableObject).getHeroCode();
			Hero.HeroCategory heroCategory = Hero.HeroCategory.fromValue(heroCode);
			if (heroCategory == Hero.HeroCategory.Warrior) {
				return Assets.instance.getWarriorTexture(image);
			}
		}
		if(figureClass != null) {
			return Assets.instance.getFigureTexture(figureClass, image);
		}

		// else look into default atlas
		return Assets.instance.getDungeonTexture(image);
	}

	private Sprite createRoomSprite(RoomInfo roomInfo, int x, int y) {

		Pixmap pixmap = new Pixmap(roomSize, roomSize, Pixmap.Format.RGB888);
		if (roomInfo.getVisibilityStatus() >= RoomObservationStatus.VISIBILITY_ITEMS) {
			pixmap.setColor(0, 0, 1, 1);
		}
		else {
			pixmap.setColor(0, 1, 0, 1);
		}
		drawCross(roomSize, pixmap);
		pixmap.drawRectangle(0, 0, roomSize, roomSize);
		Sprite sprite = new Sprite(new Texture(pixmap));
		sprite.setPosition(roomSize * x, roomSize * y);
		return sprite;
	}

	private void drawCross(int roomSize, Pixmap pixmap) {
		pixmap.drawLine(0, 0, roomSize, roomSize);
		pixmap.drawLine(roomSize, 0, 0, roomSize);
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
