package de.jdungeon.androidapp.screen;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import android.util.Log;
import animation.AnimationManager;
import animation.AnimationSet;
import animation.DefaultAnimationTask;
import control.ActionAssembler;
import control.JDGUIEngine2D;
import dungeon.ChestInfo;
import dungeon.DoorInfo;
import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.Position;
import dungeon.PositionInRoomInfo;
import dungeon.RoomInfo;
import dungeon.SpotInfo;
import event.ActionEvent;
import event.Event;
import event.EventListener;
import event.EventManager;
import event.WorldChangedEvent;
import figure.FigureInfo;
import figure.action.ShrineAction;
import figure.action.StepAction;
import figure.hero.Hero;
import figure.hero.HeroInfo;
import figure.percept.Percept;
import game.InfoEntity;
import game.PerceptHandler;
import graphics.GraphicObject;
import graphics.GraphicObjectRenderer;
import graphics.ImageManager;
import graphics.JDImageProxy;
import gui.Paragraphable;
import item.ItemInfo;
import shrine.ShrineInfo;
import text.Statement;
import user.DefaultDungeonSession;
import util.JDColor;
import util.JDDimension;

import de.jdungeon.androidapp.Control;
import de.jdungeon.androidapp.DrawUtils;
import de.jdungeon.androidapp.GameScreenPerceptHandler;
import de.jdungeon.androidapp.audio.MusicManager;
import de.jdungeon.androidapp.event.InfoObjectClickedEvent;
import de.jdungeon.androidapp.event.FocusEvent;
import de.jdungeon.androidapp.event.VisibilityIncreasedEvent;
import de.jdungeon.androidapp.gui.ColorConverter;
import de.jdungeon.androidapp.gui.FocusManager;
import de.jdungeon.androidapp.gui.GUIElement;
import de.jdungeon.androidapp.gui.GUIImageManager;
import de.jdungeon.androidapp.gui.GameOverView;
import de.jdungeon.androidapp.gui.HealthBar;
import de.jdungeon.androidapp.gui.HourGlassTimer;
import de.jdungeon.androidapp.gui.ImageGUIElement;
import de.jdungeon.androidapp.gui.InfoPanel;
import de.jdungeon.androidapp.gui.InventoryPanel;
import de.jdungeon.androidapp.gui.TextPerceptView;
import de.jdungeon.androidapp.gui.ZoomButton;
import de.jdungeon.androidapp.gui.itemWheel.ChestItemActivityProvider;
import de.jdungeon.androidapp.gui.itemWheel.ChestItemButtonClickedEvent;
import de.jdungeon.androidapp.gui.itemWheel.ItemWheel;
import de.jdungeon.androidapp.gui.itemWheel.ItemWheelBindingSetSimple;
import de.jdungeon.androidapp.gui.itemWheel.SkillActivityProvider;
import de.jdungeon.androidapp.gui.itemWheel.TakeItemActivityProvider;
import de.jdungeon.androidapp.gui.itemWheel.TakeItemButtonClickedEvent;
import de.jdungeon.androidapp.gui.itemWheel.UseItemActivityProvider;
import de.jdungeon.androidapp.gui.smartcontrol.ShrineButtonClickedEvent;
import de.jdungeon.androidapp.gui.smartcontrol.SmartControl;
import de.jdungeon.androidapp.movieSequence.DefaultMovieSequence;
import de.jdungeon.androidapp.movieSequence.MovieSequence;
import de.jdungeon.androidapp.movieSequence.MovieSequenceManager;
import de.jdungeon.androidapp.movieSequence.StraightLineScroller;
import de.jdungeon.androidapp.movieSequence.TrivialScaleSequence;
import de.jdungeon.androidapp.movieSequence.ZoomSequence;
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

	public static final int SCALE_ROOM_FIGHT_MODE = 220;
	public static final int SCALE_ROOM_DEFAULT = 180;
	private final Dungeon derDungeon;
	//private Hero hero = null;
	private HeroInfo figureInfo;

	private GraphicObjectRenderer dungeonRenderer;

	private final Map<JDPoint, List<GraphicObject>> drawnObjects = new HashMap<>();
	private final Map<JDPoint, Image> drawnRooms = new HashMap<>();
	private final MovieSequenceManager sequenceManager = new MovieSequenceManager();
	private InfoPanel infoPanel;
	private final TextPerceptView textPerceptView;
	private GameOverView gameOverView = null;

	private JDPoint viewportPosition;
	private JDPoint targetViewportPosition;
	private final double diffRatio = 1;
	private float roomSize = 180;
	private float preFightRoomSize;
	private final int maxRoomSize = 400;
	private final int minRoomSize = 100;
	private final Control control;

	private long lastDoubleTapEventTime = -1;
	private final RenderTimeLog renderTimeLog = new RenderTimeLog();

	private long lastTouchEventTime = -1;
	private long lastScaleEventTime = -1;
	private long lastScrollEventTime = -1;
	private final JDGUIEngine2D gui;

	private DefaultDungeonSession session;

	private final GameScreenPerceptHandler perceptHandler;

	private boolean roomItemWheelShowing = false;
	private boolean skillItemWheelShowing = false;
	private boolean chestItemWheelShowing = false;
	private ItemWheel itemWheelRoomItems;
	private ItemWheel itemWheelSkills;
	private ItemWheel itemWheelChest;
	private boolean worldHasChanged = true;
	private FocusManager focusManager;
	private ItemWheel itemWheelHeroItems;

	public GameScreen(Game game, JDGUIEngine2D gui) {
		super(game);
		setSession(game.getSession());

		Hero hero = this.session.getCurrentHero();
		figureInfo = this.session.getHeroInfo();
		perceptHandler = new GameScreenPerceptHandler(this);
		gui.setFigure(figureInfo);
		this.gui = gui;
		hero.setControl(gui);
		control = new Control(figureInfo, new ActionAssembler(gui));

		/*
	 * init text messages panel
	 */
		textPerceptView = new TextPerceptView(this);
		this.guiElements.add(textPerceptView);
		initGUIElements();
		this.session.startGame(gui);

		derDungeon = this.session.getCurrentDungeon();
		JDPoint heroRoomNumber = figureInfo.getRoomNumber();
		centerOn(heroRoomNumber);
		scrollTo(heroRoomNumber, 100f);

		resetDungeonRenderer();

		// be event listener
		EventManager.getInstance().registerListener(this);
	}

	private void setSession(Session session) {
		if (session instanceof DefaultDungeonSession) {
			this.session = ((DefaultDungeonSession) session);
		}
		else {
			Log.w("Error", "Invalid session object, DungeonSession expected.");
			System.exit(0);
		}
	}

	private void initGUIElements() {
		/*
		 * init info panel
		 */
		int quarterScreenX = (int) (game.getScreenWidth() * 0.25);
		int halfScreenY = (int) (game.getScreenHeight() * 0.5);
		infoPanel = new InfoPanel(new JDPoint(3 * quarterScreenX, 0),
				new JDDimension(quarterScreenX, halfScreenY), this, this.getGame());
		this.guiElements.add(infoPanel);
		focusManager = new FocusManager(infoPanel, figureInfo);

		/*
		 * init health bars
		 */
		int posX = 22;
		JDPoint healthBarPosition = new JDPoint(posX, 5);
		HealthBar healthView = new HealthBar(healthBarPosition,
				new JDDimension(160, 20), figureInfo, HealthBar.Kind.health,
				this, this.getGame());
		this.guiElements.add(healthView);
		JDPoint dustBarPosition = new JDPoint(posX, 25);
		HealthBar dustView = new HealthBar(dustBarPosition,
				new JDDimension(160, 20), figureInfo, HealthBar.Kind.dust, this, this.getGame());
		this.guiElements.add(dustView);

		/*
		 * init hour glass
		 */
		HourGlassTimer hourglass = new HourGlassTimer(new JDPoint(
				30,
				50), new JDDimension(36, 60),
				this, figureInfo, this.getGame());
		this.guiElements.add(hourglass);


		/*
		add +/- magnifier
		 */
		ImageGUIElement magnifier = new ImageGUIElement(new JDPoint(26, 156), new JDDimension(44, 70), getGUIImage(GUIImageManager.LUPE2)) {

			@Override
			public boolean handleTouchEvent(TouchEvent touch) {
				scrollTo(figureInfo.getRoomNumber(), 30, SCALE_ROOM_DEFAULT);
				return true;
			}
		};
		this.guiElements.add(new ZoomButton(new JDPoint(30, 120), new JDDimension(36, 36), this, getGUIImage(GUIImageManager.PLUS), true));
		this.guiElements.add(new ZoomButton(new JDPoint(30, 224), new JDDimension(36, 36), this, getGUIImage(GUIImageManager.MINUS), false));
		this.guiElements.add(magnifier);

		/*
		init smart control
		 */
		int smartControlSize = 220;
		JDDimension screenSize = this.getScreenSize();
		SmartControl smartControl = new SmartControl(
				new JDPoint(screenSize.getWidth() - smartControlSize, screenSize.getHeight() / 2 + 70 - smartControlSize / 2),
				new JDDimension(smartControlSize, smartControlSize), this, this
				.getGame(), figureInfo);
		this.guiElements.add(smartControl);

		/*
		 * init hero item wheel
		 */
		int screenWidth = getGame().getScreenWidth();
		int selectedIndexItem = 17;
		int screenWidthBy2 = screenWidth / 2;
		JDDimension itemWheelSize = new JDDimension(screenWidthBy2, screenWidthBy2);
		double wheelCenterY = getGame().getScreenHeight() * 1.8;
		itemWheelHeroItems = new ItemWheel(new JDPoint(0, wheelCenterY),
				itemWheelSize, figureInfo, this, this.getGame(),
				new ItemWheelBindingSetSimple(selectedIndexItem, 36,
						new UseItemActivityProvider(figureInfo, this)),
				selectedIndexItem, null, "Rucksack");
		this.guiElements.add(itemWheelHeroItems);

		@SuppressWarnings("SuspiciousNameCombination") JDPoint itemWheelPositionRightSide = new JDPoint(screenWidth, wheelCenterY);
		/*
		 * init skills wheel
		 */
		int selectedIndex = 19;
		Image skillBackgroundImage = (Image) ImageManager.inventory_box_normal.getImage();
		itemWheelSkills = new ItemWheel(itemWheelPositionRightSide,
				itemWheelSize, figureInfo, this, this.getGame(),
				new ItemWheelBindingSetSimple(selectedIndex, 36,
						new SkillActivityProvider(figureInfo, this)),
				selectedIndex,
				skillBackgroundImage, "Aktivitäten");
		this.guiElements.add(itemWheelSkills);

		/*
		init room item wheel
		it shares position with the skill wheel, toggled on/off by the user
		 */
		itemWheelRoomItems = new ItemWheel(itemWheelPositionRightSide,
				itemWheelSize, figureInfo, this, this.getGame(),
				new ItemWheelBindingSetSimple(selectedIndex, 36,
						new TakeItemActivityProvider(figureInfo, this)),
				selectedIndex,
				null, "Boden");
		itemWheelRoomItems.setVisible(false);
		this.guiElements.add(itemWheelRoomItems);


		/*
		init chest item wheel
		it shares position with the skill wheel, toggled on/off by the user
		 */
		itemWheelChest = new ItemWheel(itemWheelPositionRightSide,
				itemWheelSize, figureInfo, this, this.getGame(),
				new ItemWheelBindingSetSimple(selectedIndex, 36,
						new ChestItemActivityProvider(figureInfo, this)),
				selectedIndex,
				null, "Truhe");
		itemWheelChest.setVisible(false);
		this.guiElements.add(itemWheelChest);


		/*
		 * init inventory panel
		 */
		InventoryPanel inventory = new InventoryPanel(figureInfo, this);
		this.guiElements.add(inventory);

		/*
		 * init character panel
		 */

		//switch off
		/*
		CharAttributeView charView = new CharAttributeView(figureInfo, this, this.getGame());
		this.guiElements.add(charView);
		*/

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
	}

	private Image getGUIImage(String filename) {
		return (Image) GUIImageManager.getImageProxy(filename, game.getFileIO().getImageLoader()).getImage();
	}

	public Game getGame() {
		return game;
	}

	public JDDimension getScreenSize() {
		return new JDDimension(game.getScreenWidth(), game.getScreenHeight());
	}

	public Control getControl() {
		return control;
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

	@Override
	public void init() {
		Music music = this.game.getAudio().createMusic("music/" + "Eyes_Gone_Wrong.mp3");
		MusicManager.getInstance().playMusic(music);
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
			counter =  (counter + 1) % bufferSize;

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

	@Override
	public void paint(float arg0) {

		long renderStart = System.currentTimeMillis();
		Graphics gr = game.getGraphics();

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
		List<GUIElement> elements = this.guiElements;
		for (GUIElement guiElement : elements) {
			if (guiElement.isVisible()) {
				guiElement.paint(gr, this.viewportPosition);
			}
		}

		long renderTime  = System.currentTimeMillis() - renderStart;
		renderTimeLog.setFrameRenderTime(renderTime);

		gr.drawString(renderTimeLog.getFrameRate()+" fps", 25, 15, renderTimeLog.getPaintBuilder());
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

				RoomInfo roomInfo = figureInfo.getRoomInfo(new JDPoint(roomNumberX, roomNumberY));

				// paint this room onto the offscreen canvas
				if (roomInfo != null) {
					int roomOffsetX = (int) (roomNumberX * roomSize);
					int roomOffsetY = (int) (roomNumberY * roomSize);
					List<GraphicObject> graphicObjectsForRoom;
					// TODO: consider graphic object caching on room level
					if (worldHasChanged) {
						// world has changed, hence we need to newly generate graphic objects
						graphicObjectsForRoom = createGraphicObjectsForRoom(roomInfo, roomOffsetX, roomOffsetY);
						drawnObjects.put(roomInfo.getPoint(), graphicObjectsForRoom);
					}
					else {
						graphicObjectsForRoom = drawnObjects.get(roomInfo.getPoint());
						if (graphicObjectsForRoom == null) {
							// obviously room has not yet been drawn, i.e. has just scrolled into view
							// hence we have to create the objects
							graphicObjectsForRoom = createGraphicObjectsForRoom(roomInfo, roomOffsetX, roomOffsetY);
							drawnObjects.put(roomInfo.getPoint(), graphicObjectsForRoom);
						}
					}


					if(worldHasChanged || !this.drawnRooms.containsKey(roomInfo.getPoint()) || AnimationManager.getInstance().hasAnimations(roomInfo)) {
						Image roomOffscreenImage = DrawUtils.drawObjects(gr, graphicObjectsForRoom,
								getViewportPosition(), roomInfo, (int) getRoomSize(),
								roomOffsetX, roomOffsetY, this);
						if(roomOffscreenImage != null) {
							this.drawnRooms.put(roomInfo.getPoint(), roomOffscreenImage);
							gr.drawScaledImage(roomOffscreenImage, roomOffsetX-viewportPosition.getX(), roomOffsetY-viewportPosition.getY(), (int)roomSize, (int)roomSize, 0 , 0, roomOffscreenImage.getWidth(), roomOffscreenImage.getHeight(), true);
						}
					}
					if(!worldHasChanged && this.drawnRooms.get(roomInfo.getPoint()) != null) {
						Image roomOffscreenImage = this.drawnRooms.get(roomInfo.getPoint());
						gr.drawScaledImage(roomOffscreenImage, roomOffsetX-viewportPosition.getX(), roomOffsetY-viewportPosition.getY(), (int)roomSize, (int)roomSize, 0 , 0, roomOffscreenImage.getWidth(), roomOffscreenImage.getHeight(), true);
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
		List<GraphicObject> graphicObjectsForRoom;
		graphicObjectsForRoom = getDungeonRenderer()
				.createGraphicObjectsForRoom(roomInfo, null, roomOffsetX,
						roomOffsetY, new ArrayList<>());
		clearNulls(graphicObjectsForRoom);
		putGraphicObjects(roomInfo.getPoint(), graphicObjectsForRoom);
		return graphicObjectsForRoom;
	}

	private void clearNulls(List<?> l) {
		Iterator<?> iterator = l.iterator();
		while (iterator.hasNext()) {
			Object o = iterator.next();
			if (o == null) {
				iterator.remove();
			}
		}
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

		if ((this.roomItemWheelShowing && this.gui.getFigure().getRoomInfo().getItems().isEmpty())
				||
				(this.chestItemWheelShowing && this.gui.getFigure().getRoomInfo().getChest().getItemList().isEmpty())) {
			// we need to switch back to skills mode as user has not the respective button in this case
			switchRightItemWheel(null);
		}


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
			MovieSequence currentSequence = sequenceManager
					.getCurrentSequence(arg0);
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
							control.objectClicked(clickedObject, true);
						}
						else {
							getFocusManager().setWorldFocusObject(((InfoEntity) clickedObject));
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

			//if (roomSize < 90 && scaleFactor > 1) {
			//	scaleFactor += scaleFactor - 1;
			//}

			//scaleFactor = (float) Math.pow(scaleFactor, 2);

			int newRoomSize = (int) (roomSize * scaleFactor);

			newRoomSize = Math.max(minRoomSize,
					Math.min(maxRoomSize, newRoomSize));
			viewportPosition = new JDPoint(viewportPosition.getX() * scaleFactor, viewportPosition.getY() * scaleFactor);
			changeRoomSize(newRoomSize);
			Pair<Float, Float> currentViewCenterRoomCoordinates = getCurrentViewCenterRoomCoordinates();

			// flush events and quit processing
			List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
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
				 * catch double event recognition; should have at least 0.2s
				 * between events
				 */
				return;
			}
			lastTouchEventTime = timeNow;

			boolean guiOP = false;
			JDPoint coordinates = new JDPoint(touchDownEvent.x,
					touchDownEvent.y);
			ListIterator<GUIElement> listIterator = guiElements
					.listIterator(guiElements.size());
			while (listIterator.hasPrevious()) {
				GUIElement guiElement = listIterator.previous();
				if (guiElement.hasPoint(coordinates) && guiElement.isVisible()) {
					guiElement.handleTouchEvent(touchDownEvent);
					guiOP = true;
					break;
				}
			}

			if (!guiOP) {
				handleClickEvent(touchDownEvent);
			}
		}

		/*
		 * viewport moves towards target-viewport
		 */
		if (targetViewportPosition != null
				&& (!targetViewportPosition.equals(viewportPosition))) {

			int positionDiffX = viewportPosition.getX()
					- targetViewportPosition.getX();
			int positionDiffY = viewportPosition.getY()
					- targetViewportPosition.getY();

			int newX;
			int newY;

			int stepSize = 8;

			if (positionDiffX > 10) {
				newX = (int) Math.round(viewportPosition.getX()
						- (stepSize * diffRatio));
			}
			else if (positionDiffX < -10) {
				newX = (int) (viewportPosition.getX() + Math.round(stepSize
						* diffRatio));
			}
			else {
				newX = targetViewportPosition.getX();
			}

			if (positionDiffY > 10) {
				newY = viewportPosition.getY() - stepSize;
			}
			else if (positionDiffY < -10) {
				newY = viewportPosition.getY() + stepSize;
			}
			else {
				newY = targetViewportPosition.getY();
			}

			viewportPosition = new JDPoint(newX, newY);
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

		//System.out.println(System.currentTimeMillis()+" "+touchEvent);
		Boolean dead = this.figureInfo.isDead();
		if (dead != null && dead) {
			gameOverView.setShow(true);
		}

		JDPoint p = new JDPoint(touchEvent.x, touchEvent.y);
		Object clickedObject = findClickedObject(p);
		if (clickedObject != null) {
			if (clickedObject.equals(focusManager.getWorldFocusObject())) {
				// it was already selected, hence we should trigger an action new
				control.objectClicked(clickedObject, false);
			}
			else {
				focusManager.setWorldFocusObject(((InfoEntity) clickedObject));
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

	private JDDimension getDragDistance(List<TouchEvent> touchEvents) {
		TouchEvent startEvent = touchEvents.get(0);
		TouchEvent endEvent = touchEvents.get(touchEvents.size() - 1);

		int dx = endEvent.x - startEvent.x;
		int dy = endEvent.y - startEvent.y;

		return new JDDimension(dx, dy);
	}

	public void showVisibilityIncrease(List<JDPoint> points) {
		JDPoint heroRoom = this.getFigureInfo().getRoomInfo().getPoint();
		if (points.contains(heroRoom)) {
			// entered current room, no need to do animation
			points.remove(heroRoom);
			if (points.isEmpty()) {
				return;
			}
		}
		if (points.size() == 1 && heroRoom.isNeighbour(points.get(0))) {
			scrollTo(points.get(0), 50);
			return;
		}

		// zoom out
		int flightScale = 70;
		int stepDuration = 30;
		zoomToSize(stepDuration, flightScale);
		JDPoint last = getCurrentViewCenterRoomCoordinatesPoint();
		for (JDPoint p : points) {
			// center on each discovered room
			scrollFromTo(last, p, stepDuration, flightScale);
			last = p;
		}

		// scroll back to hero
		scrollFromToScale(last, this.getFigureInfo().getRoomNumber(), 60, flightScale, (int) this.roomSize);
	}

	/*
	Creates a movie sequence that zooms in/out
	 */
	private void zoomToSize(int duration, float roomScale) {
		zoomToSize(duration, roomScale, figureInfo.getRoomNumber());
	}

	private void zoomToSize(int duration, float targetScale, JDPoint position) {
		zoomToSize(duration, roomSize, targetScale, position);
	}

	private void zoomToSize(int duration, float startScale, float targetScale, JDPoint position) {
		MovieSequence sequence = new DefaultMovieSequence(
				new ZoomSequence(startScale, targetScale, duration),
				new StraightLineScroller(getCurrentViewCenterRoomCoordinates(), floatPair(position), duration), duration);
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
		zoomToSize(1, newScale);
	}

	public void zoomOut() {
		float newScale = roomSize;
		if (newScale > minRoomSize) {
			newScale = roomSize - 20;
			if (newScale < minRoomSize) {
				newScale = minRoomSize;
			}
		}
		zoomToSize(1, newScale);
	}

	public void exitFightMode() {
		zoomToSize(30, SCALE_ROOM_FIGHT_MODE, preFightRoomSize, figureInfo.getRoomNumber());
	}

	public void enterFightMode() {
		preFightRoomSize = roomSize;
		zoomToSize(30, SCALE_ROOM_FIGHT_MODE);
	}

	@Override
	public void tellPercept(Percept p) {
		this.perceptHandler.handlePercept(p);
	}

	public void focusTakenItem(ItemInfo item) {
		this.itemWheelHeroItems.highlightEntity(item);
	}

	static class GraphicObjectComparator implements Comparator<GraphicObject> {

		private static final int FLOOR = 0;
		private static final int SHRINE = 1;
		private static final int CHEST = 2;
		private static final int ITEM = 3;
		private static final int POSITION = 4;
		private static final int FIGURE = 5;
		private static final int DOOR = 6;
		private static final int SPOT = 7;

		@Override
		public int compare(GraphicObject arg0, GraphicObject arg1) {
			if (arg0 == null) {
				return 1;
			}
			if (arg1 == null) {
				return -1;
			}
			Object clickedObject0 = arg0.getClickableObject();
			if (clickedObject0 == null) {
				return 1;
			}
			Object clickedObject1 = arg1.getClickableObject();
			if (clickedObject1 == null) {
				return -1;
			}

			int priority0 = getPriority(clickedObject0);
			int priority1 = getPriority(clickedObject1);

			return priority1 - priority0;
		}

		private int getPriority(Object o) {
			if (o instanceof RoomInfo) {
				return FLOOR;
			}
			if (o instanceof FigureInfo) {
				return FIGURE;
			}
			if (o instanceof DoorInfo) {
				return DOOR;
			}
			if (o instanceof PositionInRoomInfo) {
				return POSITION;
			}
			if (o instanceof ShrineInfo) {
				return SHRINE;
			}
			if (o instanceof ChestInfo) {
				return CHEST;
			}
			if (o instanceof SpotInfo) {
				return SPOT;
			}
			if (o instanceof ItemInfo) {
				return ITEM;
			}
			return -1;
		}

	}

	public void newStatement(Statement s) {
		this.showNewTextPercept(s);
	}

	public void clearFigureAnimation(FigureInfo figure) {
		AnimationManager.getInstance().clearFigure(figure);
	}

	public void startAnimation(AnimationSet ani, FigureInfo info) {
		startAnimation(ani, info, null, null, info.getRoomInfo(), null, false, false, false, null, null);
	}

	public void startAnimation(AnimationSet ani, FigureInfo info, Position.Pos from, Position.Pos to) {
		startAnimation(ani, info, from, to, info.getRoomInfo(), null, false, false, false, null, null);
	}

	public void startAnimation(AnimationSet ani, FigureInfo info, String text) {
		startAnimation(ani, info, null, null, info.getRoomInfo(), text, false, false, false, null, null);
	}

	public void startAnimationDelayed(AnimationSet ani, FigureInfo info, String text) {
		startAnimation(ani, info, null, null, info.getRoomInfo(), text, true, false, false, null, null);
	}

	public void startAnimationUrgent(AnimationSet ani, FigureInfo info, String text) {
		startAnimation(ani, info, null, null, info.getRoomInfo(), text, false, true, false, null, null);
	}

	public void startAnimationUrgent(AnimationSet ani, FigureInfo fig, Position.Pos from, Position.Pos to) {
		startAnimation(ani, fig, from, to, fig.getRoomInfo(), null, false, true, false, null, null);
	}

	public void startAnimationDelayedUrgent(AnimationSet ani, FigureInfo info, String text) {
		startAnimation(ani, info, null, null, info.getRoomInfo(), text, true, true, false, null, null);
	}

	public void startAnimationDelayedUrgentPostDelay(AnimationSet ani, FigureInfo info, String text, Percept percept, JDImageProxy delayImage) {
		startAnimation(ani, info, null, null, info.getRoomInfo(), text, true, true, true, percept, delayImage);
	}

	public void startAnimationDelayedUrgent(AnimationSet ani, FigureInfo info, String text, Percept percept, JDImageProxy delayImage) {
		startAnimation(ani, info, null, null, info.getRoomInfo(), text, true, true, false, percept, delayImage);
	}

	public void startAnimation(AnimationSet ani, FigureInfo figure, Position.Pos from, Position.Pos to, RoomInfo room, String text, boolean delayed, boolean urgent, boolean postDelay, Percept percept, JDImageProxy delayImage) {
		DefaultAnimationTask task = new DefaultAnimationTask(ani,
				text, figure, from, to, room);
		task.setUrgent(urgent);
		task.setPercept(percept);
		AnimationManager.getInstance().startAnimation(task, figure, text, delayed, postDelay, delayImage);
	}

	public float getRoomSize() {
		return roomSize;
	}

	public JDPoint getViewportPosition() {
		return viewportPosition;
	}

	public GraphicObjectRenderer getDungeonRenderer() {
		return dungeonRenderer;
	}

	public void putGraphicObjects(JDPoint point, List<GraphicObject> graphicObjectsForRoom) {
		drawnObjects.put(point, graphicObjectsForRoom);
	}

	public FigureInfo getFigureInfo() {
		return figureInfo;
	}

	public void scrollTo(JDPoint number, float duration) {
		scrollTo(number, duration, (int) roomSize);
	}

	private void scrollFromTo(JDPoint start, JDPoint target, float duration, int roomScale) {
		scrollFromTo(floatPair(start), floatPair(target), duration, roomScale);
	}

	private void scrollFromTo(Pair<Float, Float> start, Pair<Float, Float> target, float duration, int roomScale) {
		MovieSequence sequence = new DefaultMovieSequence(
				new TrivialScaleSequence(roomScale),
				new StraightLineScroller(start,
						target, duration), duration);
		this.sequenceManager.addSequence(sequence);
	}

	private void scrollFromToScale(JDPoint start, JDPoint target, float duration, int startScale, int endScale) {
		scrollFromToScale(floatPair(start), floatPair(target), duration, startScale, endScale);
	}

	private void scrollFromToScale(Pair<Float, Float> start, Pair<Float, Float> target, float duration, int startScale, int endScale) {
		MovieSequence sequence = new DefaultMovieSequence(
				new ZoomSequence(startScale, endScale, duration),
				new StraightLineScroller(start,
						target, duration), duration);
		this.sequenceManager.addSequence(sequence);
	}

	private Pair<Float, Float> floatPair(JDPoint point) {
		return new Pair<>(
				(float) point.getX(), (float) point.getY());
	}

	public void scrollTo(JDPoint number, float duration, int roomScale) {
		Pair<Float, Float> currentViewCenterRoomCoordinates = getCurrentViewCenterRoomCoordinates();
		currentViewCenterRoomCoordinates = new Pair<>(
				currentViewCenterRoomCoordinates.getA() - 0.5f,
				currentViewCenterRoomCoordinates.getB() - 0.5f);
		scrollFromTo(currentViewCenterRoomCoordinates, floatPair(number), duration, roomScale);
	}

	public void showNewTextPercept(Statement p) {
		textPerceptView.addTextPercept(p);
	}


	public FocusManager getFocusManager() {
		return focusManager;
	}

	public void setInfoEntity(Paragraphable item) {
		if (item == null || item.equals(this.figureInfo)) {
			this.infoPanel.setContent(null);
		}
		else {
			this.infoPanel.setContent(item);
		}
	}

	public void flushAnimationManager() {
		long timer = 0;
		long timeStep = 20;
		while (!AnimationManager.getInstance().isEmpty()) {
			timer += 20;
			if (timer > 3000) {
				break;
			}
			try {
				Thread.sleep(timeStep);
			}
			catch (InterruptedException e) {
				Log.w("Warning", "flush animation manager sleep interrupted");
			}
		}
		AnimationManager.getInstance().clearAll();
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
		events.add(ChestItemButtonClickedEvent.class);
		events.add(ShrineButtonClickedEvent.class);
		events.add(WorldChangedEvent.class);
		return events;
	}

	@Override
	public void notify(Event event) {
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
		if (event instanceof TakeItemButtonClickedEvent || event instanceof ChestItemButtonClickedEvent) {
			switchRightItemWheel(event);
		}
		if (event instanceof ShrineButtonClickedEvent) {
			if(! (getFigureInfo().getPos().getIndex() == Position.Pos.NE.getValue())) {
				EventManager.getInstance().fireEvent(new ActionEvent(new StepAction(Position.Pos.NE.getValue())));
			}
			EventManager.getInstance().fireEvent(new ActionEvent(new ShrineAction(null, false)));
		}
		if (event instanceof WorldChangedEvent) {
			worldHasChanged = true;
		}
	}

	/**
	 * Switch/toggle between skill- and room-items ItemWheel
	 *
	 * @param event kind of the event
	 */
	private void switchRightItemWheel(Event event) {
		if (event == null) {
			setSkillWheelVisible();
		}
		else if (event instanceof TakeItemButtonClickedEvent) {
			if (!roomItemWheelShowing) {
				setRoomItemsWheelVisible();
			}
			else {
				// switching room item wheel off
				setSkillWheelVisible();
			}
		}
		else if (event instanceof ChestItemButtonClickedEvent) {
			if (!chestItemWheelShowing) {
				if(! (getFigureInfo().getPos().getIndex() == Position.Pos.NW.getValue())) {
					EventManager.getInstance().fireEvent(new ActionEvent(new StepAction(Position.Pos.NW.getValue())));
				}
				setChestItemWheelVisible();
			}
			else {
				// switching chest item wheel off
				setSkillWheelVisible();
			}
		}
	}

	private void setSkillWheelVisible() {
		itemWheelChest.setVisible(false);
		itemWheelRoomItems.setVisible(false);
		itemWheelSkills.setVisible(true);
		roomItemWheelShowing = false;
		chestItemWheelShowing = false;
		skillItemWheelShowing = true;
	}

	private void setChestItemWheelVisible() {
		itemWheelChest.setVisible(true);
		itemWheelRoomItems.setVisible(false);
		itemWheelSkills.setVisible(false);
		roomItemWheelShowing = false;
		chestItemWheelShowing = true;
		skillItemWheelShowing = false;
	}

	private void setRoomItemsWheelVisible() {
		itemWheelChest.setVisible(false);
		itemWheelRoomItems.setVisible(true);
		itemWheelSkills.setVisible(false);
		roomItemWheelShowing = true;
		chestItemWheelShowing = false;
		skillItemWheelShowing = false;
	}

}
