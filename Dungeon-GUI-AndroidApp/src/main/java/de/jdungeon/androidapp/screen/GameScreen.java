package de.jdungeon.androidapp.screen;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import android.graphics.Color;
import android.util.Pair;
import android.view.MotionEvent;
import animation.AnimationSet;
import dungeon.ChestInfo;
import dungeon.DoorInfo;
import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.PositionInRoomInfo;
import dungeon.RoomInfo;
import dungeon.SpotInfo;
import figure.FigureInfo;
import figure.hero.Hero;
import figure.hero.HeroInfo;
import game.InfoEntity;
import graphics.GraphicObject;
import graphics.GraphicObjectRenderer;
import graphics.ImageManager;
import gui.Paragraphable;
import item.ItemInfo;
import log.Log;
import shrine.ShrineInfo;
import text.Statement;
import user.DefaultDungeonSession;
import util.JDDimension;

import de.jdungeon.androidapp.AndroidScreenJDGUI;
import de.jdungeon.androidapp.Control;
import de.jdungeon.androidapp.DrawUtils;
import de.jdungeon.androidapp.JDungeonApp;
import de.jdungeon.androidapp.animation.AnimationManager;
import de.jdungeon.androidapp.gui.CharAttributeView;
import de.jdungeon.androidapp.gui.GUIElement;
import de.jdungeon.androidapp.gui.GameOverView;
import de.jdungeon.androidapp.gui.HealthBar;
import de.jdungeon.androidapp.gui.HourGlassTimer;
import de.jdungeon.androidapp.gui.InfoPanel;
import de.jdungeon.androidapp.gui.InventoryPanel;
import de.jdungeon.androidapp.gui.TextPerceptView;
import de.jdungeon.androidapp.gui.itemWheel.ItemActivityItemProvider;
import de.jdungeon.androidapp.gui.itemWheel.ItemWheel;
import de.jdungeon.androidapp.gui.itemWheel.ItemWheelBindingSetSimple;
import de.jdungeon.androidapp.gui.itemWheel.SkillActivityProvider;
import de.jdungeon.androidapp.movieSequence.DefaultMovieSequence;
import de.jdungeon.androidapp.movieSequence.MovieSequence;
import de.jdungeon.androidapp.movieSequence.MovieSequenceManager;
import de.jdungeon.androidapp.movieSequence.StraightLineScroller;
import de.jdungeon.androidapp.movieSequence.TrivialScaleSequence;
import de.jdungeon.androidapp.movieSequence.ZoomSequence;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input.TouchEvent;
import de.jdungeon.game.Screen;
import de.jdungeon.user.Session;
import de.jdungeon.util.ScrollMotion;

public class GameScreen extends Screen {

	public static final int SCALE_FIGHT_MODE = 300;
	private Dungeon derDungeon;
	private Hero hero = null;
	private HeroInfo figureInfo;
	private GraphicObjectRenderer dungeonRenderer;

	private final Map<JDPoint, List<GraphicObject>> drawnObjects = new HashMap<JDPoint, List<GraphicObject>>();

	private final MovieSequenceManager sequenceManager = new MovieSequenceManager();
	private final List<GUIElement> guiElements = new LinkedList<GUIElement>();
	private InfoPanel infoPanel;
	private final TextPerceptView textPerceptView;
	private GameOverView gameOverView = null;

	private JDPoint viewportPosition;
	private JDPoint targetViewportPosition;
	private final double diffRatio = 1;
	private float roomSize = 180;
	private float preFightRoomSize;
	private final int maxRoomSize = 400;
	private final int minRoomsize = 100;
	private final Control control;
	private InfoEntity highlightedEntity = null;

	private long lastDoubleTapEventTime = -1;

	private long lastTouchEventTime = -1;
	private long lastScaleEventTime = -1;
	private long lastScrollEventTime = -1;
	private final AndroidScreenJDGUI gui;

	private DefaultDungeonSession session;

	/*
	 * for developer mode only
	 */
	private JDPoint beacon = null;


	private float beaconCounter = 0;

	public GameScreen(Game game) {
		super(game);



		/*
	 * init text messages panel
	 */
		textPerceptView = new TextPerceptView(this);
		this.guiElements.add(textPerceptView);

		setSession(game);

		this.hero = this.session.getCurrentHero();
		this.gui = new AndroidScreenJDGUI(this);
		control = new Control((JDungeonApp) game, gui);
		hero.setControl(gui);
		figureInfo = this.session.getHeroInfo();
		initGUIElements();
		this.session.startGame(gui);


		derDungeon = this.session.getCurrentDungeon();
		JDPoint heroRoomNumber = figureInfo.getRoomNumber();
		centerOn(heroRoomNumber);
		scrollTo(heroRoomNumber, 100f);

		resetDungeonRenderer();
	}
	private void setSession(Game game) {
		Session session = game.getSession();
		if (session instanceof DefaultDungeonSession) {
			this.session = ((DefaultDungeonSession)session);
		}
		else {
			Log.severe("Invalid session object, DungeonSession expected.");
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
				new JDDimension(quarterScreenX, halfScreenY), this);
		this.guiElements.add(infoPanel);

		/*
		 * init health bars
		 */
		int posX = 22;
		HealthBar healthView = new HealthBar(new JDPoint(posX, 5),
				new JDDimension(160, 20), figureInfo, HealthBar.Kind.health,
				this);
		this.guiElements.add(healthView);
		HealthBar dustView = new HealthBar(new JDPoint(posX, 25),
				new JDDimension(160, 20), figureInfo, HealthBar.Kind.dust, this);
		this.guiElements.add(dustView);

		/*
		 * init hour glass
		 */
		HourGlassTimer hourglass = new HourGlassTimer(new JDPoint(
				game.getScreenWidth() - 50,
				game.getScreenHeight() / 2 + 10), new JDDimension(36, 60),
				this, figureInfo);
		this.guiElements.add(hourglass);

		/*
		 * init item wheel
		 */
		int selectedIndexItem = 16;
		ItemWheel wheelItems = new ItemWheel(new JDPoint(0, 780),
				new JDDimension(400, 400), figureInfo, this,
				new ItemWheelBindingSetSimple(selectedIndexItem, 36,
						new ItemActivityItemProvider(figureInfo, this)),
				selectedIndexItem, null);
		this.guiElements.add(wheelItems);

		/*
		 * init skills wheel
		 */
		int selectedIndexSkills = 19;
		ItemWheel wheelSkills = new ItemWheel(new JDPoint(800, 780),
				new JDDimension(400, 400), figureInfo, this,
				new ItemWheelBindingSetSimple(selectedIndexSkills, 36,
						new SkillActivityProvider(figureInfo, this)),
				selectedIndexSkills,
				(Image) ImageManager.inventory_box_normal.getImage());
		this.guiElements.add(wheelSkills);

		/*
		 * init inventory panel
		 */
		InventoryPanel inventory = new InventoryPanel(figureInfo, this);
		this.guiElements.add(inventory);

		/*
		 * init inventory panel
		 */
		CharAttributeView charView = new CharAttributeView(figureInfo, this);
		this.guiElements.add(charView);
		/*
		 * init game over view
		 */
		int width = game.getScreenWidth();
		int height = game.getScreenHeight();
		int widthFifth = (width / 5);
		int heightFifth = (height / 4);
		gameOverView = new GameOverView(new JDPoint((width / 2) - widthFifth,
				(height / 2) - heightFifth), new JDDimension(2 * widthFifth,
				2 * heightFifth), this);
		this.guiElements.add(gameOverView);
	}

	public Game getGame() {
		return game;
	}

	public JDDimension getScreenSize() {
		return new JDDimension(game.getScreenWidth(), game.getScreenHeight());
	}

	public InfoEntity getHighlightedEntity() {
		return highlightedEntity;
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
		centerOn((pair.first), (pair.second));
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
	public void paint(float arg0) {
		Graphics gr = game.getGraphics();

		/*
		 * clear background
		 */
		gr.drawRect(0, 0, this.game.getScreenWidth() * 2,
				this.game.getScreenHeight() * 2, Color.BLACK);

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

		/*
		 * paint beacon (dev-mode only)
		 */
		if (beacon != null) {
			gr.drawRect(beacon.getX(), beacon.getY(), 5, 5, Color.RED);
		}
	}

	private void drawWorld(Graphics gr) {
		drawnObjects.clear();
		getDungeonRenderer().clear();


		// at which room we have to start drawing
		int roomCoordinateX = (int) (viewportPosition.getX() / roomSize);
		int roomCoordinateY = (int) (viewportPosition.getY() / roomSize);

		// how many cols and rows we have to paint to cover screen
		int roomCols = (int) (game.getScreenWidth() / roomSize) + 1;
		int roomRows = (int) (game.getScreenHeight() / roomSize) + 1;

		int col = 0;
		while (col <= roomCols) {
			int roomNumberX = roomCoordinateX + col;
			col++;
			int row = 0;

			while (row <= roomRows) {
				int roomNumberY = roomCoordinateY + row;
				row++;

				RoomInfo roomInfo = RoomInfo.makeRoomInfo(derDungeon
						.getRoom(new JDPoint(roomNumberX, roomNumberY)), hero
						.getRoomVisibility());



				// paint this room onto the offscreen canvas
				if (roomInfo != null) {
					int roomOffsetX = (int) (roomNumberX * roomSize);
					int roomOffsetY = (int) (roomNumberY * roomSize);
					DrawUtils.paintSingleRoom(this, gr, roomInfo, roomOffsetX,
							roomOffsetY);
				}
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
	public void update(float arg0) {

		/*
		 * beacon timer (for dev-mode only)
		 */
		if (beaconCounter > 0) {
			beaconCounter -= arg0;
			if (beaconCounter < 0) {
				beaconCounter = 0;
				this.beacon = null;
			}
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
				Pair<Float, Float> centerViewRoomCoordinates = currentSequence
						.getViewportPosition(arg0);
				if(centerViewRoomCoordinates != null) {
					centerOn(centerViewRoomCoordinates);
				}
				int movieRoomSize = currentSequence.getScale(arg0);
				if (movieRoomSize != this.roomSize) {
					this.changeRoomSize(movieRoomSize);
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
					control.objectClicked(clickedObject, true);
					setInfoEntity(((InfoEntity) clickedObject));
					setHighlightedEntity(((InfoEntity) clickedObject));
					// scrollTo(this.figureInfo.getRoomNumber(), 10f);
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
					if (clickedObject != null
							&& clickedObject instanceof InfoEntity) {
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

			newRoomSize = Math.max(minRoomsize,
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
			if (timeNow - lastTouchEventTime < 100) {
				/*
				 * catch double event recognition; should have at least 0.1s
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

	public JDPoint normalizeRawCoordinates(MotionEvent longPressEvent) {

		/*
		 * for some reason these coordinates need to be normalized from scale
		 * 1915/1100
		 */

		int x = (int) (longPressEvent.getRawX() * (game.getScreenWidth()) / 1915);
		// int y = (int) (longPressEvent.getRawY() * (screenSize.getHeight()) /
		// 1100);
		int y = (int) (longPressEvent.getRawY() * (game.getScreenHeight()) / 1000);

		JDPoint point = new JDPoint(x, y);
		this.beacon = new JDPoint(point.getX(), point.getY());
		this.beaconCounter = 300f;

		return point;
	}

	private Pair<Float, Float> getCurrentViewCenterRoomCoordinates() {

		/*
		 * Calculate room being in center view currently
		 */

		float roomCoordinateX = ((viewportPosition.getX() + ((float) game.getScreenWidth() / 2)) / roomSize);
		float roomCoordinateY = ((viewportPosition.getY() + ((float) game
				.getScreenHeight() / 2)) / roomSize);
		return new Pair<Float, Float>(roomCoordinateX, roomCoordinateY);
	}

	private JDPoint getCurrentViewCenterRoomCoordinatesPoint() {
		Pair<Float, Float> pair = getCurrentViewCenterRoomCoordinates();
		return new JDPoint(pair.first.intValue(), pair.second.intValue());
	}

	private void changeRoomSize(int newRoomSize) {
		/*
		 * Change roomSize
		 */
		this.roomSize = newRoomSize;
		resetDungeonRenderer();
	}

	private void handleClickEvent(TouchEvent touchEvent) {

		Boolean dead = this.figureInfo.isDead();
		if (dead != null && dead.booleanValue()) {
			gameOverView.setShow(true);
		}

		JDPoint p = new JDPoint(touchEvent.x, touchEvent.y);
		Object clickedObject = findClickedObject(p);
		if (clickedObject != null) {
			if ((highlightedEntity != null && highlightedEntity
					.equals(clickedObject))
					|| (!(clickedObject instanceof FigureInfo))) {

				control.objectClicked(clickedObject, false);
			}
			setInfoEntity(((InfoEntity) clickedObject));
			setHighlightedEntity(((InfoEntity) clickedObject));
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

		return findClickedObjectsInRoom(inGameLocation, roomObjects);
	}

	private Object findClickedObject(JDPoint p) {

		JDPoint screenLocation = new JDPoint(p.getX(), p.getY());

		this.beaconCounter = 400f;
		this.beacon = screenLocation;

		JDPoint inGameLocation = new JDPoint(viewportPosition.getX()
				+ screenLocation.getX(), viewportPosition.getY()
				+ screenLocation.getY());

		JDPoint adjustedClickLocation = new JDPoint(inGameLocation.getX(),
				inGameLocation.getY());

		int roomNrX = (int) (adjustedClickLocation.getX() / roomSize);
		int roomNrY = (int) (adjustedClickLocation.getY() / roomSize);

		JDPoint roomNumber = new JDPoint(roomNrX, roomNrY);

		List<GraphicObject> roomObjects = drawnObjects.get(roomNumber);
		if (roomObjects == null) {
			return null;
		}
		return findClickedObjectsInRoom(inGameLocation, roomObjects);
	}

	private Object findClickedObjectsInRoom(JDPoint inGameLocation,
											List<GraphicObject> roomObjects) {
		Collections.sort(roomObjects, new GraphicObjectComparator());
		Object clickedObject = null;
		if (roomObjects != null) {
			for (GraphicObject graphicObject : roomObjects) {
				if (graphicObject.hasPoint(inGameLocation)) {
					clickedObject = graphicObject.getClickedObject();
					break;
				}
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

	public void showVisibilityIncreaseEvent(JDPoint p) {
		zoomToSize(30, 70);
		scrollTo(p, 100, 70);
		zoomToSize(30, 70, (int)this.roomSize, p);
		scrollFromTo(p, this.getFigureInfo().getRoomNumber(), 60, (int)this.roomSize );
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

	public void exitFightMode() {
		zoomToSize(30, SCALE_FIGHT_MODE, preFightRoomSize, figureInfo.getRoomNumber());
	}

	public void enterFightMode() {
		preFightRoomSize = roomSize;
		zoomToSize(30, SCALE_FIGHT_MODE);
	}

	class GraphicObjectComparator implements Comparator<GraphicObject> {

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
			Object clickedObject0 = arg0.getClickedObject();
			if (clickedObject0 == null) {
				return 1;
			}
			Object clickedObject1 = arg1.getClickedObject();
			if (clickedObject1 == null) {
				return -1;
			}

			int priority0 = getPriority(clickedObject0);
			int priority1 = getPriority(clickedObject1);

			return priority1 - priority0;
		}

		private int getPriority(Object o) {
			if (o instanceof RoomInfo) {
				return this.FLOOR;
			}
			if (o instanceof FigureInfo) {
				return this.FIGURE;
			}
			if (o instanceof DoorInfo) {
				return this.DOOR;
			}
			if (o instanceof PositionInRoomInfo) {
				return this.POSITION;
			}
			if (o instanceof ShrineInfo) {
				return this.SHRINE;
			}
			if (o instanceof ChestInfo) {
				return this.CHEST;
			}
			if (o instanceof SpotInfo) {
				return this.SPOT;
			}
			if (o instanceof ItemInfo) {
				return this.ITEM;
			}
			return -1;
		}

	}

	public void newStatement(Statement s) {
		this.showNewTextPercept(s);
	}

	public void startAnimation(AnimationSet ani, FigureInfo info) {
		startAnimation(ani, info, null);
	}

	public void startAnimation(AnimationSet ani, FigureInfo info, String text) {
		AnimationManager.getInstance().startAnimation(ani, info, text);
	}

	public Hero getHero() {
		return hero;
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

	public Map<JDPoint, List<GraphicObject>> getDrawnObjects() {
		return drawnObjects;
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

	private Pair<Float, Float> floatPair(JDPoint point) {
		return new Pair<Float, Float>(
				new Float(point.getX()), new Float(point.getY()));
	}

	private void scrollTo(JDPoint number, float duration, int roomScale) {
		Pair<Float, Float> currentViewCenterRoomCoordinates = getCurrentViewCenterRoomCoordinates();
		currentViewCenterRoomCoordinates = new Pair<Float, Float>(
				currentViewCenterRoomCoordinates.first - 0.5f,
				currentViewCenterRoomCoordinates.second - 0.5f);
		scrollFromTo(currentViewCenterRoomCoordinates, floatPair(number), duration, roomScale);
	}

	public void showNewTextPercept(Statement p) {
		textPerceptView.addTextPercept(p);
	}

	public void setInfoEntity(Paragraphable item) {
		if (item == null || item.equals(this.figureInfo)) {
			this.infoPanel.setContent(null);
		}
		else {
			this.infoPanel.setContent(item);
		}
	}

	public void setHighlightedEntity(InfoEntity item) {
		if (item == null || item.equals(this.figureInfo)) {
			highlightedEntity = null;
		}
		else {
			this.highlightedEntity = item;
		}
	}

	public void clearAnimationManager() {
		AnimationManager.getInstance().clear();

	}

	public void setFigure(FigureInfo f) {
		this.figureInfo = (HeroInfo) f;

	}

}
