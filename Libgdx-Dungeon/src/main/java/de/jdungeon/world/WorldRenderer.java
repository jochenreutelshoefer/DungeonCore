package de.jdungeon.world;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import animation.AnimationFrame;
import animation.AnimationManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import dungeon.ChestInfo;
import dungeon.JDPoint;
import dungeon.RoomInfo;
import event.EventManager;
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
import graphics.JDImageProxy;
import graphics.util.DrawingRectangle;
import util.JDDimension;

import de.jdungeon.CameraHelper;
import de.jdungeon.Constants;
import de.jdungeon.app.gui.FocusManager;
import de.jdungeon.app.gui.smartcontrol.ToggleChestViewEvent;
import de.jdungeon.asset.Assets;
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
	private final AnimationManager animationManager;
	private final GraphicObjectRenderer dungeonObjectRenderer;
	private final CameraHelper cameraHelper;
	private SpriteBatch batch;
	public static final int ROOM_SIZE = 80;
	private final ShapeRenderer shapeRenderer = new ShapeRenderer();
	private Class<? extends Figure>[] figureClasses;
	private Map<Class<? extends Figure>, TextureAtlas> atlasMap;

	public WorldRenderer(GraphicObjectRenderer graphicObjectRenderer, ViewModel viewModel, OrthographicCamera camera, CameraHelper cameraHelper, FocusManager focusManager, AnimationManager animationManager) {
		this.dungeonObjectRenderer = graphicObjectRenderer;
		this.cameraHelper = cameraHelper;
		this.viewModel = viewModel;
		this.camera = camera;
		this.focusManager = focusManager;
		this.animationManager = animationManager;
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

		batch.setProjectionMatrix(camera.combined);

		camera.update();

		figureClasses = new Class[] { Hero.class, Orc.class, Wolf.class, Skeleton.class };
		atlasMap = new HashMap<>();
		atlasMap.put(Hero.class, Assets.instance.getWarriorAtlas());
		atlasMap.put(Orc.class, Assets.instance.getOrcAtlas());
		atlasMap.put(Wolf.class, Assets.instance.getWolfAtlas());
		atlasMap.put(Skeleton.class, Assets.instance.getSkelAtlas());
	}

	/*
	 *	RENDER THREAD
	 */
	public void render() {
		cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		renderDungeon();
	}

	/*
	 *	RENDER THREAD
	 */
	public void update(float deltaTime) {
		// nothing yet
	}

	/*
	 *	RENDER THREAD
	 */
	private void renderDungeon() {
		batch.begin();
		renderDungeonBackgroundObjectsForAllRooms();
		renderFigureObjectsForAllRooms();
		if (highlightedObject != null) {
			batch.draw(highlightTexture, highlightBoxX, highlightBoxY, highlightBox.getWidth(), highlightBox.getHeight());
		}
		batch.end();
	}

	/*
	 *	RENDER THREAD
	 */
	private void renderDungeonBackgroundObjectsForAllRooms() {
		for (int x = 0; x < viewModel.roomViews.length; x++) {
			for (int y = 0; y < viewModel.roomViews[0].length; y++) {
				//ViewRoom room = viewModel.getRoom(x, y);
				ViewRoom room = viewModel.roomViews[x][y];

				// fetch prepared render information
				List<Pair<GraphicObject, TextureAtlas.AtlasRegion>> renderInformation = room.getBackgroundObjectsForRoom();
				if (renderInformation == null || renderInformation.isEmpty()) {
					// no render information yet, we need to fetch object information about the room and update the ViewRoom with it
					List<GraphicObject> graphicObjectsForRoom = dungeonObjectRenderer.createGraphicObjectsForRoom(room.getRoomInfo(), x * WorldRenderer.ROOM_SIZE, y * WorldRenderer.ROOM_SIZE);
					room.setGraphicObjects(graphicObjectsForRoom);
					renderInformation = room.getBackgroundObjectsForRoom();
				}

				// actual drawing call
				drawGraphicObjectsToSpritebatch(renderInformation, x, y);
			}
		}
	}

	/*
	 *	RENDER THREAD
	 */
	private void renderFigureObjectsForAllRooms() {

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

	/*
	 *	RENDER THREAD
	 */
	private void drawGraphicObjectsToSpritebatch(List<Pair<GraphicObject, TextureAtlas.AtlasRegion>> graphicObjectsForRoom, int x, int y) {
		int roomOffsetX = viewModel.roomOffSetsX[x][y];
		int roomOffsetY = viewModel.roomOffSetsY[x][y];


		for (Pair<GraphicObject, TextureAtlas.AtlasRegion> pair : graphicObjectsForRoom) {
			GraphicObject graphicObject = pair.getA();

			// check for animation for this figure
			if (graphicObject.getClickableObject() instanceof FigureInfo) {
				FigureInfo figure = (FigureInfo) graphicObject.getClickableObject();
				AnimationFrame animationImage = animationManager.getAnimationImage(figure, this.viewModel.roomViews[x][y].getRoomInfo());
				if (animationImage != null) {
					JDDimension figureInfoSize = dungeonObjectRenderer.getFigureInfoSize(figure);
					JDImageLocated locatedImage = animationImage.getLocatedImage(roomOffsetX, roomOffsetY, figureInfoSize
							.getWidth(), figureInfoSize.getHeight());
					TextureAtlas.AtlasRegion atlasRegionAnimationStep = Assets.instance.getAtlasRegion(locatedImage.getImage(), atlasMap
							.get(figure.getFigureClass()));
					if (atlasRegionAnimationStep != null) {
						batch.draw(atlasRegionAnimationStep, locatedImage.getX(roomOffsetX), locatedImage.getY(roomOffsetY), locatedImage
								.getWidth(), locatedImage.getHeight());
					}

					// we had an animation so we finish of this object
					continue;
				}
			}

			TextureAtlas.AtlasRegion atlasRegion = pair.getB();
			// no animation present for this object
			if (graphicObject instanceof JDGraphicObject) {
				if (atlasRegion != null) {
					JDImageLocated locatedImage = ((JDGraphicObject) graphicObject).getLocatedImage();
					batch.draw(atlasRegion, locatedImage.getX(roomOffsetX), locatedImage.getY(roomOffsetY), locatedImage
							.getWidth(), locatedImage.getHeight());
				}
			}
			else {
				if (atlasRegion != null) {
					DrawingRectangle destinationRectangle = graphicObject.getRectangle();
					batch.draw(atlasRegion, destinationRectangle.getX(roomOffsetX), destinationRectangle.getY(roomOffsetY), destinationRectangle
							.getWidth(), destinationRectangle.getHeight());
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

	/**
	 * Handles a mouse click on the world, doing
	 * either a delegate to the ActionAssembler or
	 * setting the focus on the clicked world object
	 * using the FocusManager
	 *
	 * @param screenX    world coordinate x
	 * @param screenY    world coordinate y
	 * @param pointer    todo: what is it?
	 * @param button     button (mouse: button 0 = left click; button 1 = right click)
	 * @param controller connection to player to delegate further action to
	 * @return whether the click event has been processed
	 */
	public boolean checkWorldClick(int screenX, int screenY, int pointer, int button, PlayerController controller) {

		// reset highlight object information
		highlightedObject = null;
		highlightBox = null;

		Vector3 worldPosUnprojected = camera.unproject(new Vector3(screenX, screenY, 0));
		int worldXunprojected = Math.round(worldPosUnprojected.x);
		int worldYunprojected = Math.round(worldPosUnprojected.y);

		int roomX = worldXunprojected / ROOM_SIZE;
		int roomY = worldYunprojected / ROOM_SIZE;

		ViewRoom room = this.viewModel.getRoom(roomX, roomY);
		if (room == null) return false;

		GraphicObject clickedGraphicObject = room.findClickedObjectInRoom(new JDPoint(worldXunprojected, worldYunprojected), roomX * ROOM_SIZE, roomY * ROOM_SIZE);
		if (clickedGraphicObject != null) {
			Object clickedObject = clickedGraphicObject.getClickableObject();

			if (clickedObject != null) {

				if (button == 1 // button 1 is right-click
						|| clickedObject.equals(focusManager.getWorldFocusObject())) {
					// object was already highlighted before
					// hence we can trigger an action
					//actionAssembler.objectClicked(clickedObject, true);

					controller.getActionAssembler().objectClicked(clickedObject, false);
					focusManager.setWorldFocusObject((clickedGraphicObject));

					// exception for chest handing, to be solved better one day
					if (clickedObject instanceof ChestInfo) {
						EventManager.getInstance().fireEvent(new ToggleChestViewEvent());
					}
				}
				else {
					// remember some data for rendering of highlight box
					focusManager.setWorldFocusObject((clickedGraphicObject));
					DrawingRectangle rectangle = clickedGraphicObject.getRectangle();
					highlightBoxX = rectangle.getX(roomX * ROOM_SIZE);
					highlightBoxY = rectangle.getY(roomY * ROOM_SIZE);
					highlightBox = createHighlightBoxPixMap(rectangle.getWidth(), rectangle.getHeight());
					highlightTexture = new Texture(highlightBox);
					highlightedObject = clickedObject;
				}

				return true;
			}
		}
		return false;
	}

	private int highlightBoxX;
	private int highlightBoxY;
	private Pixmap highlightBox;
	private Texture highlightTexture;
	private Object highlightedObject;

	private Pixmap createHighlightBoxPixMap(int width, int height) {
		Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

		pixmap.setColor(Color.YELLOW);
		pixmap.drawRectangle(0, 0, width, height);
		return pixmap;
	}
}
