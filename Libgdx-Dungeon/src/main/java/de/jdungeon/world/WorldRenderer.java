package de.jdungeon.world;

import java.util.List;

import animation.AnimationFrame;
import animation.AnimationManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import dungeon.ChestInfo;
import dungeon.JDPoint;
import event.EventManager;
import figure.Figure;
import figure.FigureInfo;
import figure.RoomObservationStatus;
import graphics.GraphicObject;
import graphics.GraphicObjectRenderer;
import graphics.JDGraphicObject;
import graphics.JDImageLocated;
import graphics.JDImageProxy;
import graphics.util.DrawingRectangle;
import log.Log;

import de.jdungeon.CameraHelper;
import de.jdungeon.Constants;
import de.jdungeon.app.gui.smartcontrol.ToggleChestViewEvent;
import de.jdungeon.asset.AssetFonts;
import de.jdungeon.asset.Assets;
import de.jdungeon.gui.LibgdxFocusManager;
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
	private final LibgdxFocusManager focusManager;
	private final AnimationManager animationManager;
	private final GraphicObjectRenderer dungeonObjectRenderer;
	private final CameraHelper cameraHelper;
	private SpriteBatch batch;
	public static final int ROOM_SIZE = 80;

	public WorldRenderer(GraphicObjectRenderer graphicObjectRenderer, ViewModel viewModel, OrthographicCamera camera, CameraHelper cameraHelper, LibgdxFocusManager focusManager, AnimationManager animationManager) {
		this.dungeonObjectRenderer = graphicObjectRenderer;
		this.cameraHelper = cameraHelper;
		this.viewModel = viewModel;
		this.camera = camera;
		this.focusManager = focusManager;
		this.animationManager = animationManager;
		init();
	}

	private void init() {
		batch = new SpriteBatch();
		viewModel.initGraphicObjects(dungeonObjectRenderer);

		cameraHelper.setZoom(1f);

		batch.setProjectionMatrix(camera.combined);

		camera.update();

		Assets.instance.initAtlasMap();

		GL20 gl = Gdx.gl20;
		int programObject = gl.glCreateProgram();
		System.out.println("position location: " + gl.glGetAttribLocation(programObject, "position"));
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

	long lastCall;

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
				if (viewModel.geVisStatus(x, y) < RoomObservationStatus.VISIBILITY_FOUND) continue;
				//ViewRoom room = viewModel.getRoom(x, y);

				// fetch prepared render information
				Array<Pair<GraphicObject, TextureAtlas.AtlasRegion>> renderInformation = viewModel.roomViews[x][y].getBackgroundObjectsForRoom();
				if (renderInformation == null || renderInformation.isEmpty()) {
					// no render information yet, we need to fetch object information about the room and update the ViewRoom with it
					List<GraphicObject> graphicObjectsForRoom = dungeonObjectRenderer.createGraphicObjectsForRoom(viewModel.roomViews[x][y]
							.getRoomInfo(), viewModel.roomOffSetsX[x][y], viewModel.roomOffSetsY[x][y]);
					viewModel.roomViews[x][y].setGraphicObjects(graphicObjectsForRoom);
					renderInformation = viewModel.roomViews[x][y].getBackgroundObjectsForRoom();
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
		for (Class<? extends Figure> figureClass : Assets.figureClasses) {
			for (int x = 0; x < viewModel.getDungeonWidth(); x++) {
				for (int y = 0; y < viewModel.getDungeonHeight(); y++) {
					Array<Pair<GraphicObject, TextureAtlas.AtlasRegion>> graphicObjectsForRoom = viewModel.roomViews[x][y]
							.getFigureObjects(figureClass);
					if (graphicObjectsForRoom != null) {
						drawGraphicObjectsToSpritebatch(graphicObjectsForRoom, x, y);
					}
				}
			}
		}
	}

	/*
	 *	RENDER THREAD
	 */
	private void drawGraphicObjectsToSpritebatch(Array<Pair<GraphicObject, TextureAtlas.AtlasRegion>> graphicObjectsForRoom, int x, int y) {
		int roomOffsetX = viewModel.roomOffSetsX[x][y];
		int roomOffsetY = viewModel.roomOffSetsY[x][y];

		for (Pair<GraphicObject, TextureAtlas.AtlasRegion> pair : graphicObjectsForRoom) {

			// check for animation for this figure
			if (pair.getA().getClickableObject() instanceof FigureInfo) {
				FigureInfo figure = (FigureInfo) pair.getA().getClickableObject();
				AnimationFrame animationImage = animationManager.getAnimationImage(figure, this.viewModel.roomViews[x][y]
						.getRoomInfo());
				if (animationImage != null) {
					JDImageLocated locatedImage = animationImage.getLocatedImage(roomOffsetX, roomOffsetY, dungeonObjectRenderer
							.getFigureInfoSize(figure)
							.getWidth(), dungeonObjectRenderer.getFigureInfoSize(figure).getHeight());
					TextureAtlas atlas = Assets.instance.atlasMap.get(figure.getFigureClass());
					if (locatedImage == null) {
						Log.warning("Located Image is null: " + figure + " - " + animationImage);
						continue;
					}
					JDImageProxy<?> image = locatedImage.getImage();
					if (image != null) {
						TextureAtlas.AtlasRegion atlasRegionAnimationStep = Assets.instance.getAtlasRegion(image, atlas);
						if (atlasRegionAnimationStep != null) {
							int imageX = locatedImage.getX(roomOffsetX);
							int imageY = locatedImage.getY(roomOffsetY);
							batch.draw(atlasRegionAnimationStep,
									imageX,
									imageY,
									locatedImage.getWidth(),
									locatedImage.getHeight());
							String text = null;
							JDPoint textOffset = null;
							if (animationImage.getText() != null) {
								text = animationImage.getText();
								textOffset = animationImage.getTextCoordinatesOffset();
								BitmapFont font = AssetFonts.instance.hit;
								font.draw(batch, text, imageX + textOffset.getX(), imageY + textOffset.getY());
							}
						}
					}

					// we had an animation so we finish off this object
					continue;
				}
			}

			// no animation present for this object
			if (pair.getA() instanceof JDGraphicObject) {
				if (pair.getB() != null) {
					batch.draw(pair.getB(),
							((JDGraphicObject) pair.getA()).getLocatedImage().getX(roomOffsetX),
							((JDGraphicObject) pair.getA()).getLocatedImage().getY(roomOffsetY),
							((JDGraphicObject) pair.getA()).getLocatedImage().getWidth(),
							((JDGraphicObject) pair.getA()).getLocatedImage().getHeight());
				}
			}
			else {
				if (pair.getB() != null) {
					batch.draw(pair.getB(),
							pair.getA().getRectangle().getX(roomOffsetX),
							pair.getA().getRectangle().getY(roomOffsetY),
							pair.getA().getRectangle().getWidth(),
							pair.getA().getRectangle().getHeight());
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
