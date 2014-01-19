package graphics;

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

import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import shrine.Angel;
import shrine.Brood;
import shrine.Luzia;
import shrine.Shrine;
import shrine.ShrineInfo;
import util.JDColor;
import util.JDDimension;
import animation.AnimationSet;
import animation.AnimationUtils;
import dungeon.Dir;
import dungeon.DoorInfo;
import dungeon.JDPoint;
import dungeon.RoomInfo;
import figure.FigureInfo;
import figure.RoomObservationStatus;
import figure.hero.Hero;
import figure.hero.HeroInfo;
import figure.monster.Monster;
import figure.monster.MonsterInfo;
import graphics.util.JDRectangle;
import graphics.util.RoomSize;

public class GraphicObjectRenderer {

	private final boolean memory = false;
	private final boolean visCheat = false;

	private final int roomSize;

	private final int ROOMSIZE_BY_36;
	private final int ROOMSIZE_BY_24;
	private final int ROOMSIZE_BY_20;
	private final int ROOMSIZE_BY_12;
	private final int ROOMSIZE_BY_10;
	private final int ROOMSIZE_BY_16;
	private final int ROOMSIZE_BY_8;
	private final int ROOMSIZE_BY_6;
	private final int ROOMSIZE_BY_5;
	private final int ROOMSIZE_BY_3;
	private final int ROOMSIZE_BY_2;
	private final JDPoint[] positionCoord = new JDPoint[8];

	public static final double HERO_POINT_QUOTIENT_X = 2.2;

	public static final double HERO_POINT_QUOTIENT_Y = 2.2;

	public static final int HERO_POINT_OFFSET_Y = 0;

	public static final int HERO_POINT_OFFSET_X = 15;

	public static final double HERO_SIZE_QUOTIENT_X = 2;

	public static final double HERO_SIZE_QUOTIENT_Y = 2;

	public Vector<GraphicObject> rooms = new Vector<GraphicObject>();

	public Vector<GraphicObject> shrines = new Vector<GraphicObject>();

	public Vector<GraphicObject> positions = new Vector<GraphicObject>();

	public Vector<GraphicObject> monster = new Vector<GraphicObject>();

	public Vector<GraphicObject> items = new Vector<GraphicObject>();

	public Vector<GraphicObject> doors = new Vector<GraphicObject>();

	public Vector<GraphicObject> chests = new Vector<GraphicObject>();

	public Vector<GraphicObject> spots = new Vector<GraphicObject>();

	public Vector<GraphicObject> walls = new Vector<GraphicObject>();

	public Vector<GraphicObject> lastWalls = new Vector<GraphicObject>();

	public GraphicObject hero;

	private final FigureInfo figure;

	private JDPoint[] getPositionCoord() {
		return positionCoord;
	}

	public GraphicObjectRenderer(int roomSize, FigureInfo info) {
		this.figure = info;
		this.roomSize = roomSize;
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

		positionCoord[0] = new JDPoint(roomSize / 4 - ROOMSIZE_BY_16, roomSize
				/ 3 - ROOMSIZE_BY_36);
		positionCoord[1] = new JDPoint(roomSize / 2 - ROOMSIZE_BY_16,
				(int) (roomSize / 3.5) - ROOMSIZE_BY_36);
		positionCoord[2] = new JDPoint(roomSize * 3 / 4 - ROOMSIZE_BY_16,
				roomSize / 3 - ROOMSIZE_BY_36);
		positionCoord[3] = new JDPoint(roomSize * 4 / 5 - ROOMSIZE_BY_16,
				(int) (roomSize / 1.8) - ROOMSIZE_BY_36);
		positionCoord[4] = new JDPoint(roomSize * 3 / 4 - ROOMSIZE_BY_16,
				roomSize * 3 / 4 - ROOMSIZE_BY_36);
		positionCoord[5] = new JDPoint(roomSize / 2 - ROOMSIZE_BY_16, roomSize
				* 4 / 5 - ROOMSIZE_BY_36);
		positionCoord[6] = new JDPoint(roomSize / 4 - ROOMSIZE_BY_16, roomSize
				* 3 / 4 - ROOMSIZE_BY_36);
		positionCoord[7] = new JDPoint(roomSize / 5 - ROOMSIZE_BY_16,
				(int) (roomSize / 1.8) - ROOMSIZE_BY_36);

	}

	public void clear() {
		rooms.clear();
		shrines.clear();
		monster.clear();
		items.clear();
		doors.clear();
		chests.clear();
		spots.clear();
		walls.clear();
		lastWalls.clear();
		positions.clear();
		hero = null;

	}

	private GraphicObject[] drawItems(int xcoord, int ycoord,
			ItemInfo[] itemArray, int roomSize) {
		if (itemArray == null) {
			return new GraphicObject[0];
		}
		JDPoint p2[] = getItemPoints(xcoord, ycoord, roomSize);

		GraphicObject[] itemObs = new GraphicObject[4];

		// Schleife bis 4
		int i = 0;
		while (i < itemArray.length && i < 4) {
			JDPoint q = p2[i];

			if (itemArray[i] != null) {

				if (AttrPotion.class.isAssignableFrom(itemArray[i]
						.getItemClass())) {

					if (((itemArray[i]).getItemKey() == Item.ITEM_KEY_HEALPOTION)) {
						itemObs[i] = new GraphicObject(itemArray[i],
								new JDRectangle(q, new JDDimension(
										12 * (roomSize / 100),
										15 * (roomSize / 100))),
								JDColor.YELLOW, ImageManager.potion_redImage);
					} else {
						itemObs[i] = new GraphicObject(itemArray[i],
								new JDRectangle(q, new JDDimension(
										12 * (roomSize / 100),
										15 * (roomSize / 100))),
								JDColor.YELLOW, ImageManager.potion_blueImage);
					}

				} else if (itemArray[i].getItemClass().equals(DustItem.class)) {
					itemObs[i] = new GraphicObject(itemArray[i],
							new JDRectangle(q,
									new JDDimension(10 * (roomSize / 100),
											8 * (roomSize / 100))),
							JDColor.YELLOW, ImageManager.dustImage);
				} else if (itemArray[i].getItemClass().equals(Sword.class)) {
					itemObs[i] = new GraphicObject(itemArray[i],
							new JDRectangle(q, new JDDimension(
									30 * (roomSize / 100),
									20 * (roomSize / 100))), JDColor.YELLOW,
							ImageManager.swordImage);
				} else if (itemArray[i].getItemClass().equals(Axe.class)) {
					itemObs[i] = new GraphicObject(itemArray[i],
							new JDRectangle(q, new JDDimension(
									30 * (roomSize / 100),
									20 * (roomSize / 100))), JDColor.YELLOW,
							ImageManager.axeImage);
				} else if (itemArray[i].getItemClass().equals(Club.class)) {
					itemObs[i] = new GraphicObject(itemArray[i],
							new JDRectangle(q, new JDDimension(
									30 * (roomSize / 100),
									20 * (roomSize / 100))), JDColor.YELLOW,
							ImageManager.clubImage);
				} else if (itemArray[i].getItemClass().equals(Lance.class)) {
					itemObs[i] = new GraphicObject(itemArray[i],
							new JDRectangle(q, new JDDimension(
									50 * (roomSize / 100),
									40 * (roomSize / 100))), JDColor.YELLOW,
							ImageManager.lanceImage);
				} else if (itemArray[i].getItemClass().equals(Wolfknife.class)) {
					itemObs[i] = new GraphicObject(itemArray[i],
							new JDRectangle(q, new JDDimension(
									20 * (roomSize / 100),
									15 * (roomSize / 100))), JDColor.YELLOW,
							ImageManager.wolfknifeImage);
				} else if (itemArray[i].getItemClass().equals(Armor.class)) {
					int sizeX = 30 * (roomSize / 100);
					int sizeY = 20 * (roomSize / 100);

					itemObs[i] = new GraphicObject(itemArray[i],
							new JDRectangle(q, new JDDimension(sizeX, sizeY)),
							JDColor.YELLOW, ImageManager.armorImage);
				} else if (itemArray[i].getItemClass().equals(Shield.class)) {
					int sizeX = 20 * (roomSize / 100);
					int sizeY = 15 * (roomSize / 100);

					itemObs[i] = new GraphicObject(itemArray[i],
							new JDRectangle(q, new JDDimension(sizeX, sizeY)),
							JDColor.YELLOW, ImageManager.shieldImage);
				} else if (itemArray[i].getItemClass().equals(Helmet.class)) {
					int sizeX = 24 * (roomSize / 100);
					int sizeY = 22 * (roomSize / 100);

					itemObs[i] = new GraphicObject(itemArray[i],
							new JDRectangle(q, new JDDimension(sizeX, sizeY)),
							JDColor.YELLOW, ImageManager.helmetImage);
				} else if (Scroll.class.isAssignableFrom(itemArray[i]
						.getItemClass())) {
					int sizeX = 15 * (roomSize / 100);
					int sizeY = 15 * (roomSize / 100);

					itemObs[i] = new GraphicObject(itemArray[i],
							new JDRectangle(q, new JDDimension(sizeX, sizeY)),
							JDColor.YELLOW, ImageManager.scrollImage);
				} else if (itemArray[i].getItemClass().equals(InfoScroll.class)) {
					int sizeX = 15 * (roomSize / 100);
					int sizeY = 15 * (roomSize / 100);

					itemObs[i] = new GraphicObject(itemArray[i],
							new JDRectangle(q, new JDDimension(sizeX, sizeY)),
							JDColor.YELLOW, ImageManager.documentImage);
				} else if (itemArray[i].getItemClass().equals(Feather.class)) {
					int sizeX = 15 * (roomSize / 100);
					int sizeY = 15 * (roomSize / 100);

					itemObs[i] = new GraphicObject(itemArray[i],
							new JDRectangle(q, new JDDimension(sizeX, sizeY)),
							JDColor.YELLOW, ImageManager.featherImage);
				} else if (itemArray[i].getItemClass().equals(Incense.class)) {
					int sizeX = 15 * (roomSize / 100);
					int sizeY = 15 * (roomSize / 100);

					itemObs[i] = new GraphicObject(itemArray[i],
							new JDRectangle(q, new JDDimension(sizeX, sizeY)),
							JDColor.YELLOW, ImageManager.potion_greenImage);
				} else if (itemArray[i].getItemClass().equals(Key.class)) {
					int sizeX = 16 * (roomSize / 100);
					int sizeY = 22 * (roomSize / 100);

					itemObs[i] = new GraphicObject(itemArray[i],
							new JDRectangle(q, new JDDimension(sizeX, sizeY)),
							JDColor.YELLOW, ImageManager.keyImage);

				} else if (itemArray[i].getItemClass().equals(Rune.class)) {
					int sizeX = 12 * (roomSize / 100);
					int sizeY = 12 * (roomSize / 100);
					JDImageProxy<?> im = null;
					if ((itemArray[i]).toString().indexOf('J') != -1) {
						im = ImageManager.rune_yellowImage;
					} else if ((itemArray[i]).toString().indexOf('A') != -1) {
						im = ImageManager.rune_greenImage;
					} else if ((itemArray[i]).toString().indexOf('V') != -1) {
						im = ImageManager.rune_redImage;
					}

					itemObs[i] = new GraphicObject(itemArray[i],
							new JDRectangle(q, new JDDimension(sizeX, sizeY)),
							JDColor.YELLOW, im);
				} else if (itemArray[i].getItemClass().equals(
						DarkMasterKey.class)) {
					int sizeX = 12 * (roomSize / 100);
					int sizeY = 12 * (roomSize / 100);
					JDImageProxy<?> im = ImageManager.cristall_redImage;

					itemObs[i] = new GraphicObject(itemArray[i],
							new JDRectangle(q, new JDDimension(sizeX, sizeY)),
							JDColor.YELLOW, im);
				} else if (itemArray[i].getClass().equals(LuziasBall.class)) {
					int sizeX = 15 * (roomSize / 100);
					int sizeY = 15 * (roomSize / 100);
					JDImageProxy<?> im = ImageManager.kugelImage;

					itemObs[i] = new GraphicObject(itemArray[i],
							new JDRectangle(q, new JDDimension(sizeX, sizeY)),
							JDColor.YELLOW, im);
				} else if (itemArray[i].getClass().equals(Book.class)) {
					int sizeX = 15 * (roomSize / 100);
					int sizeY = 15 * (roomSize / 100);
					JDImageProxy<?> im = ImageManager.bookImage;

					itemObs[i] = new GraphicObject(itemArray[i],
							new JDRectangle(q, new JDDimension(sizeX, sizeY)),
							JDColor.YELLOW, im);

				} else if (Thing.class.isAssignableFrom(itemArray[i]
						.getItemClass())) {
					int sizeX = 12 * (roomSize / 100);
					int sizeY = 12 * (roomSize / 100);
					JDImageProxy<?> im = null;

					im = ImageManager.amulettImage;

					itemObs[i] = new GraphicObject(itemArray[i],
							new JDRectangle(q, new JDDimension(sizeX, sizeY)),
							JDColor.YELLOW, im);
				} else {

					itemObs[i] = new GraphicObject(itemArray[i],
							new JDRectangle(q, new JDDimension(8, 5)),
							JDColor.YELLOW, null);
				}
			} else {
				itemObs[i] = null;
			}
			i++;
		}
		return itemObs;
	}

	private List<GraphicObject> drawDoors(RoomInfo r, int xcoord, int ycoord) {
		DoorInfo[] doors = r.getDoors();
		if (doors == null) {
			return new LinkedList<GraphicObject>();
		}
		List<GraphicObject> roomDoors = new LinkedList<GraphicObject>();
		if (doors[0] != null) {
			JDGraphicObject door0;
			if (!doors[0].hasLock().booleanValue()) {
				JDRectangle rect = getNorthDoorRect(xcoord, ycoord);
				door0 = new JDGraphicObject(new JDImageLocated(
						ImageManager.door_north, xcoord, ycoord
								- getDoorDimension(true, roomSize).getWidth(),
						roomSize, roomSize), doors[0], rect, new JDColor(180,
						150, 80), false);
			} else {
				JDRectangle rect = getNorthDoorRect(xcoord, ycoord);
				door0 = new JDGraphicObject(new JDImageLocated(
						ImageManager.door_north_lock, xcoord, ycoord
								- getDoorDimension(true, roomSize).getWidth(),
						roomSize, roomSize), doors[0], rect, new JDColor(180,
						150, 80), false);
			}
			roomDoors.add(door0);
		} else {
			JDGraphicObject door0 = new JDGraphicObject(new JDImageLocated(
					ImageManager.door_north_none, xcoord, ycoord
							- getDoorDimension(true, roomSize).getWidth(),
					roomSize, roomSize), doors[0], null, new JDColor(180, 150,
					80), false);
			roomDoors.add(door0);
		}
		if (doors[1] != null) {
			JDRectangle rect = getEastDoorRect(xcoord, ycoord);
			JDGraphicObject door1;
			if (!doors[1].hasLock().booleanValue()) {
				door1 = new JDGraphicObject(new JDImageLocated(
						ImageManager.door_east, xcoord, ycoord
								- getDoorDimension(true, roomSize).getWidth(),
						roomSize, roomSize), doors[1], rect, new JDColor(180,
						150, 80), false);
			} else {
				door1 = new JDGraphicObject(new JDImageLocated(
						ImageManager.door_east_lock, xcoord, ycoord
								- getDoorDimension(true, roomSize).getWidth(),
						roomSize, roomSize), doors[1], rect, new JDColor(180,
						150, 80), false);
			}
			roomDoors.add(door1);
		} else {
			JDGraphicObject door1 = new JDGraphicObject(new JDImageLocated(
					ImageManager.door_east_none, xcoord, ycoord
							- getDoorDimension(true, roomSize).getWidth(),
					roomSize, roomSize), doors[1], null, new JDColor(180, 150,
					80), false);
			roomDoors.add(door1);
		}
		JDRectangle rectSouth = GraphicObjectRenderer.getSouthDoorRect(xcoord,
				ycoord, roomSize);
		if (doors[2] != null) {
			/*
			 * there is a door to south
			 */
			if (doors[2].hasLock().booleanValue()) {
				/*
				 * with lock
				 */
				roomDoors.add(new JDGraphicObject(new JDImageLocated(
						ImageManager.door_south_lock, xcoord, ycoord + roomSize
								- getDoorDimension(true, roomSize).getWidth(),
						roomSize, roomSize), doors[2], rectSouth, new JDColor(
						180, 150, 80), false));
			} else {
				/*
				 * without lock
				 */
				roomDoors.add(new JDGraphicObject(new JDImageLocated(
						ImageManager.door_south, xcoord, ycoord + roomSize
								- getDoorDimension(true, roomSize).getWidth(),
						roomSize, roomSize), doors[2], rectSouth, new JDColor(
						180, 150, 80), false));
			}

		} else {
			/*
			 * no door to south at all --> wall
			 */
			roomDoors.add(new JDGraphicObject(new JDImageLocated(
					ImageManager.door_south_none, xcoord, ycoord + roomSize
							- getDoorDimension(true, roomSize).getWidth(),
					roomSize, roomSize), doors[2], rectSouth, new JDColor(180,
					150, 80), false));
		}
		if (doors[3] != null) {
			JDRectangle rect = getWestDoorRect(xcoord, ycoord);
			JDGraphicObject door3;

			if (!doors[3].hasLock().booleanValue()) {
				door3 = new JDGraphicObject(new JDImageLocated(
						ImageManager.door_west, xcoord, ycoord
								- getDoorDimension(true, roomSize).getWidth(),
						roomSize, roomSize), doors[3], rect, new JDColor(180,
						150, 80), false);
			} else {
				door3 = new JDGraphicObject(new JDImageLocated(
						ImageManager.door_west_lock, xcoord, ycoord
								- getDoorDimension(true, roomSize).getWidth(),
						roomSize, roomSize), doors[3], rect, new JDColor(180,
						150, 80), false);
			}
			roomDoors.add(door3);
		} else {
			JDGraphicObject door0 = new JDGraphicObject(new JDImageLocated(
					ImageManager.door_west_none, xcoord, ycoord
							- getDoorDimension(true, roomSize).getWidth(),
					roomSize, roomSize), doors[3], null, new JDColor(180, 150,
					80), false);
			roomDoors.add(door0);
		}

		return roomDoors;
	}

	private JDRectangle getNorthDoorRect(int x, int y) {

		JDDimension d = getDoorDimension(false, roomSize);
		return new JDRectangle(new JDPoint(x + (roomSize / 2)
				- (d.getWidth() / 2), y), d.getWidth(), d.getHeight());
	}

	private JDRectangle getEastDoorRect(int x, int y) {

		JDDimension d = getDoorDimension(true, roomSize);
		return new JDRectangle(new JDPoint(x + (roomSize) - (d.getWidth()), y
				+ (roomSize / 2) - (d.getHeight() / 2)), d.getWidth(),
				d.getHeight());
	}

	private static JDRectangle getSouthDoorRect(int x, int y, int roomSize) {

		JDDimension d = getDoorDimension(false, roomSize);
		return new JDRectangle(new JDPoint(x + (roomSize / 2)
				- (d.getWidth() / 2), y + roomSize - (d.getHeight())),
				d.getWidth(), d.getHeight());
	}

	private JDRectangle getWestDoorRect(int x, int y) {

		JDDimension d = getDoorDimension(true, roomSize);
		return new JDRectangle(new JDPoint((x), y + (roomSize / 2)
				- (d.getHeight() / 2)), d.getWidth(), d.getHeight());
	}

	private static JDDimension getDoorDimension(boolean vertical, int roomSize) {
		int width;
		int heigth;
		if (vertical) {
			width = roomSize / 15;
			heigth = roomSize / 4;
		} else {
			width = roomSize / 4;
			heigth = roomSize / 8;
		}
		return new JDDimension(width, heigth);

	}

	private JDPoint[] getItemPoints(int xcoord, int ycoord, int roomSize) {
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

	private GraphicObject drawAMonster(MonsterInfo m, JDPoint p) {
		JDImageLocated ob = null;
		int mClass = m.getMonsterClass();
		int sizeX = (getMonsterSize(m).getWidth());
		int sizeY = (getMonsterSize(m).getHeight());
		JDRectangle rect = new JDRectangle(new JDPoint(p.getX() - (sizeX / 2),
				p.getY() - (sizeY / 2)), sizeX, sizeY);
		int dir = m.getLookDir();
		if (mClass == Monster.WOLF) {

			JDImageProxy<?> wolf = ImageManager.wolfImage[dir - 1];
			if (m.getLevel() == 2) {
				wolf = ImageManager.wolfImage[dir - 1];
			}
			ob = new JDImageLocated(wolf, rect);
		} else if (mClass == Monster.ORC) {
			JDImageProxy<?> orc = ImageManager.orcImage[dir - 1];

			if (m.getLevel() == 2) {
				orc = ImageManager.orcImage[dir - 1];
			}
			ob = new JDImageLocated(orc, rect);
		} else if (mClass == Monster.SKELETON) {
			JDImageProxy<?> skel = ImageManager.skelImage[dir - 1];

			if (m.getLevel() == 2) {
				skel = ImageManager.skelImage[dir - 1];
			}
			ob = new JDImageLocated(skel, rect);
		} else if (mClass == Monster.GHUL) {
			JDImageProxy<?> ghul = ImageManager.ghulImage[dir - 1];

			if (m.getLevel() == 2) {
				ghul = ImageManager.ghulImage[dir - 1];
			}
			ob = new JDImageLocated(ghul, rect);
		} else if (mClass == Monster.OGRE) {
			JDImageProxy<?> ogre = ImageManager.ogreImage[dir - 1];

			if (m.getLevel() == 2) {
				ogre = ImageManager.ogreImage[dir - 1];
			}
			ob = new JDImageLocated(ogre, rect);
		} else if (mClass == Monster.BEAR) {
			JDImageProxy<?> bear = ImageManager.bearImage[dir - 1];

			if (m.getLevel() == 2) {
				bear = ImageManager.bearImage[dir - 1];
			}
			ob = new JDImageLocated(bear, rect);

		} else if (mClass == Monster.DARKMASTER) {
			ob = new JDImageLocated(ImageManager.darkMasterImage, rect);

		} else if (mClass == Monster.DWARF) {
			ob = new JDImageLocated(ImageManager.dark_dwarfImage, rect);

		} else if (mClass == Monster.FIR) {
			ob = new JDImageLocated(ImageManager.finImage, rect);

		} else {
			ob = new JDImageLocated(ImageManager.engelImage, rect);
		}

		if (m.isDead() != null && m.isDead().booleanValue()) {
			AnimationSet set = AnimationUtils.getFigure_tipping_over(m);
			if (set != null && set.getLength() > 0) {
				JDImageProxy<?> i = set.getImagesNr(set.getLength() - 1);
				ob = new JDImageLocated(i, rect);
			}
		}

		int mouseSize = ROOMSIZE_BY_5;
		return new JDGraphicObject(ob, m, rect, JDColor.WHITE, new JDRectangle(
				p.getX() - mouseSize / 2, p.getY() - mouseSize / 2, mouseSize,
				mouseSize));
	}

	public JDDimension getMonsterSize(MonsterInfo m) {
		int mClass = m.getMonsterClass();
		if (mClass == Monster.WOLF) {
			return new JDDimension((int) (roomSize / 2.5),
					(int) (roomSize / 2.5));
		}
		if (mClass == Monster.ORC) {
			return new JDDimension((int) (roomSize / 2.5),
					(int) (roomSize / 2.5));
		}
		if (mClass == Monster.SKELETON) {
			return new JDDimension((int) (roomSize / 2.5),
					(int) (roomSize / 2.5));
		}
		if (mClass == Monster.GHUL) {
			return new JDDimension(ROOMSIZE_BY_2, ROOMSIZE_BY_2);
		}
		if (mClass == Monster.OGRE) {
			return new JDDimension(ROOMSIZE_BY_2, ROOMSIZE_BY_2);
		}
		if (mClass == Monster.BEAR) {
			return new JDDimension((int) (roomSize / 2.2),
					(int) (roomSize / 2.2));
		}

		return new JDDimension((int) (roomSize / 2.5), (int) (roomSize / 2.5));

	}

	private GraphicObject[] drawMonster(int xcoord, int ycoord,
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
					getPositionCoordModified(position).getX() + xcoord,
					getPositionCoordModified(position).getY() + ycoord));
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

	private JDGraphicObject getShrineGraphicObject(ShrineInfo s, int xcoord,
			int ycoord) {
		JDGraphicObject ob = null;
		if (s.getShrineIndex() == Shrine.SHRINE_HEALTH_FOUNTAIN) {
			int xpos = xcoord + (2 * ROOMSIZE_BY_3);
			int ypos = ycoord + (1 * ROOMSIZE_BY_16);
			int xsize = ROOMSIZE_BY_3;
			int ysize = (int) (roomSize / 3.5);
			ob = new JDGraphicObject(new JDImageLocated(
					ImageManager.fountainImage, xpos, ypos, xsize, ysize), s,
					getShrineRect(xcoord, ycoord), JDColor.YELLOW);
		} else if (s.getShrineIndex() == Shrine.SHRINE_REPAIR) {
			int xpos = xcoord + (2 * ROOMSIZE_BY_3);
			int ypos = ycoord + (1 * ROOMSIZE_BY_16);
			int xsize = ROOMSIZE_BY_3;
			int ysize = (int) (roomSize / 3.5);
			ob = new JDGraphicObject(new JDImageLocated(
					ImageManager.repairImage, xpos, ypos, xsize, ysize), s,
					getShrineRect(xcoord, ycoord), JDColor.YELLOW);
		} else if (s.getShrineIndex() == Shrine.SHRINE_STATUE) {
			int xpos = xcoord + (16 * ROOMSIZE_BY_24);
			int ypos = ycoord + (0 * ROOMSIZE_BY_36);
			int xsize = ROOMSIZE_BY_3;
			int ysize = (int) (roomSize / 2.5);
			ob = new JDGraphicObject(new JDImageLocated(
					ImageManager.statueImage, xpos, ypos, xsize, ysize), s,
					getShrineRect(xcoord, ycoord), JDColor.YELLOW);
		} else if (s.getShrineIndex() == Shrine.SHRINE_ANGEL) {
			int xpos = xcoord + (16 * ROOMSIZE_BY_24);
			int ypos = ycoord - (1 * ROOMSIZE_BY_36);
			int xsize = ROOMSIZE_BY_3;
			int ysize = (int) (roomSize / 2.5);
			if (s.getType() == Angel.SOLVED) {
				xsize = 0;
				ysize = 0;
			}
			ob = new JDGraphicObject(new JDImageLocated(
					ImageManager.engelImage, xpos, ypos, xsize, ysize), s,
					getShrineRect(xcoord, ycoord), JDColor.YELLOW);
		} else if (s.getShrineIndex() == Shrine.SHRINE_SORCER_LAB) {
			int xpos = xcoord + (25 * roomSize / 60);
			int ypos = ycoord - (1 * ROOMSIZE_BY_12);
			int xsize = (int) (roomSize / 1.65);
			int ysize = (int) (roomSize / 1.65);
			ob = new JDGraphicObject(new JDImageLocated(
					ImageManager.sorcLabImage, xpos, ypos, xsize, ysize), s,
					getShrineRect(xcoord, ycoord), JDColor.YELLOW);
		} else if (s.getShrineIndex() == Shrine.SHRINE_BROOD) {
			if ((s).getType() == Brood.BROOD_NATURE) {
				int xpos = xcoord + (7 * roomSize / 18);
				int ypos = ycoord + (1 * ROOMSIZE_BY_12);
				int xsize = (int) (roomSize / 1.5);
				int ysize = (ROOMSIZE_BY_2);
				ob = new JDGraphicObject(new JDImageLocated(
						ImageManager.caveImage, xpos, ypos, xsize, ysize), s,
						getShrineRect(xcoord, ycoord), JDColor.YELLOW);
			} else if ((s).getType() == Brood.BROOD_CREATURE) {
				int xpos = xcoord + (2 * ROOMSIZE_BY_3);
				int ypos = ycoord + (1 * ROOMSIZE_BY_6);
				int xsize = roomSize / 4;
				int ysize = (ROOMSIZE_BY_6);
				ob = new JDGraphicObject(new JDImageLocated(
						ImageManager.falltuerImage, xpos, ypos, xsize, ysize),
						s, getShrineRect(xcoord, ycoord), JDColor.YELLOW);
			} else if ((s).getType() == Brood.BROOD_UNDEAD) {
				int xpos = xcoord + (3 * ROOMSIZE_BY_5);
				int ypos = ycoord + (1 * ROOMSIZE_BY_16);
				int xsize = (int) (roomSize / 2.9);
				int ysize = (int) (roomSize / 2.2);
				ob = new JDGraphicObject(new JDImageLocated(
						ImageManager.graveImage, xpos, ypos, xsize, ysize), s,
						getShrineRect(xcoord, ycoord), JDColor.YELLOW);
			}
		} else if (s.getShrineIndex() == Shrine.SHRINE_TRADER) {
			int xpos = xcoord + (19 * roomSize / 30);
			int ypos = ycoord + (1 * roomSize / 24);
			int xsize = (int) (roomSize / 2.8);
			int ysize = (int) (roomSize / 2.0);
			ob = new JDGraphicObject(new JDImageLocated(
					ImageManager.traderImage, xpos, ypos, xsize, ysize), s,
					getShrineRect(xcoord, ycoord), JDColor.YELLOW);

		} else if (s.getShrineIndex() == Shrine.SHRINE_RUNE) {
			int xpos = xcoord + (13 * roomSize / 20);
			int ypos = ycoord + (1 * roomSize / 36);
			int xsize = (int) (roomSize / 2.9);
			int ysize = (int) (roomSize / 2.2);
			JDImageProxy<?> im = null;
			if ((s).getType() == 1) {
				im = ImageManager.shrine_yellowImage;
			} else if ((s).getType() == 2) {
				im = ImageManager.shrine_greenImage;
			} else if (s.getType() == 3) {
				im = ImageManager.shrine_redImage;
			}
			ob = new JDGraphicObject(new JDImageLocated(im, xpos, ypos, xsize,
					ysize), s, getShrineRect(xcoord, ycoord), JDColor.YELLOW);
		} else if (s.getShrineIndex() == Shrine.SHRINE_CORPSE) {
			int xpos = xcoord + (13 * roomSize / 20);
			int ypos = ycoord + (6 * roomSize / 36);
			int xsize = (int) (roomSize / 3.2);
			int ysize = (int) (roomSize / 3.8);
			JDImageProxy<?> im = null;
			if ((s).getType() == 0) {
				im = ImageManager.dead_dwarfImage;
			} else if ((s).getType() == 1) {
				im = ImageManager.dead_warriorImage;
			} else if ((s).getType() == 2) {
				im = ImageManager.dead_thiefImage;

			} else if ((s).getType() == 3) {
				im = ImageManager.dead_druidImage;
			} else if ((s).getType() == 4) {
				im = ImageManager.dead_mageImage;
			}
			ob = new JDGraphicObject(new JDImageLocated(im, xpos, ypos, xsize,
					ysize), s, getShrineRect(xcoord, ycoord), JDColor.YELLOW);
		} else if (s.getShrineIndex() == Shrine.SHRINE_QUEST) {
			int xpos = xcoord + (13 * roomSize / 20);
			int ypos = ycoord + (1 * roomSize / 36);
			int xsize = (int) (roomSize / 2.9);
			int ysize = (int) (roomSize / 2.2);
			JDImageProxy<?> im = ImageManager.shrine_blackImage;

			ob = new JDGraphicObject(new JDImageLocated(im, xpos, ypos, xsize,
					ysize), s, getShrineRect(xcoord, ycoord), JDColor.YELLOW);
		} else if (s.getShrineIndex() == Shrine.SHRINE_XMAS) {
			int xpos = xcoord + (27 * roomSize / 60);
			int ypos = ycoord - (1 * ROOMSIZE_BY_10);
			int xsize = (int) (roomSize / 1.7);
			int ysize = (int) (roomSize / 1.7);
			JDImageProxy<?> im = ImageManager.xmasImage;

			ob = new JDGraphicObject(new JDImageLocated(im, xpos, ypos, xsize,
					ysize), s, getShrineRect(xcoord, ycoord), JDColor.YELLOW);

		} else if (s.getShrineIndex() == Shrine.SHRINE_RUNEFINDER) {
			int xpos = xcoord + (7 * ROOMSIZE_BY_10);
			int ypos = ycoord + (1 * roomSize / 24);
			int xsize = (int) (roomSize / 3.4);
			int ysize = (int) (roomSize / 2.7);
			JDImageProxy<?> im = null;
			if ((s).getType() == 1) {
				im = ImageManager.shrine_small_yellowImage;
			} else if ((s).getType() == 2) {
				im = ImageManager.shrine_small_greenImage;
			} else if ((s).getType() == 3) {
				im = ImageManager.shrine_small_redImage;
			}
			ob = new JDGraphicObject(new JDImageLocated(im, xpos, ypos, xsize,
					ysize), s, getShrineRect(xcoord, ycoord), JDColor.YELLOW);

		} else if (s.getShrineIndex() == Shrine.SHRINE_DARK_MASTER) {
			int xpos = xcoord + (7 * ROOMSIZE_BY_10);
			int ypos = ycoord + (3 * ROOMSIZE_BY_16);
			int xsize = (int) (roomSize / 3.6);
			int ysize = (int) (roomSize / 3.7);
			JDImageProxy<?> im = ImageManager.pentagrammImage;
			ob = new JDGraphicObject(new JDImageLocated(im, xpos, ypos, xsize,
					ysize), s, getShrineRect(xcoord, ycoord), JDColor.YELLOW);
		} else if (s.getShrineIndex() == Shrine.SHRINE_LUZIA) {
			int xpos = xcoord + (7 * ROOMSIZE_BY_10);
			int ypos = ycoord + (0 * ROOMSIZE_BY_36);
			int xsize = (int) (roomSize / 3.6);
			int ysize = (int) (roomSize / 2.5);

			JDImageProxy<?> im = ImageManager.luziaImage;
			if (s.getType() == Luzia.SOLVED || s.getType() == Luzia.DEAD) {
				xpos = xcoord + (8 * ROOMSIZE_BY_10);
				ypos = ycoord + (5 * ROOMSIZE_BY_36);
				xsize = (int) (roomSize / 4.5);
				ysize = (int) (roomSize / 3.5);
				im = ImageManager.luzia_hutImage;
			}

			ob = new JDGraphicObject(new JDImageLocated(im, xpos, ypos, xsize,
					ysize), s, getShrineRect(xcoord, ycoord), JDColor.YELLOW);
		}
		return ob;

	}

	private JDRectangle getShrineRect(int xcoord, int ycoord) {
		int xpos = xcoord + (13 * roomSize / 20);
		int ypos = ycoord + (1 * roomSize / 36);
		int xsize = (int) (roomSize / 2.9);
		int ysize = (int) (roomSize / 2.2);
		return new JDRectangle(new JDPoint(xpos, ypos), xsize, ysize);
	}

	private JDGraphicObject getHeroGraphicObject(int x, int y, HeroInfo info,
			GraphicObjectRenderer renderer) {
		JDPoint p = renderer.getPositionCoordModified(info
				.getPositionInRoomIndex());
		int xpos = p.getX();
		int ypos = p.getY();
		int xSize = (int) (roomSize / HERO_SIZE_QUOTIENT_X);
		int ySize = (int) (roomSize / HERO_SIZE_QUOTIENT_Y);

		JDRectangle rect = new JDRectangle(x + xpos - (xSize / 2), y + ypos
				- (ySize / 2), xSize, ySize);

		int code = info.getHeroCode();
		int dir = info.getLookDir();
		if (dir == 0) {
			dir = Dir.NORTH;
		}
		JDImageLocated im = null;
		if (code == Hero.HEROCODE_WARRIOR) {
			if (info.isDead().booleanValue()
			/* && !gui.currentAnimationThreadRunning(info.getRoomInfo()) */) {
				im = new JDImageLocated(ImageManager.warrior_tipping_over.get(
						dir - 1).getImages()[ImageManager.warrior_tipping_over
						.get(dir - 1).getLength() - 1], rect);
			} else {
				im = new JDImageLocated(ImageManager.warriorImage[dir - 1],
						rect);
			}
		} else if (code == Hero.HEROCODE_HUNTER) {
			if (info.isDead().booleanValue()
			/* && !gui.currentAnimationThreadRunning(info.getRoomInfo()) */) {
				im = new JDImageLocated(ImageManager.thief_tipping_over.get(
						dir - 1).getImages()[ImageManager.thief_tipping_over
						.get(dir - 1).getLength() - 1], rect);
			} else {
				im = new JDImageLocated(ImageManager.thiefImage[dir - 1], rect);
			}
		} else if (code == Hero.HEROCODE_DRUID) {
			if (info.isDead().booleanValue()
			/* && !gui.currentAnimationThreadRunning(info.getRoomInfo()) */) {
				im = new JDImageLocated(ImageManager.druid_tipping_over.get(
						dir - 1).getImages()[ImageManager.druid_tipping_over
						.get(dir - 1).getLength() - 1], rect);
			} else {
				im = new JDImageLocated(ImageManager.druidImage[dir - 1], rect);
			}
		} else if (code == Hero.HEROCODE_MAGE) {
			if (info.isDead().booleanValue()
			/* && !gui.currentAnimationThreadRunning(info.getRoomInfo()) */) {
				im = new JDImageLocated(ImageManager.mage_tipping_over.get(
						dir - 1).getImages()[ImageManager.mage_tipping_over
						.get(dir - 1).getLength() - 1], rect);
			} else {
				im = new JDImageLocated(ImageManager.mageImage[dir - 1], rect);
			}
		}
		return new JDGraphicObject(im, info, rect, JDColor.WHITE,
				getHalfSizeRect(rect));
	}

	private JDRectangle getHalfSizeRect(JDRectangle r) {
		int sizex = r.getWidth();
		int sizey = r.getHeight();
		int newSizex = sizex / 2;
		int newSizey = sizey / 2;
		int newPointx = r.getX() + newSizex / 2;
		int newPointy = r.getY() + newSizey / 2;
		return new JDRectangle(newPointx, newPointy, newSizex, newSizey);
	}

	private GraphicObject drawBackGround(int xcoord, int ycoord, RoomInfo r,
			GraphicObjectRenderer renderer) {

		JDColor bg = null;

		int status = r.getVisibilityStatus();
		JDImageProxy<?> im = ImageManager.floorImageArray[r.getFloorIndex()];
		if (memory) {
			List<GraphicObject> roomDoors = renderer.drawDoors(r, xcoord,
					ycoord);
			im = ImageManager.floorImage_darkArray[r.getFloorIndex()];
			this.doors.addAll(roomDoors);
		} else {
			if (status == RoomObservationStatus.VISIBILITY_FOUND) {
				im = ImageManager.floorImage_mediumArray[r.getFloorIndex()];
				List<GraphicObject> roomDoors = renderer.drawDoors(r, xcoord,
						ycoord);
				this.doors.addAll(roomDoors);

			} else if (status == RoomObservationStatus.VISIBILITY_SHRINE) {
				List<GraphicObject> roomDoors = renderer.drawDoors(r, xcoord,
						ycoord);
				im = ImageManager.floorImage_mediumArray[r.getFloorIndex()];
				this.doors.addAll(roomDoors);
				if (r.isPart_scouted()) {

					int xpos = xcoord + (1 * roomSize / 3);
					int ypos = ycoord + (1 * roomSize / 2) - (roomSize / 5);
					int xsize = roomSize / 3;
					int ysize = (int) (roomSize / 2.5);
					GraphicObject ob = new JDGraphicObject(
							new JDImageLocated(ImageManager.questionmark, xpos,
									ypos, xsize, ysize), null, new JDRectangle(
									new JDPoint(xpos, ypos), xsize, ysize),
							JDColor.YELLOW);
					items.add(ob);
				}
			} else if (status > RoomObservationStatus.VISIBILITY_SHRINE) {

				List<GraphicObject> roomDoors = renderer.drawDoors(r, xcoord,
						ycoord);
				this.doors.addAll(roomDoors);

			} else {

				bg = JDColor.DARK_GRAY;
				return new GraphicObject(r, new JDRectangle(new JDPoint(xcoord
						+ getDoorDimension(true, roomSize).getWidth(), ycoord
						+ getDoorDimension(true, roomSize).getWidth()),
						new JDDimension(roomSize - 2
								* getDoorDimension(true, roomSize).getWidth(),
								roomSize
										- 2
										* getDoorDimension(true, roomSize)
												.getWidth())), bg, false, null);

			}
		}
		return new GraphicObject(r, new JDRectangle(
				new JDPoint(xcoord, ycoord), new JDDimension(roomSize, roomSize
						- 1
						* GraphicObjectRenderer
								.getDoorDimension(true, roomSize).getWidth())),
				bg, false, im);
	}

	private GraphicObject drawWallSouth(int xcoord, int ycoord, RoomInfo r) {
		GraphicObject ob = null;
		if (r.getVisibilityStatus() >= 2 && r.isClaimed()) {
			ob = new GraphicObject(null, new JDRectangle(new JDPoint(xcoord,
					ycoord
							+ roomSize
							- GraphicObjectRenderer.getDoorDimension(true,
									roomSize).getWidth()), new JDDimension(
					roomSize, roomSize)), null, ImageManager.wall_southImage);
			lastWalls.add(ob);
		}
		return ob;
	}

	private GraphicObject drawWall(int xcoord, int ycoord, RoomInfo r) {
		JDColor bg = null;

		GraphicObject ob = null;
		int status = r.getVisibilityStatus();
		if (status >= RoomObservationStatus.VISIBILITY_FOUND) {
			if (memory) {
				bg = JDColor.DARK_GRAY;
				ob = new GraphicObject(r, new JDRectangle(new JDPoint(xcoord,
						ycoord - getDoorDimension(true, roomSize).getWidth()),
						new JDDimension(roomSize, roomSize)), bg,
						ImageManager.wall_northImage);
				lastWalls.add(new GraphicObject(r, new JDRectangle(new JDPoint(
						xcoord, ycoord
								- getDoorDimension(true, roomSize).getWidth()),
						new JDDimension(roomSize, roomSize)), bg,
						ImageManager.wall_sidesImage));
			} else {
				lastWalls.add(new GraphicObject(r, new JDRectangle(new JDPoint(
						xcoord, ycoord
								- getDoorDimension(true, roomSize).getWidth()),
						new JDDimension(roomSize, roomSize)), bg,
						ImageManager.wall_sidesImage));

				ob = new GraphicObject(r, new JDRectangle(new JDPoint(xcoord,
						ycoord - getDoorDimension(true, roomSize).getWidth()),
						new JDDimension(roomSize, roomSize)), bg,
						ImageManager.wall_northImage);
			}

		}

		return ob;
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

	public List<GraphicObject> createGraphicObjectsForRoom(RoomInfo r,
			Object obj, int xcoord, int ycoord, List<?> aniObs) {
		List<GraphicObject> graphObs = new LinkedList<GraphicObject>();
		if (r == null) {
			return graphObs;
		}

		/*
		 * room
		 */
		GraphicObject roomOb = drawBackGround(xcoord, ycoord, r, this);
		graphObs.add(roomOb);

		/*
		 * wall
		 */
		GraphicObject wallOb = drawWall(xcoord, ycoord, r);
		if (wallOb != null) {
			graphObs.add(wallOb);
		}

		/*
		 * wall south
		 */
		GraphicObject wallObSouth = drawWallSouth(xcoord, ycoord, r);
		if (wallObSouth != null) {
			graphObs.add(wallObSouth);
		}

		/*
		 * doors
		 */
		graphObs.addAll(drawDoors(r, xcoord, ycoord));

		int status = r.getVisibilityStatus();

		if ((status >= RoomObservationStatus.VISIBILITY_SHRINE)) {

			if (r.equals(figure.getRoomInfo())) {
				JDPoint[] positionCoord = getPositionCoord();
				for (int i = 0; i < positionCoord.length; i++) {
					int posSize = ROOMSIZE_BY_8;
					GraphicObject ob = new GraphicObject(
							r.getPositionInRoom(i), new JDRectangle(xcoord
									+ getPositionCoord(i).getX(), ycoord
									+ getPositionCoord(i).getY(), posSize,
									posSize), JDColor.BLACK,
							ImageManager.fieldImage);

					graphObs.add(ob);

				}
			}

			graphObs.add(new GraphicObject(r, new JDRectangle(new JDPoint(
					xcoord, ycoord
							- getDoorDimension(true, roomSize).getWidth()),
					new JDDimension(roomSize, roomSize)), JDColor.DARK_GRAY,
					ImageManager.wall_sidesImage));

			GraphicObject ob = null;
			if (r.getShrine() != null) {
				ShrineInfo s = r.getShrine();
				ob = getShrineGraphicObject(s, xcoord, ycoord);
				graphObs.add(ob);
			}

			if (r.getChest() != null) {
				GraphicObject chestOb;
				if (r.getChest().hasLock()) {
					chestOb = new GraphicObject(r.getChest(),
							new JDRectangle(getChestPoint(xcoord, ycoord),
									getChestDimension()), new JDColor(140, 90,
									20), ImageManager.chest_lockImage);
				} else {
					chestOb = new GraphicObject(r.getChest(),
							new JDRectangle(getChestPoint(xcoord, ycoord),
									getChestDimension()), new JDColor(140, 90,
									20), ImageManager.chestImage);
				}
				graphObs.add(chestOb);
			}
		}
		if ((status >= RoomObservationStatus.VISIBILITY_FIGURES)) {

			if (r.getMonsterInfos().size() > 0) {
				GraphicObject[] monsterObs = drawMonster(xcoord, ycoord,
						r.getMonsterInfos());
				for (int i = 0; i < monsterObs.length; i++) {
					if (monsterObs[i] != null) {
						boolean contains = aniObs.contains(monsterObs[i]
								.getClickedObject());
						if (!contains) {
							if (!(monsterObs[i].getClickedObject().equals(obj))) {
								graphObs.add(monsterObs[i]);
							}
						}
					}
				}
			}
		}
		if ((status >= RoomObservationStatus.VISIBILITY_ITEMS)) {

			GraphicObject[] itObs = drawItems(xcoord, ycoord, r.getItemArray(),
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
					new JDRectangle(getSpotPoint(xcoord, ycoord),
							getSpotDimension()), new JDColor(0, 0, 0),
					ImageManager.spotImage);
			graphObs.add(spotOb);
		}

		if (r.getHeroInfo() != null) {
			GraphicObject heroObject = drawHero(xcoord, ycoord,
					r.getHeroInfo(), this);
			if (heroObject != null) {
				graphObs.add(heroObject);
			}
		}

		return graphObs;
	}

	public GraphicObject getHero() {
		return hero;
	}

	public void drawRoom(int xcoord, int ycoord, RoomInfo r) {

		GraphicObject roomOb = drawBackGround(xcoord, ycoord, r, this);
		GraphicObject wallOb = drawWall(xcoord, ycoord, r);

		rooms.add(roomOb);
		if (wallOb != null) {
			walls.add(wallOb);
		}

		int status = r.getVisibilityStatus();

		if ((status >= RoomObservationStatus.VISIBILITY_SHRINE)) {
			GraphicObject ob = null;
			if (r.getShrine() != null) {
				ShrineInfo s = r.getShrine();
				ob = this.getShrineGraphicObject(s, xcoord, ycoord);

				shrines.add(ob);
			}

			if (r.getChest() != null) {
				GraphicObject chestOb;
				if (r.getChest().hasLock()) {
					chestOb = new GraphicObject(r.getChest(),
							new JDRectangle(getChestPoint(xcoord, ycoord),
									getChestDimension()), new JDColor(140, 90,
									20), ImageManager.chest_lockImage);
				} else {
					chestOb = new GraphicObject(r.getChest(),
							new JDRectangle(getChestPoint(xcoord, ycoord),
									getChestDimension()), new JDColor(140, 90,
									20), ImageManager.chestImage);
				}
				chests.add(chestOb);
			}
		}
		if ((status >= RoomObservationStatus.VISIBILITY_FIGURES)) {
			JDPoint[] positionCoord = getPositionCoord();
			if (r.equals(figure.getRoomInfo())) {
				for (int i = 0; i < positionCoord.length; i++) {
					int posSize = ROOMSIZE_BY_8;

					JDImageProxy<?> im = ImageManager.fieldImage;
					GraphicObject ob = new GraphicObject(
							r.getPositionInRoom(i), new JDRectangle(xcoord
									+ getPositionCoord(i).getX(), ycoord
									+ getPositionCoord(i).getY(), posSize,
									posSize), JDColor.BLACK, im);

					positions.add(ob);

				}
			}
			List<MonsterInfo> monsters = r.getMonsterInfos();
			if (monsters != null && monsters.size() > 0) {
				GraphicObject[] monsterObs = drawMonster(xcoord, ycoord,
						r.getMonsterInfos());
				for (int i = 0; i < monsterObs.length; i++) {
					if (monsterObs[i] != null) {
						monster.add(monsterObs[i]);
					}
				}
			}
		}
		if ((status >= RoomObservationStatus.VISIBILITY_ITEMS) || (visCheat)) {

			GraphicObject[] itObs = drawItems(xcoord, ycoord, r.getItemArray(),
					roomSize);
			for (int i = 0; i < itObs.length; i++) {
				GraphicObject o = itObs[i];
				if (o != null) {
					items.add(o);
				}
			}

		}

		if ((r.getSpot() != null) && (r.getSpot().isFound())) {
			GraphicObject spotOb = new GraphicObject(r.getSpot(),
					new JDRectangle(getSpotPoint(xcoord, ycoord),
							getSpotDimension()), JDColor.BLACK,
					ImageManager.spotImage);
			spots.add(spotOb);
		}

		if (r.getHeroInfo() != null) {
			hero = drawHero(xcoord, ycoord, r.getHeroInfo(), this);
		}
	}

	private JDPoint getSpotPoint(int xcoord, int ycoord) {
		int x1 = xcoord + (1 * roomSize / 8);
		int y1 = ycoord + (6 * roomSize / 8);

		return new JDPoint(x1, y1);
	}

	private GraphicObject drawHero(int x, int y, HeroInfo info,
			GraphicObjectRenderer renderer) {
		if (info == null) {
			return null;
		}
		return renderer.getHeroGraphicObject(x, y, info, renderer);
	}

}
