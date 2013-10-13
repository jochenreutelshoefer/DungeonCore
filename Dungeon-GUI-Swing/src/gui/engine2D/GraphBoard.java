package gui.engine2D;

import figure.FigureInfo;
import figure.RoomObservationStatus;
import figure.hero.Hero;
import figure.hero.HeroInfo;
import figure.monster.Monster;
import figure.monster.MonsterInfo;
import graphics.GraphicObject;
import graphics.GraphicObjectRenderer;
import graphics.ImageManager;
import graphics.JDGraphicObject;
import graphics.JDImageAWT;
import graphics.JDImageProxy;
import graphics.JDRectangle;
import graphics.RoomSize;
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
import dungeon.Door;
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
		MouseMotionListener, Scrollable {

	Graphics g2;



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



	}



	public void repaintRoom(Graphics g, RoomInfo r, boolean heroToo) {

		repaintRoomSmall(g, r, new LinkedList());
	}






	public void repaintRoomSmall(Graphics g, RoomInfo r, Object obj) {
		GraphicObjectRenderer renderer = new GraphicObjectRenderer(roomSize);

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

		GraphicObject roomOb = drawBackGround(xcoord, ycoord, r, renderer);

		GraphicObject wallOb = drawWall(xcoord, ycoord, r, g);
		graphObs.add(roomOb);

		graphObs.addAll(renderer.drawDoors(r, xcoord, ycoord));

		if (wallOb != null) {
			graphObs.add(wallOb);
		}

		int status = r.getVisibilityStatus();

		if ((status >= RoomObservationStatus.VISIBILITY_SHRINE)
				|| (gui.getVisibility())) {

			if (r.equals(gui.getFigure().getRoomInfo())) {
				Point[] positionCoord = renderer.getPositionCoord();
				for (int i = 0; i < positionCoord.length; i++) {
					int posSize = RoomSize.by8(roomSize);

					GraphicObject ob = new GraphicObject(
							r.getPositionInRoom(i), new Rectangle(
									renderer.getPositionCoord(i).x,
									renderer.getPositionCoord(i).y, posSize, posSize),
							Color.BLACK, ImageManager.fieldImage);

					graphObs.add(ob);

				}
			}

			GraphicObject ob = null;
			if (r.getShrine() != null) {
				ShrineInfo s = r.getShrine();
				ob = renderer.getShrineGraphicObject(s, xcoord, ycoord);
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
		if ((status >= RoomObservationStatus.VISIBILITY_FIGURES)
				|| (gui.getVisibility())) {

			if (r.getMonsterInfos().size() > 0) {
				GraphicObject[] monsterObs = renderer.drawMonster(xcoord, ycoord,
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
		if ((status >= RoomObservationStatus.VISIBILITY_ITEMS)
				|| (gui.getVisibility())) {

			GraphicObject[] itObs = GraphicObjectRenderer.drawItems(xcoord, ycoord, r.getItemArray(), roomSize);
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
			drawHero(g, xcoord, ycoord, r.getHeroInfo(), renderer);
		}

		graphObs.add(new GraphicObject(r, new Rectangle(new Point(xcoord,
				ycoord - getDoorDimension(true).width), new Dimension(roomSize,
				roomSize)), Color.darkGray, ImageManager.wall_sidesImage));

		for (int i = 0; i < graphObs.size(); i++) {
			GraphicObject o = ((GraphicObject) graphObs.get(i));
			if (o != null) {
				DrawUtils.fillGraphicObject(o,g);
			}

		}

		FigureInfo hInfo = gui.getFigure();
		boolean inRoom = hInfo.getRoomNumber().equals(r.getNumber());
		boolean paint = !aniObs.contains(hInfo);
		if (paint && inRoom) {

			if (hero != null && !hInfo.equals(obj)) {
				DrawUtils.fillGraphicObject(hero,g);
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
				return true;
			}

		}
		return false;
	}

	public void paint(Graphics g) {
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
									new Point(xcoord, ycoord),
									new Dimension(
											roomSize,
											roomSize)),
							Color.black, false, null));
				}

			}
		}

		for (int i = 0; i < rooms.size(); i++) {
			DrawUtils.fillGraphicObject(((GraphicObject) rooms.get(i)),g2);
		}
		for (int i = 0; i < walls.size(); i++) {
			DrawUtils.fillGraphicObject(((GraphicObject) walls.get(i)),g2);
		}
		for (int i = 0; i < doors.size(); i++) {
			Object o = doors.get(i);
			DrawUtils.fillGraphicObject(((GraphicObject) o),g2);
		}

		for (int i = 0; i < positions.size(); i++) {
			DrawUtils.fillGraphicObject(((GraphicObject) positions.get(i)),g2);
		}
		for (int i = 0; i < spots.size(); i++) {
			DrawUtils.fillGraphicObject(((GraphicObject) spots.get(i)),g2);
		}
		for (int i = 0; i < shrines.size(); i++) {
			DrawUtils.fillGraphicObject(((GraphicObject) shrines.get(i)),g2);
		}

		for (int i = 0; i < items.size(); i++) {
			ItemInfo a = (ItemInfo) ((GraphicObject) items.get(i))
					.getClickedObject();
			DrawUtils.fillGraphicObject(((GraphicObject) items.get(i)),g2);

		}
		for (int i = 0; i < chests.size(); i++) {
			DrawUtils.fillGraphicObject(((GraphicObject) chests.get(i)),g2);
		}
		for (int i = 0; i < monster.size(); i++) {
			GraphicObject o = ((GraphicObject) monster.get(i));
			if (o != null) {
				DrawUtils.fillGraphicObject(o,g2);
			}

		}

		for (int i = 0; i < lastWalls.size(); i++) {
			DrawUtils.fillGraphicObject(((GraphicObject) lastWalls.get(i)),g2);
		}

		boolean animationRunning = gui.currentThreadRunning(gui.getFigure()
				.getRoomInfo());

		if (!animationRunning) {
			if (hero != null) {
				DrawUtils.fillGraphicObject(hero, g2);
			}
		}

	}
	

	private GraphicObject drawBackGround(int xcoord, int ycoord, RoomInfo r, GraphicObjectRenderer renderer) {

		Color bg = null;

		int status = r.getVisibilityStatus();
		JDImageProxy im = ImageManager.floorImageArray[r.getFloorIndex()];
		if (memory) {
			List<GraphicObject> roomDoors = renderer.drawDoors(r, xcoord, ycoord);
			im = ImageManager.floorImage_darkArray[r.getFloorIndex()];
			this.doors.addAll(roomDoors);
		} else {
			if (status == RoomObservationStatus.VISIBILITY_FOUND) {
				im = ImageManager.floorImage_mediumArray[r.getFloorIndex()];
				List<GraphicObject> roomDoors = renderer.drawDoors(r, xcoord, ycoord);
				this.doors.addAll(roomDoors);

			} else if (status == RoomObservationStatus.VISIBILITY_SHRINE) {
				List<GraphicObject> roomDoors = renderer.drawDoors(r, xcoord, ycoord);
				im = ImageManager.floorImage_mediumArray[r.getFloorIndex()];
				this.doors.addAll(roomDoors);
				if (r.isPart_scouted()) {

					int xpos = xcoord + (1 * roomSize / 3);
					int ypos = ycoord + (1 * roomSize / 2) - (roomSize / 5);
					int xsize = roomSize / 3;
					int ysize = (int) (roomSize / 2.5);
					GraphicObject ob = new JDGraphicObject(
							new JDImageAWT(ImageManager.questionmark, xpos, ypos,
									xsize, ysize), null, new JDRectangle(
									new JDPoint(xpos, ypos), xsize,
											ysize), Color.yellow);
					items.add(ob);
				}
				//
			} else if (status > RoomObservationStatus.VISIBILITY_SHRINE) {

				List<GraphicObject> roomDoors = renderer.drawDoors(r, xcoord, ycoord);
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
		new Point(xcoord, ycoord), new Dimension(
				roomSize, roomSize - 1
						* GraphicObjectRenderer.getDoorDimension(true, roomSize).width)), bg, false, im);
	}

	
	private Dimension getDoorDimension(boolean b) {
		return GraphicObjectRenderer.getDoorDimension(true, roomSize);
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
										- GraphicObjectRenderer.getDoorDimension(true, roomSize).width),
								new Dimension(roomSize, roomSize)), bg,
								ImageManager.wall_southImage));

				DoorInfo[] doorArray = r.getDoors();
				JDRectangle rect = GraphicObjectRenderer.getSouthDoorRect(xcoord, ycoord, roomSize);
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











	

	

	boolean visCheat = false;

	public void setVisibility(boolean b) {
		visCheat = b;
	}

	private void drawRoom(int xcoord, int ycoord, RoomInfo r, Graphics g) {

		GraphicObjectRenderer renderer = new GraphicObjectRenderer(roomSize);
		
		GraphicObject roomOb = drawBackGround(xcoord, ycoord, r, renderer);
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
				ob = renderer.getShrineGraphicObject(s, xcoord, ycoord);

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
			Point[] positionCoord = renderer.getPositionCoord();
			if (r.equals(gui.getFigure().getRoomInfo())) {
				for (int i = 0; i < positionCoord.length; i++) {
					int posSize = RoomSize.by8(roomSize);

					JDImageProxy im = ImageManager.fieldImage;
					GraphicObject ob = new GraphicObject(
							r.getPositionInRoom(i), new Rectangle(xcoord
									+ renderer.getPositionCoord(i).x, ycoord
									+ renderer.getPositionCoord(i).y, posSize, posSize),
							Color.BLACK, im);

					positions.add(ob);

				}
			}
			List<MonsterInfo> monsters = r.getMonsterInfos();
			if (monsters != null && monsters.size() > 0) {
				GraphicObject[] monsterObs = renderer.drawMonster(xcoord, ycoord,
						r.getMonsterInfos());
				for (int i = 0; i < monsterObs.length; i++) {
					if (monsterObs[i] != null) {
						monster.add(monsterObs[i]);
					}
				}
			}
		}
		if ((status >= RoomObservationStatus.VISIBILITY_ITEMS) || (visCheat)) {

			GraphicObject[] itObs = GraphicObjectRenderer.drawItems(xcoord, ycoord, r.getItemArray(), roomSize);
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
			drawHero(g, xcoord, ycoord, r.getHeroInfo(), renderer);
		}
	}

	public void drawHero(Graphics g, int x, int y, HeroInfo info, GraphicObjectRenderer renderer) {
		if (info == null) {
			return;
		}

		hero = getHeroGraphicObject(x, y, info, renderer);

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
		JDPoint p = new JDPoint(me.getPoint().getX(),me.getPoint().getY());
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
			p = new JDPoint(-1, -1);
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
		if (lastMouseMovePoint == null) {
			return;
		}

		JDPoint p = new JDPoint(lastMouseMovePoint.x, lastMouseMovePoint.y);
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

	private Object getRoom(JDPoint p) {
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