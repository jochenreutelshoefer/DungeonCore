package de.jdungeon.androidapp;

import figure.DungeonVisibilityMap;
import figure.FigureInfo;
import figure.hero.Hero;
import figure.hero.HeroInfo;
import figure.hero.HeroUtil;
import game.DungeonGame;
import game.InfoEntity;
import game.JDEnv;
import game.JDGUI;
import graphics.GraphicObject;
import graphics.GraphicObjectRenderer;
import item.ItemInfo;
import item.equipment.weapon.Club;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shrine.ShrineInfo;
import text.Statement;
import util.JDDimension;
import android.graphics.Color;
import android.util.Pair;
import android.view.MotionEvent;
import animation.AnimationSet;
import de.jdungeon.androidapp.animation.AnimationManager;
import de.jdungeon.androidapp.gui.GUIElement;
import de.jdungeon.androidapp.gui.GameOverView;
import de.jdungeon.androidapp.gui.HealthBar;
import de.jdungeon.androidapp.gui.HourGlassTimer;
import de.jdungeon.androidapp.gui.InfoPanel;
import de.jdungeon.androidapp.gui.InventoryPanel;
import de.jdungeon.androidapp.gui.ItemWheel;
import de.jdungeon.androidapp.gui.ItemWheelBindingSetSimple;
import de.jdungeon.androidapp.movieSequence.DefaultMovieSequence;
import de.jdungeon.androidapp.movieSequence.MovieSequence;
import de.jdungeon.androidapp.movieSequence.MovieSequenceManager;
import de.jdungeon.androidapp.movieSequence.StraightLineScroller;
import de.jdungeon.androidapp.movieSequence.TrivialScaleSequence;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input.TouchEvent;
import de.jdungeon.game.Screen;
import de.jdungeon.util.ScrollMotion;
import dungeon.Chest;
import dungeon.DoorInfo;
import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.PositionInRoomInfo;
import dungeon.RoomInfo;
import dungeon.SpotInfo;
import dungeon.generate.DungeonGenerationFailedException;

public class GameScreen extends Screen {

	private Dungeon derDungeon;
	private final DungeonGame dungeonGame;
	private Hero hero;
	private final HeroInfo figureInfo;
	private GraphicObjectRenderer dungeonRenderer;
	public int DungeonSizeX = 30;

	public int DungeonSizeY = 40;

	private final Map<JDPoint, List<GraphicObject>> drawnObjects = new HashMap<JDPoint, List<GraphicObject>>();

	private final MovieSequenceManager sequenceManager = new MovieSequenceManager();
	private final List<GUIElement> guiElements = new ArrayList<GUIElement>();
	private final InfoPanel infoPanel;
	GameOverView gos = null;

	private JDPoint viewportPosition;
	private JDPoint targetViewportPosition;
	double diffRatio = 1;
	private int roomSize = 200;
	private final int maxRoomSize = 400;
	private final int minRoomsize = 100;
	private final Control control;

	private long lastDoubleTapEventTime = -1;
	private long lastTouchEventTime = -1;

	private final JDGUI gui;

	private final JDDimension screenSize = new JDDimension(800, 400);

	/*
	 * for developer mode only
	 */
	private JDPoint beacon = null;
	private float beaconCounter = 0;

	public GameScreen(Game game) {
		super(game);

		dungeonGame = DungeonGame.newInstance();
		dungeonGame.run();
		JDEnv.init();
		JDEnv.setGame(dungeonGame);
		createDungeon();
		DungeonVisibilityMap heroVisMap = hero.getRoomVisibility();
		// heroVisMap.setVisCheat();
		figureInfo = new HeroInfo(hero, heroVisMap);

		resetDungeonRenderer();

		JDPoint heroRoomNumber = figureInfo.getRoomNumber();
		centerOnRoom(heroRoomNumber);
		scrollTo(heroRoomNumber, 100f);

		/*
		 * init info panel
		 */
		int quarterScreenX = (int) (this.screenSize.getWidth() * 0.25);
		int halfScreenY = (int) (this.screenSize.getHeight() * 0.5);
		infoPanel = new InfoPanel(new JDPoint(3 * quarterScreenX, 0),
				new JDDimension(quarterScreenX, halfScreenY), this);
		this.guiElements.add(infoPanel);

		/*
		 * init health bars
		 */
		int posX = 22;
		HealthBar healthView = new HealthBar(new JDPoint(posX, 5),
				new JDDimension(200, 20), figureInfo, HealthBar.Kind.health,
				this);
		this.guiElements.add(healthView);
		HealthBar dustView = new HealthBar(new JDPoint(posX, 25),
				new JDDimension(
				200, 20), figureInfo, HealthBar.Kind.dust, this);
		this.guiElements.add(dustView);

		/*
		 * init inventory panel
		 */
		InventoryPanel inventory = new InventoryPanel(figureInfo, this);
		this.guiElements.add(inventory);

		/*
		 * init hour glass
		 */
		HourGlassTimer hourglass = new HourGlassTimer(new JDPoint(
				this.screenSize.getWidth() - 50,
				this.screenSize.getHeight() / 2 + 10), new JDDimension(36, 60),
				this, figureInfo);
		this.guiElements.add(hourglass);
		
		/*
		 * init wheel
		 */
		ItemWheel wheel = new ItemWheel(new JDPoint(780, 780), new JDDimension(
				400, 400), figureInfo, this, new ItemWheelBindingSetSimple(
				this.figureInfo, 20, 36));
		this.guiElements.add(wheel);

		/*
		 * init game over view
		 */
		int width = this.screenSize.getWidth();
		int height = this.screenSize.getHeight();
		int widghtFifth = (width / 5);
		int heightFifth = (height / 4);
		gos = new GameOverView(new JDPoint((width / 2)
				- widghtFifth, (height / 2) - heightFifth), new JDDimension(
				2 * widghtFifth, 2 * heightFifth), this);
		this.guiElements.add(gos);

		this.gui = new AndroidScreenJDGUI(this);

		dungeonGame.putGuiFigure(hero, gui);

		hero.setControl(gui);
		control = new Control((JDungeonApp) game, gui);

		Thread th = new Thread(dungeonGame);
		th.start();

	}

	public Game getGame() {
		return g;
	}

	public Control getControl() {
		return control;
	}

	private void resetDungeonRenderer() {
		this.dungeonRenderer = new GraphicObjectRenderer(roomSize, figureInfo);
	}

	private void centerOnRoom(JDPoint roomNumber) {
		centerOnRoom((float) (roomNumber.getX() + 0.5),
				(float) (roomNumber.getY() + 0.5));
	}

	private void centerOnRoom(int roomNumberX, int roomNumberY) {
		centerOnRoom((float) (roomNumberX + 0.5), (float) (roomNumberY + 0.5));
	}

	private void centerOnRoom(Pair<Float, Float> pair) {
		centerOnRoom((pair.first), (pair.second));
	}

	private void centerOnRoom(float x, float y) {
		viewportPosition = new JDPoint(x * roomSize
				- (screenSize.getWidth() / 2) + (this.roomSize / 2), y
				* roomSize - (screenSize.getHeight() / 2) + (this.roomSize / 2));
	}

	private void createDungeon() {
		hero = new Hero("DefaultHero", 1, null, 40, 10, 10, 10, 1, 1, 1, 1, 1,
				1, 1, 1, 1, 10, 1, 0);
		HeroUtil.addHeroStartWeapon(hero);

		derDungeon = new Dungeon(DungeonSizeX, DungeonSizeY, 18, 39,
				dungeonGame);
		try {
			dungeonGame.fillDungeon(derDungeon);
		} catch (DungeonGenerationFailedException e) {
			derDungeon = new Dungeon(DungeonSizeX, DungeonSizeY, 18, 39,
					dungeonGame);
			try {
				dungeonGame.fillDungeon(derDungeon);
			} catch (DungeonGenerationFailedException e1) {
				derDungeon = new Dungeon(DungeonSizeX, DungeonSizeY, 18, 39,
						dungeonGame);
				try {
					dungeonGame.fillDungeon(derDungeon);
				} catch (DungeonGenerationFailedException e2) {
					System.out
							.println("Cound not generate Dungeon - check Dungeon Generator!");
					e1.printStackTrace();
					System.exit(0);
				}
				e1.printStackTrace();
			}

		}

		hero.createVisibilityMap(derDungeon);
		hero.move(derDungeon.getRoomNr(18, 39));
		hero.getRoom().addItem(new Club(50, false));

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
		Graphics gr = g.getGraphics();

		/*
		 * clear background
		 */
		gr.drawRect(0, 0, this.screenSize.getWidth() * 2,
				this.screenSize.getHeight() * 2, Color.BLACK);

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

		int roomCoordinateX = viewportPosition.getX() / roomSize;
		int roomCoordinateY = viewportPosition.getY() / roomSize;

		int roomCols = (screenSize.getWidth() / roomSize) + 1;
		int roomRows = (screenSize.getHeight() / roomSize) + 1;

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
					int roomOffsetX = roomNumberX * roomSize;
					int roomOffsetY = roomNumberY * roomSize;
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
				centerOnRoom(centerViewRoomCoordinates);
				int movieRoomSize = currentSequence.getScale(arg0);
				if (movieRoomSize != this.roomSize) {
					this.changeRoomSize(movieRoomSize);
				}

				// flush events and quit processing
				g.getInput().getTouchEvents();
				return;
			}
		}

		{
			/*
			 * check double tap event
			 */
			MotionEvent doubleTapEvent = g.getInput().getDoubleTapEvent();
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
				for (GUIElement guiElement : guiElements) {
					if (guiElement.hasPoint(doubleTapCoordinates)
							&& guiElement.isVisible()) {
						guiElement.handleDoubleTapEvent(doubleTapEvent);
						guiOP = true;
						break;
					}
				}

				if (!guiOP) {
					scrollTo(this.figureInfo.getRoomNumber(), 10f);
				}

				// flush events and quit processing
				g.getInput().getTouchEvents();
				return;
			}
		}

		{
			/*
			 * check long press event
			 */
			MotionEvent longPressEvent = g.getInput().getLongPressEvent();
			if (longPressEvent != null) {

				JDPoint longPressedCoordinates = normalizeRawCoordinates(longPressEvent);

				boolean guiOP = false;
				for (GUIElement guiElement : guiElements) {
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
				g.getInput().getTouchEvents();
				return;
			}
		}

		/*
		 * check scale event
		 */

		float scaleFactor = g.getInput().getScaleEvent();

		if (scaleFactor != 1 && scaleFactor != 0) {
			/*
			 * handle scale event
			 */

			if (roomSize < 90 && scaleFactor > 1) {
				scaleFactor += scaleFactor - 1;
			}

			int newRoomSize = (int) (roomSize * scaleFactor);

			newRoomSize = Math.max(minRoomsize,
					Math.min(maxRoomSize, newRoomSize));

			changeRoomSize(newRoomSize);

			// flush events and quit processing
			List<TouchEvent> touchEvents = g.getInput().getTouchEvents();
			return;

		}

		/*
		 * handle scroll events
		 */
		ScrollMotion scrollEvent = g.getInput().getScrollEvent();
		if (scrollEvent != null) {
			MotionEvent startEvent = scrollEvent.getStartEvent();
			JDPoint coordinates = normalizeRawCoordinates(startEvent);
			boolean guiOP = false;
			for (GUIElement guiElement : guiElements) {
				if (guiElement.hasPoint(coordinates) && guiElement.isVisible()) {
					guiElement.handleScrollEvent(scrollEvent);
					guiOP = true;
					break;
				}
			}

			if (!guiOP) {

				this.viewportPosition = new JDPoint(viewportPosition.getX()
						+ scrollEvent.getMovement().getX(), viewportPosition.getY()
								+ scrollEvent.getMovement().getY());

			}

			// flush events and quit processing
			List<TouchEvent> touchEvents = g.getInput().getTouchEvents();
			return;
		}

		/*
		 * handle touch events
		 */
		List<TouchEvent> touchEvents = g.getInput().getTouchEvents();
		TouchEvent touchDownEvent = null;
		for (int i = 0; i < touchEvents.size(); i++) {
			TouchEvent touchEvent = touchEvents.get(i);
			boolean isTouchDownEvent = g.getInput().isTouchDown(i);
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
			for (GUIElement guiElement : guiElements) {
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
			} else if (positionDiffX < -10) {
				newX = (int) (viewportPosition.getX() + Math.round(stepSize
						* diffRatio));
			} else {
				newX = targetViewportPosition.getX();
			}

			if (positionDiffY > 10) {
				newY = viewportPosition.getY() - stepSize;
			} else if (positionDiffY < -10) {
				newY = viewportPosition.getY() + stepSize;
			} else {
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

		int x = (int) (longPressEvent.getRawX() * (screenSize.getWidth()) / 1915);
		// int y = (int) (longPressEvent.getRawY() * (screenSize.getHeight()) /
		// 1100);
		int y = (int) (longPressEvent.getRawY() * (screenSize.getHeight()) / 1000);


		JDPoint point = new JDPoint(x, y);
		this.beacon = new JDPoint(point.getX(), point.getY());
		this.beaconCounter = 300f;

		return point;
	}

	private Pair<Float, Float> getCurrentViewCenterRoomCoordinates() {

		/*
		 * Calculate room being in center view currently
		 */

		float roomCoordinateX = ((viewportPosition.getX() + ((float) this.screenSize
				.getWidth() / 2)) / roomSize);
		float roomCoordinateY = ((viewportPosition.getY() + ((float) this.screenSize
				.getHeight() / 2)) / roomSize);
		return new Pair<Float, Float>(roomCoordinateX, roomCoordinateY);
	}

	private void changeRoomSize(int newRoomSize) {

		/*
		 * Change roomSize
		 */
		this.roomSize = newRoomSize;
		resetDungeonRenderer();

		/*
		 * center to displayed room again
		 */
		centerOnRoom(getCurrentViewCenterRoomCoordinates());

	}

	private void handleClickEvent(TouchEvent touchEvent) {

		if (this.figureInfo.isDead()) {
			gos.setShow(true);
		}

		JDPoint p = new JDPoint(touchEvent.x, touchEvent.y);
		Object clickedObject = findClickedObject(p);
		if (clickedObject != null) {
			control.objectClicked(clickedObject);
			if (clickedObject instanceof InfoEntity) {
				infoPanel.setContent(((InfoEntity) clickedObject));
			}
		}

	}

	private Object findClickedObjectLongPressed(JDPoint p) {

		JDPoint inGameLocation = new JDPoint(
				viewportPosition.getX() + p.getX(), viewportPosition.getY()
						+ p.getY());

		int roomNrX = (inGameLocation.getX() / roomSize);
		int roomNrY = (inGameLocation.getY() / roomSize);

		JDPoint roomNumber = new JDPoint(roomNrX, roomNrY);

		List<GraphicObject> roomObjects = drawnObjects.get(roomNumber);
		if (roomObjects == null)
			return null;



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

		int roomNrX = (adjustedClickLocation.getX() / roomSize);
		int roomNrY = (adjustedClickLocation.getY() / roomSize);

		JDPoint roomNumber = new JDPoint(roomNrX, roomNrY);

		List<GraphicObject> roomObjects = drawnObjects.get(roomNumber);
		if (roomObjects == null)
			return null;
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
			if (arg0 == null)
				return 1;
			if (arg1 == null)
				return -1;
			Object clickedObject0 = arg0.getClickedObject();
			if (clickedObject0 == null)
				return 1;
			Object clickedObject1 = arg1.getClickedObject();
			if (clickedObject1 == null)
				return -1;

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
			if (o instanceof Chest) {
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

	public DungeonGame getDungeonGame() {
		return this.dungeonGame;
	}

	public void newStatement(String text, int styleCode) {
		// TODO Auto-generated method stub

	}

	public void newStatement(Statement s) {
		// TODO Auto-generated method stub

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

	public int getRoomSize() {
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
		Pair<Float, Float> currentViewCenterRoomCoordinates = getCurrentViewCenterRoomCoordinates();
		currentViewCenterRoomCoordinates = new Pair<Float, Float>(
				currentViewCenterRoomCoordinates.first - 0.5f,
				currentViewCenterRoomCoordinates.second - 0.5f);
		Pair<Float, Float> targetViewCenterRoomCoordinattes = new Pair<Float, Float>(
				new Float(number.getX()), new Float(number.getY()));

		System.out.println("scroll from "
				+ currentViewCenterRoomCoordinates.first + "/"
				+ currentViewCenterRoomCoordinates.second + " to "
				+ targetViewCenterRoomCoordinattes.first + "/"
				+ targetViewCenterRoomCoordinattes.second);

		MovieSequence sequence = new DefaultMovieSequence(
				new TrivialScaleSequence(this.roomSize),
				new StraightLineScroller(currentViewCenterRoomCoordinates,
						targetViewCenterRoomCoordinattes, duration), duration);
		this.sequenceManager.addSequence(sequence);

	}

	public void setInfoEntity(ItemInfo item) {
		this.infoPanel.setContent(item);

	}
}
