package graphics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import animation.DefaultAnimationSet;
import animation.Motion;
import dungeon.ChestInfo;
import dungeon.DoorInfo;
import dungeon.JDPoint;
import dungeon.Position;
import dungeon.PositionInRoomInfo;
import dungeon.RoomInfo;
import dungeon.util.RouteInstruction;
import figure.FigureInfo;
import figure.RoomObservationStatus;
import figure.hero.HeroInfo;
import figure.monster.Ghul;
import figure.monster.Monster;
import figure.monster.MonsterInfo;
import figure.monster.Ogre;
import figure.monster.Orc;
import figure.monster.Skeleton;
import figure.monster.Spider;
import figure.monster.Wolf;
import figure.other.Lioness;
import game.JDGUI;
import game.RoomInfoEntity;
import graphics.util.DrawingRectangle;
import graphics.util.RelativeRectangle;
import graphics.util.RoomSize;
import item.AttrPotion;
import item.DustItem;
import item.Item;
import item.ItemInfo;
import item.Key;
import item.equipment.Armor;
import item.equipment.Helmet;
import item.equipment.Shield;
import item.equipment.weapon.Axe;
import item.equipment.weapon.Club;
import item.equipment.weapon.Lance;
import item.equipment.weapon.Sword;
import item.equipment.weapon.Wolfknife;
import item.paper.Book;
import item.paper.InfoScroll;
import item.paper.Scroll;
import item.quest.DarkMasterKey;
import item.quest.Feather;
import item.quest.Incense;
import item.quest.Rune;
import item.quest.Thing;
import location.Corpse;
import location.HealthFountain;
import location.LevelExit;
import location.Location;
import location.LocationState;
import location.MoonRuneFinderShrine;
import location.RevealMapShrine;
import location.ScoutShrine;
import location.LocationInfo;
import location.SorcerLab;
import location.Statue;
import location.defender.DefenderLocation;
import log.Log;
import util.JDColor;
import util.JDDimension;

public class GraphicObjectRenderer {

	private final boolean memory = false;
	private final boolean visCheat = false;

	private int roomSize;

	private int roomSize_by_100;
	private int ROOMSIZE_BY_36;
	private int ROOMSIZE_BY_24;
	private int ROOMSIZE_BY_20;
	private int ROOMSIZE_BY_12;
	private int ROOMSIZE_BY_10;
	private int ROOMSIZE_BY_16;
	private static int ROOMSIZE_BY_8;
	private int ROOMSIZE_BY_6;
	private int ROOMSIZE_BY_5;
	private int ROOMSIZE_BY_3;
	private int ROOMSIZE_BY_2;
	private static final JDPoint[] positionCoord = new JDPoint[8];
	private final JDPoint[] positionCoordModified = new JDPoint[8];
	private final RelativeRectangle[] positionRectangles = new RelativeRectangle[8];

	public static final double HERO_SIZE_QUOTIENT_X = 2;

	public static final double HERO_SIZE_QUOTIENT_Y = 2;

	private JDGUI gui;
	private JDPoint[] itemPointsRelative;
	private int ROOMSIZE_BY_100;
	private JDDimension spotDimension;
	private JDPoint chestPosition;
	private RelativeRectangle westDoorRect;
	private RelativeRectangle northDoorRect;
	private RelativeRectangle eastDoorRect;
	private RelativeRectangle southDoorRect;
	private JDDimension chestDimension;
	private RelativeRectangle chestRect;
	private JDDimension monsterSizeS;
	private JDDimension monsterSizeL;
	private JDDimension monsterSizeSpider;
	private GraphicObject wallSouth;
	private JDDimension heroDimension;
	private JDImageLocated imageDoorWest;
	private JDImageLocated imageDoorWestLocked;
	private JDGraphicObject doorWestNone;
	private JDGraphicObject doorSouthNone;
	private JDImageLocated doorSouth;
	private JDImageLocated doorSouthLocked;
	private JDGraphicObject doorEastNone;
	private JDImageLocated doorEast;
	private JDImageLocated doorEastLocked;
	private JDGraphicObject doorNorthNone;
	private JDImageLocated doorNorth;
	private JDImageLocated doorNorthLocked;
	private RelativeRectangle shrineRect;
	private JDPoint spotPosition;

	private final Map<RoomInfoEntity, GraphicObject> graphicObjectCache = new HashMap<>();
	private RelativeRectangle roomRect;
	private RelativeRectangle wallRect;
	private ShrineRenderDimension healthFountainDimension;
	private ShrineRenderDimension statueDimension;
	private ShrineRenderDimension defenderDimension;
	private ShrineRenderDimension scoutShrineDimension;
	private ShrineRenderDimension sorcerLabDimension;
	private ShrineRenderDimension levelExitDimension;
	private ShrineRenderDimension corpseDimension;
	private ShrineRenderDimension revealMapShrineDimension;

	private JDPoint[] getPositionCoord() {
		return positionCoord;
	}

	public GraphicObjectRenderer(int roomSize) {
		init(roomSize);
	}

	public GraphicObjectRenderer(int roomSize, JDGUI gui) {
		this.gui = gui;
		if (gui == null) {
			throw new NullPointerException();
		}
		init(roomSize);
	}

	private void init(int roomSize) {
		this.roomSize = roomSize;
		ROOMSIZE_BY_100 = RoomSize.by(100, roomSize);
		ROOMSIZE_BY_36 = RoomSize.by(36, roomSize);
		ROOMSIZE_BY_24 = RoomSize.by(24, roomSize);
		ROOMSIZE_BY_20 = RoomSize.by(20, roomSize);
		ROOMSIZE_BY_12 = RoomSize.by(12, roomSize);
		ROOMSIZE_BY_10 = RoomSize.by(10, roomSize);
		ROOMSIZE_BY_16 = RoomSize.by(16, roomSize);
		ROOMSIZE_BY_8 = RoomSize.by(8, roomSize);
		ROOMSIZE_BY_6 = RoomSize.by(6, roomSize);
		ROOMSIZE_BY_5 = RoomSize.by(5, roomSize);
		ROOMSIZE_BY_3 = RoomSize.by(3, roomSize);
		ROOMSIZE_BY_2 = RoomSize.by(2, roomSize);

		updateLocationRenderInformationMap();

		itemPointsRelative = getItemPointsRelative();

		spotDimension = new JDDimension(roomSize / 7, roomSize / 7);
		spotPosition = new JDPoint((ROOMSIZE_BY_8), (6 * ROOMSIZE_BY_8));

		chestPosition = new JDPoint((ROOMSIZE_BY_8), ROOMSIZE_BY_10);
		chestDimension = new JDDimension(roomSize / 5, roomSize / 5);
		chestRect = new RelativeRectangle(chestPosition,
				chestDimension);

		for (int i = 0; i < positionCoord.length; i++) {
			positionCoord[i] = getPositionCoordinates(Position.Pos.fromValue(i), roomSize);
			positionCoordModified[i] = new JDPoint(positionCoord[i].getX() + ROOMSIZE_BY_20, positionCoord[i].getY());
			positionRectangles[i] = new RelativeRectangle(positionCoord[i].getX(), positionCoord[i].getY(), ROOMSIZE_BY_8, ROOMSIZE_BY_8);
		}

		JDDimension westDoorDimension = getDoorDimension(true, roomSize);
		westDoorRect = new RelativeRectangle(0, (roomSize / 2)
				- (westDoorDimension.getHeight() / 2), westDoorDimension.getWidth(), westDoorDimension.getHeight());

		JDDimension northDoorDimension = getDoorDimension(false, roomSize);
		northDoorRect = new RelativeRectangle(new JDPoint((roomSize / 2)
				- (northDoorDimension.getWidth() / 2), 0), northDoorDimension.getWidth(), northDoorDimension.getHeight());

		JDDimension eastDoorDimension = getDoorDimension(true, roomSize);
		eastDoorRect = new RelativeRectangle(new JDPoint((roomSize) - (eastDoorDimension.getWidth()), (roomSize / 2) - (eastDoorDimension
				.getHeight() / 2)), eastDoorDimension.getWidth(), eastDoorDimension.getHeight());

		JDDimension southDoorDimension = getDoorDimension(false, roomSize);
		southDoorRect = new RelativeRectangle((roomSize / 2) - (southDoorDimension.getWidth() / 2), roomSize - (southDoorDimension
				.getHeight()), southDoorDimension.getWidth(), southDoorDimension.getHeight());

		imageDoorWest = new JDImageLocated(
				ImageManager.door_west, new RelativeRectangle(0, 0
				- getDoorDimension(true, roomSize).getWidth(), roomSize, roomSize));

		imageDoorWestLocked = new JDImageLocated(
				ImageManager.door_west_lock, new RelativeRectangle(0, 0
				- getDoorDimension(true, roomSize).getWidth(), roomSize, roomSize));

		doorWestNone = new JDGraphicObject(new JDImageLocated(
				ImageManager.door_west_none, new RelativeRectangle(0, 0
				- getDoorDimension(true, roomSize).getWidth(), roomSize, roomSize)), null, null);

		doorSouthNone = new JDGraphicObject(new JDImageLocated(
				ImageManager.door_south_none, new RelativeRectangle(0, roomSize
				- getDoorDimension(true, roomSize).getWidth(),
				roomSize, roomSize)), null, southDoorRect);

		doorSouth = new JDImageLocated(
				ImageManager.door_south, new RelativeRectangle(0, roomSize
				- getDoorDimension(true, roomSize).getWidth(),
				roomSize, roomSize));

		doorSouthLocked = new JDImageLocated(
				ImageManager.door_south_lock, new RelativeRectangle(0, roomSize
				- getDoorDimension(true, roomSize).getWidth(),
				roomSize, roomSize));

		JDDimension doorDimension = this.getDoorDimension(true, roomSize);

		doorEastNone = new JDGraphicObject(new JDImageLocated(
				ImageManager.door_east_none, new RelativeRectangle(0, 0
				- getDoorDimension(true, roomSize).getWidth(),
				roomSize, roomSize)), null, null);

		doorEast = new JDImageLocated(
				ImageManager.door_east_lock, new RelativeRectangle(0, 0
				- getDoorDimension(true, roomSize).getWidth(),
				roomSize, roomSize));

		doorEastLocked = new JDImageLocated(
				ImageManager.door_east, new RelativeRectangle(0, 0
				- getDoorDimension(true, roomSize).getWidth(),
				roomSize, roomSize));

		doorNorthNone = new JDGraphicObject(new JDImageLocated(
				ImageManager.door_north_none, new RelativeRectangle(0, 0
				- getDoorDimension(true, roomSize).getWidth(),
				roomSize, roomSize)), null, null);

		doorNorth = new JDImageLocated(
				ImageManager.door_north_lock, new RelativeRectangle(0, 0
				- getDoorDimension(true, roomSize).getWidth(),
				roomSize, roomSize));

		doorNorthLocked = new JDImageLocated(
				ImageManager.door_north, new RelativeRectangle(0, 0
				- getDoorDimension(true, roomSize).getWidth(),
				roomSize, roomSize));

		wallSouth = new GraphicObject(null,
				new RelativeRectangle(0, roomSize - doorDimension.getWidth(),
						roomSize, roomSize), null, ImageManager.wall_southImage);

		wallRect = new RelativeRectangle(
				0, -getDoorDimension(true, roomSize).getWidth(),
				roomSize, roomSize);

		// room floor
		JDDimension roomBlackBackgroundDimension = new JDDimension(
				roomSize - 2 * getDoorDimension(true, roomSize).getWidth(),
				roomSize - 2 * getDoorDimension(true, roomSize).getWidth());
		RelativeRectangle roomBlackBackGroundRect = new RelativeRectangle(new JDPoint(
				getDoorDimension(true, roomSize).getWidth(),
				getDoorDimension(true, roomSize).getWidth()),
				roomBlackBackgroundDimension);

		JDDimension roomBackgroundDimension = new JDDimension(roomSize, (roomSize - (this.getDoorDimension(true, roomSize)
				.getWidth()))
				+ 1  // for some reason we need an additional pixel here to prevent a black gap (probably due to rounding issues somewhere)
		);
		roomRect = new RelativeRectangle(new JDPoint(0, 0), roomBackgroundDimension);

		heroDimension = new JDDimension((int) (((double) roomSize) / HERO_SIZE_QUOTIENT_X), (int) (((double) roomSize) / HERO_SIZE_QUOTIENT_Y));

		int roomSizeBy2Point5 = (int) (roomSize / 2.5);
		monsterSizeS = new JDDimension((int) roomSizeBy2Point5,
				(int) roomSizeBy2Point5);

		int roomSizeBy2 = (int) (roomSize / 2);
		monsterSizeL = new JDDimension(roomSizeBy2, roomSizeBy2);

		monsterSizeSpider = new JDDimension((int) (roomSize / 2.2),
				(int) (roomSize / 2.2));

		int xpos = (13 * roomSize / 20);
		int ypos = (roomSize / 36);
		int xsize = (int) (roomSize / 2.9);
		int ysize = (int) (roomSize / 2.2);
		shrineRect = new RelativeRectangle(new JDPoint(xpos, ypos), xsize, ysize);
	}

	public static int getPosSize() {
		return ROOMSIZE_BY_8;
	}

	public static JDPoint getPositionCoordinates(Position.Pos pos) {
		return positionCoord[pos.getValue()];
	}

	public static JDPoint getPositionCoordinates(Position.Pos pos, int roomSize) {
		int ROOMSIZE_BY_16 = roomSize / 16;
		int ROOMSIZE_BY_36 = roomSize / 36;
		if (pos == Position.Pos.NW) {
			return new JDPoint(roomSize / 4 - ROOMSIZE_BY_16,
					roomSize * 11 / 36 - ROOMSIZE_BY_36);
		}
		if (pos == Position.Pos.N) {
			return new JDPoint(roomSize / 2 - ROOMSIZE_BY_16,
					(int) (roomSize / 3.6) - ROOMSIZE_BY_36);
		}
		if (pos == Position.Pos.NE) {
			return new JDPoint(roomSize * 3 / 4 - ROOMSIZE_BY_16,
					roomSize * 11 / 36 - ROOMSIZE_BY_36);
		}
		if (pos == Position.Pos.E) {
			return new JDPoint(roomSize * 4 / 5 - ROOMSIZE_BY_16,
					(int) (roomSize / 1.85) - ROOMSIZE_BY_36);
		}
		if (pos == Position.Pos.SE) {
			return new JDPoint(roomSize * 3 / 4 - ROOMSIZE_BY_16,
					roomSize * 25 / 32 - ROOMSIZE_BY_36);
		}
		if (pos == Position.Pos.S) {
			return new JDPoint(roomSize / 2 - ROOMSIZE_BY_16,
					roomSize * 32 / 40 - ROOMSIZE_BY_36);
		}
		if (pos == Position.Pos.SW) {
			return new JDPoint(roomSize / 4 - ROOMSIZE_BY_16,
					roomSize * 25 / 32 - ROOMSIZE_BY_36);
		}
		if (pos == Position.Pos.W) {
			return new JDPoint(roomSize / 5 - ROOMSIZE_BY_16,
					(int) (roomSize / 1.85) - ROOMSIZE_BY_36);
		}

		Log.warning("Could not create coordinates for " + pos);
		return null;
	}

	private GraphicObject[] drawItems(ItemInfo[] itemArray) {
		if (itemArray == null) {
			return new GraphicObject[0];
		}

		GraphicObject[] itemObs = new GraphicObject[4];

		int i = 0;
		while (i < itemArray.length && i < 4) {
			JDPoint itemPointRelative = itemPointsRelative[i];

			if (itemArray[i] != null) {

				int roomSize_15_100 = 15 * ROOMSIZE_BY_100;
				int roomSize_12_100 = 12 * ROOMSIZE_BY_100;
				if (AttrPotion.class.isAssignableFrom(itemArray[i]
						.getItemClass())) {

					if (((itemArray[i]).getItemKey() == Item.ITEM_KEY_HEALPOTION)) {
						itemObs[i] = new GraphicObject(itemArray[i],
								new RelativeRectangle(itemPointRelative,
										roomSize_12_100,
										roomSize_15_100),
								JDColor.YELLOW,
								ImageManager.getImage(itemArray[i]));
					}
					else {
						itemObs[i] = new GraphicObject(itemArray[i],
								new RelativeRectangle(itemPointRelative,
										roomSize_12_100,
										roomSize_15_100),
								JDColor.YELLOW,
								ImageManager.getImage(itemArray[i]));
					}
				}
				else if (itemArray[i].getItemClass().equals(DustItem.class)) {
					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative,
									10 * ROOMSIZE_BY_100,
									8 * ROOMSIZE_BY_100),
							JDColor.YELLOW, ImageManager.getImage(itemArray[i]));
				}
				else if (itemArray[i].getItemClass().equals(Sword.class)) {
					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative,
									30 * ROOMSIZE_BY_100,
									20 * ROOMSIZE_BY_100), JDColor.YELLOW,
							ImageManager.getImage(itemArray[i]));
				}
				else if (itemArray[i].getItemClass().equals(Axe.class)) {
					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative,
									30 * ROOMSIZE_BY_100,
									20 * ROOMSIZE_BY_100), JDColor.YELLOW,
							ImageManager.getImage(itemArray[i]));
				}
				else if (itemArray[i].getItemClass().equals(Club.class)) {
					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative,
									30 * ROOMSIZE_BY_100,
									20 * ROOMSIZE_BY_100), JDColor.YELLOW,
							ImageManager.getImage(itemArray[i]));
				}
				else if (itemArray[i].getItemClass().equals(Lance.class)) {
					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative,
									50 * ROOMSIZE_BY_100,
									40 * ROOMSIZE_BY_100), JDColor.YELLOW,
							ImageManager.getImage(itemArray[i]));
				}
				else if (itemArray[i].getItemClass().equals(Wolfknife.class)) {
					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative,
									20 * ROOMSIZE_BY_100,
									roomSize_15_100), JDColor.YELLOW,
							ImageManager.getImage(itemArray[i]));
				}
				else if (itemArray[i].getItemClass().equals(Armor.class)) {
					int sizeX = 30 * ROOMSIZE_BY_100;
					int sizeY = 20 * ROOMSIZE_BY_100;

					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative, sizeX, sizeY),
							JDColor.YELLOW, ImageManager.getImage(itemArray[i]));
				}
				else if (itemArray[i].getItemClass().equals(Shield.class)) {
					int sizeX = 20 * ROOMSIZE_BY_100;

					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative, sizeX, roomSize_15_100),
							JDColor.YELLOW, ImageManager.getImage(itemArray[i]));
				}
				else if (itemArray[i].getItemClass().equals(Helmet.class)) {
					int sizeX = 24 * ROOMSIZE_BY_100;
					int sizeY = 22 * ROOMSIZE_BY_100;

					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative, sizeX, sizeY),
							JDColor.YELLOW, ImageManager.getImage(itemArray[i]));
				}
				else if (Scroll.class.isAssignableFrom(itemArray[i]
						.getItemClass())) {

					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative, roomSize_15_100, roomSize_15_100),
							JDColor.YELLOW, ImageManager.getImage(itemArray[i]));
				}
				else if (itemArray[i].getItemClass().equals(InfoScroll.class)) {

					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative, roomSize_15_100, roomSize_15_100),
							JDColor.YELLOW, ImageManager.getImage(itemArray[i]));
				}
				else if (itemArray[i].getItemClass().equals(Feather.class)) {

					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative, roomSize_15_100, roomSize_15_100),
							JDColor.YELLOW, ImageManager.getImage(itemArray[i]));
				}
				else if (itemArray[i].getItemClass().equals(Incense.class)) {

					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative, roomSize_15_100, roomSize_15_100),
							JDColor.YELLOW, ImageManager.getImage(itemArray[i]));
				}
				else if (itemArray[i].getItemClass().equals(Key.class)) {
					int sizeX = 16 * ROOMSIZE_BY_100;
					int sizeY = 22 * ROOMSIZE_BY_100;

					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative, sizeX, sizeY),
							JDColor.YELLOW, ImageManager.getImage(itemArray[i]));
				}
				else if (itemArray[i].getItemClass().equals(Rune.class)) {

					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative, roomSize_12_100, roomSize_12_100),
							JDColor.YELLOW, ImageManager.getImage(itemArray[i]));
				}
				else if (itemArray[i].getItemClass().equals(
						DarkMasterKey.class)) {

					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative, roomSize_12_100, roomSize_12_100),
							JDColor.YELLOW, ImageManager.getImage(itemArray[i]));
				}
				else if (itemArray[i].getItemClass().equals(Book.class)) {

					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative, roomSize_15_100, roomSize_15_100),
							JDColor.YELLOW, ImageManager.getImage(itemArray[i]));
				}
				else if (Thing.class.isAssignableFrom(itemArray[i]
						.getItemClass())) {

					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative, roomSize_12_100, roomSize_12_100),
							JDColor.YELLOW, ImageManager.getImage(itemArray[i]));
				}
				else {
					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative, roomSize_15_100, roomSize_15_100),
							JDColor.YELLOW, ImageManager.getImage(itemArray[i]));
				}
			}
			else {
				itemObs[i] = null;
			}
			i++;
		}
		return itemObs;
	}

	private void drawDoors(RoomInfo r, Collection<GraphicObject> drawObjects) {
		DoorInfo[] doors = r.getDoors();
		if (doors == null) return;
		if (doors[0] != null) {
			JDGraphicObject door0;
			if (!doors[0].hasLock()) {
				door0 = new JDGraphicObject(doorNorthLocked, doors[0], northDoorRect);
			}
			else {
				door0 = new JDGraphicObject(doorNorth, doors[0], northDoorRect);
			}
			drawObjects.add(door0);
		}
		else {
			drawObjects.add(doorNorthNone);
		}
		if (doors[1] != null) {
			JDGraphicObject door1;
			if (!doors[1].hasLock()) {
				door1 = new JDGraphicObject(doorEastLocked, doors[1], eastDoorRect);
			}
			else {
				door1 = new JDGraphicObject(doorEast, doors[1], eastDoorRect);
			}
			drawObjects.add(door1);
		}
		else {
			drawObjects.add(doorEastNone);
		}
		if (doors[2] != null) {
			/*
			 * there is a door to south
			 */
			if (doors[2].hasLock()) {
				/*
				 * with lock
				 */
				drawObjects.add(new JDGraphicObject(doorSouthLocked, doors[2], southDoorRect, null));
			}
			else {
				/*
				 * without lock
				 */
				drawObjects.add(new JDGraphicObject(doorSouth, doors[2], southDoorRect, null));
			}
		}
		else {
			/*
			 * no door to south at all --> wall
			 */

			drawObjects.add(doorSouthNone);
		}
		if (doors[3] != null) {
			JDGraphicObject door3;
			if (!doors[3].hasLock()) {
				door3 = new JDGraphicObject(imageDoorWest, doors[3], westDoorRect, null);
			}
			else {
				door3 = new JDGraphicObject(imageDoorWestLocked, doors[3], westDoorRect, null);
			}
			drawObjects.add(door3);
		}
		else {
			drawObjects.add(doorWestNone);
		}
	}

	private JDDimension getDoorDimension(boolean vertical, int roomSize) {
		int width;
		int heigth;
		if (vertical) {
			width = roomSize / 15;
			heigth = (roomSize / 4);
		}
		else {
			width = roomSize / 4;
			heigth = roomSize / 8;
		}
		return new JDDimension(width, heigth);
	}

	private JDPoint[] getItemPoints(int xcoord, int ycoord) {
		JDPoint[] points = new JDPoint[4];

		points[0] = new JDPoint(xcoord + ROOMSIZE_BY_2 - ROOMSIZE_BY_8, ycoord
				+ ROOMSIZE_BY_3);
		points[1] = new JDPoint(xcoord + ROOMSIZE_BY_2, ycoord + ROOMSIZE_BY_2
				- ROOMSIZE_BY_16);
		points[2] = new JDPoint(xcoord + ROOMSIZE_BY_3, ycoord
				+ (3 * ROOMSIZE_BY_5));
		points[3] = new JDPoint(xcoord + 3 * ROOMSIZE_BY_5, ycoord
				+ (3 * ROOMSIZE_BY_5));

		return points;
	}

	private JDPoint[] getItemPointsRelative() {
		return getItemPoints(0, 0);
	}

	public JDDimension getFigureInfoSize(FigureInfo figureInfo) {
		if (figureInfo instanceof HeroInfo) {
			return heroDimension;
		}
		if (figureInfo instanceof MonsterInfo) {
			return getMonsterSize((MonsterInfo) figureInfo);
		}
		return null;
	}

	private GraphicObject drawAFigure(FigureInfo m, JDPoint relativeCoordinates) {

		JDDimension figureInfoSize = getFigureInfoSize(m);
		RelativeRectangle monsterDrawRect = getHeroRectangle(relativeCoordinates.getX(), relativeCoordinates
				.getY(), figureInfoSize);
		JDImageProxy<?> image = ImageManager.getImage(m, m.getLookDirection());
		if (m.isDead()) {
			final DefaultAnimationSet dyingAnimationSet = ImageManager.getAnimationSet((FigureInfo)m, Motion.TippingOver, m.getLookDirection());

			if (dyingAnimationSet != null) {
				image = dyingAnimationSet.getImagesNr(dyingAnimationSet.getLength() - 1);
			}
		}
		JDImageLocated ob = new JDImageLocated(image, monsterDrawRect);

		int fifthRoomSize = roomSize / 5;
		RelativeRectangle monsterClickRect = new RelativeRectangle(
				relativeCoordinates.getX() - fifthRoomSize / 2,
				relativeCoordinates.getY() - fifthRoomSize / 2,
				fifthRoomSize,
				fifthRoomSize);

		return new JDGraphicObject(ob, m, monsterDrawRect, JDColor.WHITE, monsterClickRect);
	}

	private JDDimension getMonsterSize(MonsterInfo m) {
		Class<? extends Monster> mClass = m.getMonsterClass();
		if (mClass == Wolf.class) {
			return monsterSizeS;
		}
		if (mClass == Orc.class) {
			return monsterSizeS;
		}
		if (mClass == Skeleton.class) {
			return monsterSizeS;
		}
		if (mClass == Ghul.class) {
			return monsterSizeL;
		}
		if (mClass == Ogre.class) {
			return monsterSizeL;
		}
		if (mClass == Spider.class) {
			return monsterSizeSpider;
		}
		if (mClass == Lioness.class) {
			return monsterSizeL;
		}
		return monsterSizeS;
	}

	private GraphicObject[] drawFigures(List<FigureInfo> monsterList) {
		if (monsterList == null) {
			return new GraphicObject[] {};
		}
		int k = monsterList.size();
		GraphicObject obs[] = new GraphicObject[k];
		if (monsterList.size() > 8) {
			obs = new GraphicObject[8];
		}
		for (int i = 0; i < monsterList.size(); i++) {

			FigureInfo m = (monsterList.get(i));
			int position = m.getPositionInRoomIndex();

			GraphicObject gr = drawAFigure(m, getPositionCoordModified(position));
			if (i >= 8) {
				break;
			}
			obs[i] = gr;
		}

		return obs;
	}

	private GraphicObject[] drawDeadFigures(List<FigureInfo> figures) {
		if (figures == null) {
			return new GraphicObject[] {};
		}
		int k = figures.size();
		GraphicObject obs[] = new GraphicObject[k];
		for (int i = 0; i < figures.size(); i++) {

			FigureInfo m = (figures.get(i));
			int position = m.getPositionInRoomIndex();
			GraphicObject gr = drawAFigure(m, getPositionCoordModified(position));
			obs[i] = gr;
		}

		return obs;
	}

	public JDPoint getPositionCoordModified(int index) {
		if (index >= 0 && index < 8) {
			return positionCoordModified[index];
		}
		return null;
	}

	private static class ShrineRenderDimension extends RelativeRectangle {

		public ShrineRenderDimension(int offsetInRoomX, int offsetInRoomY, int width, int height) {
			super(offsetInRoomX, offsetInRoomY, width, height);
		}
	}

	private static final Map<Class<? extends Location>, Map<LocationState, JDImageLocated>> locationRenderInformationMap = new HashMap<>();

	private void updateLocationRenderInformationMap() {
		healthFountainDimension = new ShrineRenderDimension( (2 * ROOMSIZE_BY_3),  (1 * ROOMSIZE_BY_16), ROOMSIZE_BY_3, (int) (roomSize / 3.5));
		statueDimension = new ShrineRenderDimension( (16 * ROOMSIZE_BY_24),  (0 * ROOMSIZE_BY_36), ROOMSIZE_BY_3, (int) (roomSize / 2.5));
		defenderDimension = new ShrineRenderDimension( (16 * ROOMSIZE_BY_24),  (0 * ROOMSIZE_BY_36), (int) (roomSize / 1.9), (int) (roomSize / 2.0));
		scoutShrineDimension = new ShrineRenderDimension( (16 * ROOMSIZE_BY_24),  (0 * ROOMSIZE_BY_36), ROOMSIZE_BY_3, (int) (roomSize / 2.5));
		sorcerLabDimension = new ShrineRenderDimension( (25 * roomSize / 60),   - (1 * ROOMSIZE_BY_12), (int) (roomSize / 1.65),  (int) (roomSize / 1.65));
		levelExitDimension = new ShrineRenderDimension( (2 * ROOMSIZE_BY_3),   (1 * ROOMSIZE_BY_6), roomSize / 4,  (ROOMSIZE_BY_6));
		corpseDimension = new ShrineRenderDimension( (13 * roomSize / 20),   (6 * roomSize / 36), (int) (roomSize / 3.2),  (int) (roomSize / 3.8));
		revealMapShrineDimension = new ShrineRenderDimension( (7 * ROOMSIZE_BY_10),   (1 * roomSize / 24), (int) (roomSize / 3.4),  (int) (roomSize / 2.7));

		locationRenderInformationMap.put(HealthFountain.class, createDefaultLocationMap(healthFountainDimension, HealthFountain.class));
		locationRenderInformationMap.put(MoonRuneFinderShrine.class, createDefaultLocationMap(healthFountainDimension, MoonRuneFinderShrine.class));
		locationRenderInformationMap.put(Statue.class, createDefaultLocationMap(statueDimension, MoonRuneFinderShrine.class));

		Map<LocationState, JDImageLocated> defenderMap = createDefaultLocationMap(defenderDimension, MoonRuneFinderShrine.class);
		defenderMap.put(DefenderLocation.DefenderState.Activated, new JDImageLocated(ImageManager.getImage(DefenderLocation.class), defenderDimension));
		defenderMap.put(DefenderLocation.DefenderState.Inactive, new JDImageLocated(ImageManager.getImage(DefenderLocation.class), defenderDimension));
		locationRenderInformationMap.put(DefenderLocation.class, defenderMap);

		locationRenderInformationMap.put(ScoutShrine.class, createDefaultLocationMap(scoutShrineDimension, ScoutShrine.class));
		locationRenderInformationMap.put(SorcerLab.class, createDefaultLocationMap(sorcerLabDimension, SorcerLab.class));
		locationRenderInformationMap.put(LevelExit.class, createDefaultLocationMap(levelExitDimension, LevelExit.class));
		locationRenderInformationMap.put(Corpse.class, createDefaultLocationMap(corpseDimension, Corpse.class));
		locationRenderInformationMap.put(RevealMapShrine.class, createDefaultLocationMap(revealMapShrineDimension, RevealMapShrine.class));
	}

	private Map<LocationState, JDImageLocated> createDefaultLocationMap(ShrineRenderDimension statueDimension, Class<? extends Location> locationClazz) {
		Map<LocationState, JDImageLocated> statueMap = new HashMap<>();
		JDImageLocated statuePair = new JDImageLocated(ImageManager.getImage(locationClazz), statueDimension);
		statueMap.put(Location.DEFAULT_STATE, statuePair);
		return statueMap;
	}

	private JDGraphicObject getShrineGraphicObject(LocationInfo s) {

		Class<? extends Location> locationClass = s.getShrineClass();
		Map<LocationState, JDImageLocated> locationRenderInformationMap = GraphicObjectRenderer.locationRenderInformationMap.get(locationClass);
		if(locationRenderInformationMap != null) {
			JDImageLocated imageInfo = locationRenderInformationMap.get(s.getState());
			return new JDGraphicObject(imageInfo, s, shrineRect);
		}
		return null;
	}


	public JDGraphicObject getHeroGraphicObject(HeroInfo info) {
		int positionInRoomIndex = info
				.getPositionInRoomIndex();
		if (positionInRoomIndex == -1) return null;
		JDPoint p = getPositionCoordModified(positionInRoomIndex);
		int xpos = p.getX();
		int ypos = p.getY();
		JDDimension figureInfoSize = getFigureInfoSize(info);
		RelativeRectangle heroRectangle = getHeroRectangle(xpos, ypos, figureInfoSize);

		JDImageLocated im = getImage(info, heroRectangle);

		return new JDGraphicObject(im, info, heroRectangle, JDColor.WHITE, getHalfSizeRect(heroRectangle));
	}

	private RelativeRectangle getHeroRectangle(int xpos, int ypos, JDDimension figureInfoSize) {
		int xHeroSize = figureInfoSize.getWidth();
		int yHeroSize = figureInfoSize.getHeight();
		return new RelativeRectangle(xpos - (xHeroSize / 2), ypos - (yHeroSize / 2), xHeroSize, yHeroSize);
	}

	private JDImageLocated getImage(HeroInfo info, RelativeRectangle rect) {
		if (info == null) return null;
		JDImageLocated im = null;
		JDImageProxy imageProxy = null;
		if (info.isDead()) {
			DefaultAnimationSet animationSet = ImageManager.getAnimationSet(info.getFigurePresentation(), Motion.TippingOver, RouteInstruction.Direction.fromInteger(info.getLookDirection().getValue()));
			// we take the last image from the tipping over animation
			if (animationSet == null) {
				Log.warning("No image found for dead " + info.toString());
				return null;
			}
			imageProxy = animationSet.getImagesNr(animationSet.getLength() - 1);
		}
		else {
			Motion motion = null;
			RoomInfo roomInfo = info.getRoomInfo();
			boolean fightRunning = false;
			if (roomInfo != null) {
				fightRunning = roomInfo.fightRunning();
			}
			if (fightRunning) {
				motion = Motion.Slaying;
			}
			else {
				motion = Motion.Walking;
			}
			DefaultAnimationSet animationSet = ImageManager.getAnimationSet(info.getFigurePresentation(), motion, RouteInstruction.Direction
					.fromInteger(info.getLookDirection().getValue()));
			if (animationSet == null) {
				Log.warning("No image found for motion " + motion + " + " + info.toString());
				return null;
			}
			imageProxy = animationSet.getImagesNr(0);
		}
		im = new JDImageLocated(imageProxy, rect);
		return im;
	}

	private RelativeRectangle getHalfSizeRect(DrawingRectangle r) {
		int sizex = r.getWidth();
		int sizey = r.getHeight();
		int newSizex = sizex / 2;
		int newSizey = sizey / 2;
		int newPointx = r.getX(0) + newSizex / 2;
		int newPointy = r.getY(0) + newSizey / 2;
		return new RelativeRectangle(newPointx, newPointy, newSizex, newSizey);
	}

	private GraphicObject drawBackGround(RoomInfo roomInfo) {

		int status = roomInfo.getVisibilityStatus();
		int floorType = roomInfo.getFloorIndex();
		JDImageProxy<?> im = null; //ImageManager.floorImageArray[floorType];
		if (status < RoomObservationStatus.VISIBILITY_FOUND) {
			// not found, just draw dark background
			return null;
		}
		else if (status == RoomObservationStatus.VISIBILITY_FOUND || status == RoomObservationStatus.VISIBILITY_SHRINE) {
			im = ImageManager.floorImage_mediumArray[floorType];
		}
		else if (status > RoomObservationStatus.VISIBILITY_SHRINE) {
			im = ImageManager.floorImageArray[floorType];
		}

		return new GraphicObject(roomInfo, roomRect, null, false, im);
	}

	private GraphicObject drawWallSouth(RoomInfo r) {
		if (r.getVisibilityStatus() >= RoomObservationStatus.VISIBILITY_FOUND) {
			return wallSouth;
		}
		return null;
	}


	public void invalidateCache(RoomInfoEntity entity) {
		graphicObjectCache.remove(entity);
	}

	public List<GraphicObject> createGraphicObjectsForRoom(RoomInfo r, int roomOffsetX, int roomOffsetY) {

		List<GraphicObject> graphObs = new ArrayList<>();
		if (r == null) {
			return graphObs;
		}

		/*
		 * room
		 */
		GraphicObject roomOb = drawBackGround(r);
		if (roomOb != null) {
			graphObs.add(roomOb);
		}

		/*
		 * wall
		 */
		int status = r.getVisibilityStatus();
		if (status >= RoomObservationStatus.VISIBILITY_FOUND) {

			graphObs.add(new GraphicObject(r, wallRect, null,
					ImageManager.wall_sidesImage));

			graphObs.add(new GraphicObject(r, wallRect, null,
					ImageManager.wall_northImage));
		}

		/*
		 * wall south
		 */
		GraphicObject wallObSouth = drawWallSouth(r);
		if (wallObSouth != null) {
			graphObs.add(wallObSouth);
		}

		/*
		 * doors
		 */
		drawDoors(r, graphObs);

		if ((status >= RoomObservationStatus.VISIBILITY_SHRINE)) {

			// draw room position markers
			if (r.equals(gui.getFigure().getRoomInfo())) {
				JDPoint[] positionCoord = getPositionCoord();
				for (int i = 0; i < positionCoord.length; i++) {
					PositionInRoomInfo positionInRoom = r.getPositionInRoom(i);
					GraphicObject ob = graphicObjectCache.get(positionInRoom);
					if (ob == null) {
						ob = new GraphicObject(
								positionInRoom, positionRectangles[i], JDColor.BLACK,
								ImageManager.fieldImage);
						graphicObjectCache.put(positionInRoom, ob);
					}
					graphObs.add(ob);
				}
			}

			GraphicObject wallSides = new GraphicObject(r, wallRect, JDColor.DARK_GRAY,
					ImageManager.wall_sidesImage);
			graphObs.add(wallSides);

			GraphicObject ob = null;
			if (r.getShrine() != null) {
				LocationInfo s = r.getShrine();
				ob = graphicObjectCache.get(s);
				if (ob == null) {
					ob = getShrineGraphicObject(s);
					if(ob != null) {
						graphicObjectCache.put(s, ob);
					}
				}
				graphObs.add(ob);
			}

			ChestInfo chest = r.getChest();
			if (chest != null) {
				GraphicObject chestOb = graphicObjectCache.get(chest);
				if (chestOb == null) {
					if (chest.hasLock()) {
						chestOb = new GraphicObject(chest, chestRect, null, ImageManager.chest_lockImage);
					}
					else {
						chestOb = new GraphicObject(chest, chestRect, null, ImageManager.chestImage);
					}
					graphicObjectCache.put(chest, chestOb);
				}
				graphObs.add(chestOb);
			}
		}
 		if ((status >= RoomObservationStatus.VISIBILITY_FIGURES)) {

			// draw dead figures first, as they lay down on the floor
			final List<FigureInfo> deadFigures = r.getDeadFigureInfos();
			if (deadFigures != null && !deadFigures.isEmpty()) {
				GraphicObject[] deadFigureObs = drawDeadFigures(deadFigures);
				graphObs.addAll(Arrays.asList(deadFigureObs));
			}

			// draw alive figures second to be displayed in foreground
			List<FigureInfo> figureInfos = r.getFigureInfos();
			if (figureInfos != null && !figureInfos.isEmpty()) {
				GraphicObject[] monsterObs = drawFigures(figureInfos);
				for (int i = 0; i < monsterObs.length; i++) {
					if (monsterObs[i] != null) {
						graphObs.add(monsterObs[i]);
					}
				}
			}
		}
		if ((status >= RoomObservationStatus.VISIBILITY_ITEMS)) {

			GraphicObject[] itObs = drawItems(r.getItemArray());
			for (int i = 0; i < itObs.length; i++) {
				GraphicObject o = itObs[i];
				if (o != null) {
					graphObs.add(o);
				}
			}
		}


		return graphObs;
	}

}
