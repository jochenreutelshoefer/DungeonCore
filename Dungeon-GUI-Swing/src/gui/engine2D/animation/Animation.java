/*
 * Created on 10.02.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gui.engine2D.animation;

import java.awt.Graphics;

import dungeon.JDPoint;
import dungeon.RoomInfo;
import figure.FigureInfo;
import gui.engine2D.GraphBoard;

/**
 * @author Jochen
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class Animation {

	public static final int SLAYS = 1;
	public static final int BEEN_HIT = 2;
	public static final int TIPPING_OVER = 3;
	public static final int RUNNING = 4;
	public static final int WALKING = 5;
	public static final int SORCERING = 6;
	public static final int USING = 7;
	public static final int PAUSE = 8;

	protected FigureInfo o;

	protected int time;

	protected double sizeModifier = 1;


	protected int aniType;

	protected int roomSize;

	protected int counter = 0;

	protected boolean deathAnimation = false;

	protected GraphBoard bild;

	protected RoomInfo r;

	public Animation(RoomInfo r) {
		this.r = r;
	}

	public void setDeathAnimation(boolean deathAnimation) {
		this.deathAnimation = deathAnimation;
	}

	public FigureInfo getObject() {
		return o;
	}

	public RoomInfo getRoomInfo() {
		return r;
	}


	public abstract int getLength();


	public abstract void paintPic(int num, Graphics g);

	/**
	 * @param sizeModifier
	 *            The sizeModifier to set.
	 */
	public void setSizeModifier(double sizeModifier) {
		this.sizeModifier = sizeModifier;
	}

	protected JDPoint getPointInRoom() {
		return bild.getPositionCoordModified(o.getPositionInRoomIndex());
	}

}
