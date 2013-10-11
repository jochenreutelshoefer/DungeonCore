/*
 * 
 * Created on 08.02.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gui.engine2D.animation;

import figure.FigureInfo;
import figure.hero.HeroInfo;
import figure.monster.MonsterInfo;
import game.JDGUI;
import gui.MyJDGui;
import gui.audio.AudioSet;
import gui.engine2D.GraphicObject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Set;

import dungeon.JDPoint;
import dungeon.RoomInfo;

/**
 * @author Jochen
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AnimationReal extends Animation /* implements Runnable */{

	AnimationSet set;
	int fromPosIndex = -1;
	int toPosIndex = -1;
	JDGUI gui;
	boolean inverted = false;
	Point startFigurePositionInRoom;

	/**
	 * @param inverted
	 *            The inverted to set.
	 */
	public void setInverted(boolean inverted) {
		this.inverted = inverted;
	}

	public AnimationReal(AnimationSet set, FigureInfo o, int type, RoomInfo r,
			MyJDGui gui) {
		super(r);
		if(o == null) {
			throw new NullPointerException("FigureInfo was null");
		}
		this.set = set;
		time = 35;
		aniType = type;
		this.gui = gui;
		// times = t;
		this.o = o;
		// set.preLoad();
		g = gui.getGraphics();
		bild = gui.getMainFrame().getSpielfeld().getSpielfeldBild();

		startFigurePositionInRoom = bild.getPositionCoordModified(o
				.getPositionInRoomIndex());
		roomSize = bild.getRoomSize();
		// r = RoomInfo.makeRoomInfo(game.getHero().getRoom());
	}

	public int getLength() {
		if (set == null) {
			return 0;
		}
		return set.getLength();
	}

	public void run() {

	}

	public void paintPic(int num, Graphics g) {
		if (o.isDead() != null && o.isDead().booleanValue()
				&& !this.deathAnimation)
			return;
		Image im = set.getImagesNr(num);
		Dimension d = getSize();
		// Dimension d2 = new Dimension((int)(d.width *
		// sizeModifier),(int)(d.height * sizeModifier));

		int positionInRoomIndex = o.getPositionInRoomIndex();
		if(positionInRoomIndex == -1) return;
		Point point = bild.getPositionCoordModified(positionInRoomIndex);
		RoomInfo roomInfo = o.getRoomInfo();
		if (!roomInfo.fightRunning().booleanValue()) {
			point = this.startFigurePositionInRoom;
		}
		int x = (int) (point.getX()) + getMoveModX(num);
		int y = (int) (point.getY()) + getMoveModY(num);
		if (toPosIndex == -1 || fromPosIndex == -1) {
			x = (int) (getPointInRoom().getX());
			y = (int) (getPointInRoom().getY());
		}

		/*
		 * play sound during animation
		 */
		Set<AudioSet> sounds = set.getSound(num);
		if (sounds != null) {
			for (AudioSet audioSet : sounds) {
				if(audioSet != null) {
					audioSet.playRandomSound();
				}
			}
		}

		Point p = new Point((int) (x - (d.getWidth() / 2)),
				(int) (y - (d.getHeight() / 2)));
		GraphicObject c = new GraphicObject(null, new Rectangle(p, getSize()),
				Color.white, im);
		// c.setFlipped(true);
		c.fill(g);
	}

	private int getMoveModX(int num) {
		if (toPosIndex == -1 || fromPosIndex == -1) {
			return 0;
		}
		int xFrom = this.bild.getPositionCoord(fromPosIndex).x;
		int xTo = this.bild.getPositionCoord(toPosIndex).x;
		int diff = xTo - xFrom;
		int setSize = set.getLength();
		float factor = ((float) num) / setSize;
		int offset = (int) (factor * diff);
		return offset;
	}

	private int getMoveModY(int num) {
		if (toPosIndex == -1 || fromPosIndex == -1) {
			return 0;
		}
		int yFrom = this.bild.getPositionCoord(fromPosIndex).y;
		int yTo = this.bild.getPositionCoord(toPosIndex).y;
		int diff = yTo - yFrom;
		int setSize = set.getLength();
		float factor = ((float) num) / setSize;
		int offset = (int) (factor * diff);
		return offset;
	}

	protected JDPoint getPoint(RoomInfo r) {
		int x = r.getPoint().getX();
		int j = r.getPoint().getY();
		int xcoord = bild.getOffset() + (roomSize * x);
		int ycoord = bild.getOffset() + (roomSize * j);
		return new JDPoint(xcoord, ycoord);
	}

	// private Image getActualImage() {
	// return images[counter];
	// }

	private Dimension getSize() {
		if (o instanceof HeroInfo) {
			return bild.getHeroSize();
		} else if (o instanceof MonsterInfo) {
			return bild.getMonsterSize((MonsterInfo) o);
		} else {
			return null;
		}

	}

	/**
	 * @return Returns the offscreenImage.
	 */
	public Image getOffscreenImage() {
		return offscreenImage;
	}

	/**
	 * @param fromPosIndex
	 *            The fromPosIndex to set.
	 */
	public void setFromPosIndex(int fromPosIndex) {
		this.fromPosIndex = fromPosIndex;
	}

	/**
	 * @param toPosIndex
	 *            The toPosIndex to set.
	 */
	public void setToPosIndex(int toPosIndex) {
		this.toPosIndex = toPosIndex;
	}
}