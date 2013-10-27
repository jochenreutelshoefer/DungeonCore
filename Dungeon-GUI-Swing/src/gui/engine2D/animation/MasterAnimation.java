/*
 * Created on 25.11.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gui.engine2D.animation;

import figure.FigureInfo;
import figure.RoomObservationStatus;
import gui.MyJDGui;
import gui.engine2D.GraphBoard;

import java.awt.Graphics;
import java.awt.Image;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import dungeon.JDPoint;
import dungeon.RoomInfo;

public class MasterAnimation extends Thread implements Runnable {

	public final int TIMESTEP = 50;

	private final Vector<AnimationTask> animations = new Vector<AnimationTask>();

	private int counter = 0;

	private Graphics g2;

	private Image offscreenImage;

	private final int roomSize;

	private final GraphBoard bild;

	private RoomInfo r;

	private final MyJDGui gui;

	private final Graphics g;

	private final Vector<AnimationTask> additionalAnis = new Vector<AnimationTask>();

	private final Map<AnimationTask, Integer> toPaint = new HashMap<AnimationTask, Integer>();
	
	private final List<FigureInfo> paintedObs = new LinkedList<FigureInfo>();
	
	private final List<AnimationTask> oldAnis = new LinkedList<AnimationTask>();

	public MasterAnimation(int size, GraphBoard bord, RoomInfo r, MyJDGui gui) {
		roomSize = size;
		this.gui = gui;
		bild = bord;
		g = gui.getGraphics();
		this.r = r;
	}


	public void addAnimation(Animation ani) {
		additionalAnis.add(new AnimationTask(ani, counter));
	}

	public void addAnimationAt(Animation ani, int time) {
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
		for (Iterator<AnimationTask> iter = animations.iterator(); iter.hasNext();) {
			AnimationTask element = iter.next();
			int time = element.getRound();
			if (time > max) {
				max = time;
				maxAni = element;
			}
		}
		for (Iterator<AnimationTask> iter = additionalAnis.iterator(); iter.hasNext();) {
			AnimationTask element = iter.next();
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


	@Override
	public void run() {
		work();
		finished = true;
		gui.repaintPicture();
	}
	
	
	private void work() {
		while (animations.size() > 0 || additionalAnis.size() > 0) {
			if (stop)
				break;
			try {
				Thread.sleep(TIMESTEP);

			} catch (Exception e) {
				System.out.println("masteranimation cannot sleep");
			}
			int visStat = gui.getFigure().getVisStatus(r);
			if (visStat < RoomObservationStatus.VISIBILITY_FIGURES) {
				break;
			}

			animations.addAll(additionalAnis);
			additionalAnis.clear();

			toPaint.clear();
			paintedObs.clear();
			oldAnis.clear();
			;

			for (Iterator<AnimationTask> iter = animations.iterator(); iter.hasNext();) {
				AnimationTask actuallAnimation = iter.next();
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

			if (stop)
				break;
			bild.repaintRoomSmall(g2, r, paintedObs);

			for (Iterator<AnimationTask> iter = toPaint.keySet().iterator(); iter.hasNext();) {
				AnimationTask actuallAnimation = iter.next();
				actuallAnimation.getAni().paintPic(
						toPaint.get(actuallAnimation).intValue(),
						g2);
			}

			JDPoint p = getPoint(r);
			g.drawImage(offscreenImage, p.getX(), p.getY(), roomSize, roomSize
					- (roomSize / 15), null);
			for (Iterator<AnimationTask> iter = oldAnis.iterator(); iter.hasNext();) {
				Object element = iter.next();
				animations.remove(element);
			}
			counter++;
		}
	}

	public boolean finished = false;

	public boolean isFinished() {
		return finished;
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
	public Vector<AnimationTask> getAnimations() {
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
