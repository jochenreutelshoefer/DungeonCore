/*
 * Created on 10.02.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gui.engine2D.animation;
import figure.FigureInfo;
import game.JDEnv;
import gui.MyJDGui;
import gui.audio.AudioSet;
import gui.engine2D.GraphBoard;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import dungeon.JDPoint;
import dungeon.Room;
import dungeon.RoomInfo;

/**
 * @author Jochen
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public abstract class Animation /*extends JDEnv*/ implements Runnable{
	
	public static final int SLAYS = 1;
	public static final int BEEN_HIT = 2;
	public static final int TIPPING_OVER = 3;
	public static final int RUNNING = 4;
	public static final int WALKING = 5;
	public static final int SORCERING = 6;
	public static final int USING = 7;
	public static final int PAUSE = 8;

	protected FigureInfo o;

	public int time;
	Graphics g2;
	
	protected double sizeModifier = 1;

	Image offscreenImage;

	Graphics g;


	int aniType;


	int roomSize;
	int counter = 0;
	
	public boolean deathAnimation = false;
	
	GraphBoard bild;


	RoomInfo r;

	
	MyJDGui f;
	
//private AudioSet finishingSound;
//	
//	public AudioSet getFinishingSound() {
//		return finishingSound;
//	}
//
//
//	public void setFinishingSound(AudioSet finishingSound) {
//		this.finishingSound = finishingSound;
//	}


	public Animation(RoomInfo r) {
		this.r = r;
	}
	public FigureInfo getObject() {
		return o;
	}
	
	public RoomInfo getRoomInfo() {
		return r;
	}

	public abstract void run();
	public abstract int getLength();

	
	public int getType() {
		return aniType;
	}

	
	public Image getOffscreenImage() {
		return offscreenImage;
	}
	/**
	 * @return Returns the sizeModifier.
	 */
	public double getSizeModifier() {
		return sizeModifier;
	}
	
	public abstract void paintPic(int num, Graphics g) ;
		
	
	/**
	 * @param sizeModifier The sizeModifier to set.
	 */
	public void setSizeModifier(double sizeModifier) {
		this.sizeModifier = sizeModifier;
	}

		protected Point getPointInRoom() {
	
			return bild.getPositionCoordModified(o.getPositionInRoomIndex());
			
		}

}
