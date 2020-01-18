package de.jdungeon.app.screen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Logger;

import animation.AnimationManager;
import animation.DefaultAnimationSet;
import animation.DefaultAnimationTask;
import control.JDGUIEngine2D;
import dungeon.JDPoint;
import dungeon.Position;
import dungeon.RoomInfo;
import event.Event;
import event.EventListener;
import event.EventManager;
import event.WorldChangedEvent;
import figure.FigureInfo;
import figure.action.Action;
import figure.action.ShrineAction;
import figure.action.StepAction;
import figure.hero.Hero;
import figure.hero.HeroInfo;
import figure.percept.Percept;
import game.InfoEntity;
import game.PerceptHandler;
import game.RoomInfoEntity;
import graphics.GraphicObject;
import graphics.GraphicObjectRenderer;
import graphics.JDImageProxy;
import gui.Paragraphable;
import item.ItemInfo;
import text.Statement;
import user.DefaultDungeonSession;
import util.JDColor;
import util.JDDimension;

import de.jdungeon.app.ActionController;
import de.jdungeon.app.DrawUtils;
import de.jdungeon.app.GameScreenPerceptHandler;
import de.jdungeon.app.audio.MusicManager;
import de.jdungeon.app.event.FocusEvent;
import de.jdungeon.app.event.InfoObjectClickedEvent;
import de.jdungeon.app.event.VisibilityIncreasedEvent;
import de.jdungeon.app.gui.ColorConverter;
import de.jdungeon.app.gui.FocusManager;
import de.jdungeon.app.gui.GUIElement;
import de.jdungeon.app.gui.GUIImageManager;
import de.jdungeon.app.gui.GameOverView;
import de.jdungeon.app.gui.HealthBar;
import de.jdungeon.app.gui.HourGlassTimer;
import de.jdungeon.app.gui.ImageGUIElement;
import de.jdungeon.app.gui.InfoPanel;
import de.jdungeon.app.gui.InventoryPanel;
import de.jdungeon.app.gui.Popup;
import de.jdungeon.app.gui.TextPerceptView;
import de.jdungeon.app.gui.ZoomButton;
import de.jdungeon.app.gui.smartcontrol.ToggleChestViewEvent;
import de.jdungeon.app.gui.activity.TakeItemButtonClickedEvent;
import de.jdungeon.app.gui.smartcontrol.ShrineButtonClickedEvent;
import de.jdungeon.app.gui.smartcontrol.SmartControl;
import de.jdungeon.app.movieSequence.DefaultMovieSequence;
import de.jdungeon.app.movieSequence.CameraFlightSequence;
import de.jdungeon.app.movieSequence.CameraFlightSequenceManager;
import de.jdungeon.app.movieSequence.StraightLineScroller;
import de.jdungeon.app.movieSequence.TrivialScaleSequence;
import de.jdungeon.app.movieSequence.ZoomSequence;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input.TouchEvent;
import de.jdungeon.game.MotionEvent;
import de.jdungeon.game.Music;
import de.jdungeon.game.ScrollMotion;
import de.jdungeon.user.Session;
import de.jdungeon.util.PaintBuilder;
import de.jdungeon.util.Pair;

public class GameScreen extends StandardScreen implements EventListener, PerceptHandler {

	private static final int SCALE_ROOM_FIGHT_MODE = 180;
	private static final int SCALE_ROOM_DEFAULT = 150;
	private HeroInfo figureInfo;

	private GraphicObjectRenderer dungeonRenderer;

	private final Map<JDPoint, List<GraphicObject>> drawnObjects = new HashMap<>();
	private final Map<JDPoint, Image> drawnRooms = new HashMap<>();
	private final CameraFlightSequenceManager sequenceManager = new CameraFlightSequenceManager();
	private InfoPanel infoPanel;
	private TextPerceptView textPerceptView;
	private GameOverView gameOverView = null;
	private Popup messagePopup = null;

	private JDPoint viewportPosition;
	private float roomSize = 180;
	private float preFightRoomSize;
	private final int maxRoomSize = 400;
	private final int minRoomSize = 100;
	private ActionController actionAssembler;

	private long lastDoubleTapEventTime = -1;
	private final RenderTimeLog renderTimeLog = new RenderTimeLog();

	private long lastTouchEventTime = -1;
	private long lastScaleEventTime = -1;
	private long lastScrollEventTime = -1;
	private boolean touchEventAfterPaint = false;
	private SmartControl smartControl;

	public JDGUIEngine2D getGui() {
		return gui;
	}

	private final JDGUIEngine2D gui;

	private DefaultDungeonSession session;

	private final GameScreenPerceptHandler perceptHandler;

	private boolean worldHasChanged = true;
	private FocusManager focusManager;

	public long counterCreatingRoomDrawingObjects = 0;
	public long counterReusingRoomDrawingObjects = 0;

	public GameScreen(Game game, JDGUIEngine2D gui) {
		super(game);
		setSession(game.getSession());
		this.gui = gui;
		perceptHandler = new GameScreenPerceptHandler(this);

		// be event listener
		EventManager.getInstance().registerListener(this);

		Logger.getLogger(this.getClass().getName()).info("GameScreen Created");
	}

	@Override
	public void init() {
		Logger.getLogger(this.getClass().getName()).info(("GameScreen init start"));
		Music music = this.game.getAudio().createMusic("music/" + "Eyes_Gone_Wrong.mp3");
		MusicManager.getInstance().playMusic(music);

		Hero hero = this.session.getCurrentHero();
		figureInfo = this.session.getHeroInfo();

		perceptHandler.init();

		gui.setFigure(figureInfo);

		hero.setControl(gui);


		actionAssembler = new ActionController(figureInfo, gui);


		initGUIElements();
		this.session.startGame(gui);

		JDPoint heroRoomNumber = figureInfo.getRoomNumber();
		centerOn(heroRoomNumber);
		scrollTo(heroRoomNumber, 100f, "init");

		// trigger complete re-render
		worldHasChanged = true;

		resetDungeonRenderer();
		Logger.getLogger(this.getClass().getName()).info("GameScreen init finished");
	}

	private void setSession(Session session) {
		if (session instanceof DefaultDungeonSession) {
			this.session = ((DefaultDungeonSession) session);
		}
		else {
			Logger.getLogger(this.getClass().getName()).severe( "Invalid session object, DungeonSession expected.");
			System.exit(0);
		}
	}

	private void initGUIElements() {
		Logger.getLogger(this.getClass().getName()).info("Start init GUI-elements");
		/*
		 * init text messages panel
		 */
		this.guiElements.clear();
		textPerceptView = new TextPerceptView(this);
		this.guiElements.add(textPerceptView);

		/*
		 * init info panel
		 */
		int infoPanelWidth = (int) (game.getScreenWidth() * 0.2);
		int infoPanelHeight = (int) (game.getScreenHeight() * 0.4);
		infoPanel = new InfoPanel(new JDPoint(game.getScreenWidth()  - infoPanelWidth, 0),
				new JDDimension(infoPanelWidth, infoPanelHeight), this, this.getGame());
		this.guiElements.add(infoPanel);
		focusManager = new FocusManager(infoPanel, figureInfo);

		/*
		 * init health bars
		 */
		int posX = 22;
		JDPoint healthBarPosition = new JDPoint(posX, 5);
		HealthBar healthView = new HealthBar(healthBarPosition,
				new JDDimension(160, 20), figureInfo, HealthBar.Kind.health, this.getGame());
		this.guiElements.add(healthView);
		JDPoint dustBarPosition = new JDPoint(posX, 25);
		HealthBar dustView = new HealthBar(dustBarPosition,
				new JDDimension(160, 20), figureInfo, HealthBar.Kind.dust,  this.getGame());
		this.guiElements.add(dustView);

		/*
		 * init hour glass
		 */
		HourGlassTimer hourglass = new HourGlassTimer(new JDPoint(
				30,
				50), new JDDimension(36, 60), this, figureInfo,  this.getGame());
		this.guiElements.add(hourglass);


		/*
		add +/- magnifier
		 */
		ImageGUIElement magnifier = new ImageGUIElement(new JDPoint(26, 156), new JDDimension(44, 70), getGUIImage(GUIImageManager.LUPE2), getGame()) {

			@Override
			public boolean handleTouchEvent(TouchEvent touch) {
				scrollTo(figureInfo.getRoomNumber(), 30, SCALE_ROOM_DEFAULT, "user reseted view port with magnifier button");
				return true;
			}
		};
		this.guiElements.add(new ZoomButton(new JDPoint(30, 120), new JDDimension(36, 36), this, getGUIImage(GUIImageManager.PLUS), true));
		this.guiElements.add(new ZoomButton(new JDPoint(30, 224), new JDDimension(36, 36), this, getGUIImage(GUIImageManager.MINUS), false));
		this.guiElements.add(magnifier);

		smartControl = new SmartControl(new JDPoint(0, 0), getScreenSize(), this, game, figureInfo, actionAssembler, focusManager);
		this.guiElements.add(smartControl);

		/*
		 * init inventory panel
		 */
		InventoryPanel inventory = new InventoryPanel(figureInfo, this);
		this.guiElements.add(inventory);

		/*
		 * init game over view
		 */
		int width = game.getScreenWidth();
		int height = game.getScreenHeight();
		int widthFifth = (width / 5);
		int heightFifth = (height / 4);
		gameOverView = new GameOverView(new JDPoint((width / 2) - widthFifth,
				(height / 2) - heightFifth), new JDDimension(2 * widthFifth,
				2 * heightFifth), this, this.getGame());
		this.guiElements.add(gameOverView);

		/*
		 * init general message popup
		 */
		messagePopup = new Popup(new JDPoint((width / 2) - widthFifth,
				(height / 2) - heightFifth), new JDDimension(2 * widthFifth,
				2 * heightFifth),  this.getGame(), new InfoMessageClearPopupEvent());
		this.guiElements.add(messagePopup);

		Logger.getLogger(this.getClass().getName()).info("Finished init GUI-elements");
	}

	private Image getGUIImage(String filename) {
		return (Image) GUIImageManager.getImageProxy(filename, game.getFileIO().getImageLoader()).getImage();
	}

	@Override
	public Game getGame() {
		return game;
	}


	public ActionController getActionAssembler() {
		return actionAssembler;
	}

	private void resetDungeonRenderer() {
		this.dungeonRenderer = new GraphicObjectRenderer((int) roomSize, gui);
	}

	private void centerOn(JDPoint roomNumber) {
		centerOn((float) (roomNumber.getX() + 0.5),
				(float) (roomNumber.getY() + 0.5));
	}

	private void centerOn(int roomNumberX, int roomNumberY) {
		centerOn((float) (roomNumberX + 0.5), (float) (roomNumberY + 0.5));
	}

	private void centerOn(Pair<Float, Float> pair) {
		centerOn((pair.getA()), (pair.getB()));
	}

	private void centerOn(float x, float y) {
		viewportPosition = new JDPoint(x * roomSize
				- (game.getScreenWidth() / 2) + (this.roomSize / 2), y
				* roomSize - (game.getScreenHeight() / 2) + (this.roomSize / 2));
	}

	@Override
	public void backButton() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}


	private static class RenderTimeLog {

		private final int bufferSize = 10;
		private final long[] durations = new long[bufferSize];
		private int counter;

		public PaintBuilder getPaintBuilder() {
			return paintBuilder;
		}

		private PaintBuilder paintBuilder;

		public void setFrameRenderTime(long duration) {
			durations[counter] = duration;
			counter = (counter + 1) % bufferSize;

			paintBuilder = new PaintBuilder();
			paintBuilder.setColor(ColorConverter.getColor(JDColor.red));
			paintBuilder.setFontSize(16);

		}

		public int getFrameRate() {
			long result = 0;
			for (long duration : durations) {
				result += duration;
			}
			return Math.round(1000 / (result / 10));
		}

	}

	Map<GUIElement, Image> guiPaintingCache = new HashMap<>();

	@Override
	public void paint(float arg0) {

		long renderStart = System.currentTimeMillis();
		Graphics gr = game.getGraphics(null);

		/*
		 * clear background
		 */
		gr.fillRect(0, 0, this.game.getScreenWidth() * 2,
				this.game.getScreenHeight() * 2, Colors.BLACK);

		/*
		 * draw game world
		 */
		drawWorld(gr);


		/*
		 * draw gui elements
		 */
		if (worldHasChanged || touchEventAfterPaint) {
			guiPaintingCache.clear();
			touchEventAfterPaint = false;
		}
		for (GUIElement guiElement : this.guiElements) {
			if (guiElement.isVisible()) {
				if (guiElement.needsRepaint()) {
					guiElement.paint(gr, viewportPosition);
				}
				else {
					Image guiElementImage = guiPaintingCache.get(guiElement);
					JDPoint positionOnScreen = guiElement.getPositionOnScreen();
					JDDimension dimension = guiElement.getDimension();
					if (guiElementImage == null) {
						gr.setTempCanvas(positionOnScreen.getX(), positionOnScreen.getY(), dimension.getWidth(), dimension
								.getHeight());
						guiElement.paint(gr, this.viewportPosition);
						guiElementImage = gr.getTempImage();
						gr.flushAndResetTempCanvas();
						guiPaintingCache.put(guiElement, guiElementImage);
					}
					gr.drawImage(guiElementImage, positionOnScreen.getX(), positionOnScreen.getY(), true);
				}
			}
		}

		long renderTime = System.currentTimeMillis() - renderStart;
		renderTimeLog.setFrameRenderTime(renderTime);

		gr.fillRect(0, 0, 30, 30, Colors.WHITE);
		gr.drawString(renderTimeLog.getFrameRate() + " fps", 25, 15, renderTimeLog.getPaintBuilder());
	}

	private void drawWorld(Graphics gr) {

		// at which room we have to start drawing
		int startingRoomNumberX = (int) (viewportPosition.getX() / roomSize);
		int startingRoomNumberY = (int) (viewportPosition.getY() / roomSize);

		// how many cols and rows we have to paint to cover screen
		int roomCols = (int) (game.getScreenWidth() / roomSize) + 1;
		int roomRows = (int) (game.getScreenHeight() / roomSize) + 1;

		boolean resetWorldChangedAfterwards = false;
		if (worldHasChanged) {
			resetWorldChangedAfterwards = true;
		}

		int col = 0;
		while (col <= roomCols) {
			int roomNumberX = startingRoomNumberX + col;
			col++;
			int row = 0;

			while (row <= roomRows) {
				int roomNumberY = startingRoomNumberY + row;
				row++;

				RoomInfo roomInfo = figureInfo.getRoomInfo(roomNumberX, roomNumberY);

				// paint this room onto the offscreen canvas
				if (roomInfo != null) {
					int roomOffsetX = (int) (roomNumberX * roomSize);
					int roomOffsetY = (int) (roomNumberY * roomSize);
					List<GraphicObject> graphicObjectsForRoom;
					// TODO: consider graphic object caching on room level
					if (worldHasChanged) {
						// world has changed, hence we need to newly generate graphic objects
						graphicObjectsForRoom = createGraphicObjectsForRoom(roomInfo, roomOffsetX, roomOffsetY);
						counterCreatingRoomDrawingObjects++;
						drawnObjects.put(roomInfo.getPoint(), graphicObjectsForRoom);
					}
					else {
						graphicObjectsForRoom = drawnObjects.get(roomInfo.getPoint());
						if (graphicObjectsForRoom == null) {
							// obviously room has not yet been drawn, i.e. has just scrolled into view
							// hence we have to create the objects
							graphicObjectsForRoom = createGraphicObjectsForRoom(roomInfo, roomOffsetX, roomOffsetY);
							counterCreatingRoomDrawingObjects++;
							drawnObjects.put(roomInfo.getPoint(), graphicObjectsForRoom);
						}
						else {
							counterReusingRoomDrawingObjects++;
						}
					}

					if (worldHasChanged
							|| !this.drawnRooms.containsKey(roomInfo.getPoint())
							|| AnimationManager.getInstance().hasAnimations(roomInfo)) {
						Image roomOffscreenImage = DrawUtils.drawObjects(gr,
								graphicObjectsForRoom,
								getViewportPosition(),
								roomInfo,
								roomOffsetX,
								roomOffsetY,
								this,
								dungeonRenderer,
								(int)roomSize);

						if (roomOffscreenImage != null) {
							this.drawnRooms.put(roomInfo.getPoint(), roomOffscreenImage);
							gr.drawScaledImage(roomOffscreenImage, roomOffsetX - viewportPosition.getX(), roomOffsetY - viewportPosition
									.getY(), (int) roomSize, (int) roomSize, 0, 0, roomOffscreenImage.getWidth(), roomOffscreenImage
									.getHeight(), true);
						}
					}
					if (!worldHasChanged && this.drawnRooms.get(roomInfo.getPoint()) != null) {
						Image roomOffscreenImage = this.drawnRooms.get(roomInfo.getPoint());
						gr.drawScaledImage(roomOffscreenImage, roomOffsetX - viewportPosition.getX(), roomOffsetY - viewportPosition
								.getY(), (int) roomSize, (int) roomSize, 0, 0, roomOffscreenImage.getWidth(), roomOffscreenImage
								.getHeight(), true);
					}

				}
			}
		}
		// close world changed flag;
		// as long as it stays false, the cached graphic objects are used for rendering
		if (resetWorldChangedAfterwards) {
			worldHasChanged = false;
		}

		// flush graphics (room tile offscreen render cache) before gui rendering is started
		gr.flushAndResetTempCanvas();
	}

	private List<GraphicObject> createGraphicObjectsForRoom(RoomInfo roomInfo, int roomOffsetX, int roomOffsetY) {
		List<GraphicObject> graphicObjectsForRoom = this.dungeonRenderer.createGraphicObjectsForRoom(roomInfo, roomOffsetX, roomOffsetY);
		drawnObjects.put(roomInfo.getPoint(), graphicObjectsForRoom);
		return graphicObjectsForRoom;
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public synchronized void update(float arg0) {

		/*
		 * trigger gui-elements
		 */
		for (GUIElement guiElement : guiElements) {
			guiElement.update(arg0);
		}

		{
			/*
			 * check for movie sequences
			 */
			CameraFlightSequence currentSequence = sequenceManager
					. 	getCurrentSequence(arg0);
			if (currentSequence != null) {
				// movie running
				int movieRoomSize = currentSequence.getScale(arg0);
				if (movieRoomSize != this.roomSize) {
					this.changeRoomSize(movieRoomSize);
				}

				Pair<Float, Float> centerViewRoomCoordinates = currentSequence
						.getViewportPosition(arg0);
				if (centerViewRoomCoordinates != null) {
					centerOn(centerViewRoomCoordinates);
				}

				// flush events and quit processing
				game.getInput().getTouchEvents();
				return;
			}
		}

		{
			/*
			 * check double tap event
			 */
			MotionEvent doubleTapEvent = game.getInput().getDoubleTapEvent();
			if (doubleTapEvent != null) {
				long timeNow = System.currentTimeMillis();
				if (timeNow - lastDoubleTapEventTime < 100) {
					/*
					 * catch double event recognition; should have at least 0.1s
					 * between events
					 */
					return;
				}
				lastDoubleTapEventTime = timeNow;

				JDPoint doubleTapCoordinates = normalizeRawCoordinates(doubleTapEvent);

				/*
				distinguish whether a gui elements was clicked or the click was into the 'world'
				 */
				boolean guiOP = false;
				ListIterator<GUIElement> listIterator = guiElements
						.listIterator(guiElements.size());
				while (listIterator.hasPrevious()) {
					GUIElement guiElement = listIterator.previous();
					if (guiElement.hasPoint(doubleTapCoordinates)
							&& guiElement.isVisible()) {
						guiElement.handleDoubleTapEvent(doubleTapEvent);
						guiOP = true;
						break;
					}
				}

				if (!guiOP) {
					Object clickedObject = findClickedObject(doubleTapCoordinates);
					if (clickedObject != null) {
						if (clickedObject.equals(getFocusManager().getWorldFocusObject())) {
							// object was already highlighted before
							// hence we can trigger an action
							actionAssembler.objectClicked(clickedObject, true);
						}
						else {
							getFocusManager().setWorldFocusObject(((RoomInfoEntity) clickedObject));
						}
					}
				}

				// flush events and quit processing
				game.getInput().getTouchEvents();
				return;
			}
		}

		{
			/*
			 * check long press event
			 */
			MotionEvent longPressEvent = game.getInput().getLongPressEvent();
			if (longPressEvent != null) {

				JDPoint longPressedCoordinates = normalizeRawCoordinates(longPressEvent);

				boolean guiOP = false;
				ListIterator<GUIElement> listIterator = guiElements
						.listIterator(guiElements.size());
				while (listIterator.hasPrevious()) {
					GUIElement guiElement = listIterator.previous();
					if (guiElement.hasPoint(longPressedCoordinates)
							&& guiElement.isVisible()) {
						guiElement.handleLongPressEvent(longPressEvent);
						guiOP = true;
						break;
					}
				}

				if (!guiOP) {
					Object clickedObject = findClickedObjectLongPressed(longPressedCoordinates);
					if (clickedObject instanceof InfoEntity) {
						infoPanel.setContent(((InfoEntity) clickedObject));
					}
				}

				// flush events and quit processing
				game.getInput().getTouchEvents();
				return;
			}
		}

		/*
		 * check scale event
		 */

		float scaleFactor = game.getInput().getScaleEvent();

		if (scaleFactor != 1 && scaleFactor != 0) {
			/*
			 * handle scale event
			 */

			int newRoomSize = (int) (roomSize * scaleFactor);

			newRoomSize = Math.max(minRoomSize,
					Math.min(maxRoomSize, newRoomSize));
			viewportPosition = new JDPoint(viewportPosition.getX() * scaleFactor, viewportPosition.getY() * scaleFactor);
			changeRoomSize(newRoomSize);

			// flush events and quit processing
			game.getInput().getTouchEvents();
			lastScaleEventTime = System.currentTimeMillis();
			return;

		}

		/*
		 * handle scroll events
		 */
		ScrollMotion scrollEvent = game.getInput().getScrollEvent();
		if (scrollEvent != null) {
			MotionEvent startEvent = scrollEvent.getStartEvent();
			JDPoint coordinates = normalizeRawCoordinates(startEvent);
			boolean guiOP = false;
			ListIterator<GUIElement> listIterator = guiElements
					.listIterator(guiElements.size());
			while (listIterator.hasPrevious()) {
				GUIElement guiElement = listIterator.previous();
				if (guiElement.hasPoint(coordinates) && guiElement.isVisible()) {
					guiElement.handleScrollEvent(scrollEvent);
					guiOP = true;
					break;
				}
			}

			if (!guiOP) {
				this.viewportPosition = new JDPoint(viewportPosition.getX()
						+ scrollEvent.getMovement().getX(),
						viewportPosition.getY()
								+ scrollEvent.getMovement().getY());
			}

			// flush events and quit processing
			List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
			lastScrollEventTime = System.currentTimeMillis();
			return;
		}

		/*
		 * handle touch events
		 */
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		TouchEvent touchDownEvent = null;

		for (int i = 0; i < touchEvents.size(); i++) {
			TouchEvent touchEvent = touchEvents.get(i);
			boolean isTouchDownEvent = game.getInput().isTouchDown(i);
			if (isTouchDownEvent) {
				touchDownEvent = touchEvent;
			}
		}

		if (touchDownEvent != null) {
			long timeNow = System.currentTimeMillis();
			if (timeNow - lastTouchEventTime < 200) {
				/*
				 * TODO: find out why in the hell duplicate touch events occur frequently
				 * catch double event recognition; should have at least 0.2s
				 * between events
				 */
				//Log.i("events", "skipping touch event (<200 msec)");
				return;
			}
			lastTouchEventTime = timeNow;
			touchEventAfterPaint = true;

			boolean guiOP = false;
			JDPoint coordinates = new JDPoint(touchDownEvent.x,
					touchDownEvent.y);
			ListIterator<GUIElement> listIterator = guiElements
					.listIterator(guiElements.size());
			while (listIterator.hasPrevious()) {
				GUIElement guiElement = listIterator.previous();
				if (guiElement.hasPoint(coordinates) && guiElement.isVisible()) {
					//Log.i("touch event fired", this.getClass().getSimpleName()+": touch event fired");
					guiElement.handleTouchEvent(touchDownEvent);
					guiOP = true;
					break;
				}
			}

			if (!guiOP) {
				handleClickEvent(touchDownEvent);
			}
		}

	}

	private Pair<Float, Float> getCurrentViewCenterRoomCoordinates() {

		/*
		 * Calculate room being in center view currently
		 */

		float roomCoordinateX = ((viewportPosition.getX() + ((float) game.getScreenWidth() / 2)) / roomSize);
		float roomCoordinateY = ((viewportPosition.getY() + ((float) game
				.getScreenHeight() / 2)) / roomSize);
		return new Pair<>(roomCoordinateX, roomCoordinateY);
	}

	private JDPoint getCurrentViewCenterRoomCoordinatesPoint() {
		Pair<Float, Float> pair = getCurrentViewCenterRoomCoordinates();
		return new JDPoint(pair.getA().intValue(), pair.getB().intValue());
	}

	private void changeRoomSize(int newRoomSize) {
		/*
		 * Change roomSize
		 */
		this.roomSize = newRoomSize;
		resetDungeonRenderer();
		// we need to clear drawing objects cache as it is not independent of roomSize parameter
		// TODO: can the objects also be made independent of the roomSize parameter?
		// then we could also use drawing object cache during zooming
		this.drawnObjects.clear();
	}

	private void handleClickEvent(TouchEvent touchEvent) {

		Boolean dead = this.figureInfo.isDead();
		if (dead != null && dead) {
			gameOverView.setShow(true);
		}

		JDPoint p = new JDPoint(touchEvent.x, touchEvent.y);
		Object clickedObject = findClickedObject(p);
		if (clickedObject != null) {
			if (clickedObject.equals(focusManager.getWorldFocusObject())) {
				// it was already selected, hence we should trigger an action new
				actionAssembler.objectClicked(clickedObject, false);
			}
			else {
				focusManager.setWorldFocusObject(((RoomInfoEntity) clickedObject));
			}
		}
	}

	private Object findClickedObjectLongPressed(JDPoint p) {

		JDPoint inGameLocation = new JDPoint(
				viewportPosition.getX() + p.getX(), viewportPosition.getY()
				+ p.getY());

		int roomNrX = (int) (inGameLocation.getX() / roomSize);
		int roomNrY = (int) (inGameLocation.getY() / roomSize);

		JDPoint roomNumber = new JDPoint(roomNrX, roomNrY);

		List<GraphicObject> roomObjects = drawnObjects.get(roomNumber);
		if (roomObjects == null) {
			return null;
		}
		int roomOffsetX = (int) (roomNrX * roomSize);
		int roomOffsetY = (int) (roomNrY * roomSize);
		return findClickedObjectsInRoom(inGameLocation, roomObjects, roomOffsetX, roomOffsetY);
	}

	private Object findClickedObject(JDPoint p) {

		JDPoint inGameLocation = new JDPoint(viewportPosition.getX()
				+ p.getX(), viewportPosition.getY()
				+ p.getY());

		// TODO: why do we need to adjust click location here, but not when long pressed?
		JDPoint adjustedClickLocation = new JDPoint(inGameLocation.getX(),
				inGameLocation.getY());

		int roomNrX = (int) (adjustedClickLocation.getX() / roomSize);
		int roomNrY = (int) (adjustedClickLocation.getY() / roomSize);

		JDPoint roomNumber = new JDPoint(roomNrX, roomNrY);

		List<GraphicObject> roomObjects = drawnObjects.get(roomNumber);
		if (roomObjects == null) {
			return null;
		}
		int roomOffsetX = (int) (roomNrX * roomSize);
		int roomOffsetY = (int) (roomNrY * roomSize);
		return findClickedObjectsInRoom(inGameLocation, new ArrayList<>(roomObjects), roomOffsetX, roomOffsetY);
	}

	private Object findClickedObjectsInRoom(JDPoint inGameLocation,
											List<GraphicObject> roomObjects, int roomOffsetX, int roomOffsetY) {
		Collections.sort(roomObjects, new GraphicObjectComparator());
		Object clickedObject = null;
		for (GraphicObject graphicObject : roomObjects) {
			if (graphicObject.hasPoint(inGameLocation, roomOffsetX, roomOffsetY)) {
				clickedObject = graphicObject.getClickableObject();
				break;
			}
		}
		return clickedObject;
	}

	public void showVisibilityIncrease(List<JDPoint> points) {
		if (points.isEmpty()) {
			return;
		}

		JDPoint heroRoom = this.getFigureInfo().getRoomInfo().getPoint();
		if (points.contains(heroRoom)) {
			// entered current room, no need to do animation
			points.remove(heroRoom);

		}

		final Iterator<JDPoint> pointIterator = points.iterator();
		while(pointIterator.hasNext()) {
			final JDPoint point = pointIterator.next();
			if(heroRoom.isNeighbour(point)) {
				pointIterator.remove();
			}
		}

		if (points.isEmpty()) {
			return;
		}

		// we dont animate discovered neighbour rooms any more
		/*
		if (points.size() == 1 && heroRoom.isNeighbour(points.get(0))) {
			scrollTo(points.get(0), 50, "show Visibility increase 1 room hero neighbour");
			return;
		}
		*/

		// zoom out
		int flightScale = 70;
		int stepDuration = 40;
		zoomToSize(stepDuration, flightScale, "show visibility increase PART 1 zoom out");
		JDPoint last = getCurrentViewCenterRoomCoordinatesPoint();
		for (JDPoint p : points) {
			// center on each discovered room
			scrollFromTo(last, p, stepDuration, flightScale, "show visibility increase PART 2 center to each room");
			last = p;
		}

		// scroll back to hero
		scrollFromToScale(last, this.getFigureInfo().getRoomNumber(), 60, flightScale, (int) this.roomSize, "show visibility increase PART 3 zoom and scroll back to hero");
	}

	/*
	Creates a movie sequence that zooms in/out
	 */
	private void zoomToSize(int duration, float roomScale, String title) {
		zoomToSize(duration, roomScale, figureInfo.getRoomNumber(), title);
	}

	private void zoomToSize(int duration, float targetScale, JDPoint position, String title) {
		zoomToSize(duration, roomSize, targetScale, position, title);
	}

	private void zoomToSize(int duration, float startScale, float targetScale, JDPoint position, String title) {
		CameraFlightSequence sequence = new DefaultMovieSequence(
				new ZoomSequence(startScale, targetScale, duration),
				new StraightLineScroller(getCurrentViewCenterRoomCoordinates(), floatPair(position), duration), duration, title);
		this.sequenceManager.addSequence(sequence);
	}

	public void zoomIn() {
		float newScale = roomSize;
		if (newScale < maxRoomSize) {
			newScale = roomSize + 20;
			if (newScale > maxRoomSize) {
				newScale = maxRoomSize;
			}
		}
		zoomToSize(1, newScale, "zoom in button used");
	}

	public void zoomOut() {
		float newScale = roomSize;
		if (newScale > minRoomSize) {
			newScale = roomSize - 20;
			if (newScale < minRoomSize) {
				newScale = minRoomSize;
			}
		}
		zoomToSize(1, newScale, "zoom out button used");
	}

	public void exitFightMode() {
		zoomToSize(30, SCALE_ROOM_FIGHT_MODE, preFightRoomSize, figureInfo.getRoomNumber(), "exit fight mode zoom back");
	}

	public void enterFightMode() {
		preFightRoomSize = roomSize;
		zoomToSize(30, SCALE_ROOM_FIGHT_MODE, "enter fight mode zoom in");
	}

	@Override
	public void tellPercept(Percept p) {
		this.perceptHandler.handlePercept(p);
	}

	public void focusTakenItem(ItemInfo item) {
		focusManager.setGuiFocusObject(item);
		smartControl.focusTakenItem(item);
	}

	public void newStatement(Statement s) {
		this.showNewTextPercept(s);
	}

	public void clearFigureAnimation(FigureInfo figure) {
		AnimationManager.getInstance().clearFigure(figure);
	}

	public void startAnimation(DefaultAnimationSet ani, FigureInfo info) {
		startAnimation(ani, info, null, Position.Pos.fromValue(info.getPositionInRoomIndex()), info.getRoomInfo(), null, false, false, false, null, null);
	}

	public void startAnimation(DefaultAnimationSet ani, FigureInfo info, Position.Pos from, Position.Pos to) {
		startAnimation(ani, info, from, to, info.getRoomInfo(), null, false, false, false, null, null);
	}

	public void startAnimation(DefaultAnimationSet ani, FigureInfo info, String text) {
		startAnimation(ani, info, null, null, info.getRoomInfo(), text, false, false, false, null, null);
	}

	public void startAnimationDelayed(DefaultAnimationSet ani, FigureInfo info, String text) {
		startAnimation(ani, info, null, null, info.getRoomInfo(), text, true, false, false, null, null);
	}

	public void startAnimationUrgent(DefaultAnimationSet ani, FigureInfo info, String text) {
		startAnimation(ani, info, null, null, info.getRoomInfo(), text, false, true, false, null, null);
	}

	public void startAnimationUrgent(DefaultAnimationSet ani, FigureInfo fig, Position.Pos from, Position.Pos to) {
		startAnimation(ani, fig, from, to, fig.getRoomInfo(), null, false, true, false, null, null);
	}

	public void startAnimationDelayedUrgent(DefaultAnimationSet ani, FigureInfo info, String text) {
		startAnimation(ani, info, null, null, info.getRoomInfo(), text, true, true, false, null, null);
	}

	public void startAnimationDelayedUrgentPostDelay(DefaultAnimationSet ani, FigureInfo info, String text, Percept percept, JDImageProxy delayImage) {
		startAnimation(ani, info, null, null, info.getRoomInfo(), text, true, true, true, percept, delayImage);
	}

	public void startAnimationDelayedUrgent(DefaultAnimationSet ani, FigureInfo info, String text, Percept percept, JDImageProxy delayImage) {
		startAnimation(ani, info, null, null, info.getRoomInfo(), text, true, true, false, percept, delayImage);
	}

	public void startAnimation(DefaultAnimationSet ani, FigureInfo figure, Position.Pos from, Position.Pos to, RoomInfo room, String text, boolean delayed, boolean urgent, boolean postDelay, Percept percept, JDImageProxy delayImage) {
		DefaultAnimationTask task = new DefaultAnimationTask(ani,
				text, figure, from, to, room);
		task.setUrgent(urgent);
		task.setPercept(percept);
		AnimationManager.getInstance().startAnimation(task, figure,  delayed, postDelay, delayImage);
	}

	public JDPoint getViewportPosition() {
		return viewportPosition;
	}

	public FigureInfo getFigureInfo() {
		return figureInfo;
	}

	public void scrollTo(JDPoint number, float duration, String title) {
		scrollTo(number, duration, (int) roomSize, title);
	}

	private void scrollFromTo(JDPoint start, JDPoint target, float duration, int roomScale, String title) {
		scrollFromTo(floatPair(start), floatPair(target), duration, roomScale, title);
	}

	private void scrollFromTo(Pair<Float, Float> start, Pair<Float, Float> target, float duration, int roomScale, String title) {
		CameraFlightSequence sequence = new DefaultMovieSequence(
				new TrivialScaleSequence(roomScale),
				new StraightLineScroller(start,
						target, duration), duration, title);
		this.sequenceManager.addSequence(sequence);
	}

	private void scrollFromToScale(JDPoint start, JDPoint target, float duration, int startScale, int endScale, String title) {
		scrollFromToScale(floatPair(start), floatPair(target), duration, startScale, endScale, title);
	}

	private void scrollFromToScale(Pair<Float, Float> start, Pair<Float, Float> target, float duration, int startScale, int endScale, String title) {
		CameraFlightSequence sequence = new DefaultMovieSequence(
				new ZoomSequence(startScale, endScale, duration),
				new StraightLineScroller(start,
						target, duration), duration, title);
		this.sequenceManager.addSequence(sequence);
	}

	private Pair<Float, Float> floatPair(JDPoint point) {
		return new Pair<>(
				(float) point.getX(), (float) point.getY());
	}

	public void scrollTo(JDPoint number, float duration, int roomScale, String title) {
		Pair<Float, Float> currentViewCenterRoomCoordinates = getCurrentViewCenterRoomCoordinates();
		currentViewCenterRoomCoordinates = new Pair<>(
				currentViewCenterRoomCoordinates.getA() - 0.5f,
				currentViewCenterRoomCoordinates.getB() - 0.5f);
		scrollFromTo(currentViewCenterRoomCoordinates, floatPair(number), duration, roomScale, title);
	}

	public void showNewTextPercept(Statement p) {
		textPerceptView.addTextPercept(p);
	}

	public FocusManager getFocusManager() {
		return focusManager;
	}

	public void setFigure(FigureInfo f) {
		this.figureInfo = (HeroInfo) f;
	}

	@Override
	public Collection<Class<? extends Event>> getEvents() {
		Collection<Class<? extends Event>> events = new ArrayList<>();
		events.add(FocusEvent.class);
		events.add(InfoObjectClickedEvent.class);
		events.add(VisibilityIncreasedEvent.class);
		events.add(TakeItemButtonClickedEvent.class);
		events.add(ToggleChestViewEvent.class);
		events.add(ShrineButtonClickedEvent.class);
		events.add(WorldChangedEvent.class);
		events.add(InfoMessagePopupEvent.class);
		events.add(InfoMessageClearPopupEvent.class);
		//events.add(ExitUsedEvent.class);
		return events;
	}

	@Override
	public void notify(Event event) {

		if(event instanceof InfoMessagePopupEvent) {
			final String message = ((InfoMessagePopupEvent) event).getMessage();
			messagePopup.setText(message);
			messagePopup.setShow(true);
		}

		if(event instanceof InfoMessageClearPopupEvent) {
			messagePopup.setShow(false);
			smartControl.setMessage(null);
		}

		if (event instanceof FocusEvent) {
			Paragraphable infoEntity = ((FocusEvent) event).getObject();
			focusManager.setGuiFocusObject(infoEntity);
		}
		if (event instanceof InfoObjectClickedEvent) {
			focusManager.setWorldFocusObject(((InfoObjectClickedEvent) event).getClickedEntity());
		}
		if (event instanceof VisibilityIncreasedEvent) {
			showVisibilityIncrease(((VisibilityIncreasedEvent) event).getPoints());
		}

		if (event instanceof ShrineButtonClickedEvent) {
			List<Action> actions = new ArrayList<>();
			if (!(getFigureInfo().getPos().getIndex() == Position.Pos.NE.getValue())) {

				StepAction stepAction = new StepAction(Position.Pos.NE.getValue());
				actions.add(stepAction);
			}
			ShrineAction shrineAction = new ShrineAction(null, false);
			actions.add(shrineAction);
			this.actionAssembler.plugActions(actions);
		}
		if (event instanceof WorldChangedEvent) {
			worldHasChanged = true;
		}
	}

}
