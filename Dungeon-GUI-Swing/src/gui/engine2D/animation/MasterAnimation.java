/*
 * Created on 25.11.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gui.engine2D.animation;

import figure.Figure;
import figure.FigureInfo;
import figure.RoomObservationStatus;
import figure.hero.HeroInfo;
import figure.monster.MonsterInfo;
import game.Game;
import game.JDEnv;
import graphics.GraphicObject;
import graphics.GraphicObjectRenderer;
import graphics.JDGraphicObject;
import gui.MyJDGui;
import gui.engine2D.GraphBoard;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import dungeon.JDPoint;
import dungeon.Room;
import dungeon.RoomInfo;

public class MasterAnimation extends Thread implements Runnable {

	public final int TIMESTEP = 50;

	Vector animations = new Vector();

	int counter = 0;

	Graphics g2;

	Image offscreenImage;

	int roomSize;

	GraphBoard bild;

	RoomInfo r;

	MyJDGui gui;

	Graphics g;

	public MasterAnimation(int size, GraphBoard bord, RoomInfo r, MyJDGui gui) {
		//System.out.println("erzeuge MasterAni : "+r.toString());
		roomSize = size;
		this.gui = gui;
		bild = bord;
		g = gui.getGraphics();
		this.r = r;
	}

	Vector additionalAnis = new Vector();

	public void addAnimation(Animation ani) {
		additionalAnis.add(new AnimationTask(ani, counter));
	}

	public void addAnimation(Animation ani, int offset) {
		// System.out.println("adding new ani mit offset:"+offset);
		additionalAnis.add(new AnimationTask(ani, counter + offset));
	}

	public void addAnimationAt(Animation ani, int time) {
		// System.out.println("adding new ani mit offset:"+offset);
		additionalAnis.add(new AnimationTask(ani, time));
	}

	boolean stop = false;

	public void myStop() {
		stop = true;
	}

	boolean onlyDeath = false;

	public void onlyDeath() {
		onlyDeath = true;
	}

	public void addAnimationAsNext(Animation ani) {
		int max = -1;
		AnimationTask maxAni = null;
		for (Iterator iter = animations.iterator(); iter.hasNext();) {
			AnimationTask element = (AnimationTask) iter.next();
			int time = element.getRound();
			if (time > max) {
				max = time;
				maxAni = element;
			}
		}
		for (Iterator iter = additionalAnis.iterator(); iter.hasNext();) {
			AnimationTask element = (AnimationTask) iter.next();
			int time = element.getRound();
			if (time > max) {
				max = time;
				maxAni = element;
			}
		}
		if (maxAni != null) {
			additionalAnis.add(new AnimationTask(ani, maxAni.getRound()
					+ maxAni.getLength()));
		} else {
			addAnimation(ani);
		}
	}

	HashMap toPaint = new HashMap();

	LinkedList paintedObs = new LinkedList();

	LinkedList oldAnis = new LinkedList();

	public void run() {
		
		work();
		
		finished = true;
		gui.repaintPicture();
		
		//waitForFurtherAnis();
	}
	
	private void waitForFurtherAnis() {
		Thread thisThread = Thread.currentThread();
		
		while(animations.size() == 0 && additionalAnis.size() == 0) {
			try {
				thisThread.sleep(TIMESTEP);

			} catch (Exception e) {
				System.out.println("masteranimation cannot sleep"+"\n"+e.toString());
			}
		}
		run();
	}
	
	private void work() {
		
		//System.out.println("starte work MasterAni");
		Thread thisThread = Thread.currentThread();
		// System.out.println("anis : " + animations.size());
		while (animations.size() > 0 || additionalAnis.size() > 0) {
			//System.out.println("ani: " + animations.size() + " - addi: "+ additionalAnis.size());
			if (stop)
				break;
			
			try {
				thisThread.sleep(TIMESTEP);

			} catch (Exception e) {
				System.out.println("masteranimation cannot sleep");
			}
			int visStat = gui.getFigure().getVisStatus(r);
			// System.out.println("visStat:"+visStat);
			if (visStat < RoomObservationStatus.VISIBILITY_FIGURES) {
				// System.out.println("break");
				break;
			}

			animations.addAll(additionalAnis);
			additionalAnis.clear();

			toPaint.clear();
			paintedObs.clear();
			oldAnis.clear();
			;

			for (Iterator iter = animations.iterator(); iter.hasNext();) {

				AnimationTask actuallAnimation = (AnimationTask) iter.next();
				boolean death = actuallAnimation.getAni().deathAnimation;
				FigureInfo f = actuallAnimation.getFigure();
				if (!death) {
					if (f.isDead() != null && f.isDead().booleanValue()) {

						oldAnis.add(actuallAnimation);
						continue;
					}
				}
				
				if(!f.getRoomInfo().equals(this.r)) {
					//wenn er gar nicht mehr in dem Raum ist.
					oldAnis.add(actuallAnimation);
					continue;
				}

				int num = counter - actuallAnimation.getRound();
				if (num >= 0 && num < actuallAnimation.getLength()) {
					toPaint.put(actuallAnimation, new Integer(num));
					paintedObs.add(actuallAnimation.getFigure());
				}
				if (num >= actuallAnimation.getLength() - 1) {

					oldAnis.add(actuallAnimation);

				}

			}

			offscreenImage = bild.createImage(roomSize, roomSize
					- (roomSize / 15));
			if (offscreenImage == null) {
				break;
			}
			g2 = offscreenImage.getGraphics();

			// System.out.println("rufe small auf mit: ");
			// GraphBoard.printList(paintedObs);
			if (stop)
				break;
			bild.repaintRoomSmall(g2, r, paintedObs);

			for (Iterator iter = toPaint.keySet().iterator(); iter.hasNext();) {

				AnimationTask actuallAnimation = (AnimationTask) iter.next();

				actuallAnimation.getAni().paintPic(
						((Integer) toPaint.get(actuallAnimation)).intValue(),
						g2);

			}

			JDPoint p = getPoint(r);

			int y2 = roomSize - (roomSize / 15);

			g.drawImage(offscreenImage, p.getX(), p.getY(), roomSize, roomSize
					- (roomSize / 15), null);

			for (Iterator iter = oldAnis.iterator(); iter.hasNext();) {
				// System.out.println("removing Ani");
				Object element = (Object) iter.next();
				animations.remove(element);

			}
			counter++;
		}
//		animations.clear();
//		additionalAnis.clear();
	}

	public boolean finished = false;

	public boolean isFinished() {
		return finished;
	}

	private Dimension getSize(Object o) {
		if (o instanceof HeroInfo) {
			return bild.getHeroSize();
		} else if (o instanceof MonsterInfo) {
			return GraphicObjectRenderer.getMonsterSize((MonsterInfo) o, roomSize);
		} else {
			return null;
		}

	}

	protected JDPoint getPoint(RoomInfo r) {
		int x = r.getPoint().getX();
		int j = r.getPoint().getY();
		int xcoord = bild.getOffset() + (roomSize * x);
		int ycoord = bild.getOffset() + (roomSize * j);
		return new JDPoint(xcoord, ycoord);
	}

	/**
	 * @return Returns the animations.
	 */
	public Vector getAnimations() {
		return animations;
	}
	
	public void resetQueue() {
		animations.clear();
	}

	/**
	 * @return Returns the r.
	 */
	public RoomInfo getRoom() {
		return r;
	}

	/**
	 * @param r
	 *            The r to set.
	 */
	public void setRoom(RoomInfo r) {
		this.r = r;
	}

	// private List makeObjectsList() {
	// List l = new LinkedList();
	// for (Iterator iter = animations.keySet().iterator(); iter.hasNext();) {
	// Animation element = (Animation) iter.next();
	// l.add(element.getObject());
	// }
	// return l;
	// }

}
