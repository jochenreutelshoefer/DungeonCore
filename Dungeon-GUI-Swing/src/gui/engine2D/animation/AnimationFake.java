/*
 * Created on 10.02.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gui.engine2D.animation;

import figure.FigureInfo;
import figure.hero.HeroInfo;
import figure.monster.MonsterInfo;
import game.JDGUI;
import graphics.GraphicObject;
import graphics.GraphicObjectRenderer;
import graphics.JDImageProxy;
import graphics.util.JDColor;
import graphics.util.JDDimension;
import graphics.util.JDRectangle;
import gui.MyJDGui;
import gui.engine2D.DrawUtils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import dungeon.JDPoint;
import dungeon.RoomInfo;

/**
 * @author Jochen
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AnimationFake extends Animation implements Runnable {

	private JDImageProxy<?> im;
	private JDGUI gui;
	private int roomSize;

	public AnimationFake(JDImageProxy<?> i, FigureInfo o, int type, RoomInfo r,
			MyJDGui gui) {
		super(r);
		im = i;
		aniType = type;
		this.o = o;
		this.gui = gui;
		g = gui.getGraphics();
		bild = gui.getMainFrame().getSpielfeld().getSpielfeldBild();
		roomSize = bild.getRoomSize();
	}

	public int getLength() {
		return 5;
	}

	public void run() {
		while (counter < 5) {
			int t = 60;
			try {
				Thread.sleep(t);
			} catch (Exception e) {
			}

			if (offscreenImage == null) {
				offscreenImage = bild.createImage(roomSize, roomSize
						- (roomSize / 15));
				g2 = offscreenImage.getGraphics();
			}

			bild.repaintRoomSmall(g2, r, o);

			paintPic(counter, g2);
			JDPoint p = getPoint(r);

			g.drawImage(this.offscreenImage, p.getX(), p.getY(), roomSize,
					roomSize - (roomSize / 15), null);
			counter++;
		}
		gui.animationDone();
	}

	public void paintPic(int num, Graphics g) {
		DrawUtils
				.fillGraphicObject(new GraphicObject(new String("hero"),
						new JDRectangle(getPointInRoom(), getSize()),
						JDColor.WHITE, im), g);
	}

	private JDDimension getSize() {
		if (o instanceof HeroInfo) {
			return bild.getHeroSize();
		} else if (o instanceof MonsterInfo) {
			return bild.getMonsterSize((MonsterInfo) o);
		} else {
			return null;
		}

	}

	protected JDPoint getPoint(RoomInfo r) {
		int x = r.getPoint().getX();
		int j = r.getPoint().getY();
		int xcoord = bild.getOffset() + (roomSize * x);
		int ycoord = bild.getOffset() + (roomSize * j);
		return JDPoint.getPoint(xcoord, ycoord);
	}

}
