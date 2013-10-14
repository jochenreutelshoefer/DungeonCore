package gui.engine2D;

import figure.FigureInfo;
import figure.monster.MonsterInfo;
import graphics.GraphicObject;
import graphics.GraphicObjectRenderer;
import graphics.ImageManager;
import graphics.JDImageProxy;
import graphics.util.JDDimension;
import gui.JDJPanel;
import gui.MyJDGui;
import gui.Paragraph;
import gui.Paragraphable;
import item.ItemInfo;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Scrollable;

import shrine.ShrineInfo;
import animation.AnimationSet;
import dungeon.ChestInfo;
import dungeon.Dir;
import dungeon.DoorInfo;
import dungeon.HiddenSpot;
import dungeon.JDPoint;
import dungeon.Position;
import dungeon.PositionInRoomInfo;
import dungeon.RoomInfo;

/**
 * @author Duke1
 * 
 *         To change this generated comment edit the template variable
 *         "typecomment": Window>Preferences>Java>Templates. To enable and
 *         disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
@SuppressWarnings("serial")
public class GraphBoard extends JDJPanel implements MouseListener,
		MouseMotionListener, Scrollable {

	public static final int screenSize = 14000;

	public static final int DEFAULT_ROOM_SIZE = 180;

	private int roomSize = 180;

	private int offset = 500;

	private boolean memory = false;

	private Cursor cursor1;

	private Cursor cursor2;

	private Cursor cursor3;

	private Cursor cursor4;

	private Cursor cursor5;

	private Cursor cursor6;

	private Cursor cursor7;

	private Cursor cursor_boots;

	private Cursor cursor_wand;

	private Cursor cursor_boots_not;

	private Cursor cursor_use;

	private GraphicObjectRenderer renderer;

	private Cursor createCustomCursor(JDImageProxy<?> image, Point p,
			String label) {
		Toolkit toolkit = this.getToolkit();
		return toolkit.createCustomCursor((Image) image.getImage(), p, label);
	}

	public GraphBoard(Applet a, MyJDGui gui) {
		super(gui);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		initCursors();
		this.renderer = new GraphicObjectRenderer(roomSize, gui);
		this.setPreferredSize(new Dimension(screenSize, screenSize));
	}

	private void initCursors() {
		cursor1 = createCustomCursor(ImageManager.hand_zeigt1_Image, new Point(
				1, 6), "hand1");
		cursor2 = createCustomCursor(ImageManager.hand_greift1_Image,
				new Point(1, 8), "hand2");
		cursor3 = createCustomCursor(ImageManager.cursor_key_Image, new Point(
				1, 8), "key");
		cursor4 = createCustomCursor(ImageManager.cursor_key_not_Image,
				new Point(1, 8), "key_not");
		cursor5 = createCustomCursor(ImageManager.cursor_sword,
				new Point(1, 8), "sword");
		cursor6 = createCustomCursor(ImageManager.cursor_clock,
				new Point(4, 8), "clock");
		cursor7 = createCustomCursor(ImageManager.cursor_scout,
				new Point(4, 8), "scout");
		cursor_boots = createCustomCursor(ImageManager.cursor_go_Image,
				new Point(4, 8), "go");
		cursor_boots_not = createCustomCursor(ImageManager.cursor_go_not_Image,
				new Point(4, 8), "go_not");
		cursor_wand = createCustomCursor(ImageManager.cursor_wand, new Point(1,
				6), "wand");
		cursor_use = createCustomCursor(ImageManager.cursor_use_Image,
				new Point(4, 8), "go_not");
	}

	private void sizeChanged() {
		this.renderer = new GraphicObjectRenderer(roomSize, gui);
	}
	
	public Dimension getMonsterSize(MonsterInfo m) {
		JDDimension dimension = this.renderer.getMonsterSize(m);
		return new Dimension(dimension.getWidth(), dimension.getHeight());
	}

	public void repaintRoomSmall(Graphics g, RoomInfo r, Object obj) {
		GraphicObjectRenderer renderer = new GraphicObjectRenderer(roomSize,
				gui);

		int xcoord = 0;
		int ycoord = 0;

		List<?> aniObs = new LinkedList<Object>();
		if (obj instanceof LinkedList) {
			aniObs = (LinkedList<?>) obj;
		}
		List<GraphicObject> graphObs = renderer.createGraphicObjectsForRoom(r,
				obj, xcoord, ycoord, aniObs);

		for (int i = 0; i < graphObs.size(); i++) {
			GraphicObject o = ((GraphicObject) graphObs.get(i));
			if (o != null) {
				DrawUtils.fillGraphicObject(o, g);
			}

		}

		FigureInfo hInfo = gui.getFigure();
		boolean inRoom = hInfo.getRoomNumber().equals(r.getNumber());
		boolean paint = !aniObs.contains(hInfo);
		if (paint && inRoom) {

			if (renderer.hero != null && !hInfo.equals(obj)) {
				DrawUtils.fillGraphicObject(renderer.hero, g);
			}
		}
	}

	public Point getPositionCoordModified(int index) {
		JDPoint point = renderer.getPositionCoordModified(index);
		return new Point(point.getX(), point.getY());
	}

	public Point getPositionCoord(int index) {
		JDPoint point = renderer.getPositionCoord(index);
		return new Point(point.getX(), point.getY());
	}

	public void paint(Graphics g) {
		Graphics g2 = g;

		this.renderer.clear();

		// blank putzen
		g2.setColor(Color.black);
		g2.fillRect(0, 0, (int) getSize().getHeight(), (int) getSize()
				.getWidth());

		JDPoint p = gui.getFigure().getDungeonSize();
		int xrooms = p.getX();
		int yrooms = p.getY();

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

					renderer.drawRoom(xcoord, ycoord, r);

				} else {
					renderer.rooms.add(new GraphicObject(new Point(i, j),
							new Rectangle(new Point(xcoord, ycoord),
									new Dimension(roomSize, roomSize)),
							Color.black, false, null));
				}

			}
		}

		for (int i = 0; i < renderer.rooms.size(); i++) {
			DrawUtils.fillGraphicObject(
					((GraphicObject) renderer.rooms.get(i)), g2);
		}
		for (int i = 0; i < renderer.walls.size(); i++) {
			DrawUtils.fillGraphicObject(
					((GraphicObject) renderer.walls.get(i)), g2);
		}
		for (int i = 0; i < renderer.doors.size(); i++) {
			Object o = renderer.doors.get(i);
			DrawUtils.fillGraphicObject(((GraphicObject) o), g2);
		}
		for (int i = 0; i < renderer.positions.size(); i++) {
			DrawUtils.fillGraphicObject(
					((GraphicObject) renderer.positions.get(i)), g2);
		}
		for (int i = 0; i < renderer.spots.size(); i++) {
			DrawUtils.fillGraphicObject(
					((GraphicObject) renderer.spots.get(i)), g2);
		}
		for (int i = 0; i < renderer.shrines.size(); i++) {
			DrawUtils.fillGraphicObject(
					((GraphicObject) renderer.shrines.get(i)), g2);
		}
		for (int i = 0; i < renderer.items.size(); i++) {
			DrawUtils.fillGraphicObject(
					((GraphicObject) renderer.items.get(i)), g2);
		}
		for (int i = 0; i < renderer.chests.size(); i++) {
			DrawUtils.fillGraphicObject(
					((GraphicObject) renderer.chests.get(i)), g2);
		}
		for (int i = 0; i < renderer.monster.size(); i++) {
			GraphicObject o = ((GraphicObject) renderer.monster.get(i));
			if (o != null) {
				DrawUtils.fillGraphicObject(o, g2);
			}

		}

		for (int i = 0; i < renderer.lastWalls.size(); i++) {
			DrawUtils.fillGraphicObject(
					((GraphicObject) renderer.lastWalls.get(i)), g2);
		}

		boolean animationRunning = gui.currentAnimationThreadRunning(gui
				.getFigure().getRoomInfo());

		if (!animationRunning) {
			if (renderer.hero != null) {
				DrawUtils.fillGraphicObject(renderer.hero, g2);
			}
		}

	}

	public Dimension getHeroSize() {
		return new Dimension(
				(int) (roomSize / GraphicObjectRenderer.HERO_SIZE_QUOTIENT_X),
				(int) (roomSize / GraphicObjectRenderer.HERO_SIZE_QUOTIENT_Y));
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
		JDPoint p = new JDPoint(me.getPoint().getX(), me.getPoint().getY());
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

		if (gui.getMainFrame().isNoControl()) {
			p = new JDPoint(-1, -1);
		}

		boolean found = false;

		for (int i = 0; i < renderer.items.size(); i++) {
			GraphicObject ob = ((GraphicObject) renderer.items.get(i));

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
			for (int i = 0; i < renderer.monster.size(); i++) {
				GraphicObject ob = ((GraphicObject) renderer.monster.get(i));
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
			for (int i = 0; i < renderer.positions.size(); i++) {
				GraphicObject ob = (GraphicObject) renderer.positions.get(i);
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
			for (int i = 0; i < renderer.shrines.size(); i++) {
				GraphicObject ob = ((GraphicObject) renderer.shrines.get(i));
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
			for (int i = 0; i < renderer.doors.size(); i++) {
				GraphicObject ob = ((GraphicObject) renderer.doors.get(i));
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
			for (int i = 0; i < renderer.spots.size(); i++) {
				GraphicObject ob = ((GraphicObject) renderer.spots.get(i));
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
			for (int i = 0; i < renderer.chests.size(); i++) {
				GraphicObject ob = ((GraphicObject) renderer.chests.get(i));
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

			if (renderer.hero.hasPoint(p)) {
				gui.getControl().heroClicked();
				found = true;

			}
		}
		if (!found) {
			for (int i = 0; i < renderer.rooms.size(); i++) {
				GraphicObject ob = ((GraphicObject) renderer.rooms.get(i));
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

		for (int i = 0; i < renderer.items.size(); i++) {
			GraphicObject ob = ((GraphicObject) renderer.items.get(i));
			if ((ob != null) && ob.hasPoint(p)) {
				itemCrossed(ob.getClickedObject());
				found = true;
				break;
			}
		}

		if (!found) {

			for (int i = 0; i < renderer.monster.size(); i++) {
				GraphicObject ob = ((GraphicObject) renderer.monster.get(i));
				if ((ob != null) && ob.hasPoint(p)) {
					monsterCrossed(ob.getClickedObject());
					found = true;
					break;
				}
			}
		}
		if (!found) {
			for (int i = 0; i < renderer.positions.size(); i++) {
				GraphicObject ob = (GraphicObject) renderer.positions.get(i);
				if (ob.hasPoint(p)) {
					if ((((GraphicObject) ob).getClickedObject()) != null)
						positionCrossed(((GraphicObject) ob).getClickedObject());
					found = true;
					break;
				}
			}
		}
		if (!found) {
			for (int i = 0; i < renderer.spots.size(); i++) {
				GraphicObject ob = ((GraphicObject) renderer.spots.get(i));
				if ((ob != null) && ob.hasPoint(p)) {
					spotCrossed(ob.getClickedObject());
					found = true;
					break;
				}
			}
		}
		if (!found) {
			for (int i = 0; i < renderer.shrines.size(); i++) {
				GraphicObject ob = ((GraphicObject) renderer.shrines.get(i));
				if (ob.hasPoint(p)) {
					shrineCrossed(ob.getClickedObject());
					found = true;
					break;
				}
			}
		}
		if (!found) {
			for (int i = 0; i < renderer.doors.size(); i++) {
				GraphicObject ob = ((GraphicObject) renderer.doors.get(i));
				if (ob.hasPoint(p)) {
					doorCrossed((DoorInfo) ob.getClickedObject());
					found = true;
					break;
				}
			}
		}
		if (!found) {
			for (int i = 0; i < renderer.chests.size(); i++) {
				GraphicObject ob = ((GraphicObject) renderer.chests.get(i));
				if (ob.hasPoint(p)) {
					chestCrossed(ob.getClickedObject());
					found = true;
					break;
				}
			}
		}
		if (!found) {
			if (renderer.hero != null) {
				if (renderer.hero.hasPoint(p)) {
					heroCrossed();
					found = true;
				}
			}
		}

		if (!found) {
			for (int i = 0; i < renderer.rooms.size(); i++) {
				GraphicObject ob = ((GraphicObject) renderer.rooms.get(i));
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
		for (int i = 0; i < renderer.rooms.size(); i++) {
			ob = ((GraphicObject) renderer.rooms.get(i));
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

	/**
	 * @return Returns the luzia_ball_greyImage.
	 */
	public JDImageProxy<?> getLuzia_ball_greyImage() {
		return ImageManager.luzia_ball_greyImage;
	}

	/**
	 * @return Returns the luzia_ball_redImage.
	 */
	public JDImageProxy<?> getLuzia_ball_redImage() {
		return ImageManager.luzia_ball_redImage;
	}

	/**
	 * @return Returns the puff.
	 */
	public AnimationSet getPuff() {
		return ImageManager.puff;
	}
}