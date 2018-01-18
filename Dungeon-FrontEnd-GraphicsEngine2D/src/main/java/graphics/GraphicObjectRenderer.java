package graphics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import animation.DefaultAnimationSet;
import animation.AnimationUtils;
import animation.Motion;
import dungeon.DoorInfo;
import dungeon.JDPoint;
import dungeon.Position;
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
import game.JDGUI;
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
import item.quest.LuziasBall;
import item.quest.Rune;
import item.quest.Thing;
import log.Log;
import shrine.Angel;
import shrine.Brood;
import shrine.Luzia;
import shrine.Shrine;
import shrine.ShrineInfo;
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
	private int ROOMSIZE_BY_8;
	private int ROOMSIZE_BY_6;
	private int ROOMSIZE_BY_5;
	private int ROOMSIZE_BY_3;
	private int ROOMSIZE_BY_2;
	private final JDPoint[] positionCoord = new JDPoint[8];

	public static final double HERO_POINT_QUOTIENT_X = 2.2;

	public static final double HERO_POINT_QUOTIENT_Y = 2.2;

	public static final int HERO_POINT_OFFSET_Y = 0;

	public static final int HERO_POINT_OFFSET_X = 15;

	public static final double HERO_SIZE_QUOTIENT_X = 2;

	public static final double HERO_SIZE_QUOTIENT_Y = 2;

	//private final Vector<GraphicObject> rooms = new Vector<GraphicObject>();

	//private final Vector<GraphicObject> shrines = new Vector<GraphicObject>();

	//private final Vector<GraphicObject> items = new Vector<GraphicObject>();

	//private final Vector<GraphicObject> doors = new Vector<GraphicObject>();

	//private final Vector<GraphicObject> chests = new Vector<GraphicObject>();

	//private final Vector<GraphicObject> spots = new Vector<GraphicObject>();

	//private final Vector<GraphicObject> walls = new Vector<GraphicObject>();

	//private final Vector<GraphicObject> lastWalls = new Vector<GraphicObject>();

	//private GraphicObject hero;

	private JDGUI gui;
	private int posSize;
	private JDPoint[] itemPointsRelative;
	private int ROOMSIZE_BY_100;

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
		itemPointsRelative = getItemPointsRelative();
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

		posSize = getPosSize(roomSize);

		assert positionCoord != null;
		for (int i = 0; i < positionCoord.length; i++) {
			positionCoord[i] = getPositionCoordinates(Position.Pos.fromValue(i), roomSize);
		}
	}

	public static int getPosSize(int roomSize) {
		return RoomSize.by(8, roomSize);
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
					roomSize *  11 / 36 - ROOMSIZE_BY_36);
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
					roomSize * 33 / 40 - ROOMSIZE_BY_36);
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

	private GraphicObject[] drawItems(ItemInfo[] itemArray, int roomSize) {
		if (itemArray == null) {
			return new GraphicObject[0];
		}


		GraphicObject[] itemObs = new GraphicObject[4];

		// Schleife bis 4
		int i = 0;
		while (i < itemArray.length && i < 4) {
			JDPoint itemPointRelative = itemPointsRelative[i];

			if (itemArray[i] != null) {


				if (AttrPotion.class.isAssignableFrom(itemArray[i]
						.getItemClass())) {

					if (((itemArray[i]).getItemKey() == Item.ITEM_KEY_HEALPOTION)) {
						itemObs[i] = new GraphicObject(itemArray[i],
								new RelativeRectangle(itemPointRelative,
										12 * ROOMSIZE_BY_100,
										15 * ROOMSIZE_BY_100),
								JDColor.YELLOW,
								ImageManager.getImage(itemArray[i]));
					}
					else {
						itemObs[i] = new GraphicObject(itemArray[i],
								new RelativeRectangle(itemPointRelative,
										12 * ROOMSIZE_BY_100,
										15 * ROOMSIZE_BY_100),
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
									15 * ROOMSIZE_BY_100), JDColor.YELLOW,
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
					int sizeY = 15 * ROOMSIZE_BY_100;

					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative, sizeX, sizeY),
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
					int sizeX = 15 * ROOMSIZE_BY_100;
					int sizeY = 15 * ROOMSIZE_BY_100;

					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative, sizeX, sizeY),
							JDColor.YELLOW, ImageManager.getImage(itemArray[i]));
				}
				else if (itemArray[i].getItemClass().equals(InfoScroll.class)) {
					int sizeX = 15 * ROOMSIZE_BY_100;
					int sizeY = 15 * ROOMSIZE_BY_100;

					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative, sizeX, sizeY),
							JDColor.YELLOW, ImageManager.getImage(itemArray[i]));
				}
				else if (itemArray[i].getItemClass().equals(Feather.class)) {
					int sizeX = 15 * ROOMSIZE_BY_100;
					int sizeY = 15 * ROOMSIZE_BY_100;

					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative, sizeX, sizeY),
							JDColor.YELLOW, ImageManager.getImage(itemArray[i]));
				}
				else if (itemArray[i].getItemClass().equals(Incense.class)) {
					int sizeX = 15 * ROOMSIZE_BY_100;
					int sizeY = 15 * ROOMSIZE_BY_100;

					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative, sizeX, sizeY),
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
					int sizeX = 12 * ROOMSIZE_BY_100;
					int sizeY = 12 * ROOMSIZE_BY_100;

					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative, sizeX, sizeY),
							JDColor.YELLOW, ImageManager.getImage(itemArray[i]));
				}
				else if (itemArray[i].getItemClass().equals(
						DarkMasterKey.class)) {
					int sizeX = 12 * ROOMSIZE_BY_100;
					int sizeY = 12 * ROOMSIZE_BY_100;

					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative, sizeX, sizeY),
							JDColor.YELLOW, ImageManager.getImage(itemArray[i]));
				}
				else if (itemArray[i].getItemClass().equals(LuziasBall.class)) {
					int sizeX = 15 * ROOMSIZE_BY_100;
					int sizeY = 15 * ROOMSIZE_BY_100;

					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative, sizeX, sizeY),
							JDColor.YELLOW, ImageManager.getImage(itemArray[i]));
				}
				else if (itemArray[i].getItemClass().equals(Book.class)) {
					int sizeX = 15 * ROOMSIZE_BY_100;
					int sizeY = 15 * ROOMSIZE_BY_100;

					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative, sizeX, sizeY),
							JDColor.YELLOW, ImageManager.getImage(itemArray[i]));

				}
				else if (Thing.class.isAssignableFrom(itemArray[i]
						.getItemClass())) {
					int sizeX = 12 * ROOMSIZE_BY_100;
					int sizeY = 12 * ROOMSIZE_BY_100;

					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative, sizeX, sizeY),
							JDColor.YELLOW, ImageManager.getImage(itemArray[i]));
				}
				else {
					int sizeX = 15 * ROOMSIZE_BY_100;
					int sizeY = 15 * ROOMSIZE_BY_100;
					itemObs[i] = new GraphicObject(itemArray[i],
							new RelativeRectangle(itemPointRelative, sizeX, sizeY),
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

	private List<GraphicObject> drawDoors(RoomInfo r, int roomOffsetX, int roomOffsetY) {
		DoorInfo[] doors = r.getDoors();
		if (doors == null) {
			return Collections.emptyList();
		}
		List<GraphicObject> roomDoors = new LinkedList<GraphicObject>();
		if (doors[0] != null) {
			JDGraphicObject door0;
			if (!doors[0].hasLock()) {
				DrawingRectangle rect = getNorthDoorRect();
				door0 = new JDGraphicObject(new JDImageLocated(
						ImageManager.door_north, roomOffsetX, roomOffsetY
						- getDoorDimension(true, roomSize).getWidth(),
						roomSize, roomSize), doors[0], rect, null, false);
			}
			else {
				DrawingRectangle rect = getNorthDoorRect();
				door0 = new JDGraphicObject(new JDImageLocated(
						ImageManager.door_north_lock, roomOffsetX, roomOffsetY
						- getDoorDimension(true, roomSize).getWidth(),
						roomSize, roomSize), doors[0], rect, null, false);
			}
			roomDoors.add(door0);
		}
		else {
			JDGraphicObject door0 = new JDGraphicObject(new JDImageLocated(
					ImageManager.door_north_none, roomOffsetX, roomOffsetY
					- getDoorDimension(true, roomSize).getWidth(),
					roomSize, roomSize), doors[0], null, null, false);
			roomDoors.add(door0);
		}
		if (doors[1] != null) {
			DrawingRectangle rect = getEastDoorRect();
			JDGraphicObject door1;
			if (!doors[1].hasLock().booleanValue()) {
				door1 = new JDGraphicObject(new JDImageLocated(
						ImageManager.door_east, roomOffsetX, roomOffsetY
						- getDoorDimension(true, roomSize).getWidth(),
						roomSize, roomSize), doors[1], rect, null, false);
			}
			else {
				door1 = new JDGraphicObject(new JDImageLocated(
						ImageManager.door_east_lock, roomOffsetX, roomOffsetY
						- getDoorDimension(true, roomSize).getWidth(),
						roomSize, roomSize), doors[1], rect, null, false);
			}
			roomDoors.add(door1);
		}
		else {
			JDGraphicObject door1 = new JDGraphicObject(new JDImageLocated(
					ImageManager.door_east_none, roomOffsetX, roomOffsetY
					- getDoorDimension(true, roomSize).getWidth(),
					roomSize, roomSize), doors[1], null, null, false);
			roomDoors.add(door1);
		}
		DrawingRectangle rectSouth = GraphicObjectRenderer.getSouthDoorRect(roomSize);
		if (doors[2] != null) {
			/*
			 * there is a door to south
			 */
			if (doors[2].hasLock()) {
				/*
				 * with lock
				 */
				roomDoors.add(new JDGraphicObject(new JDImageLocated(
						ImageManager.door_south_lock, roomOffsetX, roomOffsetY + roomSize
						- getDoorDimension(true, roomSize).getWidth(),
						roomSize, roomSize), doors[2], rectSouth, null, false));
			}
			else {
				/*
				 * without lock
				 */
				roomDoors.add(new JDGraphicObject(new JDImageLocated(
						ImageManager.door_south, roomOffsetX, roomOffsetY + roomSize
						- getDoorDimension(true, roomSize).getWidth(),
						roomSize, roomSize), doors[2], rectSouth, null, false));
			}

		}
		else {
			/*
			 * no door to south at all --> wall
			 */
			roomDoors.add(new JDGraphicObject(new JDImageLocated(
					ImageManager.door_south_none, roomOffsetX, roomOffsetY + roomSize
					- getDoorDimension(true, roomSize).getWidth(),
					roomSize, roomSize), doors[2], rectSouth, null, false));
		}
		if (doors[3] != null) {
			DrawingRectangle rect = getWestDoorRect();
			JDGraphicObject door3;

			if (!doors[3].hasLock()) {
				JDImageLocated jdImageLocated = new JDImageLocated(
						ImageManager.door_west, roomOffsetX, roomOffsetY
						- getDoorDimension(true, roomSize).getWidth(),
						roomSize, roomSize);
				door3 = new JDGraphicObject(jdImageLocated, doors[3], rect, null, false);
			}
			else {
				door3 = new JDGraphicObject(new JDImageLocated(
						ImageManager.door_west_lock, roomOffsetX, roomOffsetY
						- getDoorDimension(true, roomSize).getWidth(),
						roomSize, roomSize), doors[3], rect, null, false);
			}
			roomDoors.add(door3);
		}
		else {
			JDGraphicObject door0 = new JDGraphicObject(new JDImageLocated(
					ImageManager.door_west_none, roomOffsetX, roomOffsetY
					- getDoorDimension(true, roomSize).getWidth(),
					roomSize, roomSize), doors[3], null, null, false);
			roomDoors.add(door0);
		}

		return roomDoors;
	}

	private DrawingRectangle getNorthDoorRect() {

		JDDimension d = getDoorDimension(false, roomSize);
		return new RelativeRectangle(new JDPoint((roomSize / 2)
				- (d.getWidth() / 2), 0), d.getWidth(), d.getHeight());
	}

	private DrawingRectangle getEastDoorRect() {

		JDDimension d = getDoorDimension(true, roomSize);
		return new RelativeRectangle(new JDPoint((roomSize) - (d.getWidth()), (roomSize / 2) - (d.getHeight() / 2)), d.getWidth(),
				d.getHeight());
	}

	private static DrawingRectangle getSouthDoorRect(int roomSize) {

		JDDimension d = getDoorDimension(false, roomSize);
		return new RelativeRectangle((roomSize / 2) - (d.getWidth() / 2), roomSize - (d.getHeight()),
				d.getWidth(), d.getHeight());
	}

	private DrawingRectangle getWestDoorRect() {

		JDDimension d = getDoorDimension(true, roomSize);
		return new RelativeRectangle(0, (roomSize / 2)
				- (d.getHeight() / 2), d.getWidth(), d.getHeight());
	}

	private static JDDimension getDoorDimension(boolean vertical, int roomSize) {
		int width;
		int heigth;
		if (vertical) {
			width = roomSize / 15;
			heigth = roomSize / 4;
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

	public static JDDimension getFigureInfoSize(FigureInfo figureInfo, int roomSize) {
		if (figureInfo instanceof HeroInfo) {
			return new JDDimension((int) (((double) roomSize) / HERO_SIZE_QUOTIENT_X), (int) (((double) roomSize) / HERO_SIZE_QUOTIENT_Y));

		}
		if (figureInfo instanceof MonsterInfo) {
			return getMonsterSize((MonsterInfo) figureInfo, roomSize);
		}
		return null;
	}

	private static GraphicObject drawAMonster(MonsterInfo m, JDPoint relativeCoordinates, int roomSize) {

		JDDimension figureInfoSize = getFigureInfoSize(m, roomSize);
		int sizeX = figureInfoSize.getWidth();
		int sizeY = figureInfoSize.getHeight();
		RelativeRectangle rect = new RelativeRectangle(new JDPoint(relativeCoordinates.getX() - (sizeX / 2),
				relativeCoordinates.getY() - (sizeY / 2)), sizeX, sizeY);
		JDImageLocated ob = new JDImageLocated(ImageManager.getImage(m, m.getLookDirection()), rect);

		if (m.isDead() != null && m.isDead()) {
			DefaultAnimationSet set = AnimationUtils.getFigure_tipping_over(m);
			if (set != null && set.getLength() > 0) {
				JDImageProxy<?> i = set.getImagesNr(set.getLength() - 1);
				ob = new JDImageLocated(i, rect);
			}
		}

		int fifthRoomSize = roomSize / 5;
		return new JDGraphicObject(ob, m, rect, JDColor.WHITE, new RelativeRectangle(
				relativeCoordinates.getX() - fifthRoomSize / 2, relativeCoordinates.getY() - fifthRoomSize / 2, fifthRoomSize,
				fifthRoomSize));
	}

	public static JDDimension getMonsterSize(MonsterInfo m, double roomSize) {
		Class<? extends Monster> mClass = m.getMonsterClass();
		int roomSizeBy2Point5 = (int) (roomSize / 2.5);
		if (mClass == Wolf.class) {
			return new JDDimension((int) roomSizeBy2Point5,
					(int) roomSizeBy2Point5);
		}
		if (mClass == Orc.class) {
			return new JDDimension((int) roomSizeBy2Point5,
					(int) roomSizeBy2Point5);
		}
		if (mClass == Skeleton.class) {
			return new JDDimension((int) roomSizeBy2Point5,
					(int) roomSizeBy2Point5);
		}
		int roomSizeBy2 = (int) (roomSize / 2);
		if (mClass == Ghul.class) {
			return new JDDimension(roomSizeBy2, roomSizeBy2);
		}
		if (mClass == Ogre.class) {
			return new JDDimension(roomSizeBy2, roomSizeBy2);
		}
		if (mClass == Spider.class) {
			return new JDDimension((int) (roomSize / 2.2),
					(int) (roomSize / 2.2));
		}

		return new JDDimension((int) roomSizeBy2Point5, (int) roomSizeBy2Point5);

	}

	private GraphicObject[] drawMonster(int roomOffsetX, int roomOffsetY,
										List<MonsterInfo> monsterList) {
		if (monsterList == null) {
			return new GraphicObject[] {};
		}
		int k = monsterList.size();
		GraphicObject obs[] = new GraphicObject[k];
		if (monsterList.size() > 8) {
			obs = new GraphicObject[8];
		}
		for (int i = 0; i < monsterList.size(); i++) {

			MonsterInfo m = (monsterList.get(i));
			int position = m.getPositionInRoomIndex();

			GraphicObject gr = drawAMonster(m, new JDPoint(
					getPositionCoordModified(position).getX(),
					getPositionCoordModified(position).getY()), roomSize);
			if (i >= 8) {
				break;
			}
			obs[i] = gr;
		}

		return obs;
	}

	public JDPoint getPositionCoordModified(int index) {
		if (index >= 0 && index < 8) {
			int posX = positionCoord[index].getX();
			int posY = positionCoord[index].getY();
			return new JDPoint(posX + ROOMSIZE_BY_20, posY);
		}
		return null;
	}

	public JDPoint getPositionCoord(int index) {
		if (index >= 0 && index < 8) {
			return positionCoord[index];
		}
		return null;
	}

	private JDGraphicObject getShrineGraphicObject(ShrineInfo s, int roomOffsetX,
												   int roomOffsetY) {
		// TODO: refactor this
		JDGraphicObject ob = null;
		if (s.getShrineIndex() == Shrine.SHRINE_HEALTH_FOUNTAIN) {
			int xpos = roomOffsetX + (2 * ROOMSIZE_BY_3);
			int ypos = roomOffsetY + (1 * ROOMSIZE_BY_16);
			int xsize = ROOMSIZE_BY_3;
			int ysize = (int) (roomSize / 3.5);
			ob = new JDGraphicObject(new JDImageLocated(
					ImageManager.getImage(s), xpos, ypos, xsize, ysize), s,
					getShrineRect(roomOffsetX, roomOffsetY), JDColor.YELLOW);
		}
		else if (s.getShrineIndex() == Shrine.SHRINE_REPAIR) {
			int xpos = roomOffsetX + (2 * ROOMSIZE_BY_3);
			int ypos = roomOffsetY + (1 * ROOMSIZE_BY_16);
			int xsize = ROOMSIZE_BY_3;
			int ysize = (int) (roomSize / 3.5);
			ob = new JDGraphicObject(new JDImageLocated(
					ImageManager.getImage(s), xpos, ypos, xsize, ysize), s,
					getShrineRect(roomOffsetX, roomOffsetY), JDColor.YELLOW);
		}
		else if (s.getShrineIndex() == Shrine.SHRINE_STATUE) {
			int xpos = roomOffsetX + (16 * ROOMSIZE_BY_24);
			int ypos = roomOffsetY + (0 * ROOMSIZE_BY_36);
			int xsize = ROOMSIZE_BY_3;
			int ysize = (int) (roomSize / 2.5);
			ob = new JDGraphicObject(new JDImageLocated(
					ImageManager.getImage(s), xpos, ypos, xsize, ysize), s,
					getShrineRect(roomOffsetX, roomOffsetY), JDColor.YELLOW);
		}
		else if (s.getShrineIndex() == Shrine.SHRINE_ANGEL) {
			int xpos = roomOffsetX + (16 * ROOMSIZE_BY_24);
			int ypos = roomOffsetY - (1 * ROOMSIZE_BY_36);
			int xsize = ROOMSIZE_BY_3;
			int ysize = (int) (roomSize / 2.5);
			if (s.getType() == Angel.SOLVED) {
				xsize = 0;
				ysize = 0;
			}
			ob = new JDGraphicObject(new JDImageLocated(
					ImageManager.getImage(s), xpos, ypos, xsize, ysize), s,
					getShrineRect(roomOffsetX, roomOffsetY), JDColor.YELLOW);
		}
		else if (s.getShrineIndex() == Shrine.SHRINE_SORCER_LAB) {
			int xpos = roomOffsetX + (25 * roomSize / 60);
			int ypos = roomOffsetY - (1 * ROOMSIZE_BY_12);
			int xsize = (int) (roomSize / 1.65);
			int ysize = (int) (roomSize / 1.65);
			ob = new JDGraphicObject(new JDImageLocated(
					ImageManager.getImage(s), xpos, ypos, xsize, ysize), s,
					getShrineRect(roomOffsetX, roomOffsetY), JDColor.YELLOW);
		}
		else if (s.getShrineIndex() == Shrine.SHRINE_EXIT) {
			int xpos = roomOffsetX + (2 * ROOMSIZE_BY_3);
			int ypos = roomOffsetY + (1 * ROOMSIZE_BY_6);
			int xsize = roomSize / 4;
			int ysize = (ROOMSIZE_BY_6);
			ob = new JDGraphicObject(new JDImageLocated(
					ImageManager.getImage(s), xpos, ypos, xsize, ysize), s,
					getShrineRect(roomOffsetX, roomOffsetY), JDColor.YELLOW);
		}
		else if (s.getShrineIndex() == Shrine.SHRINE_BROOD) {
			if ((s).getType() == Brood.BROOD_NATURE) {
				int xpos = roomOffsetX + (7 * roomSize / 18);
				int ypos = roomOffsetY + (1 * ROOMSIZE_BY_12);
				int xsize = (int) (roomSize / 1.5);
				int ysize = (ROOMSIZE_BY_2);
				ob = new JDGraphicObject(new JDImageLocated(
						ImageManager.getImage(s), xpos, ypos, xsize, ysize), s,
						getShrineRect(roomOffsetX, roomOffsetY), JDColor.YELLOW);
			}
			else if ((s).getType() == Brood.BROOD_CREATURE) {
				int xpos = roomOffsetX + (2 * ROOMSIZE_BY_3);
				int ypos = roomOffsetY + (1 * ROOMSIZE_BY_6);
				int xsize = roomSize / 4;
				int ysize = (ROOMSIZE_BY_6);
				ob = new JDGraphicObject(new JDImageLocated(
						ImageManager.getImage(s), xpos, ypos, xsize, ysize), s,
						getShrineRect(roomOffsetX, roomOffsetY), JDColor.YELLOW);
			}
			else if ((s).getType() == Brood.BROOD_UNDEAD) {
				int xpos = roomOffsetX + (3 * ROOMSIZE_BY_5);
				int ypos = roomOffsetY + (1 * ROOMSIZE_BY_16);
				int xsize = (int) (roomSize / 2.9);
				int ysize = (int) (roomSize / 2.2);
				ob = new JDGraphicObject(new JDImageLocated(
						ImageManager.getImage(s), xpos, ypos, xsize, ysize), s,
						getShrineRect(roomOffsetX, roomOffsetY), JDColor.YELLOW);
			}
		}
		else if (s.getShrineIndex() == Shrine.SHRINE_TRADER) {
			int xpos = roomOffsetX + (19 * roomSize / 30);
			int ypos = roomOffsetY + (1 * roomSize / 24);
			int xsize = (int) (roomSize / 2.8);
			int ysize = (int) (roomSize / 2.0);
			ob = new JDGraphicObject(new JDImageLocated(
					ImageManager.getImage(s), xpos, ypos, xsize, ysize), s,
					getShrineRect(roomOffsetX, roomOffsetY), JDColor.YELLOW);

		}
		else if (s.getShrineIndex() == Shrine.SHRINE_RUNE) {
			int xpos = roomOffsetX + (13 * roomSize / 20);
			int ypos = roomOffsetY + (1 * roomSize / 36);
			int xsize = (int) (roomSize / 2.9);
			int ysize = (int) (roomSize / 2.2);
			JDImageProxy<?> im = ImageManager.getImage(s);

			ob = new JDGraphicObject(new JDImageLocated(im, xpos, ypos, xsize,
					ysize), s, getShrineRect(roomOffsetX, roomOffsetY), JDColor.YELLOW);
		}
		else if (s.getShrineIndex() == Shrine.SHRINE_CORPSE) {
			int xpos = roomOffsetX + (13 * roomSize / 20);
			int ypos = roomOffsetY + (6 * roomSize / 36);
			int xsize = (int) (roomSize / 3.2);
			int ysize = (int) (roomSize / 3.8);
			JDImageProxy<?> im = ImageManager.getImage(s);
			ob = new JDGraphicObject(new JDImageLocated(im, xpos, ypos, xsize,
					ysize), s, getShrineRect(roomOffsetX, roomOffsetY), JDColor.YELLOW);
		}
		else if (s.getShrineIndex() == Shrine.SHRINE_QUEST) {
			int xpos = roomOffsetX + (13 * roomSize / 20);
			int ypos = roomOffsetY + (1 * roomSize / 36);
			int xsize = (int) (roomSize / 2.9);
			int ysize = (int) (roomSize / 2.2);
			JDImageProxy<?> im = ImageManager.getImage(s);

			ob = new JDGraphicObject(new JDImageLocated(im, xpos, ypos, xsize,
					ysize), s, getShrineRect(roomOffsetX, roomOffsetY), JDColor.YELLOW);
		}
		else if (s.getShrineIndex() == Shrine.SHRINE_XMAS) {
			int xpos = roomOffsetX + (27 * roomSize / 60);
			int ypos = roomOffsetY - (1 * ROOMSIZE_BY_10);
			int xsize = (int) (roomSize / 1.7);
			int ysize = (int) (roomSize / 1.7);
			JDImageProxy<?> im = ImageManager.getImage(s);

			ob = new JDGraphicObject(new JDImageLocated(im, xpos, ypos, xsize,
					ysize), s, getShrineRect(roomOffsetX, roomOffsetY), JDColor.YELLOW);

		}
		else if (s.getShrineIndex() == Shrine.SHRINE_RUNEFINDER) {
			int xpos = roomOffsetX + (7 * ROOMSIZE_BY_10);
			int ypos = roomOffsetY + (1 * roomSize / 24);
			int xsize = (int) (roomSize / 3.4);
			int ysize = (int) (roomSize / 2.7);
			JDImageProxy<?> im = ImageManager.getImage(s);
			ob = new JDGraphicObject(new JDImageLocated(im, xpos, ypos, xsize,
					ysize), s, getShrineRect(roomOffsetX, roomOffsetY), JDColor.YELLOW);

		}
		else if (s.getShrineIndex() == Shrine.SHRINE_REVEALMAP) {
			int xpos = roomOffsetX + (7 * ROOMSIZE_BY_10);
			int ypos = roomOffsetY + (1 * roomSize / 24);
			int xsize = (int) (roomSize / 3.4);
			int ysize = (int) (roomSize / 2.7);
			JDImageProxy<?> im = ImageManager.getImage(s);
			ob = new JDGraphicObject(new JDImageLocated(im, xpos, ypos, xsize,
					ysize), s, getShrineRect(roomOffsetX, roomOffsetY), JDColor.YELLOW);
		}
		else if (s.getShrineIndex() == Shrine.SHRINE_DARK_MASTER) {
			int xpos = roomOffsetX + (7 * ROOMSIZE_BY_10);
			int ypos = roomOffsetY + (3 * ROOMSIZE_BY_16);
			int xsize = (int) (roomSize / 3.6);
			int ysize = (int) (roomSize / 3.7);
			JDImageProxy<?> im = ImageManager.getImage(s);
			ob = new JDGraphicObject(new JDImageLocated(im, xpos, ypos, xsize,
					ysize), s, getShrineRect(roomOffsetX, roomOffsetY), JDColor.YELLOW);
		}
		else if (s.getShrineIndex() == Shrine.SHRINE_LUZIA) {
			int xpos = roomOffsetX + (7 * ROOMSIZE_BY_10);
			int ypos = roomOffsetY + (0 * ROOMSIZE_BY_36);
			int xsize = (int) (roomSize / 3.6);
			int ysize = (int) (roomSize / 2.5);

			JDImageProxy<?> im = ImageManager.getImage(s);
			if (s.getType() == Luzia.SOLVED || s.getType() == Luzia.DEAD) {
				xpos = roomOffsetX + (8 * ROOMSIZE_BY_10);
				ypos = roomOffsetY + (5 * ROOMSIZE_BY_36);
				xsize = (int) (roomSize / 4.5);
				ysize = (int) (roomSize / 3.5);
			}

			ob = new JDGraphicObject(new JDImageLocated(im, xpos, ypos, xsize,
					ysize), s, getShrineRect(roomOffsetX, roomOffsetY), JDColor.YELLOW);
		}
		return ob;

	}

	private RelativeRectangle getShrineRect(int roomOffsetX, int roomOffsetY) {
		int xpos = (13 * roomSize / 20);
		int ypos = (roomSize / 36);
		int xsize = (int) (roomSize / 2.9);
		int ysize = (int) (roomSize / 2.2);
		return new RelativeRectangle(new JDPoint(xpos, ypos), xsize, ysize);
	}

	private JDGraphicObject getHeroGraphicObject(int roomOffsetX, int roomOffsetY, HeroInfo info,
												 GraphicObjectRenderer renderer) {
		int positionInRoomIndex = info
				.getPositionInRoomIndex();
		if (positionInRoomIndex == -1) return null;
		JDPoint p = renderer.getPositionCoordModified(positionInRoomIndex);
		int xpos = p.getX();
		int ypos = p.getY();
		JDDimension figureInfoSize = getFigureInfoSize(info, roomSize);
		int xHeroSize = figureInfoSize.getWidth();
		int yHeroSize = figureInfoSize.getHeight();

		RelativeRectangle rect = new RelativeRectangle(xpos - (xHeroSize / 2), ypos
				- (yHeroSize / 2), xHeroSize, yHeroSize);

		JDImageLocated im = getImage(info, rect);
		return new JDGraphicObject(im, info, rect, JDColor.WHITE,
				getHalfSizeRect(rect));
	}

	private JDImageLocated getImage(HeroInfo info, RelativeRectangle rect) {
		int code = info.getHeroCode();
		RouteInstruction.Direction direction = info.getLookDirection();
		JDImageLocated im = null;
		JDImageProxy imageProxy = null;
		if (info.isDead()) {
			DefaultAnimationSet animationSet = ImageManager.getAnimationSet(info.getHeroCategory(), Motion.TippingOver, RouteInstruction.Direction
					.fromInteger(info.getLookDirection().getValue()));
			// we take the last image from the tipping over animation
			if (animationSet == null) {
				Log.warning("No image found for dead " + info.toString());
				return null;
			}
			imageProxy = animationSet.getImagesNr(animationSet.getLength() - 1);

		}
		else {
			Motion motion = null;
			Boolean fightRunning = info.getRoomInfo().fightRunning();
			if (fightRunning != null && fightRunning) {
				motion = Motion.Slaying;
			}
			else {
				motion = Motion.Walking;
			}
			DefaultAnimationSet animationSet = ImageManager.getAnimationSet(info.getHeroCategory(), motion, RouteInstruction.Direction
					.fromInteger(info.getLookDirection().getValue()));
			if (animationSet == null) {
				Log.warning("No image found for motion " + motion + " (info.toString()+");
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

	private GraphicObject drawBackGround(int roomOffsetX, int roomOffsetY, RoomInfo roomInfo,
										 GraphicObjectRenderer renderer) {

		int status = roomInfo.getVisibilityStatus();
		int floorType = roomInfo.getFloorIndex();
		JDImageProxy<?> im = null; //ImageManager.floorImageArray[floorType];
		if (status < RoomObservationStatus.VISIBILITY_FOUND) {
			// not found, just draw dark background
			return new GraphicObject(roomInfo, new RelativeRectangle(new JDPoint(
					getDoorDimension(true, roomSize).getWidth(),
					getDoorDimension(true, roomSize).getWidth()),
					new JDDimension(roomSize - 2
							* getDoorDimension(true, roomSize).getWidth(),
							roomSize - 2
									* getDoorDimension(true, roomSize)
									.getWidth())), JDColor.DARK_GRAY, false, null);
		}
		else if (status == RoomObservationStatus.VISIBILITY_FOUND || status == RoomObservationStatus.VISIBILITY_SHRINE) {
			im = ImageManager.floorImage_mediumArray[floorType];
		}
		else if (status > RoomObservationStatus.VISIBILITY_SHRINE) {
			im = ImageManager.floorImageArray[floorType];
		}
		return new GraphicObject(roomInfo, new RelativeRectangle(
				new JDPoint(0, 0), new JDDimension(roomSize, roomSize
				- GraphicObjectRenderer
				.getDoorDimension(true, roomSize).getWidth())),
				null, false, im);
	}

	private GraphicObject drawWallSouth(int xcoord, int ycoord, RoomInfo r) {
		GraphicObject ob = null;
		if (r.getVisibilityStatus() >= RoomObservationStatus.VISIBILITY_FOUND) {
			JDDimension doorDimension = GraphicObjectRenderer.getDoorDimension(true,
					roomSize);
			ob = new GraphicObject(null,
					new RelativeRectangle(new JDPoint(0, roomSize - doorDimension.getWidth()),
							new JDDimension(roomSize, roomSize)), null, ImageManager.wall_southImage);
		}
		return ob;
	}

	private Collection<GraphicObject> drawWall(int roomOffsetX, int roomOffsetY, RoomInfo roomInfo) {
		JDColor bg = null;

		Collection<GraphicObject> result = new ArrayList<>();
		int status = roomInfo.getVisibilityStatus();
		if (status >= RoomObservationStatus.VISIBILITY_FOUND) {

			result.add(new GraphicObject(roomInfo, new RelativeRectangle(new JDPoint(
					0, -getDoorDimension(true, roomSize).getWidth()),
					new JDDimension(roomSize, roomSize)), bg,
					ImageManager.wall_sidesImage));

			result.add(new GraphicObject(roomInfo, new RelativeRectangle(new JDPoint(0,
					-getDoorDimension(true, roomSize).getWidth()),
					new JDDimension(roomSize, roomSize)), bg,
					ImageManager.wall_northImage));

		}

		return result;
	}

	private JDDimension getSpotDimension() {
		return new JDDimension(roomSize / 7, roomSize / 7);
	}

	private JDDimension getChestDimension() {
		return new JDDimension(roomSize / 5, roomSize / 5);
	}

	private JDPoint getChestPoint(int xcoord, int ycoord) {
		int x1 = xcoord + (roomSize / 8);
		int y1 = ycoord + roomSize / 10;

		return new JDPoint(x1, y1);
	}

	private JDPoint getChestPointRelative() {
		return getChestPoint(0, 0);
	}

	public List<GraphicObject> createGraphicObjectsForRoom(RoomInfo r,
														   Object obj, int roomOffsetX, int roomOffsetY, List<?> animatedObs) {
		List<GraphicObject> graphObs = new LinkedList<GraphicObject>();
		if (r == null) {
			return graphObs;
		}

		/*
		 * room
		 */
		GraphicObject roomOb = drawBackGround(roomOffsetX, roomOffsetY, r, this);
		if(roomOb != null) {
			graphObs.add(roomOb);
		}

		/*
		 * wall
		 */
		Collection<GraphicObject> wallOb = drawWall(roomOffsetX, roomOffsetY, r);
		if (wallOb != null) {
			graphObs.addAll(wallOb);
		}

		/*
		 * wall south
		 */
		GraphicObject wallObSouth = drawWallSouth(roomOffsetX, roomOffsetY, r);
		if (wallObSouth != null) {
			graphObs.add(wallObSouth);
		}

		/*
		 * doors
		 */
		graphObs.addAll(drawDoors(r, roomOffsetX, roomOffsetY));

		int status = r.getVisibilityStatus();

		if ((status >= RoomObservationStatus.VISIBILITY_SHRINE)) {

			// draw room position markers
			if (r.equals(gui.getFigure().getRoomInfo())) {
				JDPoint[] positionCoord = getPositionCoord();
				for (int i = 0; i < positionCoord.length; i++) {
					int posSize = ROOMSIZE_BY_8;
					GraphicObject ob = new GraphicObject(
							r.getPositionInRoom(i), new RelativeRectangle(getPositionCoord(i).getX(), getPositionCoord(i)
							.getY(), posSize,
							posSize), JDColor.BLACK,
							ImageManager.fieldImage);

					graphObs.add(ob);

				}
			}

			graphObs.add(new GraphicObject(r, new RelativeRectangle(
					new JDPoint(0, -getDoorDimension(true, roomSize).getWidth()),
					new JDDimension(roomSize, roomSize)), JDColor.DARK_GRAY,
					ImageManager.wall_sidesImage));

			GraphicObject ob = null;
			if (r.getShrine() != null) {
				ShrineInfo s = r.getShrine();
				ob = getShrineGraphicObject(s, roomOffsetX, roomOffsetY);
				graphObs.add(ob);
			}

			if (r.getChest() != null) {
				GraphicObject chestOb;
				if (r.getChest().hasLock()) {
					chestOb = new GraphicObject(r.getChest(),
							new RelativeRectangle(getChestPointRelative(),
									getChestDimension()), new JDColor(140, 90,
							20), ImageManager.chest_lockImage);
				}
				else {
					chestOb = new GraphicObject(r.getChest(),
							new RelativeRectangle(getChestPointRelative(),
									getChestDimension()), new JDColor(140, 90,
							20), ImageManager.chestImage);
				}
				graphObs.add(chestOb);
			}
		}
		if ((status >= RoomObservationStatus.VISIBILITY_FIGURES)) {
			if (r.getMonsterInfos() != null && !r.getMonsterInfos().isEmpty()) {
				GraphicObject[] monsterObs = drawMonster(roomOffsetX, roomOffsetY,
						r.getMonsterInfos());
				for (int i = 0; i < monsterObs.length; i++) {
					if (monsterObs[i] != null) {
						boolean contains = animatedObs.contains(monsterObs[i]
								.getClickableObject());
						if (!contains) {
							if (!(monsterObs[i].getClickableObject().equals(obj))) {
								graphObs.add(monsterObs[i]);
							}
						}
					}
				}
			}
		}
		if ((status >= RoomObservationStatus.VISIBILITY_ITEMS)) {

			GraphicObject[] itObs = drawItems(r.getItemArray(),
					roomSize);
			for (int i = 0; i < itObs.length; i++) {
				GraphicObject o = itObs[i];
				if (o != null) {
					graphObs.add(o);
				}
			}

		}

		if ((r.getSpot() != null) && (r.getSpot().isFound())) {
			GraphicObject spotOb = new GraphicObject(r.getSpot(),
					new RelativeRectangle(getSpotPointRelative(),
							getSpotDimension()), new JDColor(0, 0, 0),
					ImageManager.spotImage);
			graphObs.add(spotOb);
		}

		List<HeroInfo> heroInfos = r.getHeroInfos();
		if (!heroInfos.isEmpty()) {
			for (HeroInfo heroInfo : heroInfos) {
				if (!animatedObs.contains(heroInfo)) {
					GraphicObject heroObject = drawHero(roomOffsetX, roomOffsetY,
							heroInfo, this);
					if (heroObject != null) {
						graphObs.add(heroObject);
					}
				}
			}
		}
		return graphObs;
	}

	private JDPoint getSpotPointRelative() {
		return getSpotPoint(0, 0);
	}

	private JDPoint getSpotPoint(int xcoord, int ycoord) {
		int x1 = xcoord + (roomSize / 8);
		int y1 = ycoord + (6 * roomSize / 8);

		return new JDPoint(x1, y1);
	}

	private GraphicObject drawHero(int roomOffsetX, int roomOffsetY, HeroInfo info,
								   GraphicObjectRenderer renderer) {
		if (info == null) {
			return null;
		}
		return renderer.getHeroGraphicObject(roomOffsetX, roomOffsetY, info, renderer);
	}

}
