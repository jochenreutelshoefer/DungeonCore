package de.jdungeon.world;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.jdungeon.animation.AnimationFrame;
import de.jdungeon.animation.AnimationManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import de.jdungeon.dungeon.ChestInfo;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.event.EventManager;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.FigurePresentation;
import de.jdungeon.figure.RoomObservationStatus;
import de.jdungeon.dungeon.RoomInfoEntity;
import de.jdungeon.graphics.GraphicObject;
import de.jdungeon.graphics.GraphicObjectRenderer;
import de.jdungeon.graphics.JDGraphicObject;
import de.jdungeon.graphics.JDImageLocated;
import de.jdungeon.graphics.JDImageProxy;
import de.jdungeon.log.Log;
import de.jdungeon.util.JDDimension;

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
	private SpriteBatch worldSpritebatch;
	public static final int ROOM_SIZE = 80;

	// for offscreen background image generation
	private final static boolean USE_OFFSCREEN_FRAMEBUFFER_FOR_BACKGROUND = false;
	private FrameBuffer offscreenBackgroundFramebuffer;
	private final SpriteBatch offscreenSpritebatch = new SpriteBatch();
	private Texture backgroundTexture;

	private static final float FRAMEBUFFER_SCALER = 1.0f;

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
		worldSpritebatch = new SpriteBatch();

		viewModel.initGraphicObjects(dungeonObjectRenderer);

		cameraHelper.setZoom(1f);

		worldSpritebatch.setProjectionMatrix(camera.combined);

		camera.update();

		// create new framebuffer of required size (large enough to draw entire dungeon)
		if (USE_OFFSCREEN_FRAMEBUFFER_FOR_BACKGROUND) {
			float screenRatio = ((float)Gdx.app.getGraphics().getWidth()) / Gdx.app.getGraphics().getHeight();
			int worldRenderSizeX = this.viewModel.roomViews.length * ROOM_SIZE;
			int worldRenderSizeY = this.viewModel.roomViews[0].length * ROOM_SIZE;
			int framebufferWidth = (int) (worldRenderSizeX * FRAMEBUFFER_SCALER);
			int frameBufferHeight = (int) (worldRenderSizeY * FRAMEBUFFER_SCALER);
			offscreenBackgroundFramebuffer = new FrameBuffer(Pixmap.Format.RGBA8888, (int) (framebufferWidth * 1), frameBufferHeight, false);



			Matrix4 matrix = new Matrix4();
			//matrix.setToOrtho2D(0, 0, framebufferWidth  , framebufferWidth ); // here is the actual size you want
			//offscreenSpritebatch.setProjectionMatrix(matrix);
			//batch.setProjectionMatrix(cam.combined);

		}

		GL20 gl = Gdx.gl20;
		int programObject = gl.glCreateProgram();
		Log.info("position de.jdungeon.location: " + gl.glGetAttribLocation(programObject, "position"));
	}

	/*
	 *	RENDER THREAD
	 */
	public void render() {
		cameraHelper.applyTo(camera);
		worldSpritebatch.setProjectionMatrix(camera.combined);
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

		if (USE_OFFSCREEN_FRAMEBUFFER_FOR_BACKGROUND) {
			// offscreen framebuffer mode - check first whether update of background texture is necessary
			if (backgroundTexture == null || viewModel.getResetBackgroundUpdateRequired()) {
				this.backgroundTexture = updateBackgroundTexture();
			}

			//int worldRenderSizeX = this.viewModel.roomViews.length * ROOM_SIZE;
			//int worldRenderSizeY = this.viewModel.roomViews[0].length * ROOM_SIZE;

			float screenRatio = ((float)Gdx.app.getGraphics().getWidth()) / Gdx.app.getGraphics().getHeight();

			// draw background texture
			worldSpritebatch.begin();
			worldSpritebatch.draw(backgroundTexture,
					0,
					0,
					this.viewModel.roomViews.length * ROOM_SIZE,
					this.viewModel.roomViews[0].length * ROOM_SIZE,
					0,
					0,
					(int) (offscreenBackgroundFramebuffer.getWidth()),
					(int) (offscreenBackgroundFramebuffer.getHeight()),
					false,
					true);

			/*
						worldSpritebatch.draw(backgroundTexture,
					0,
					0,
					offscreenBackgroundFramebuffer.getWidth() / FRAMEBUFFER_SCALER,
					offscreenBackgroundFramebuffer.getHeight() / FRAMEBUFFER_SCALER,
					0,
					0,
					(int) (offscreenBackgroundFramebuffer.getWidth()),
					(int) (offscreenBackgroundFramebuffer.getHeight()),
					false,
					true);
			 */
		}
		else {
			worldSpritebatch.begin();
			renderDungeonBackgroundObjectsForAllRooms(worldSpritebatch, 1);
		}
		renderFigureObjectsForAllRooms();

		worldSpritebatch.end();
	}

	private Texture updateBackgroundTexture() {
		// prepare and activate offscreen framebuffer
		this.offscreenBackgroundFramebuffer.bind();
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.offscreenSpritebatch.begin();

		// draw actual content
		//..
		renderDungeonBackgroundObjectsForAllRooms(offscreenSpritebatch, FRAMEBUFFER_SCALER);

		// close and unbind SpriteBatch and Framebuffer
		this.offscreenSpritebatch.end();
		this.offscreenBackgroundFramebuffer.unbind();

		Texture newBackgroundTexture = this.offscreenBackgroundFramebuffer.getColorBufferTexture();
		// does not seem to help...
		//newBackgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.MipMap);
		return newBackgroundTexture;
	}

	/*
	 *	RENDER THREAD
	 */
	private void renderDungeonBackgroundObjectsForAllRooms(SpriteBatch batch, float scale) {
		for (int x = 0; x < viewModel.roomViews.length; x++) {
			for (int y = 0; y < viewModel.roomViews[0].length; y++) {
				if (viewModel.getVisStatus(x, y) < RoomObservationStatus.VISIBILITY_FOUND) continue;
				//ViewRoom room = viewModel.getRoom(x, y);

				// fetch prepared render information
				Array<Pair<GraphicObject, TextureAtlas.AtlasRegion>> renderInformation = viewModel.roomViews[x][y].getBackgroundObjectsForRoom();
				if (renderInformation == null || renderInformation.isEmpty()) {

					// no render information yet, we need to fetch object information about the room and update the ViewRoom with it
					List<GraphicObject> graphicObjectsForRoom = dungeonObjectRenderer.createGraphicObjectsForRoom(viewModel.roomViews[x][y].getRoomInfo());
					viewModel.roomViews[x][y].setGraphicObjects(graphicObjectsForRoom);
					renderInformation = viewModel.roomViews[x][y].getBackgroundObjectsForRoom();
				}

				// actual drawing call
				drawGraphicObjectsToSpritebatch(renderInformation, x, y, batch, scale);
			}
		}
	}

	/*
	 *	RENDER THREAD
	 */
	private void renderFigureObjectsForAllRooms() {

		// iterate first for de.jdungeon.figure classes to have less atlas switches as each de.jdungeon.figure has a distinct atlas
		for (FigurePresentation figureClass : FigurePresentation.values()) {
			for (int x = 0; x < viewModel.getDungeonWidth(); x++) {
				for (int y = 0; y < viewModel.getDungeonHeight(); y++) {
					Array<Pair<GraphicObject, TextureAtlas.AtlasRegion>> graphicObjectsForRoom = viewModel.roomViews[x][y]
							.getFigureObjects(figureClass);
					if (graphicObjectsForRoom != null) {
						drawGraphicObjectsToSpritebatch(graphicObjectsForRoom, x, y, worldSpritebatch, 1);
					}
				}
			}
		}
	}

	private final double SPRITE_SIZE_RATIO_128_TO_96 = ((float) 128) / 96;
	private final double SPRITE_SIZE_RATIO_192_TO_96 = ((float) 192) / 96;
	//private final double SPRITE_SIZE_RATIO_128_TO_96 = ((float)96) / 128;

	/*
	 *	RENDER THREAD
	 */
	private void drawGraphicObjectsToSpritebatch(Array<Pair<GraphicObject, TextureAtlas.AtlasRegion>> graphicObjectsForRoom, int x, int y, SpriteBatch batch, float scale) {
		int roomOffsetX = viewModel.roomOffSetsX[x][y];
		int roomOffsetY = viewModel.roomOffSetsY[x][y];

		for (Pair<GraphicObject, TextureAtlas.AtlasRegion> pair : graphicObjectsForRoom) {

			// check for de.jdungeon.animation for this de.jdungeon.figure
			Object clickableObject = pair.getA().getClickableObject();
			if (clickableObject instanceof FigureInfo) {
				FigureInfo figure = (FigureInfo) clickableObject;
				AnimationFrame animationImage = animationManager.getAnimationImage(figure, this.viewModel.roomViews[x][y]
						.getRoomInfo());
				if (animationImage != null) {
					JDImageLocated locatedImage = animationImage.getLocatedImage(roomOffsetX, roomOffsetY, dungeonObjectRenderer
							.getFigureInfoSize(figure)
							.getWidth(), dungeonObjectRenderer.getFigureInfoSize(figure).getHeight());
					TextureAtlas atlas = Assets.instance.atlasMap.get(figure.getFigurePresentation());
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
							int width = locatedImage.getWidth();
							int height = locatedImage.getHeight();
							drawAltasRegionAdaptSpriteSize(batch, atlasRegionAnimationStep, imageX, imageY, width, height, locatedImage
									.getImage(), pair.getA(), scale);
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

					// we had an de.jdungeon.animation so we finish off this object
					continue;
				}
			}

			// no de.jdungeon.animation present for this object
			if (pair.getA() instanceof JDGraphicObject) {
				if (pair.getB() != null) {
					int width = ((JDGraphicObject) pair.getA()).getLocatedImage().getWidth();
					int height = ((JDGraphicObject) pair.getA()).getLocatedImage().getHeight();
					drawAltasRegionAdaptSpriteSize(batch, pair.getB(),
							((JDGraphicObject) pair.getA()).getLocatedImage().getX(roomOffsetX),
							((JDGraphicObject) pair.getA()).getLocatedImage().getY(roomOffsetY),
							width,
							height,
							((JDGraphicObject) pair.getA()).getLocatedImage().getImage(),
							pair.getA(),
							scale);
				}
			}
			else {
				if (pair.getB() != null) {
					drawAltasRegionAdaptSpriteSize(batch, pair.getB(),
							pair.getA().getRectangle().getX(roomOffsetX),
							pair.getA().getRectangle().getY(roomOffsetY),
							pair.getA().getRectangle().getWidth(),
							pair.getA().getRectangle().getHeight(),
							pair.getA().getImage(),
							pair.getA(),
							scale);
				}
			}
		}
	}

	private void drawAltasRegionAdaptSpriteSize(SpriteBatch batch, TextureAtlas.AtlasRegion atlasRegion, int x, int y, int width, int height, JDImageProxy image, GraphicObject clickObject, float scale) {
		x *= scale;
		y *= scale;
		width *= scale;
		height *= scale;

		int posX = x;
		int posY = y;
		int drawWidth = width;
		int drawHeight = height;

		if (clickObject.getClickableObject() instanceof FigureInfo) {
			// we have to cope with different sprites sizes unfortunately (within one de.jdungeon.figure de.jdungeon.animation set)
			int originalSpriteWidth = atlasRegion.originalWidth;
			if (originalSpriteWidth != atlasRegion.originalHeight) {
				Gdx.app.error(TAG, "Warning: not an  quadratic sprite: " + image.getFilenameBlank() + " original width: " + originalSpriteWidth + "; height: " + atlasRegion.originalHeight);
			}
			if (originalSpriteWidth == 96) {
				// is okay
			}
			else if (originalSpriteWidth == 128) {
				// adapt values for 128er sprites
				drawWidth = (int) (width * SPRITE_SIZE_RATIO_128_TO_96);
				posX = x - ((drawWidth - width) / 2); // shift left half size adjustment
				drawHeight = (int) (height * SPRITE_SIZE_RATIO_128_TO_96);
				posY = y - ((drawHeight - height) / 2); // shift up half size adjustment
			}
			else if (originalSpriteWidth == 192) {
				// adapt values for 128er sprites
				drawWidth = (int) (width * SPRITE_SIZE_RATIO_192_TO_96);
				posX = x - ((drawWidth - width) / 2); // shift left half size adjustment
				drawHeight = (int) (height * SPRITE_SIZE_RATIO_192_TO_96);
				posY = y - ((drawHeight - height) / 2); // shift up half size adjustment
			}
			else {
				Gdx.app.error(TAG, "Warning: unknown sprite size : " + image.getFilenameBlank() + " original width: " + originalSpriteWidth + "; height: " + atlasRegion.originalHeight);
			}
		}

		// finally draw
		batch.draw(atlasRegion,
				posX,
				posY,
				drawWidth,
				drawHeight);

		RoomInfoEntity worldFocusObject = focusManager.getWorldFocusObject();
		if (worldFocusObject != null && worldFocusObject.equals(clickObject.getClickableObject())) {
			setHighlightBox(x, y, width, height, batch);
		}
	}

	public void resize(int width, int height) {
		init();
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
		camera.update();

	}

	@Override
	public void dispose() {
		worldSpritebatch.dispose();
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
	 * @return whether the click de.jdungeon.event has been processed
	 */
	public boolean checkWorldClick(int screenX, int screenY, int pointer, int button, PlayerController controller) {

		// reset highlight object information
		highlightedObject = null;

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
				}

				return true;
			}
		}
		else {
			// 'nothing' has been clicked by the de.jdungeon.user, hence we de-select the selected object
			focusManager.setWorldFocusObject((RoomInfoEntity) null);
		}
		return false;
	}

	private void setHighlightBox(int x, int y, int width, int height, SpriteBatch batch) {
		highlightBoxX = x;
		highlightBoxY = y;
		highlightTexture = highlightingBoxTextureCache.getTexture(width, height);
		batch.draw(highlightTexture, highlightBoxX, highlightBoxY, highlightTexture.getWidth(), highlightTexture
				.getHeight());
	}

	private int highlightBoxX;
	private int highlightBoxY;
	private Texture highlightTexture;
	private Object highlightedObject;

	private Pixmap createHighlightBoxPixMap(int width, int height) {
		Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.YELLOW);
		pixmap.drawRectangle(0, 0, width, height);
		return pixmap;
	}

	public void invalidateEntityRenderCache(RoomInfoEntity location) {
		this.dungeonObjectRenderer.invalidateCache(location);
	}

	private final HighlightingBoxTextureCache highlightingBoxTextureCache = new HighlightingBoxTextureCache();

	static class HighlightingBoxTextureCache {

		private final Map<JDDimension, Texture> cache = new HashMap<>();

		Texture getTexture(int width, int height) {
			JDDimension requestDimension = new JDDimension(width, height);
			if (cache.containsKey(requestDimension)) {
				return cache.get(requestDimension);
			}
			else {
				Pixmap pixmap = new Pixmap((int) requestDimension.getWidth(), (int) requestDimension.getHeight(), Pixmap.Format.RGBA8888);
				pixmap.setColor(Color.YELLOW);
				pixmap.drawRectangle(0, 0, (int) requestDimension.getWidth(), requestDimension.getHeight());
				Texture texture = new Texture(pixmap);
				cache.put(requestDimension, texture);
				return texture;
			}
		}
	}
}
