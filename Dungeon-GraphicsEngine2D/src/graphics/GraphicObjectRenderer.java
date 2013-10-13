package graphics;

import figure.RoomObservationStatus;
import figure.hero.Hero;
import figure.hero.HeroInfo;
import figure.monster.Monster;
import figure.monster.MonsterInfo;
import game.JDGUI;
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import shrine.Angel;
import shrine.Brood;
import shrine.Luzia;
import shrine.Shrine;
import shrine.ShrineInfo;
import animation.AnimationSet;
import animation.AnimationUtils;
import dungeon.Dir;
import dungeon.DoorInfo;
import dungeon.JDPoint;
import dungeon.RoomInfo;
import dungeon.RouteInstruction;

public class GraphicObjectRenderer {
	
	private boolean memory = false;
	private boolean visCheat = false;
	
	private int roomSize;
	
	private int ROOMSIZE_BY_36;
	private int ROOMSIZE_BY_24; 
	private int ROOMSIZE_BY_20;
	private int ROOMSIZE_BY_12;
	private int ROOMSIZE_BY_10; 
	private int ROOMSIZE_BY_16;
	private int ROOMSIZE_BY_8; 
	private int ROOMSIZE_BY_6;
	private int ROOMSIZE_BY_5; 
	private int ROOMSIZE_BY_4; 
	private int ROOMSIZE_BY_3; 
	private int ROOMSIZE_BY_2;
	private Point[] positionCoord = new Point[8];
	
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
	
	private JDGUI gui;

	
	private Point[] getPositionCoord() {
		return positionCoord;
	}

	public GraphicObjectRenderer(int roomSize, JDGUI gui) {
		this.gui = gui;
		this.roomSize = roomSize;
		ROOMSIZE_BY_36 = RoomSize.by(36,roomSize);
		ROOMSIZE_BY_24 = RoomSize.by(24,roomSize);
		ROOMSIZE_BY_20 = RoomSize.by(20,roomSize);
		ROOMSIZE_BY_12 = RoomSize.by(12,roomSize);
		ROOMSIZE_BY_10 = RoomSize.by(10,roomSize);
		ROOMSIZE_BY_16 = RoomSize.by(16,roomSize);
		ROOMSIZE_BY_8 = RoomSize.by(8,roomSize);
		ROOMSIZE_BY_6 = RoomSize.by(6,roomSize);
		ROOMSIZE_BY_5 = RoomSize.by(5,roomSize);
		ROOMSIZE_BY_4 = RoomSize.by(4,roomSize);
		ROOMSIZE_BY_3 = RoomSize.by(3,roomSize);
		ROOMSIZE_BY_2 = RoomSize.by(2,roomSize);

		positionCoord[0] = new Point(roomSize / 4 - ROOMSIZE_BY_16,
				(int) (roomSize / 3) - ROOMSIZE_BY_36);
		positionCoord[1] = new Point(roomSize / 2 - ROOMSIZE_BY_16,
				(int) (roomSize / 3.5) - ROOMSIZE_BY_36);
		positionCoord[2] = new Point(roomSize * 3 / 4 - ROOMSIZE_BY_16,
				(int) (roomSize / 3) - ROOMSIZE_BY_36);
		positionCoord[3] = new Point(roomSize * 4 / 5 - ROOMSIZE_BY_16,
				(int) (roomSize / 1.8) - ROOMSIZE_BY_36);
		positionCoord[4] = new Point(roomSize * 3 / 4 - ROOMSIZE_BY_16,
				roomSize * 3 / 4 - ROOMSIZE_BY_36);
		positionCoord[5] = new Point(roomSize / 2 - ROOMSIZE_BY_16, roomSize
				* 4 / 5 - ROOMSIZE_BY_36);
		positionCoord[6] = new Point(roomSize / 4 - ROOMSIZE_BY_16, roomSize
				* 3 / 4 - ROOMSIZE_BY_36);
		positionCoord[7] = new Point(roomSize / 5 - ROOMSIZE_BY_16,
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

	}
	
	private static GraphicObject[] drawItems(int xcoord, int ycoord,
			ItemInfo[] itemArray, int roomSize) {
		if (itemArray == null) {
			return new GraphicObject[0];
		}
		Point p2[] = getItemPoints(xcoord, ycoord, roomSize);

		GraphicObject[] itemObs = new GraphicObject[4];

		// Schleife bis 4
		int i = 0;
		while (i < itemArray.length && i < 4) {
			Point q = p2[i];

			if (itemArray[i] != null) {

				if (AttrPotion.class.isAssignableFrom(itemArray[i]
						.getItemClass())) {

					if (((itemArray[i]).getItemKey() == Item.ITEM_KEY_HEALPOTION)) {
						itemObs[i] = new GraphicObject(itemArray[i], new Rectangle(q, new Dimension(
										12 * (roomSize / 100),
										15 * (roomSize / 100))), Color.yellow,ImageManager.potion_redImage);
					} else {
						itemObs[i] = new GraphicObject(
								itemArray[i], new Rectangle(q, new Dimension(
										12 * (roomSize / 100),
										15 * (roomSize / 100))), Color.yellow,ImageManager.potion_blueImage);
					}

				} else if (itemArray[i].getItemClass().equals(DustItem.class)) {
					itemObs[i] = new GraphicObject(itemArray[i], new Rectangle(q,
									new Dimension(10 * (roomSize / 100),
											8 * (roomSize / 100))),Color.yellow,ImageManager.dustImage);
				} else if (itemArray[i].getItemClass().equals(Sword.class)) {
					itemObs[i] = new GraphicObject(itemArray[i], new Rectangle(q, new Dimension(
									30 * (roomSize / 100),
									20 * (roomSize / 100))), Color.yellow,ImageManager.swordImage);
				} else if (itemArray[i].getItemClass().equals(Axe.class)) {
					itemObs[i] = new GraphicObject(itemArray[i], new Rectangle(q, new Dimension(
									30 * (roomSize / 100),
									20 * (roomSize / 100))), Color.yellow,ImageManager.axeImage);
				} else if (itemArray[i].getItemClass().equals(Club.class)) {
					itemObs[i] = new GraphicObject(
							itemArray[i], new Rectangle(q, new Dimension(
									30 * (roomSize / 100),
									20 * (roomSize / 100))), Color.yellow,ImageManager.clubImage);
				} else if (itemArray[i].getItemClass().equals(Lance.class)) {
					itemObs[i] = new GraphicObject( itemArray[i], new Rectangle(q, new Dimension(
									50 * (roomSize / 100),
									40 * (roomSize / 100))), Color.yellow,ImageManager.lanceImage);
				} else if (itemArray[i].getItemClass().equals(Wolfknife.class)) {
					itemObs[i] = new GraphicObject(	itemArray[i], new Rectangle(q, new Dimension(
									20 * (roomSize / 100),
									15 * (roomSize / 100))), Color.yellow,ImageManager.wolfknifeImage);
				} else if (itemArray[i].getItemClass().equals(Armor.class)) {
					int sizeX = 30 * (roomSize / 100);
					int sizeY = 20 * (roomSize / 100);

					itemObs[i] = new GraphicObject(
							itemArray[i], new Rectangle(q, new Dimension(sizeX,
									sizeY)), Color.yellow,ImageManager.armorImage);
				} else if (itemArray[i].getItemClass().equals(Shield.class)) {
					int sizeX = 20 * (roomSize / 100);
					int sizeY = 15 * (roomSize / 100);

					itemObs[i] = new GraphicObject(
							itemArray[i], new Rectangle(q, new Dimension(sizeX,
									sizeY)), Color.yellow, ImageManager.shieldImage);
				} else if (itemArray[i].getItemClass().equals(Helmet.class)) {
					int sizeX = 24 * (roomSize / 100);
					int sizeY = 22 * (roomSize / 100);

					itemObs[i] = new GraphicObject(
							itemArray[i], new Rectangle(q, new Dimension(sizeX,
									sizeY)), Color.yellow, ImageManager.helmetImage);
				} else if (Scroll.class.isAssignableFrom(itemArray[i]
						.getItemClass())) {
					int sizeX = 15 * (roomSize / 100);
					int sizeY = 15 * (roomSize / 100);

					itemObs[i] = new GraphicObject(itemArray[i], new Rectangle(q, new Dimension(sizeX,
									sizeY)), Color.yellow,ImageManager.scrollImage);
				} else if (itemArray[i].getItemClass().equals(InfoScroll.class)) {
					int sizeX = 15 * (roomSize / 100);
					int sizeY = 15 * (roomSize / 100);

					itemObs[i] = new GraphicObject(itemArray[i], new Rectangle(
									q, new Dimension(sizeX, sizeY)),
							Color.yellow,ImageManager.documentImage);
				} else if (itemArray[i].getItemClass().equals(Feather.class)) {
					int sizeX = 15 * (roomSize / 100);
					int sizeY = 15 * (roomSize / 100);

					itemObs[i] = new GraphicObject(	itemArray[i], new Rectangle(q, new Dimension(sizeX,
									sizeY)), Color.yellow,ImageManager.featherImage);
				} else if (itemArray[i].getItemClass().equals(Incense.class)) {
					int sizeX = 15 * (roomSize / 100);
					int sizeY = 15 * (roomSize / 100);

					itemObs[i] = new GraphicObject(	 itemArray[i], new Rectangle(q,
							new Dimension(sizeX, sizeY)), Color.yellow,ImageManager.potion_greenImage);
				} else if (itemArray[i].getItemClass().equals(Key.class)) {
					int sizeX = 16 * (roomSize / 100);
					int sizeY = 22 * (roomSize / 100);

					itemObs[i] = new GraphicObject(	itemArray[i], new Rectangle(q, new Dimension(sizeX,
									sizeY)), Color.yellow,ImageManager.keyImage);

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

					itemObs[i] = new GraphicObject(itemArray[i], new Rectangle(q,
							new Dimension(sizeX, sizeY)), Color.yellow,im);
				} else if (itemArray[i].getItemClass().equals(
						DarkMasterKey.class)) {
					int sizeX = 12 * (roomSize / 100);
					int sizeY = 12 * (roomSize / 100);
					JDImageProxy<?> im = ImageManager.cristall_redImage;

					itemObs[i] = new GraphicObject(itemArray[i], new Rectangle(q,
							new Dimension(sizeX, sizeY)), Color.yellow,im);
				} else if (itemArray[i].getClass().equals(LuziasBall.class)) {
					int sizeX = 15 * (roomSize / 100);
					int sizeY = 15 * (roomSize / 100);
					JDImageProxy<?> im = ImageManager.kugelImage;

					itemObs[i] = new GraphicObject(itemArray[i], new Rectangle(q,
							new Dimension(sizeX, sizeY)), Color.yellow,im);
				} else if (itemArray[i].getClass().equals(Book.class)) {
					int sizeX = 15 * (roomSize / 100);
					int sizeY = 15 * (roomSize / 100);
					JDImageProxy<?> im = ImageManager.bookImage;

					itemObs[i] = new GraphicObject( itemArray[i], new Rectangle(q,
							new Dimension(sizeX, sizeY)), Color.yellow,im);

				} else if (Thing.class.isAssignableFrom(itemArray[i]
						.getItemClass())) {
					int sizeX = 12 * (roomSize / 100);
					int sizeY = 12 * (roomSize / 100);
					JDImageProxy<?> im = null;

					im = ImageManager.amulettImage;

					itemObs[i] = new GraphicObject(itemArray[i], new Rectangle(q,
							new Dimension(sizeX, sizeY)), Color.yellow,im);
				} else {

					itemObs[i] = new GraphicObject(itemArray[i], new Rectangle(
							q, new Dimension(8, 5)), Color.yellow, null);
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
				door0 = new JDGraphicObject(new JDImageAWT(
						ImageManager.door_north, xcoord, ycoord
								- getDoorDimension(true, roomSize).width, roomSize,
						roomSize), doors[0], rect, new Color(180, 150, 80),
						false);
			} else {
				JDRectangle rect = getNorthDoorRect(xcoord, ycoord);
				door0 = new JDGraphicObject(new JDImageAWT(
						ImageManager.door_north_lock, xcoord, ycoord
								- getDoorDimension(true, roomSize).width, roomSize,
						roomSize), doors[0], rect, new Color(180, 150, 80),
						false);
			}
			roomDoors.add(door0);
		} else {
			JDGraphicObject door0 = new JDGraphicObject(
					new JDImageAWT(ImageManager.door_north_none, xcoord, ycoord
							- getDoorDimension(true, roomSize).width, roomSize, roomSize),
					doors[0], null, new Color(180, 150, 80), false);
			roomDoors.add(door0);
		}
		if (doors[1] != null) {
			JDRectangle rect = getEastDoorRect(xcoord, ycoord);
			JDGraphicObject door1;
			if (!doors[1].hasLock().booleanValue()) {
				door1 = new JDGraphicObject(new JDImageAWT(ImageManager.door_east,
						xcoord, ycoord - getDoorDimension(true, roomSize).width,
						roomSize, roomSize), doors[1], rect, new Color(180,
						150, 80), false);
			} else {
				door1 = new JDGraphicObject(new JDImageAWT(
						ImageManager.door_east_lock, xcoord, ycoord
								- getDoorDimension(true, roomSize).width, roomSize,
						roomSize), doors[1], rect, new Color(180, 150, 80),
						false);
			}
			roomDoors.add(door1);
		} else {
			JDGraphicObject door1 = new JDGraphicObject(
					new JDImageAWT(ImageManager.door_east_none, xcoord, ycoord
							- getDoorDimension(true, roomSize).width, roomSize, roomSize),
					doors[1], null, new Color(180, 150, 80), false);
			roomDoors.add(door1);
		}
		if (doors[2] != null) {
			// die suedliche tuer wird immer vom Raum unterhalb gerendert
		}
		if (doors[3] != null) {
			JDRectangle rect = getWestDoorRect(xcoord, ycoord);
			if (rect == null) {
				System.out.println("rect ist null");
				System.exit(0);
			}
			JDGraphicObject door3;

			if (!doors[3].hasLock().booleanValue()) {
				door3 = new JDGraphicObject(new JDImageAWT(ImageManager.door_west,
						xcoord, ycoord - getDoorDimension(true, roomSize).width,
						roomSize, roomSize), doors[3], rect, new Color(180,
						150, 80), false);
			} else {
				door3 = new JDGraphicObject(new JDImageAWT(
						ImageManager.door_west_lock, xcoord, ycoord
								- getDoorDimension(true, roomSize).width, roomSize,
						roomSize), doors[3], rect, new Color(180, 150, 80),
						false);
			}
			roomDoors.add(door3);
		} else {
			JDGraphicObject door0 = new JDGraphicObject(
					new JDImageAWT(ImageManager.door_west_none, xcoord, ycoord
							- getDoorDimension(true, roomSize).width, roomSize, roomSize),
					doors[3], null, new Color(180, 150, 80), false);
			roomDoors.add(door0);
		}

		return roomDoors;
	}
	
	private JDRectangle getNorthDoorRect(int x, int y) {

		Dimension d = getDoorDimension(false, roomSize);
		return new JDRectangle(new JDPoint(
				(int) (x + (roomSize / 2) - (d.getWidth() / 2)), y), d.width, d.height);
	}

	private JDRectangle getEastDoorRect(int x, int y) {

		Dimension d = getDoorDimension(true, roomSize);
		return new JDRectangle(new JDPoint((int) (x + (roomSize) - (d.getWidth())),
				(int) (y + (roomSize / 2) - (d.getHeight() / 2))),  d.width, d.height);
	}

	private static JDRectangle getSouthDoorRect(int x, int y, int roomSize) {

		Dimension d = getDoorDimension(false,roomSize);
		return new JDRectangle(new JDPoint(
				(int) (x + (roomSize / 2) - (d.getWidth() / 2)), (int) (y
						+ roomSize - (d.getHeight()))), d.width, d.height);
	}

	private JDRectangle getWestDoorRect(int x, int y) {

		Dimension d = getDoorDimension(true, roomSize);
		return new JDRectangle(new JDPoint((int) (x),
				(int) (y + (roomSize / 2) - (d.getHeight() / 2))),  d.width, d.height);
	}
	
	private static Dimension getDoorDimension(boolean vertical, int roomSize) {
		int width;
		int heigth;
		if (vertical) {
			width = roomSize / 15;
			heigth = roomSize / 4;
		} else {
			width = roomSize / 4;
			heigth = roomSize / 8;
		}
		return new Dimension(width, heigth);

	}
	
	private static Point[] getItemPoints(int xcoord, int ycoord, int roomSize) {
		Point[] points = new Point[4];

		points[0] = new Point(xcoord + RoomSize.by2(roomSize) -  RoomSize.by8(roomSize),
				ycoord + RoomSize.by3(roomSize));
		points[1] = new Point(xcoord + RoomSize.by2(roomSize), ycoord + RoomSize.by2(roomSize)
				- RoomSize.by16(roomSize));
		points[2] = new Point(xcoord + RoomSize.by3(roomSize), ycoord
				+ (3 * RoomSize.by5(roomSize)));
		points[3] = new Point(xcoord + (int) (3 * RoomSize.by5(roomSize)), ycoord
				+ (3 * RoomSize.by5(roomSize)));

		return points;
	}
	
	private static GraphicObject drawAMonster(MonsterInfo m, Point p, int roomSize) {
		JDImageAWT ob = null;
		int mClass = m.getMonsterClass();
		int sizeX = (int) (getMonsterSize(m, roomSize).width);
		int sizeY = (int) (getMonsterSize(m, roomSize).height);
		JDRectangle rect = new JDRectangle(new JDPoint(p.x - (sizeX / 2), p.y
				- (sizeY / 2)), sizeX, sizeY);
		int dir = m.getLookDir();
		if (mClass == Monster.WOLF) {

			JDImageProxy<?> wolf = ImageManager.wolfImage[dir - 1];
			if (m.getLevel() == 2) {
				wolf = ImageManager.wolfImage[dir - 1];
			}
			ob = new JDImageAWT(wolf, rect);
		} else if (mClass == Monster.ORC) {
			JDImageProxy<?> orc = ImageManager.orcImage[dir - 1];

			if (m.getLevel() == 2) {
				orc = ImageManager.orcImage[dir - 1];
			}
			ob = new JDImageAWT(orc, rect);
		} else if (mClass == Monster.SKELETON) {
			JDImageProxy<?> skel = ImageManager.skelImage[dir - 1];

			if (m.getLevel() == 2) {
				skel = ImageManager.skelImage[dir - 1];
			}
			ob = new JDImageAWT(skel, rect);
		} else if (mClass == Monster.GHUL) {
			JDImageProxy<?> ghul = ImageManager.ghulImage[dir - 1];

			if (m.getLevel() == 2) {
				ghul = ImageManager.ghulImage[dir - 1];
			}
			ob = new JDImageAWT(ghul, rect);
		} else if (mClass == Monster.OGRE) {
			JDImageProxy<?> ogre = ImageManager.ogreImage[dir - 1];

			if (m.getLevel() == 2) {
				ogre = ImageManager.ogreImage[dir - 1];
			}
			ob = new JDImageAWT(ogre, rect);
		} else if (mClass == Monster.BEAR) {
			JDImageProxy<?> bear = ImageManager.bearImage[dir - 1];

			if (m.getLevel() == 2) {
				bear = ImageManager.bearImage[dir - 1];
			}
			ob = new JDImageAWT(bear, rect);

		} else if (mClass == Monster.DARKMASTER) {
			ob = new JDImageAWT(ImageManager.darkMasterImage, rect);

		} else if (mClass == Monster.DWARF) {
			ob = new JDImageAWT(ImageManager.dark_dwarfImage, rect);

		} else if (mClass == Monster.FIR) {
			ob = new JDImageAWT(ImageManager.finImage, rect);

		} else {
			ob = new JDImageAWT(ImageManager.engelImage, rect);
		}

		if (m.isDead() != null && m.isDead().booleanValue()) {
			// System.out.println("will totes Monster malen!");
			AnimationSet set = AnimationUtils.getFigure_tipping_over(m);
			if (set != null && set.getLength() > 0) {
				JDImageProxy<?> i = set.getImagesNr(set.getLength() - 1);
				ob = new JDImageAWT(i, rect);
			}
		}

		int mouseSize = RoomSize.by5(roomSize);
		return new JDGraphicObject(ob, m, rect, Color.white, new JDRectangle(p.x - mouseSize / 2, p.y - mouseSize / 2,
				mouseSize, mouseSize));
	}
	
	public static Dimension getMonsterSize(MonsterInfo m, int roomSize) {
		int mClass = m.getMonsterClass();
		if (mClass == Monster.WOLF) {
			return new Dimension((int) (roomSize / 2.5), (int) (roomSize / 2.5));
		}
		if (mClass == Monster.ORC) {
			return new Dimension((int) (roomSize / 2.5), (int) (roomSize / 2.5));
		}
		if (mClass == Monster.SKELETON) {
			return new Dimension((int) (roomSize / 2.5), (int) (roomSize / 2.5));
		}
		if (mClass == Monster.GHUL) {
			return new Dimension(RoomSize.by2(roomSize), RoomSize.by2(roomSize));
		}
		if (mClass == Monster.OGRE) {
			return new Dimension(RoomSize.by2(roomSize), RoomSize.by2(roomSize));
		}
		if (mClass == Monster.BEAR) {
			return new Dimension((int) (roomSize / 2.2), (int) (roomSize / 2.2));
		}

		return new Dimension((int) (roomSize / 2.5), (int) (roomSize / 2.5));

	}
	
	private GraphicObject[] drawMonster(int xcoord, int ycoord,
			List<MonsterInfo> monsterList) {
		int k = monsterList.size();
		GraphicObject obs[] = new GraphicObject[k];
		if (monsterList.size() > 8) {
			obs = new GraphicObject[8];
		}
		for (int i = 0; i < monsterList.size(); i++) {

			MonsterInfo m = ((MonsterInfo) monsterList.get(i));
			int position = m.getPositionInRoomIndex();

			GraphicObject gr = GraphicObjectRenderer.drawAMonster(m, new Point(
					getPositionCoordModified(position).x + xcoord,
					getPositionCoordModified(position).y + ycoord), roomSize);
			if (i >= 8) {
				break;
			}
			obs[i] = gr;
		}

		return obs;
	}
	
	public Point getPositionCoordModified(int index) {
		if (index >= 0 && index < 8) {
			int posX = positionCoord[index].x;
			int posY = positionCoord[index].y;
			return new Point(posX + ROOMSIZE_BY_20, posY);
		}
		return null;
	}
	
	public Point getPositionCoord(int index) {
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
			ob = new JDGraphicObject(new JDImageAWT(ImageManager.fountainImage,
					xpos, ypos, xsize, ysize), s,
					getShrineRect(xcoord, ycoord), Color.yellow);
		} else if (s.getShrineIndex() == Shrine.SHRINE_REPAIR) {
			int xpos = xcoord + (2 * ROOMSIZE_BY_3);
			int ypos = ycoord + (1 * ROOMSIZE_BY_16);
			int xsize = ROOMSIZE_BY_3;
			int ysize = (int) (roomSize / 3.5);
			ob = new JDGraphicObject(new JDImageAWT(ImageManager.repairImage,
					xpos, ypos, xsize, ysize), s,
					getShrineRect(xcoord, ycoord), Color.yellow);
		} else if (s.getShrineIndex() == Shrine.SHRINE_STATUE) {
			int xpos = xcoord + (16 * ROOMSIZE_BY_24);
			int ypos = ycoord + (0 * ROOMSIZE_BY_36);
			int xsize = ROOMSIZE_BY_3;
			int ysize = (int) (roomSize / 2.5);
			ob = new JDGraphicObject(new JDImageAWT(ImageManager.statueImage,
					xpos, ypos, xsize, ysize), s,
					getShrineRect(xcoord, ycoord), Color.yellow);
		} else if (s.getShrineIndex() == Shrine.SHRINE_ANGEL) {
			int xpos = xcoord + (16 * ROOMSIZE_BY_24);
			int ypos = ycoord - (1 * ROOMSIZE_BY_36);
			int xsize = ROOMSIZE_BY_3;
			int ysize = (int) (roomSize / 2.5);
			if (s.getType() == Angel.SOLVED) {
				xsize = 0;
				ysize = 0;
			}
			ob = new JDGraphicObject(new JDImageAWT(ImageManager.engelImage, xpos,
					ypos, xsize, ysize), s, getShrineRect(xcoord, ycoord),
					Color.yellow);
		} else if (s.getShrineIndex() == Shrine.SHRINE_SORCER_LAB) {
			int xpos = xcoord + (25 * roomSize / 60);
			int ypos = ycoord - (1 * ROOMSIZE_BY_12);
			int xsize = (int) (roomSize / 1.65);
			int ysize = (int) (roomSize / 1.65);
			ob = new JDGraphicObject(new JDImageAWT(ImageManager.sorcLabImage,
					xpos, ypos, xsize, ysize), s,
					getShrineRect(xcoord, ycoord), Color.yellow);
		} else if (s.getShrineIndex() == Shrine.SHRINE_BROOD) {
			if ((s).getType() == Brood.BROOD_NATURE) {
				int xpos = xcoord + (7 * roomSize / 18);
				int ypos = ycoord + (1 * ROOMSIZE_BY_12);
				int xsize = (int) (roomSize / 1.5);
				int ysize = (int) (ROOMSIZE_BY_2);
				ob = new JDGraphicObject(new JDImageAWT(ImageManager.caveImage,
						xpos, ypos, xsize, ysize), s, getShrineRect(xcoord,
						ycoord), Color.yellow);
			} else if ((s).getType() == Brood.BROOD_CREATURE) {
				int xpos = xcoord + (2 * ROOMSIZE_BY_3);
				int ypos = ycoord + (1 * ROOMSIZE_BY_6);
				int xsize = roomSize / 4;
				int ysize = (int) (ROOMSIZE_BY_6);
				ob = new JDGraphicObject(new JDImageAWT(
						ImageManager.falltuerImage, xpos, ypos, xsize, ysize),
						s, getShrineRect(xcoord, ycoord), Color.yellow);
			} else if ((s).getType() == Brood.BROOD_UNDEAD) {
				int xpos = xcoord + (3 * ROOMSIZE_BY_5);
				int ypos = ycoord + (1 * ROOMSIZE_BY_16);
				int xsize = (int) (roomSize / 2.9);
				int ysize = (int) (roomSize / 2.2);
				ob = new JDGraphicObject(new JDImageAWT(ImageManager.graveImage,
						xpos, ypos, xsize, ysize), s, getShrineRect(xcoord,
						ycoord), Color.yellow);
			}
		} else if (s.getShrineIndex() == Shrine.SHRINE_TRADER) {
			int xpos = xcoord + (19 * roomSize / 30);
			int ypos = ycoord + (1 * roomSize / 24);
			int xsize = (int) (roomSize / 2.8);
			int ysize = (int) (roomSize / 2.0);
			ob = new JDGraphicObject(new JDImageAWT(ImageManager.traderImage,
					xpos, ypos, xsize, ysize), s,
					getShrineRect(xcoord, ycoord), Color.yellow);

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
			ob = new JDGraphicObject(new JDImageAWT(im, xpos, ypos, xsize, ysize),
					s, getShrineRect(xcoord, ycoord), Color.yellow);
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
			ob = new JDGraphicObject(new JDImageAWT(im, xpos, ypos, xsize, ysize),
					s, getShrineRect(xcoord, ycoord), Color.yellow);
		} else if (s.getShrineIndex() == Shrine.SHRINE_QUEST) {
			int xpos = xcoord + (13 * roomSize / 20);
			int ypos = ycoord + (1 * roomSize / 36);
			int xsize = (int) (roomSize / 2.9);
			int ysize = (int) (roomSize / 2.2);
			JDImageProxy<?> im = ImageManager.shrine_blackImage;

			ob = new JDGraphicObject(new JDImageAWT(im, xpos, ypos, xsize, ysize),
					s, getShrineRect(xcoord, ycoord), Color.yellow);
		} else if (s.getShrineIndex() == Shrine.SHRINE_XMAS) {
			int xpos = xcoord + (27 * roomSize / 60);
			int ypos = ycoord - (1 * ROOMSIZE_BY_10);
			int xsize = (int) (roomSize / 1.7);
			int ysize = (int) (roomSize / 1.7);
			JDImageProxy<?> im = ImageManager.xmasImage;

			ob = new JDGraphicObject(new JDImageAWT(im, xpos, ypos, xsize, ysize),
					s, getShrineRect(xcoord, ycoord), Color.yellow);

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
			ob = new JDGraphicObject(new JDImageAWT(im, xpos, ypos, xsize, ysize),
					s, getShrineRect(xcoord, ycoord), Color.yellow);

		} else if (s.getShrineIndex() == Shrine.SHRINE_DARK_MASTER) {
			int xpos = xcoord + (7 * ROOMSIZE_BY_10);
			int ypos = ycoord + (3 * ROOMSIZE_BY_16);
			int xsize = (int) (roomSize / 3.6);
			int ysize = (int) (roomSize / 3.7);
			JDImageProxy<?> im = ImageManager.pentagrammImage;
			ob = new JDGraphicObject(new JDImageAWT(im, xpos, ypos, xsize, ysize),
					s, getShrineRect(xcoord, ycoord), Color.yellow);
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

			ob = new JDGraphicObject(new JDImageAWT(im, xpos, ypos, xsize, ysize),
					s, getShrineRect(xcoord, ycoord), Color.yellow);
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
	
	private JDGraphicObject getHeroGraphicObject(int x, int y, HeroInfo info, GraphicObjectRenderer renderer) {
		Point p = renderer.getPositionCoordModified(info.getPositionInRoomIndex());
		int xpos = (int) p.getX();
		int ypos = (int) p.getY();
		int xSize = (int) (roomSize / HERO_SIZE_QUOTIENT_X);
		int ySize = (int) (roomSize / HERO_SIZE_QUOTIENT_Y);

		JDRectangle rect = new JDRectangle(x + xpos - (xSize / 2), y
				+ ypos - (ySize / 2), xSize, ySize);


		int code = info.getHeroCode();
		int dir = info.getLookDir();
		if (dir == 0) {
			dir = Dir.NORTH;
		}
		JDImageAWT im = null;
		if (code == Hero.HEROCODE_WARRIOR) {
			if (info.isDead().booleanValue()
					/*&& !gui.currentAnimationThreadRunning(info.getRoomInfo())*/) {
				im = new JDImageAWT(ImageManager.warrior_tipping_over.get(dir - 1)
						.getImages()[ImageManager.warrior_tipping_over.get(
						dir - 1).getLength() - 1], rect);
			} else {
				im = new JDImageAWT(ImageManager.warriorImage[dir - 1], rect);
			}
		} else if (code == Hero.HEROCODE_HUNTER) {
			if (info.isDead().booleanValue()
					/*&& !gui.currentAnimationThreadRunning(info.getRoomInfo())*/) {
				im = new JDImageAWT(ImageManager.thief_tipping_over.get(dir - 1)
						.getImages()[ImageManager.thief_tipping_over.get(
						dir - 1).getLength() - 1], rect);
			} else {
				im = new JDImageAWT(ImageManager.thiefImage[dir - 1], rect);
			}
		} else if (code == Hero.HEROCODE_DRUID) {
			if (info.isDead().booleanValue()
					/*&& !gui.currentAnimationThreadRunning(info.getRoomInfo())*/) {
				im = new JDImageAWT(ImageManager.druid_tipping_over.get(dir - 1)
						.getImages()[ImageManager.druid_tipping_over.get(
						dir - 1).getLength() - 1], rect);
			} else {
				im = new JDImageAWT(ImageManager.druidImage[dir - 1], rect);
			}
		} else if (code == Hero.HEROCODE_MAGE) {
			if (info.isDead().booleanValue()
					/*&& !gui.currentAnimationThreadRunning(info.getRoomInfo())*/) {
				im = new JDImageAWT(ImageManager.mage_tipping_over.get(dir - 1)
						.getImages()[ImageManager.mage_tipping_over
						.get(dir - 1).getLength() - 1], rect);
			} else {
				im = new JDImageAWT(ImageManager.mageImage[dir - 1], rect);
			}
		}
		return new JDGraphicObject(im, info, rect, Color.white,
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

		Color bg = null;

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
							new JDImageAWT(ImageManager.questionmark, xpos,
									ypos, xsize, ysize), null, new JDRectangle(
									new JDPoint(xpos, ypos), xsize, ysize),
							Color.yellow);
					items.add(ob);
				}
			} else if (status > RoomObservationStatus.VISIBILITY_SHRINE) {

				List<GraphicObject> roomDoors = renderer.drawDoors(r, xcoord,
						ycoord);
				this.doors.addAll(roomDoors);

			} else {

				bg = Color.darkGray;
				return new GraphicObject(r, new Rectangle(new Point(xcoord
						+ getDoorDimension(true, roomSize).width, ycoord
						+ getDoorDimension(true, roomSize).width), new Dimension(roomSize
						- 2 * getDoorDimension(true, roomSize).width, roomSize - 2
						* getDoorDimension(true, roomSize).width)), bg, false, null);

			}
		}
		return new GraphicObject(r, new Rectangle(new Point(xcoord, ycoord),
				new Dimension(roomSize, roomSize
						- 1
						* GraphicObjectRenderer
								.getDoorDimension(true, roomSize).width)), bg,
				false, im);
	}
	
	private GraphicObject drawWall(int xcoord, int ycoord, RoomInfo r) {
		Color bg = null;

		GraphicObject ob = null;
		RoomInfo southRoom = r.getNeighbourRoom(RouteInstruction.SOUTH);
		if (southRoom != null) {
			if (r.getVisibilityStatus() >= 2
					&& southRoom.getVisibilityStatus() < RoomObservationStatus.VISIBILITY_FOUND
					&& r.isClaimed()) {
				lastWalls
						.add(new GraphicObject(null, new Rectangle(new Point(
								xcoord, ycoord
										+ roomSize
										- GraphicObjectRenderer
												.getDoorDimension(true,
														roomSize).width),
								new Dimension(roomSize, roomSize)), bg,
								ImageManager.wall_southImage));

				DoorInfo[] doorArray = r.getDoors();
				JDRectangle rect = GraphicObjectRenderer.getSouthDoorRect(
						xcoord, ycoord, roomSize);
				if (doorArray[2] != null) {
					if (doorArray[2].hasLock().booleanValue()) {
						doors.add(new JDGraphicObject(new JDImageAWT(
								ImageManager.door_south_lock, xcoord, ycoord
										+ roomSize
										- getDoorDimension(true,roomSize).width,
								roomSize, roomSize), doorArray[2], rect,
								new Color(180, 150, 80), false));
					} else {
						doors.add(new JDGraphicObject(new JDImageAWT(
								ImageManager.door_south, xcoord, ycoord
										+ roomSize
										- getDoorDimension(true,roomSize).width,
								roomSize, roomSize), doorArray[2], rect,
								new Color(180, 150, 80), false));
					}
				} else {
					doors.add(new JDGraphicObject(new JDImageAWT(
							ImageManager.door_south_none, xcoord, ycoord
									+ roomSize - getDoorDimension(true,roomSize).width,
							roomSize, roomSize), doorArray[2], rect, new Color(
							180, 150, 80), false));
				}
			}
		}

		int status = r.getVisibilityStatus();
		if (status >= RoomObservationStatus.VISIBILITY_FOUND) {
			if (memory) {
				bg = Color.darkGray;
				ob = new GraphicObject(r, new Rectangle(new Point(xcoord,
						ycoord - getDoorDimension(true,roomSize).width), new Dimension(
						roomSize, roomSize)), bg, ImageManager.wall_northImage);
				lastWalls.add(new GraphicObject(r, new Rectangle(new Point(
						xcoord, ycoord - getDoorDimension(true,roomSize).width),
						new Dimension(roomSize, roomSize)), bg,
						ImageManager.wall_sidesImage));
			} else {
				lastWalls.add(new GraphicObject(r, new Rectangle(new Point(
						xcoord, ycoord - getDoorDimension(true,roomSize).width),
						new Dimension(roomSize, roomSize)), bg,
						ImageManager.wall_sidesImage));
				ob = new GraphicObject(r, new Rectangle(new Point(xcoord,
						ycoord - getDoorDimension(true,roomSize).width), new Dimension(
						roomSize, roomSize)), bg, ImageManager.wall_northImage);
			}

		}

		return ob;
	}
	
	private Dimension getSpotDimension() {
		return new Dimension(roomSize / 7, roomSize / 7);
	}

	private Dimension getChestDimension() {
		return new Dimension(roomSize / 5, roomSize / 5);
	}

	
	private Point getChestPoint(int xcoord, int ycoord) {
		int x1 = xcoord + (roomSize / 8);
		int y1 = ycoord + roomSize / 10;

		return new Point(x1, y1);
	}
	
	public List<GraphicObject> createGraphicObjectsForRoom(RoomInfo r,
			Object obj, int xcoord, int ycoord,
			List aniObs) {
		List<GraphicObject> graphObs = new LinkedList<GraphicObject>();

		GraphicObject roomOb = drawBackGround(xcoord, ycoord, r, this);

		GraphicObject wallOb = drawWall(xcoord, ycoord, r);
		graphObs.add(roomOb);

		graphObs.addAll(drawDoors(r, xcoord, ycoord));

		if (wallOb != null) {
			graphObs.add(wallOb);
		}

		int status = r.getVisibilityStatus();

		if ((status >= RoomObservationStatus.VISIBILITY_SHRINE)
				) {

			if (r.equals(gui.getFigure().getRoomInfo())) {
				Point[] positionCoord = getPositionCoord();
				for (int i = 0; i < positionCoord.length; i++) {
					int posSize = RoomSize.by8(roomSize);

					GraphicObject ob = new GraphicObject(
							r.getPositionInRoom(i), new Rectangle(
									getPositionCoord(i).x,
									getPositionCoord(i).y, posSize,
									posSize), Color.BLACK,
							ImageManager.fieldImage);

					graphObs.add(ob);

				}
			}

			GraphicObject ob = null;
			if (r.getShrine() != null) {
				ShrineInfo s = r.getShrine();
				ob = getShrineGraphicObject(s, xcoord, ycoord);
				graphObs.add(ob);
			}
			
			if (r.getChest() != null) {
				// game.newStatement("chest vorhanden!", 1);
				GraphicObject chestOb;
				if (r.getChest().hasLock()) {
					chestOb = new GraphicObject(r.getChest(),
							new Rectangle(getChestPoint(xcoord, ycoord),
									getChestDimension()),
							new Color(140, 90, 20),
							ImageManager.chest_lockImage);
				} else {
					chestOb = new GraphicObject(r.getChest(),
							new Rectangle(getChestPoint(xcoord, ycoord),
									getChestDimension()),
							new Color(140, 90, 20), ImageManager.chestImage);
				}
				graphObs.add(chestOb);
			}
		}
		if ((status >= RoomObservationStatus.VISIBILITY_FIGURES)) {

			if (r.getMonsterInfos().size() > 0) {
				GraphicObject[] monsterObs = drawMonster(xcoord,
						ycoord, r.getMonsterInfos());
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

			GraphicObject[] itObs = GraphicObjectRenderer.drawItems(xcoord,
					ycoord, r.getItemArray(), roomSize);
			for (int i = 0; i < itObs.length; i++) {
				GraphicObject o = itObs[i];
				if (o != null) {
					graphObs.add(o);
				}
			}

		}

		if ((r.getSpot() != null) && (r.getSpot().isFound())) {
			GraphicObject spotOb = new GraphicObject(r.getSpot(),
					new Rectangle(getSpotPoint(xcoord, ycoord),
							getSpotDimension()), new Color(0, 0, 0),
					ImageManager.spotImage);
			graphObs.add(spotOb);
		}

		if (r.getHeroInfo() != null) {
			drawHero(xcoord, ycoord, r.getHeroInfo(), this);
		}

		graphObs.add(new GraphicObject(r, new Rectangle(new Point(xcoord,
				ycoord - getDoorDimension(true, roomSize).width), new Dimension(roomSize,
				roomSize)), Color.darkGray, ImageManager.wall_sidesImage));
		return graphObs;
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
							new Rectangle(getChestPoint(xcoord, ycoord),
									getChestDimension()),
							new Color(140, 90, 20),
							ImageManager.chest_lockImage);
				} else {
					chestOb = new GraphicObject(r.getChest(),
							new Rectangle(getChestPoint(xcoord, ycoord),
									getChestDimension()),
							new Color(140, 90, 20), ImageManager.chestImage);
				}
				chests.add(chestOb);
			}
		}
		if ((status >= RoomObservationStatus.VISIBILITY_FIGURES)) {
			Point[] positionCoord = getPositionCoord();
			if (r.equals(gui.getFigure().getRoomInfo())) {
				for (int i = 0; i < positionCoord.length; i++) {
					int posSize = RoomSize.by8(roomSize);

					JDImageProxy<?> im = ImageManager.fieldImage;
					GraphicObject ob = new GraphicObject(
							r.getPositionInRoom(i), new Rectangle(xcoord
									+ getPositionCoord(i).x, ycoord
									+ getPositionCoord(i).y, posSize,
									posSize), Color.BLACK, im);

					positions.add(ob);

				}
			}
			List<MonsterInfo> monsters = r.getMonsterInfos();
			if (monsters != null && monsters.size() > 0) {
				GraphicObject[] monsterObs = drawMonster(xcoord,
						ycoord, r.getMonsterInfos());
				for (int i = 0; i < monsterObs.length; i++) {
					if (monsterObs[i] != null) {
						monster.add(monsterObs[i]);
					}
				}
			}
		}
		if ((status >= RoomObservationStatus.VISIBILITY_ITEMS) || (visCheat)) {

			GraphicObject[] itObs = GraphicObjectRenderer.drawItems(xcoord,
					ycoord, r.getItemArray(), roomSize);
			for (int i = 0; i < itObs.length; i++) {
				GraphicObject o = itObs[i];
				if (o != null) {
					items.add(o);
				}
			}

		}

		if ((r.getSpot() != null) && (r.getSpot().isFound())) {
			GraphicObject spotOb = new GraphicObject(r.getSpot(),
					new Rectangle(getSpotPoint(xcoord, ycoord),
							getSpotDimension()), new Color(0, 0, 0),
					ImageManager.spotImage);
			spots.add(spotOb);
		}

		if (r.getHeroInfo() != null) {
			drawHero(xcoord, ycoord, r.getHeroInfo(), this);
		}
	}
	
	private Point getSpotPoint(int xcoord, int ycoord) {
		int x1 = xcoord + (1 * roomSize / 8);
		int y1 = ycoord + (6 * roomSize / 8);

		return new Point(x1, y1);
	}

	private void drawHero(int x, int y, HeroInfo info,
			GraphicObjectRenderer renderer) {
		if (info == null) {
			return;
		}
		hero = renderer.getHeroGraphicObject(x, y, info, renderer);
	}
	
}
