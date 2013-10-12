package gui.engine2D;

import figure.FigureInfo;
import figure.RoomObservationStatus;
import figure.hero.Hero;
import figure.hero.HeroInfo;
import figure.monster.Monster;
import figure.monster.MonsterInfo;
import graphics.ImageManager;
import graphics.JDImageProxy;
import gui.MyJDGui;
import gui.JDJPanel;
import gui.Paragraph;
import gui.Paragraphable;
import gui.mainframe.MainFrame;
import gui.mainframe.component.BoardView;
import io.ResourceLoader;
import item.AttrPotion;
import item.DustItem;
import item.Item;
import item.Key;
import item.ItemInfo;
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

import java.applet.Applet;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.Scrollable;









import animation.AnimationSet;
import animation.AnimationUtils;
import control.ActionAssembler;
//import shrine.Brood;
import shrine.Angel;
import shrine.Brood;
import shrine.Corpse;
import shrine.DarkMasterShrine;
import shrine.HealthFountain;
import shrine.Luzia;
import shrine.QuestShrine;
import shrine.RepairShrine;
import shrine.RuneFinder;
import shrine.RuneShrine;
import shrine.Shrine;
import shrine.SorcerLab;
import shrine.Statue;
import shrine.Trader;
import shrine.ShrineInfo;
import shrine.Xmas;
import dungeon.ChestInfo;
import dungeon.Dir;
import dungeon.DoorInfo;
import dungeon.HiddenSpot;
import dungeon.JDPoint;
import dungeon.Position;
import dungeon.PositionInRoomInfo;
import dungeon.Room;
import dungeon.RoomInfo;
import dungeon.RouteInstruction;

//import java.net.*;

/**
 * @author Duke1
 * 
 *         To change this generated comment edit the template variable
 *         "typecomment": Window>Preferences>Java>Templates. To enable and
 *         disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class GraphBoard extends JDJPanel implements MouseListener,
		MouseMotionListener, Scrollable, Runnable {

	Graphics g2;

	Point[] positionCoord = new Point[8];

	Map roomObjectMap = new HashMap();

	int screenSize = 14000;

	public static final int DEFAULT_ROOM_SIZE = 180;
	private int roomSize = 180;

	int offset = 500;

	int mouse_x = 0;

	int mouse_y = 0;

	Point heroPoint;

	boolean memory = false;

	boolean writeOut = false;

	Vector<GraphicObject> rooms = new Vector<GraphicObject>();

	Vector<GraphicObject> shrines = new Vector<GraphicObject>();

	Vector<GraphicObject> positions = new Vector<GraphicObject>();

	Vector<GraphicObject> monster = new Vector<GraphicObject>();

	Vector<GraphicObject> items = new Vector<GraphicObject>();

	Vector<GraphicObject> doors = new Vector<GraphicObject>();

	Vector<GraphicObject> chests = new Vector<GraphicObject>();

	Vector<GraphicObject> spots = new Vector<GraphicObject>();

	Vector<GraphicObject> walls = new Vector<GraphicObject>();

	Vector<GraphicObject> lastWalls = new Vector<GraphicObject>();

	GraphicObject hero;

	Color shrineBase = new Color(190, 190, 190);

	ResourceLoader rl;

	Cursor cursor1;

	Cursor cursor2;

	Cursor cursor3;

	Cursor cursor4;

	Cursor cursor5;

	Cursor cursor6;

	Cursor cursor7;

	Cursor cursor_boots;

	Cursor cursor_wand;
	Cursor cursor_boots_not;

	Cursor cursor_use;

	int[] fastAnimTimes9 = { 45, 45, 45, 45, 45, 45, 45, 45, 45 };

	int[] fastAnimTimes7 = { 45, 45, 45, 45, 45, 45, 45 };

	int[] slowAnimTimes9 = { 80, 80, 80, 80, 80, 80, 80, 80, 80 };

	ActionAssembler control;
	
	private Cursor createCustomCursor(JDImageProxy image, Point p, String label) {
		 Toolkit toolkit = this.getToolkit();
		return toolkit.createCustomCursor((Image)image.getImage(),p,label);
	}

	public GraphBoard(Applet a, MyJDGui gui) {
		super(gui);
		this.control = gui.getControl();

		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		cursor1 = createCustomCursor(
				ImageManager.hand_zeigt1_Image, new Point(1, 6), "hand1");
		cursor2 = createCustomCursor(
				ImageManager.hand_greift1_Image, new Point(1, 8), "hand2");
		cursor3 = createCustomCursor(
				ImageManager.cursor_key_Image, new Point(1, 8), "key");
		cursor4 = createCustomCursor(
				ImageManager.cursor_key_not_Image, new Point(1, 8), "key_not");

		cursor5 = createCustomCursor(
				ImageManager.cursor_sword, new Point(1, 8), "sword");
		cursor6 = createCustomCursor(
				ImageManager.cursor_clock, new Point(4, 8), "clock");
		cursor7 = createCustomCursor(
				ImageManager.cursor_scout, new Point(4, 8), "scout");
		cursor_boots = createCustomCursor(
				ImageManager.cursor_go_Image, new Point(4, 8), "go");

		cursor_boots_not = createCustomCursor(
				ImageManager.cursor_go_not_Image, new Point(4, 8), "go_not");
		cursor_wand = createCustomCursor(
				ImageManager.cursor_wand, new Point(1, 6), "wand");
		cursor_use = createCustomCursor(
				ImageManager.cursor_use_Image, new Point(4, 8), "go_not");
		sizeChanged();
		this.setPreferredSize(new Dimension(screenSize, screenSize));

	}

	private void sizeChanged() {

		// ROOMSIZE_BY_10 = roomSize / 10;
		ROOMSIZE_BY_36 = roomSize / 36;
		ROOMSIZE_BY_24 = roomSize / 24;
		ROOMSIZE_BY_20 = roomSize / 20;
		ROOMSIZE_BY_12 = roomSize / 12;
		ROOMSIZE_BY_10 = roomSize / 10;
		ROOMSIZE_BY_16 = roomSize / 16;
		ROOMSIZE_BY_8 = roomSize / 8;
		ROOMSIZE_BY_6 = roomSize / 6;
		ROOMSIZE_BY_5 = roomSize / 5;
		ROOMSIZE_BY_4 = roomSize / 4;
		ROOMSIZE_BY_3 = roomSize / 3;
		ROOMSIZE_BY_2 = roomSize / 2;

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

	public void update(Graphics g) {
		System.out.println("leeres update!!!");
		// paint(g);
	}

	public void run() {

	}



	public void repaintRoom(Graphics g, RoomInfo r, boolean heroToo) {

		repaintRoomSmall(g, r, new LinkedList());
	}

	public static void printList(LinkedList l) {
		System.out.println("List: ");
		for (Iterator iter = l.iterator(); iter.hasNext();) {
			Object element = (Object) iter.next();
			System.out.println(element.toString());

		}
		System.out.println();
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

	public void repaintRoomSmall(Graphics g, RoomInfo r, Object obj) {


		int xcoord = 0;
		int ycoord = 0;

		List aniObs = new LinkedList();
		if (obj instanceof LinkedList) {
			aniObs = (LinkedList) obj;
			if (aniObs.size() > 1) {
				int i = 0;
				i++;
			}
		}
		List<GraphicObject> graphObs = new LinkedList<GraphicObject>();

		GraphicObject roomOb = drawBackGround(xcoord, ycoord, r, g);

		GraphicObject wallOb = drawWall(xcoord, ycoord, r, g);
		graphObs.add(roomOb);

		graphObs.addAll(drawDoors(r, xcoord, ycoord, g));

		if (wallOb != null) {
			graphObs.add(wallOb);
		}

		int status = r.getVisibilityStatus();

		if ((status >= RoomObservationStatus.VISIBILITY_SHRINE)
				|| (gui.getVisibility())) {

			if (r.equals(gui.getFigure().getRoomInfo())) {
				for (int i = 0; i < positionCoord.length; i++) {
					int posSize = ROOMSIZE_BY_8;

					GraphicObject ob = new GraphicObject(
							r.getPositionInRoom(i), new Rectangle(
									getPositionCoord(i).x,
									getPositionCoord(i).y, posSize, posSize),
							Color.BLACK, ImageManager.fieldImage);

					graphObs.add(ob);

				}
			}

			GraphicObject ob = null;
			if (r.getShrine() != null) {
				ShrineInfo s = r.getShrine();
				ob = this.getShrineGraphicObject(s, xcoord, ycoord);
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
			// question_mark = true;
		}
		if ((status >= RoomObservationStatus.VISIBILITY_FIGURES)
				|| (gui.getVisibility())) {

			// question_mark = false;
			if (r.getMonsterInfos().size() > 0) {
				// writeOut("Monster im Raum: " + r.getDieMonster().size());
				GraphicObject[] monsterObs = drawMonster(xcoord, ycoord,
						r.getMonsterInfos(), g);
				for (int i = 0; i < monsterObs.length; i++) {
					if (monsterObs[i] != null) {
						// System.out.println("monster:
						// "+monsterObs[i].getClickedObject());
						// printList(aniObs);
						// boolean contains =
						// contains(aniObs,(FigureInfo)monsterObs[i].getClickedObject());
						boolean contains = aniObs.contains(monsterObs[i]
								.getClickedObject());
						if (!contains) {
							if (!(monsterObs[i].getClickedObject().equals(obj))) {
								// System.out.println("male in small:
								// "+monsterObs[i].getClickedObject());
								graphObs.add(monsterObs[i]);
							}
						}
					}
				}
			}
		}
		if ((status >= RoomObservationStatus.VISIBILITY_ITEMS)
				|| (gui.getVisibility())) {

			GraphicObject[] itObs = drawItems(xcoord, ycoord, r.getItemArray(),
					g);
			for (int i = 0; i < itObs.length; i++) {
				GraphicObject o = itObs[i];
				if (o != null) {
					// ////System.out.println("adding graph-Ob");
					graphObs.add(o);
				}
			}

		}

		// if(question_mark && (status == room.SHRINE)) {

		// }

		if ((r.getSpot() != null) && (r.getSpot().isFound())) {
			GraphicObject spotOb = new GraphicObject(r.getSpot(),
					new Rectangle(getSpotPoint(xcoord, ycoord),
							getSpotDimension()), new Color(0, 0, 0),
							ImageManager.spotImage);
			graphObs.add(spotOb);
		}

		if (r.getHeroInfo() != null) {
			// System.out.println("drawHero2");
			drawHero(g, xcoord, ycoord, r.getHeroInfo());

		}

		graphObs.add(new GraphicObject(r, new Rectangle(new Point(xcoord,
				ycoord - getDoorDimension(true).width), new Dimension(roomSize,
				roomSize)), Color.darkGray, ImageManager.wall_sidesImage));

		for (int i = 0; i < graphObs.size(); i++) {
			GraphicObject o = ((GraphicObject) graphObs.get(i));
			if (o != null) {

				o.fill(g);
			}

		}

		FigureInfo hInfo = gui.getFigure();
		boolean inRoom = hInfo.getRoomNumber().equals(r.getNumber());
		boolean paint = !aniObs.contains(hInfo);
		// System.out.println("paintHero: "+paint);
		if (paint && inRoom) {

			if (hero != null && !hInfo.equals(obj)) {
				hero.fill(g);
			}
		}
	}

	private Image getImage(JDImageProxy im) {
		return (Image)im.getImage();
	}

	private boolean contains(LinkedList l, FigureInfo f) {
		for (Iterator iter = l.iterator(); iter.hasNext();) {
			FigureInfo element = (FigureInfo) iter.next();
			if (element.equals(f)) {
				// System.out.println("contains!");
				return true;
			}

		}
		// System.out.println("contains false;");
		return false;
	}

	public void paint(Graphics g) {
		// System.out.println("PAINT "+Math.random());
		g2 = g;

		rooms = new Vector();
		shrines = new Vector();
		monster = new Vector();
		items = new Vector();
		doors = new Vector();
		chests = new Vector();
		spots = new Vector();
		walls = new Vector();
		lastWalls = new Vector();
		positions = new Vector();

		// blank putzen
		g2.setColor(Color.black);
		g2.fillRect(0, 0, (int) getSize().getHeight(), (int) getSize()
				.getWidth());

		JDPoint p = gui.getFigure().getDungeonSize();
		int xrooms = p.getX();
		int yrooms = p.getY();
		BoardView view = gui.getMainFrame().getSpielfeld();

		for (int i = 0; i < xrooms; i++) {
			for (int j = 0; j < yrooms; j++) {

				RoomInfo r = null;
				if (memory) {

					// r = gui.getFigure().getMemory(i, j);

				} else {
					r = gui.getFigure().getRoomInfo(i, j);
				}

				int xcoord = offset + (roomSize * i);
				int ycoord = offset + (roomSize * j);
				Point portPoint = gui.getMainFrame().getSpielfeld()
						.getViewport().getViewPosition();
				Rectangle rect = new Rectangle(portPoint.x - roomSize,
						portPoint.y - roomSize, 760 + roomSize, 525 + roomSize);
				if (!rect.contains(xcoord, ycoord)) {
					continue;
				}

				g2.setColor(Color.darkGray);
				g2.fillRect(xcoord, ycoord, roomSize, roomSize);
				if (r != null) {

					drawRoom(xcoord, ycoord, r, g2);

				} else {
					rooms.add(new GraphicObject(
							new Point(i, j),
							new Rectangle(
									new Point(xcoord/*
													 * +
													 * getDoorDimension(true).width
													 */, ycoord /*
																 * +
																 * getDoorDimension
																 * (
																 * false).height
																 */),
									new Dimension(
											roomSize /*- 2
														 * getDoorDimension(true).width*/,
											roomSize
									/*- 2 * getDoorDimension(false).height*/)),
							Color.black, false, null));
				}

			}
		}

		for (int i = 0; i < rooms.size(); i++) {
			((GraphicObject) rooms.get(i)).fill(g2);
		}
		for (int i = 0; i < walls.size(); i++) {

			((GraphicObject) walls.get(i)).fill(g2);
		}
		for (int i = 0; i < doors.size(); i++) {
			Object o = doors.get(i);

			((GraphicObject) o).fill(g2);
		}

		for (int i = 0; i < positions.size(); i++) {
			((GraphicObject) positions.get(i)).fill(g2);
		}
		for (int i = 0; i < spots.size(); i++) {
			((GraphicObject) spots.get(i)).fill(g2);
		}
		for (int i = 0; i < shrines.size(); i++) {
			((GraphicObject) shrines.get(i)).fill(g2);
		}

		for (int i = 0; i < items.size(); i++) {
			ItemInfo a = (ItemInfo) ((GraphicObject) items.get(i))
					.getClickedObject();
			// game.newStatement("filling: "+a.toString(),1);
			((GraphicObject) items.get(i)).fill(g2);

		}
		for (int i = 0; i < chests.size(); i++) {
			// game.newStatement("drawing chest!", 2);
			((GraphicObject) chests.get(i)).fill(g2);
		}
		for (int i = 0; i < monster.size(); i++) {
			GraphicObject o = ((GraphicObject) monster.get(i));
			if (o != null) {
				o.fill(g2);
			}

		}

		for (int i = 0; i < lastWalls.size(); i++) {

			((GraphicObject) lastWalls.get(i)).fill(g2);
		}

		boolean animationRunning = gui.currentThreadRunning(gui.getFigure()
				.getRoomInfo());

		if (!animationRunning) {
			if (hero != null) {
				// System.out.println("fill hero");
				hero.fill(g2);

			}
		}

	}

	// private void drawTestTracker(Graphics g) {
	//
	// LinkedList way = game.getTracker().getWay();
	// for (int i = 0; i < way.size(); i++) {
	// Room r = ((Room) way.get(i));
	// if (r != null) {
	//
	// g.setColor(new Color(255 - 5 * i, 255, 255));
	// int xcoord = offset + (roomSize * r.getNumber().getX());
	// int ycoord = offset + (roomSize * r.getNumber().getY());
	// g.fillRect(xcoord + 20 + (getNumber(r, i, way) % 4) * 20,
	// ycoord + (((int) getNumber(r, i, way) / 4) * 30) + 20,
	// 15, 15);
	// g.setColor(Color.black);
	// g.drawString(Integer.toString(i), xcoord + 20
	// + ((getNumber(r, i, way) % 4) * 20), (((int) getNumber(
	// r, i, way) / 4) * 30)
	// + ycoord + 32);
	// //
	// r.d.game.getGui().getMainFrame().getSpielfeld().setViewPoint(xcoord-100,ycoord-100);
	// } else {
	// // System.out.println("TRACKER RAUM IST NULL!");
	// }
	// }
	// }

	private int getNumber(Room r, int k, LinkedList w) {
		int number = 1;
		for (int i = 0; i < k; i++) {
			Room r2 = ((Room) w.get(i));
			if (r == r2) {
				number++;
			}
		}
		return number;
	}

	private GraphicObject drawBackGround(int xcoord, int ycoord, RoomInfo r,
			Graphics g) {

		Color bg = null;

		int status = r.getVisibilityStatus();
		JDImageProxy im = ImageManager.floorImageArray[r.getFloorIndex()];
		if (memory) {
			List<GraphicObject> roomDoors = drawDoors(r, xcoord, ycoord, g);
			im = ImageManager.floorImage_darkArray[r.getFloorIndex()];
			this.doors.addAll(roomDoors);
		} else {
			if (status == RoomObservationStatus.VISIBILITY_FOUND) {
				im = ImageManager.floorImage_mediumArray[r.getFloorIndex()];
				List<GraphicObject> roomDoors = drawDoors(r, xcoord, ycoord, g);
				this.doors.addAll(roomDoors);

			} else if (status == RoomObservationStatus.VISIBILITY_SHRINE) {
				List<GraphicObject> roomDoors = drawDoors(r, xcoord, ycoord, g);
				im = ImageManager.floorImage_mediumArray[r.getFloorIndex()];
				this.doors.addAll(roomDoors);
				if (r.isPart_scouted()) {

					int xpos = xcoord + (1 * roomSize / 3);
					int ypos = ycoord + (1 * roomSize / 2) - (roomSize / 5);
					int xsize = roomSize / 3;
					int ysize = (int) (roomSize / 2.5);
					GraphicObject ob = new JDGraphicObject(
							new JDImageAWT(ImageManager.questionmark, xpos, ypos,
									xsize, ysize), null, new Rectangle(
									new Point(xpos, ypos), new Dimension(xsize,
											ysize)), Color.yellow);
					items.add(ob);
				}
				//
			} else if (status > RoomObservationStatus.VISIBILITY_SHRINE) {

				List<GraphicObject> roomDoors = drawDoors(r, xcoord, ycoord, g);
				this.doors.addAll(roomDoors);

				// im = floorImageArray[r.getFloorIndex()];

			} else {

				bg = Color.darkGray;
				return new GraphicObject(r, new Rectangle(new Point(xcoord
						+ getDoorDimension(true).width, ycoord
						+ getDoorDimension(true).width), new Dimension(roomSize
						- 2 * getDoorDimension(true).width, roomSize - 2
						* getDoorDimension(true).width)), bg, false, null);

			}
		}
		return new GraphicObject(r, new Rectangle(

		new Point(xcoord /* + getDoorDimension(true).width */, ycoord
		/* + getDoorDimension(true).width */), new Dimension(
				roomSize /*- 1
							 * getDoorDimension(true).width*/, roomSize - 1
						* getDoorDimension(true).width)), bg, false, im);
	}

	private List<GraphicObject> drawDoors(RoomInfo r, int xcoord, int ycoord, Graphics g) {
		DoorInfo[] doors = r.getDoors();
		if (doors == null) {
			return new LinkedList<GraphicObject>();
		}
		List<GraphicObject> roomDoors = new LinkedList<GraphicObject>();
		if (doors[0] != null) {
			JDGraphicObject door0;
			if (!doors[0].hasLock().booleanValue()) {
				Rectangle rect = getNorthDoorRect(xcoord, ycoord);
				door0 = new JDGraphicObject(new JDImageAWT(
						ImageManager.door_north, xcoord, ycoord
								- getDoorDimension(true).width, roomSize,
						roomSize), doors[0], rect, new Color(180, 150, 80),
						false);
			} else {
				Rectangle rect = getNorthDoorRect(xcoord, ycoord);
				door0 = new JDGraphicObject(new JDImageAWT(
						ImageManager.door_north_lock, xcoord, ycoord
								- getDoorDimension(true).width, roomSize,
						roomSize), doors[0], rect, new Color(180, 150, 80),
						false);
			}
			roomDoors.add(door0);
		} else {
			Rectangle rect = getNorthDoorRect(xcoord, ycoord);
			JDGraphicObject door0 = new JDGraphicObject(
					new JDImageAWT(ImageManager.door_north_none, xcoord, ycoord
							- getDoorDimension(true).width, roomSize, roomSize),
					doors[0], null, new Color(180, 150, 80), false);
			roomDoors.add(door0);
		}
		if (doors[1] != null) {
			Rectangle rect = getEastDoorRect(xcoord, ycoord);
			JDGraphicObject door1;
			if (!doors[1].hasLock().booleanValue()) {
				door1 = new JDGraphicObject(new JDImageAWT(ImageManager.door_east,
						xcoord, ycoord - getDoorDimension(true).width,
						roomSize, roomSize), doors[1], rect, new Color(180,
						150, 80), false);
			} else {
				door1 = new JDGraphicObject(new JDImageAWT(
						ImageManager.door_east_lock, xcoord, ycoord
								- getDoorDimension(true).width, roomSize,
						roomSize), doors[1], rect, new Color(180, 150, 80),
						false);
			}
			roomDoors.add(door1);
		} else {
			Rectangle rect = getNorthDoorRect(xcoord, ycoord);
			JDGraphicObject door1 = new JDGraphicObject(
					new JDImageAWT(ImageManager.door_east_none, xcoord, ycoord
							- getDoorDimension(true).width, roomSize, roomSize),
					doors[1], null, new Color(180, 150, 80), false);
			roomDoors.add(door1);
		}
		if (doors[2] != null) {
			// wird eventuell bei drawWall() gemacht

			// Rectangle rect = getSouthDoorRect(xcoord, ycoord);
			// graphicObject door2 =
			// new graphicObject(
			// doors[2],
			// rect,
			// new Color(180, 150, 80),
			// false,
			// null,
			// game);
			// this.doors.add(door2);
		}
		if (doors[3] != null) {
			Rectangle rect = getWestDoorRect(xcoord, ycoord);
			if (rect == null) {
				System.out.println("rect ist null");
				System.exit(0);
			}
			JDGraphicObject door3;

			if (!doors[3].hasLock().booleanValue()) {
				door3 = new JDGraphicObject(new JDImageAWT(ImageManager.door_west,
						xcoord, ycoord - getDoorDimension(true).width,
						roomSize, roomSize), doors[3], rect, new Color(180,
						150, 80), false);
			} else {
				door3 = new JDGraphicObject(new JDImageAWT(
						ImageManager.door_west_lock, xcoord, ycoord
								- getDoorDimension(true).width, roomSize,
						roomSize), doors[3], rect, new Color(180, 150, 80),
						false);
			}
			roomDoors.add(door3);
		} else {
			JDGraphicObject door0 = new JDGraphicObject(
					new JDImageAWT(ImageManager.door_west_none, xcoord, ycoord
							- getDoorDimension(true).width, roomSize, roomSize),
					doors[3], null, new Color(180, 150, 80), false);
			roomDoors.add(door0);
		}

		return roomDoors;
	}

	private GraphicObject drawWall(int xcoord, int ycoord, RoomInfo r,
			Graphics g) {
		Color bg = null;

		GraphicObject ob = null;
		RoomInfo southRoom = r.getNeighbourRoom(RouteInstruction.SOUTH);
		if (southRoom != null) {
			if (r.getVisibilityStatus() >= 2
					&& southRoom.getVisibilityStatus() < RoomObservationStatus.VISIBILITY_FOUND
					&& r.isClaimed()) {
				lastWalls
						.add(new GraphicObject(null, new Rectangle(new Point(
								xcoord, ycoord + roomSize
										- getDoorDimension(true).width),
								new Dimension(roomSize, roomSize)), bg,
								ImageManager.wall_southImage));

				DoorInfo[] doorArray = r.getDoors();
				Rectangle rect = getSouthDoorRect(xcoord, ycoord);
				if (doorArray[2] != null) {
					// Rectangle clickRect = new Rectangle(xcoord)
					if (doorArray[2].hasLock().booleanValue()) {
						doors.add(new JDGraphicObject(new JDImageAWT(
								ImageManager.door_south_lock, xcoord, ycoord
										+ roomSize
										- getDoorDimension(true).width,
								roomSize, roomSize), doorArray[2], rect,
								new Color(180, 150, 80), false));
					} else {
						doors.add(new JDGraphicObject(new JDImageAWT(
								ImageManager.door_south, xcoord, ycoord
										+ roomSize
										- getDoorDimension(true).width,
								roomSize, roomSize), doorArray[2], rect,
								new Color(180, 150, 80), false));
					}
				} else {
					doors.add(new JDGraphicObject(new JDImageAWT(
							ImageManager.door_south_none, xcoord, ycoord
									+ roomSize - getDoorDimension(true).width,
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
						ycoord - getDoorDimension(true).width), new Dimension(
						roomSize, roomSize)), bg, ImageManager.wall_northImage);
				lastWalls.add(new GraphicObject(r, new Rectangle(new Point(
						xcoord, ycoord - getDoorDimension(true).width),
						new Dimension(roomSize, roomSize)), bg,
						ImageManager.wall_sidesImage));
			} else {
				lastWalls.add(new GraphicObject(r, new Rectangle(new Point(
						xcoord, ycoord - getDoorDimension(true).width),
						new Dimension(roomSize, roomSize)), bg,
						ImageManager.wall_sidesImage));
				ob = new GraphicObject(r, new Rectangle(new Point(xcoord,
						ycoord - getDoorDimension(true).width), new Dimension(
						roomSize, roomSize)), bg, ImageManager.wall_northImage);
			}

		} else {
			// ob = new graphicObject(
			// r,
			// new Rectangle(
			// new Point(
			// xcoord + getDoorDimension(true).width,
			// ycoord + getDoorDimension(true).width),
			// new Dimension(
			// roomSize - 2 * getDoorDimension(true).width,
			// roomSize - 2 * getDoorDimension(true).width)),
			// bg,
			// floorImage,
			// game);
		}

		return ob;
	}

	private Dimension getDoorDimension(boolean vertical) {
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

	private Rectangle getNorthDoorRect(int x, int y) {

		Dimension d = getDoorDimension(false);
		return new Rectangle(new Point(
				(int) (x + (roomSize / 2) - (d.getWidth() / 2)), y), d);
	}

	private Rectangle getEastDoorRect(int x, int y) {

		Dimension d = getDoorDimension(true);
		return new Rectangle(new Point((int) (x + (roomSize) - (d.getWidth())),
				(int) (y + (roomSize / 2) - (d.getHeight() / 2))), d);
	}

	private Rectangle getSouthDoorRect(int x, int y) {

		Dimension d = getDoorDimension(false);
		return new Rectangle(new Point(
				(int) (x + (roomSize / 2) - (d.getWidth() / 2)), (int) (y
						+ roomSize - (d.getHeight()))), d);
	}

	private Rectangle getWestDoorRect(int x, int y) {

		Dimension d = getDoorDimension(true);
		return new Rectangle(new Point((int) (x),
				(int) (y + (roomSize / 2) - (d.getHeight() / 2))), d);
	}


	private GraphicObject[] drawMonster(int xcoord, int ycoord,
			List<MonsterInfo> monsterList, Graphics g) {
		int k = monsterList.size();
		GraphicObject obs[] = new GraphicObject[k];
		if (monsterList.size() > 8) {
			obs = new GraphicObject[8];
		}
		for (int i = 0; i < monsterList.size(); i++) {

			MonsterInfo m = ((MonsterInfo) monsterList.get(i));
			int position = m.getPositionInRoomIndex();

			GraphicObject gr = drawAMonster(m, new Point(
					getPositionCoordModified(position).x + xcoord,
					getPositionCoordModified(position).y + ycoord), g);
			if (i >= 8) {
				break;
			}
			obs[i] = gr;
		}

		return obs;
	}


	public Dimension getMonsterSize(MonsterInfo m) {
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
			return new Dimension((int) (ROOMSIZE_BY_2), (int) (ROOMSIZE_BY_2));
		}
		if (mClass == Monster.OGRE) {
			return new Dimension((int) (ROOMSIZE_BY_2), (int) (ROOMSIZE_BY_2));
		}
		if (mClass == Monster.BEAR) {
			return new Dimension((int) (roomSize / 2.2), (int) (roomSize / 2.2));
		}

		return new Dimension((int) (roomSize / 2.5), (int) (roomSize / 2.5));

	}

	private GraphicObject drawAMonster(MonsterInfo m, Point p, Graphics g) {
		JDImageAWT ob = null;
		int mClass = m.getMonsterClass();
		int sizeX = (int) (getMonsterSize(m).width);
		int sizeY = (int) (getMonsterSize(m).height);
		Rectangle rect = new Rectangle(new Point(p.x - (sizeX / 2), p.y
				- (sizeY / 2)), new Dimension(sizeX, sizeY));
		int dir = m.getLookDir();
		if (mClass == Monster.WOLF) {

			JDImageProxy wolf = ImageManager.wolfImage[dir - 1];
			if (m.getLevel() == 2) {
				wolf = ImageManager.wolfImage[dir - 1];
			}
			ob = new JDImageAWT(wolf, rect);
		} else if (mClass == Monster.ORC) {
			JDImageProxy orc = ImageManager.orcImage[dir - 1];

			if (m.getLevel() == 2) {
				orc = ImageManager.orcImage[dir - 1];
			}
			ob = new JDImageAWT(orc, rect);
		} else if (mClass == Monster.SKELETON) {
			JDImageProxy skel = ImageManager.skelImage[dir - 1];

			if (m.getLevel() == 2) {
				skel = ImageManager.skelImage[dir - 1];
			}
			ob = new JDImageAWT(skel, rect);
		} else if (mClass == Monster.GHUL) {
			JDImageProxy ghul = ImageManager.ghulImage[dir - 1];

			if (m.getLevel() == 2) {
				ghul = ImageManager.ghulImage[dir - 1];
			}
			ob = new JDImageAWT(ghul, rect);
		} else if (mClass == Monster.OGRE) {
			JDImageProxy ogre = ImageManager.ogreImage[dir - 1];

			if (m.getLevel() == 2) {
				ogre = ImageManager.ogreImage[dir - 1];
			}
			ob = new JDImageAWT(ogre, rect);
		} else if (mClass == Monster.BEAR) {
			JDImageProxy bear = ImageManager.bearImage[dir - 1];

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
				JDImageProxy i = set.getImagesNr(set.getLength() - 1);
				ob = new JDImageAWT(i, rect);
			}
		}

		int mouseSize = this.ROOMSIZE_BY_5;
		return new JDGraphicObject(ob, m, rect, Color.white, new Rectangle(
				new Point(p.x - mouseSize / 2, p.y - mouseSize / 2),
				new Dimension(mouseSize, mouseSize)));
	}

	private GraphicObject[] drawItems(int xcoord, int ycoord,
			ItemInfo[] itemArray, Graphics g) {
		if (itemArray == null) {
			return new GraphicObject[0];
		}
		Point p2[] = getItemPoints(xcoord, ycoord);

		GraphicObject[] itemObs = new GraphicObject[4];

		// Schleife bis 4
		int i = 0;
		while (i < itemArray.length && i < 4) {
			Point q = p2[i];

			if (itemArray[i] != null) {

				if (AttrPotion.class.isAssignableFrom(itemArray[i]
						.getItemClass())) {

					if (((itemArray[i]).getItemKey() == Item.ITEM_KEY_HEALPOTION)) {
						itemObs[i] = new JDGraphicObject(new JDImageAWT(
								ImageManager.potion_redImage, q.x, q.y,
								12 * (roomSize / 100), 15 * (roomSize / 100)),
								itemArray[i], new Rectangle(q, new Dimension(
										12 * (roomSize / 100),
										15 * (roomSize / 100))), Color.yellow);
					} else {
						itemObs[i] = new JDGraphicObject(new JDImageAWT(
								ImageManager.potion_blueImage, q.x, q.y,
								12 * (roomSize / 100), 15 * (roomSize / 100)),
								itemArray[i], new Rectangle(q, new Dimension(
										12 * (roomSize / 100),
										15 * (roomSize / 100))), Color.yellow);
					}

				} else if (itemArray[i].getItemClass().equals(DustItem.class)) {
					itemObs[i] = new JDGraphicObject(new JDImageAWT(
							ImageManager.dustImage, q.x, q.y,
							10 * (roomSize / 100), 8 * (roomSize / 100)),
							itemArray[i], new Rectangle(q,
									new Dimension(10 * (roomSize / 100),
											8 * (roomSize / 100))),
							Color.yellow);
				} else if (itemArray[i].getItemClass().equals(Sword.class)) {
					itemObs[i] = new JDGraphicObject(new JDImageAWT(
							ImageManager.swordImage, q.x, q.y,
							30 * (roomSize / 100), 20 * (roomSize / 100)),
							itemArray[i], new Rectangle(q, new Dimension(
									30 * (roomSize / 100),
									20 * (roomSize / 100))), Color.yellow);
				} else if (itemArray[i].getItemClass().equals(Axe.class)) {
					itemObs[i] = new JDGraphicObject(new JDImageAWT(
							ImageManager.axeImage, q.x, q.y,
							30 * (roomSize / 100), 20 * (roomSize / 100)),
							itemArray[i], new Rectangle(q, new Dimension(
									30 * (roomSize / 100),
									20 * (roomSize / 100))), Color.yellow);
				} else if (itemArray[i].getItemClass().equals(Club.class)) {
					itemObs[i] = new JDGraphicObject(new JDImageAWT(
							ImageManager.clubImage, q.x, q.y,
							30 * (roomSize / 100), 20 * (roomSize / 100)),
							itemArray[i], new Rectangle(q, new Dimension(
									30 * (roomSize / 100),
									20 * (roomSize / 100))), Color.yellow);
				} else if (itemArray[i].getItemClass().equals(Lance.class)) {
					itemObs[i] = new JDGraphicObject(new JDImageAWT(
							ImageManager.lanceImage, q.x, q.y,
							50 * (roomSize / 100), 40 * (roomSize / 100)),
							itemArray[i], new Rectangle(q, new Dimension(
									50 * (roomSize / 100),
									40 * (roomSize / 100))), Color.yellow);
				} else if (itemArray[i].getItemClass().equals(Wolfknife.class)) {
					itemObs[i] = new JDGraphicObject(new JDImageAWT(
							ImageManager.wolfknifeImage, q.x, q.y,
							20 * (roomSize / 100), 15 * (roomSize / 100)),
							itemArray[i], new Rectangle(q, new Dimension(
									20 * (roomSize / 100),
									15 * (roomSize / 100))), Color.yellow);
				} else if (itemArray[i].getItemClass().equals(Armor.class)) {
					int sizeX = 30 * (roomSize / 100);
					int sizeY = 20 * (roomSize / 100);

					itemObs[i] = new JDGraphicObject(new JDImageAWT(
							ImageManager.armorImage, q.x, q.y, sizeX, sizeY),
							itemArray[i], new Rectangle(q, new Dimension(sizeX,
									sizeY)), Color.yellow);
				} else if (itemArray[i].getItemClass().equals(Shield.class)) {
					int sizeX = 20 * (roomSize / 100);
					int sizeY = 15 * (roomSize / 100);

					itemObs[i] = new JDGraphicObject(new JDImageAWT(
							ImageManager.shieldImage, q.x, q.y, sizeX, sizeY),
							itemArray[i], new Rectangle(q, new Dimension(sizeX,
									sizeY)), Color.yellow);
				} else if (itemArray[i].getItemClass().equals(Helmet.class)) {
					int sizeX = 24 * (roomSize / 100);
					int sizeY = 22 * (roomSize / 100);

					itemObs[i] = new JDGraphicObject(new JDImageAWT(
							ImageManager.helmetImage, q.x, q.y, sizeX, sizeY),
							itemArray[i], new Rectangle(q, new Dimension(sizeX,
									sizeY)), Color.yellow);
				} else if (Scroll.class.isAssignableFrom(itemArray[i]
						.getItemClass())) {
					int sizeX = 15 * (roomSize / 100);
					int sizeY = 15 * (roomSize / 100);

					itemObs[i] = new JDGraphicObject(new JDImageAWT(
							ImageManager.scrollImage, q.x, q.y, sizeX, sizeY),
							itemArray[i], new Rectangle(q, new Dimension(sizeX,
									sizeY)), Color.yellow);
				} else if (itemArray[i].getItemClass().equals(InfoScroll.class)) {
					int sizeX = 15 * (roomSize / 100);
					int sizeY = 15 * (roomSize / 100);

					itemObs[i] = new JDGraphicObject(
							new JDImageAWT(ImageManager.documentImage, q.x, q.y,
									sizeX, sizeY), itemArray[i], new Rectangle(
									q, new Dimension(sizeX, sizeY)),
							Color.yellow);
				} else if (itemArray[i].getItemClass().equals(Feather.class)) {
					int sizeX = 15 * (roomSize / 100);
					int sizeY = 15 * (roomSize / 100);

					itemObs[i] = new JDGraphicObject(new JDImageAWT(
							ImageManager.featherImage, q.x, q.y, sizeX, sizeY),
							itemArray[i], new Rectangle(q, new Dimension(sizeX,
									sizeY)), Color.yellow);
				} else if (itemArray[i].getItemClass().equals(Incense.class)) {
					int sizeX = 15 * (roomSize / 100);
					int sizeY = 15 * (roomSize / 100);

					itemObs[i] = new JDGraphicObject(new JDImageAWT(
							ImageManager.potion_greenImage, q.x, q.y, sizeX,
							sizeY), itemArray[i], new Rectangle(q,
							new Dimension(sizeX, sizeY)), Color.yellow);
				} else if (itemArray[i].getItemClass().equals(Key.class)) {
					int sizeX = 16 * (roomSize / 100);
					int sizeY = 22 * (roomSize / 100);

					itemObs[i] = new JDGraphicObject(new JDImageAWT(
							ImageManager.keyImage, q.x, q.y, sizeX, sizeY),
							itemArray[i], new Rectangle(q, new Dimension(sizeX,
									sizeY)), Color.yellow);

				} else if (itemArray[i].getItemClass().equals(Rune.class)) {
					int sizeX = 12 * (roomSize / 100);
					int sizeY = 12 * (roomSize / 100);
					JDImageProxy im = null;
					if ((itemArray[i]).toString().indexOf('J') != -1) {
						im = ImageManager.rune_yellowImage;
					} else if ((itemArray[i]).toString().indexOf('A') != -1) {
						im = ImageManager.rune_greenImage;
					} else if ((itemArray[i]).toString().indexOf('V') != -1) {
						im = ImageManager.rune_redImage;
					}

					itemObs[i] = new JDGraphicObject(new JDImageAWT(im, q.x, q.y,
							sizeX, sizeY), itemArray[i], new Rectangle(q,
							new Dimension(sizeX, sizeY)), Color.yellow);
				} else if (itemArray[i].getItemClass().equals(
						DarkMasterKey.class)) {
					int sizeX = 12 * (roomSize / 100);
					int sizeY = 12 * (roomSize / 100);
					JDImageProxy im = ImageManager.cristall_redImage;

					itemObs[i] = new JDGraphicObject(new JDImageAWT(im, q.x, q.y,
							sizeX, sizeY), itemArray[i], new Rectangle(q,
							new Dimension(sizeX, sizeY)), Color.yellow);
				} else if (itemArray[i].getClass().equals(LuziasBall.class)) {
					int sizeX = 15 * (roomSize / 100);
					int sizeY = 15 * (roomSize / 100);
					JDImageProxy im = ImageManager.kugelImage;

					itemObs[i] = new JDGraphicObject(new JDImageAWT(im, q.x, q.y,
							sizeX, sizeY), itemArray[i], new Rectangle(q,
							new Dimension(sizeX, sizeY)), Color.yellow);
				} else if (itemArray[i].getClass().equals(Book.class)) {
					int sizeX = 15 * (roomSize / 100);
					int sizeY = 15 * (roomSize / 100);
					JDImageProxy im = ImageManager.bookImage;

					itemObs[i] = new JDGraphicObject(new JDImageAWT(im, q.x, q.y,
							sizeX, sizeY), itemArray[i], new Rectangle(q,
							new Dimension(sizeX, sizeY)), Color.yellow);

				} else if (Thing.class.isAssignableFrom(itemArray[i]
						.getItemClass())) {
					int sizeX = 12 * (roomSize / 100);
					int sizeY = 12 * (roomSize / 100);
					JDImageProxy im = null;

					im = ImageManager.amulettImage;

					itemObs[i] = new JDGraphicObject(new JDImageAWT(im, q.x, q.y,
							sizeX, sizeY), itemArray[i], new Rectangle(q,
							new Dimension(sizeX, sizeY)), Color.yellow);
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


	boolean visCheat = false;

	public void setVisibility(boolean b) {
		visCheat = b;
	}

	private void drawRoom(int xcoord, int ycoord, RoomInfo r, Graphics g) {

		GraphicObject roomOb = drawBackGround(xcoord, ycoord, r, g);
		GraphicObject wallOb = drawWall(xcoord, ycoord, r, g);

		rooms.add(roomOb);
		if (wallOb != null) {
			walls.add(wallOb);
		}

		int status = r.getVisibilityStatus();

		if ((status >= RoomObservationStatus.VISIBILITY_SHRINE)
				|| (gui.getVisibility())) {
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
		if ((status >= RoomObservationStatus.VISIBILITY_FIGURES)
				|| (gui.getVisibility())) {

			if (r.equals(gui.getFigure().getRoomInfo())) {
				for (int i = 0; i < positionCoord.length; i++) {
					int posSize = ROOMSIZE_BY_8;

					JDImageProxy im = ImageManager.fieldImage;
					GraphicObject ob = new GraphicObject(
							r.getPositionInRoom(i), new Rectangle(xcoord
									+ getPositionCoord(i).x, ycoord
									+ getPositionCoord(i).y, posSize, posSize),
							Color.BLACK, im);

					positions.add(ob);

				}
			}
			List<MonsterInfo> monsters = r.getMonsterInfos();
			if (monsters != null && monsters.size() > 0) {
				GraphicObject[] monsterObs = drawMonster(xcoord, ycoord,
						r.getMonsterInfos(), g);
				for (int i = 0; i < monsterObs.length; i++) {
					if (monsterObs[i] != null) {
						monster.add(monsterObs[i]);
					}
				}
			}
		}
		if ((status >= RoomObservationStatus.VISIBILITY_ITEMS) || (visCheat)) {

			GraphicObject[] itObs = drawItems(xcoord, ycoord, r.getItemArray(),
					g);
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
			drawHero(g, xcoord, ycoord, r.getHeroInfo());
		}
	}

	public void drawHero(Graphics g, int x, int y, HeroInfo info) {
		JDImageAWT im = null;
		if (info == null) {
			return;
		}
		int index = info.getPositionInRoomIndex();
		if (index == -1) {
			int dkls = 4;
		}

		int pos = info.getPositionInRoomIndex();
		if (pos == -1) {
			System.out.println("drawHero nicht m�glich, weil PosIndex auf -1!");
		}
		Point p = getPositionCoordModified(info.getPositionInRoomIndex());
		int xpos = (int) p.getX();
		int ypos = (int) p.getY();
		int xSize = (int) (roomSize / HERO_SIZE_QUOTIENT_X);
		int ySize = (int) (roomSize / HERO_SIZE_QUOTIENT_Y);

		Rectangle rect = new Rectangle(new Point(x + xpos - (xSize / 2), y
				+ ypos - (ySize / 2)), new Dimension(xSize, ySize));


		int code = info.getHeroCode();
		int dir = info.getLookDir();
		if (dir == 0) {
			dir = Dir.NORTH;
		}

		if (code == Hero.HEROCODE_WARRIOR) {
			if (info.isDead().booleanValue()
					&& !gui.currentThreadRunning(info.getRoomInfo())) {
				im = new JDImageAWT(ImageManager.warrior_tipping_over.get(dir - 1)
						.getImages()[ImageManager.warrior_tipping_over.get(
						dir - 1).getLength() - 1], rect);
			} else {
				im = new JDImageAWT(ImageManager.warriorImage[dir - 1], rect);
			}
		} else if (code == Hero.HEROCODE_HUNTER) {
			if (info.isDead().booleanValue()
					&& !gui.currentThreadRunning(info.getRoomInfo())) {
				im = new JDImageAWT(ImageManager.thief_tipping_over.get(dir - 1)
						.getImages()[ImageManager.thief_tipping_over.get(
						dir - 1).getLength() - 1], rect);
			} else {
				im = new JDImageAWT(ImageManager.thiefImage[dir - 1], rect);
			}
		} else if (code == Hero.HEROCODE_DRUID) {
			if (info.isDead().booleanValue()
					&& !gui.currentThreadRunning(info.getRoomInfo())) {
				im = new JDImageAWT(ImageManager.druid_tipping_over.get(dir - 1)
						.getImages()[ImageManager.druid_tipping_over.get(
						dir - 1).getLength() - 1], rect);
			} else {
				im = new JDImageAWT(ImageManager.druidImage[dir - 1], rect);
			}
		} else if (code == Hero.HEROCODE_MAGE) {
			if (info.isDead().booleanValue()
					&& !gui.currentThreadRunning(info.getRoomInfo())) {
				im = new JDImageAWT(ImageManager.mage_tipping_over.get(dir - 1)
						.getImages()[ImageManager.mage_tipping_over
						.get(dir - 1).getLength() - 1], rect);
			} else {
				im = new JDImageAWT(ImageManager.mageImage[dir - 1], rect);
			}
		}
		hero = new JDGraphicObject(im, info, rect, Color.white,
				getHalfSizeRect(rect));

	}

	private Rectangle getHalfSizeRect(Rectangle r) {
		int sizex = r.width;
		int sizey = r.height;
		int newSizex = sizex / 2;
		int newSizey = sizey / 2;
		int newPointx = r.x + newSizex / 2;
		int newPointy = r.y + newSizey / 2;
		return new Rectangle(new Point(newPointx, newPointy), new Dimension(
				newSizex, newSizey));
	}

	public static final double HERO_POINT_QUOTIENT_X = 2.2;

	public static final double HERO_POINT_QUOTIENT_Y = 2.2;

	public static final int HERO_POINT_OFFSET_Y = 0;

	public static final int HERO_POINT_OFFSET_X = 15;

	public static final double HERO_SIZE_QUOTIENT_X = 2;

	public static final double HERO_SIZE_QUOTIENT_Y = 2;

	public Point getHeroPos() {
		return new Point(((int) (roomSize / HERO_POINT_QUOTIENT_X))
				- HERO_POINT_OFFSET_X, (int) (roomSize / HERO_POINT_QUOTIENT_Y)
				- HERO_POINT_OFFSET_Y);
	}

	public Dimension getHeroSize() {
		return new Dimension((int) (roomSize / HERO_SIZE_QUOTIENT_X),
				(int) (roomSize / HERO_SIZE_QUOTIENT_Y));
	}

	public void decSize() {
		if (roomSize >= 50) {
			roomSize -= 10;
			sizeChanged();
			gui.updateGui();
			gui.repaintPicture();
		}
	}

	public void incSize() {
		if (roomSize <= 240) {
			roomSize += 10;
			sizeChanged();
			gui.updateGui();
			gui.repaintPicture();
		}
	}

	public void incSize(int inc) {
		roomSize += inc;
		sizeChanged();
		gui.updateGui();
		gui.repaintPicture();
	}

	public void mousePressed(MouseEvent me) {

		boolean right = false;
		Point p = me.getPoint();
		Object o = getRoom(p);
		RoomInfo r = null;
		if (o instanceof RoomInfo) {
			r = (RoomInfo) o;
		}
		if (me.getModifiers() == MouseEvent.BUTTON3_MASK) {
			right = true;
		}
		if (o instanceof Point) {
			Point point = (Point) o;
			gui.getControl().roomClicked(new JDPoint(point.x, point.y), right);
			return;
		}
		if (r == null) {
			return;
		}

		mouse_x = (int) p.getX();
		mouse_y = (int) p.getY();
		if (gui.getMainFrame().isNoControl()) {
			p = new Point(-1, -1);
		}

		boolean found = false;

		for (int i = 0; i < items.size(); i++) {
			GraphicObject ob = ((GraphicObject) items.get(i));

			if ((ob != null) && ob.hasPoint(p)) {
				gui.getControl().itemClicked((ItemInfo) ob.getClickedObject(),
						right);
				if ((((GraphicObject) ob).getClickedObject()) != null) {
					// Wenn kein Fragezeichen....
					gui.getMainFrame()
							.getText()
							.setText(
									((Paragraphable) (((GraphicObject) ob)
											.getClickedObject()))
											.getParagraphs());
					found = true;

					break;
				}
			}
		}

		if (!found) {
			for (int i = 0; i < monster.size(); i++) {
				GraphicObject ob = ((GraphicObject) monster.get(i));
				if (ob.hasPoint(p)) {
					Object f = ((GraphicObject) ob).getClickedObject();
					if (f != null && f instanceof FigureInfo) {
						gui.getControl().monsterClicked((FigureInfo) f, right);
					}
					gui.getMainFrame()
							.getText()
							.setText(
									((Paragraphable) (((GraphicObject) ob)
											.getClickedObject()))
											.getParagraphs());

					found = true;
					break;
				}
			}
		}

		if (!found) {
			for (int i = 0; i < positions.size(); i++) {
				GraphicObject ob = (GraphicObject) positions.get(i);
				if (ob.hasPoint(p)) {
					if ((((GraphicObject) ob).getClickedObject()) != null)
						gui.getControl().positionClicked(
								(PositionInRoomInfo) ob.getClickedObject(),
								right);
					found = true;
					break;
				}
			}
		}

		if (!found) {
			for (int i = 0; i < shrines.size(); i++) {
				GraphicObject ob = ((GraphicObject) shrines.get(i));
				if (ob.hasPoint(p)) {
					if ((((GraphicObject) ob).getClickedObject()) != null)
						gui.getControl().shrineClicked(right);
					gui.getMainFrame()
							.getText()
							.setText(
									((Paragraphable) (((GraphicObject) ob)
											.getClickedObject()))
											.getParagraphs());

					found = true;
					break;
				}
			}
		}

		if (!found) {
			for (int i = 0; i < doors.size(); i++) {
				GraphicObject ob = ((GraphicObject) doors.get(i));
				if (ob.hasPoint(p)) {
					if ((((GraphicObject) ob).getClickedObject()) != null)
						gui.getControl().doorClicked(ob.getClickedObject(),
								right);
					Paragraphable pa = ((Paragraphable) (((GraphicObject) ob)
							.getClickedObject()));
					if (pa != null) {
						gui.getMainFrame().getText()
								.setText(pa.getParagraphs());
					}
					found = true;
					break;
				}
			}
		}

		if (!found) {
			for (int i = 0; i < spots.size(); i++) {
				GraphicObject ob = ((GraphicObject) spots.get(i));
				if (ob.hasPoint(p)) {
					if ((((GraphicObject) ob).getClickedObject()) != null)
						gui.getControl().spotClicked(ob.getClickedObject());
					gui.getMainFrame()
							.getText()
							.setText(
									((Paragraphable) (((GraphicObject) ob)
											.getClickedObject()))
											.getParagraphs());

					found = true;
					break;
				}
			}
		}

		if (!found) {
			for (int i = 0; i < chests.size(); i++) {
				GraphicObject ob = ((GraphicObject) chests.get(i));
				if (ob.hasPoint(p)) {
					if ((((GraphicObject) ob).getClickedObject()) != null) {

						gui.getControl().chestClicked(ob.getClickedObject(),
								right);
						gui.getMainFrame()
								.getText()
								.setText(
										((Paragraphable) (((GraphicObject) ob)
												.getClickedObject()))
												.getParagraphs());

						found = true;
						break;
					}
				}
			}
		}

		if (!found) {

			if (hero.hasPoint(p)) {
				gui.getControl().heroClicked();
				found = true;

			}
		}
		if (!found) {
			for (int i = 0; i < rooms.size(); i++) {
				GraphicObject ob = ((GraphicObject) rooms.get(i));
				if (ob.hasPoint(p)) {
					gui.getControl().roomClicked(ob.getClickedObject(), right);
					found = true;
					break;
				}
			}
		}
	}

	public void mouseClicked(MouseEvent me) {

	}

	public void mouseEntered(MouseEvent me) {
		this.setCursor(cursor1);

	}

	public void mouseReleased(MouseEvent me) {

	}

	public void mouseExited(MouseEvent me) {

	}

	boolean gameStarted = false;

	public void setGameStarted() {
		gameStarted = true;
	}

	public void updateInfoForMouseCursor() {
		Point p = lastMouseMovePoint;
		if (p == null) {
			return;
		}

		boolean found = false;

		this.setCursor(cursor1);
		Object o = getRoom(p);
		if (o == null) {
			gui.getMainFrame().getText().setText(new Paragraph[0]);
			return;
		}

		if (o instanceof Point) {
			roomCrossed((Point) o);
			return;
		}

		for (int i = 0; i < items.size(); i++) {
			GraphicObject ob = ((GraphicObject) items.get(i));
			if ((ob != null) && ob.hasPoint(p)) {
				itemCrossed(ob.getClickedObject());
				found = true;
				break;
			}
		}

		if (!found) {

			for (int i = 0; i < monster.size(); i++) {
				GraphicObject ob = ((GraphicObject) monster.get(i));
				if ((ob != null) && ob.hasPoint(p)) {
					monsterCrossed(ob.getClickedObject());
					found = true;
					break;
				}
			}
		}
		if (!found) {
			for (int i = 0; i < positions.size(); i++) {
				GraphicObject ob = (GraphicObject) positions.get(i);
				if (ob.hasPoint(p)) {
					if ((((GraphicObject) ob).getClickedObject()) != null)
						positionCrossed(((GraphicObject) ob).getClickedObject());
					found = true;
					break;
				}
			}
		}
		if (!found) {
			for (int i = 0; i < spots.size(); i++) {
				GraphicObject ob = ((GraphicObject) spots.get(i));
				if ((ob != null) && ob.hasPoint(p)) {
					spotCrossed(ob.getClickedObject());
					found = true;
					break;
				}
			}
		}
		if (!found) {
			for (int i = 0; i < shrines.size(); i++) {
				GraphicObject ob = ((GraphicObject) shrines.get(i));
				if (ob.hasPoint(p)) {
					shrineCrossed(ob.getClickedObject());
					found = true;
					break;
				}
			}
		}
		if (!found) {
			for (int i = 0; i < doors.size(); i++) {
				GraphicObject ob = ((GraphicObject) doors.get(i));
				if (ob.hasPoint(p)) {
					doorCrossed((DoorInfo) ob.getClickedObject());
					found = true;
					break;
				}
			}
		}
		if (!found) {
			for (int i = 0; i < chests.size(); i++) {
				GraphicObject ob = ((GraphicObject) chests.get(i));
				if (ob.hasPoint(p)) {
					chestCrossed(ob.getClickedObject());
					found = true;
					break;
				}
			}
		}
		if (!found) {
			if (hero != null) {
				if (hero.hasPoint(p)) {
					heroCrossed();
					found = true;
				}
			}
		}

		if (!found) {
			for (int i = 0; i < rooms.size(); i++) {
				GraphicObject ob = ((GraphicObject) rooms.get(i));
				if (ob.hasPoint(p)) {
					roomCrossed(ob.getClickedObject());
					found = true;
					break;
				}
			}
		}

	}

	private Point lastMouseMovePoint;

	public void mouseMoved(MouseEvent me) {
		if (gameStarted) {

			Point p = me.getPoint();
			lastMouseMovePoint = p;
			this.updateInfoForMouseCursor();
		}

	}

	private Object getRoom(Point p) {
		GraphicObject ob = null;
		boolean found = false;
		for (int i = 0; i < rooms.size(); i++) {
			ob = ((GraphicObject) rooms.get(i));
			if (ob.hasPoint(p)) {
				found = true;
				break;
			}
		}
		if (!found) {
			return null;
		}
		return ob.getClickedObject();
	}

	private void doorCrossed(DoorInfo o) {
		if (o instanceof DoorInfo) {
			gui.getMainFrame().getText()
					.setText(((DoorInfo) o).getParagraphs());
			if (((DoorInfo) o).hasLock().booleanValue()) {
				if (gui.getFigure().hasKey((DoorInfo) o).booleanValue()) {
					this.setCursor(cursor3);
				} else {
					this.setCursor(cursor4);
				}
			}
		}
	}

	private void chestCrossed(Object o) {
		if (o instanceof ChestInfo) {
			gui.getMainFrame().getText()
					.setText(((ChestInfo) o).getParagraphs());
		}
	}

	private void spotCrossed(Object o) {
		if (o instanceof HiddenSpot) {
			gui.getMainFrame().getText()
					.setText(((HiddenSpot) o).getParagraphs());
		}
	}

	private void itemCrossed(Object o) {
		if (o instanceof ItemInfo) {
			if (this.spellMetaDown) {
				this.setCursor(cursor_wand);

			} else if (setUseWithTarget) {
				this.setCursor(cursor_use);
			} else {
				this.setCursor(cursor2);
			}
			gui.getMainFrame().getText()
					.setText(((ItemInfo) o).getParagraphs());

		}

	}

	private void heroCrossed() {
		this.setCursor(cursor6);
		gui.getMainFrame().getText().setText((gui.getFigure()).getParagraphs());
	}

	private boolean spellMetaDown = false;

	public void setSpellMetaDown(boolean b) {
		spellMetaDown = b;
	}

	private boolean setUseWithTarget = false;

	public void setUseWithTarget(boolean b) {
		setUseWithTarget = b;
	}

	private void positionCrossed(Object o) {
		if (o instanceof PositionInRoomInfo) {
			if (gui.getFigure().getRoomInfo().fightRunning().booleanValue()) {
				int crossedIndex = ((PositionInRoomInfo) o).getIndex();
				int posIndex = gui.getFigure().getPositionInRoomIndex();
				int dist = Position
						.getMinDistanceFromTo(posIndex, crossedIndex);
				if (dist > 1) {
					this.setCursor(this.cursor_boots_not);
				} else {
					this.setCursor(cursor_boots);
				}
			} else {
				this.setCursor(cursor_boots);
			}
		}
	}

	private void shrineCrossed(Object o) {
		if (o instanceof ShrineInfo) {
			gui.getMainFrame().getText()
					.setText(((ShrineInfo) o).getParagraphs());
		}

	}

	private void monsterCrossed(Object o) {
		if (o instanceof FigureInfo) {

			gui.getMainFrame().getText()
					.setText(((MonsterInfo) o).getParagraphs());
			if (this.spellMetaDown) {
				this.setCursor(cursor_wand);

			} else {
				this.setCursor(cursor5);
			}
		}

	}

	private void roomCrossed(Object o) {
		if (o != null) {
			if (o instanceof RoomInfo) {
				gui.getMainFrame().getText()
						.setText(((RoomInfo) o).getParagraphs());
				if (((RoomInfo) o).getConnectionTo(gui.getFigure()
						.getRoomInfo()) != null) {

					this.setCursor(cursor7);

				}
			} else if (o instanceof Point) {
				gui.getMainFrame().getText().setText(new Paragraph[0]);
				int dir = Dir.getDirFromToIfNeighbour(gui.getFigure()
						.getRoomNumber(), (Point) o);
				if (dir != -1) {
					if (gui.getFigure().getRoomDoors()[dir - 1] != 0) {
						this.setCursor(cursor7);
					}
				}
			}
		}

	}

	public void setMemory(boolean b) {
		memory = b;
	}

	public void setCursor(Cursor c) {
		if (spellMetaDown) {
			super.setCursor(cursor_wand);
		} else if (setUseWithTarget) {
			super.setCursor(cursor_use);
		} else {
			super.setCursor(c);
		}
	}

	public void mouseDragged(MouseEvent me) {

	}

	public int getRoomSize() {
		return roomSize;
	}

	public void setRoomSize(int size) {
		this.roomSize = size;
	}

	public int getOffset() {
		return offset;
	}


	public Point getMonsterPos(int k) {
		return getMonsterPoints(0, 0)[k];
	}

	private Point[] getMonsterPoints(int xcoord, int ycoord) {
		Point[] points = new Point[4];
		int half = (int) ((double) roomSize) / 2;
		points[2] = new Point(xcoord + half - (int) (roomSize / 1.8), ycoord
				+ half - ((int) (roomSize / 4)));
		points[3] = new Point(xcoord + half + (int) (roomSize / 8), ycoord
				+ half - ((int) (roomSize / 4)));
		points[0] = new Point(xcoord + half - (roomSize / 3), ycoord + half
				- ((int) (roomSize / 2.8)));
		points[1] = new Point(xcoord + half - (roomSize / 16), ycoord + half
				- ((int) (roomSize / 2.8)));

		return points;
	}

	private Point[] getItemPoints(int xcoord, int ycoord) {
		Point[] points = new Point[4];

		points[0] = new Point(xcoord + ROOMSIZE_BY_2 - (int) (ROOMSIZE_BY_8),
				ycoord + (ROOMSIZE_BY_3));
		points[1] = new Point(xcoord + ROOMSIZE_BY_2, ycoord + ROOMSIZE_BY_2
				- (int) (ROOMSIZE_BY_16));
		points[2] = new Point(xcoord + (ROOMSIZE_BY_3), ycoord
				+ (3 * ROOMSIZE_BY_5));
		points[3] = new Point(xcoord + (int) (3 * ROOMSIZE_BY_5), ycoord
				+ (3 * ROOMSIZE_BY_5));

		return points;
	}

	private Point getChestPoint(int xcoord, int ycoord) {
		int x1 = xcoord + (roomSize / 8);
		int y1 = ycoord + roomSize / 10;

		return new Point(x1, y1);
	}

	private Point getSpotPoint(int xcoord, int ycoord) {
		int x1 = xcoord + (1 * roomSize / 8);
		int y1 = ycoord + (6 * roomSize / 8);

		return new Point(x1, y1);
	}

	private Dimension getSpotDimension() {
		return new Dimension(roomSize / 7, roomSize / 7);
	}

	private Dimension getChestDimension() {
		return new Dimension(roomSize / 5, roomSize / 5);
	}

	protected Image getImageFromJAR(String fileName) {
		if (fileName == null)
			return null;

		Image image = null;
		byte[] thanksToNetscape = null;
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		InputStream in = getClass().getResourceAsStream(fileName);

		try {
			int length = in.available();
			thanksToNetscape = new byte[length];
			in.read(thanksToNetscape);
			image = toolkit.createImage(thanksToNetscape);
		} catch (Exception exc) {
			System.out.println(exc + " getting resource " + fileName);
			return null;
		}
		return image;
	}

	public int getScrollableUnitIncrement(Rectangle r, int a, int b) {
		return roomSize;
	}

	public int getScrollableBlockIncrement(Rectangle r, int a, int b) {
		return 2 * roomSize;
	}

	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	public int getPreferredViewportSize() {
		return 100;
	}

	public Dimension getPreferredScrollableViewportSize() {
		return new Dimension(500, 500);
	}

	private Rectangle getShrineRect(int xcoord, int ycoord) {
		int xpos = xcoord + (13 * roomSize / 20);
		int ypos = ycoord + (1 * roomSize / 36);
		int xsize = (int) (roomSize / 2.9);
		int ysize = (int) (roomSize / 2.2);
		return new Rectangle(new Point(xpos, ypos), new Dimension(xsize, ysize));
	}

	public static int ROOMSIZE_BY_2;

	public static int ROOMSIZE_BY_3;

	public static int ROOMSIZE_BY_4;

	public static int ROOMSIZE_BY_5;

	public static int ROOMSIZE_BY_6;

	public static int ROOMSIZE_BY_8;

	public static int ROOMSIZE_BY_10;

	public static int ROOMSIZE_BY_16;

	public static int ROOMSIZE_BY_12;

	public static int ROOMSIZE_BY_24;

	public static int ROOMSIZE_BY_36;

	public static int ROOMSIZE_BY_20;

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
			JDImageProxy im = null;
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
			JDImageProxy im = null;
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
			JDImageProxy im = ImageManager.shrine_blackImage;

			ob = new JDGraphicObject(new JDImageAWT(im, xpos, ypos, xsize, ysize),
					s, getShrineRect(xcoord, ycoord), Color.yellow);
		} else if (s.getShrineIndex() == Shrine.SHRINE_XMAS) {
			int xpos = xcoord + (27 * roomSize / 60);
			int ypos = ycoord - (1 * ROOMSIZE_BY_10);
			int xsize = (int) (roomSize / 1.7);
			int ysize = (int) (roomSize / 1.7);
			JDImageProxy im = ImageManager.xmasImage;

			ob = new JDGraphicObject(new JDImageAWT(im, xpos, ypos, xsize, ysize),
					s, getShrineRect(xcoord, ycoord), Color.yellow);

		} else if (s.getShrineIndex() == Shrine.SHRINE_RUNEFINDER) {
			int xpos = xcoord + (7 * ROOMSIZE_BY_10);
			int ypos = ycoord + (1 * roomSize / 24);
			int xsize = (int) (roomSize / 3.4);
			int ysize = (int) (roomSize / 2.7);
			JDImageProxy im = null;
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
			JDImageProxy im = ImageManager.pentagrammImage;
			ob = new JDGraphicObject(new JDImageAWT(im, xpos, ypos, xsize, ysize),
					s, getShrineRect(xcoord, ycoord), Color.yellow);
		} else if (s.getShrineIndex() == Shrine.SHRINE_LUZIA) {
			int xpos = xcoord + (7 * ROOMSIZE_BY_10);
			int ypos = ycoord + (0 * ROOMSIZE_BY_36);
			int xsize = (int) (roomSize / 3.6);
			int ysize = (int) (roomSize / 2.5);

			JDImageProxy im = ImageManager.luziaImage;
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




	

	/**
	 * @return Returns the luzia_ball_greyImage.
	 */
	public JDImageProxy getLuzia_ball_greyImage() {
		return ImageManager.luzia_ball_greyImage;
	}

	/**
	 * @return Returns the luzia_ball_redImage.
	 */
	public JDImageProxy getLuzia_ball_redImage() {
		return ImageManager.luzia_ball_redImage;
	}

	/**
	 * @return Returns the puff.
	 */
	public AnimationSet getPuff() {
		return ImageManager.puff;
	}
}