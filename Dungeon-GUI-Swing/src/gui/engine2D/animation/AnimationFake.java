/*
 * Created on 10.02.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gui.engine2D.animation;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import dungeon.JDPoint;
import dungeon.Room;
import dungeon.RoomInfo;
import figure.FigureInfo;
import figure.hero.Hero;
import figure.hero.HeroInfo;
import figure.monster.Monster;
import figure.monster.MonsterInfo;
import game.JDGUI;
import graphics.JDImageProxy;
import gui.MyJDGui;
import gui.engine2D.GraphBoard;
import gui.engine2D.GraphicObject;


/**
 * @author Jochen
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AnimationFake extends Animation implements Runnable {
	JDImageProxy im;
	
	
	
	int [] pointsModsY;
	JDGUI gui;
	
	public AnimationFake(JDImageProxy i, FigureInfo o,int type, RoomInfo r,MyJDGui gui) {
		super(r);
		im = i;
		aniType = type;
		this.o = o;
		this.gui = gui;
		g = gui.getGraphics();
		bild = gui.getMainFrame().getSpielfeld()
		.getSpielfeldBild();
		roomSize = bild.getRoomSize();
		//r = RoomInfo.makeRoomInfo(game.getHero().getRoom());
		
		
		
	}
	
	public int getLength() {
		return 5;
	}
	
	
	
	private int getModY(int k) {
		int []  hit = {0,-roomSize/20,-roomSize/10,-roomSize/20,0};
		int []  slay = {0,roomSize/20,roomSize/10,roomSize/20,0};
		int res = 0;
		if(aniType == AnimationReal.BEEN_HIT) {
			res = hit[k];
		}
		if(aniType == AnimationReal.SLAYS) {
			res = slay[k];
		}
		return res;
	}
	
	public void run() {
		Thread thisThread = Thread.currentThread();
		while (counter < 5) {
			int t =	60;
			try{thisThread.sleep(t);}catch(Exception e){}

			if(offscreenImage == null) {
				//System.out.println("OffscreenImage ist null!");
				offscreenImage = bild.createImage(roomSize, roomSize -(roomSize/15));
				g2 = offscreenImage.getGraphics();
			}
			
		
			bild.repaintRoomSmall(g2,r,o);
						
			
			//Image im = set.getImagesNr(counter);
//			JDPoint heroRoom = game.getGui().getMainFrame().getSpielfeld()
//					.getSpielfeldBild().getHeroRoomPos();
			paintPic(counter,g2);
			
			
			JDPoint p = getPoint(r);
			
			g.drawImage(this.offscreenImage,p.getX(),p.getY(),roomSize,roomSize-(roomSize/15),null);
			counter++;
		}
		gui.animationDone();
	}
	
	public void paintPic(int num, Graphics g) {
		new GraphicObject(
				new String("hero"),
				new Rectangle(
					getPointInRoom(),
					getSize()),
				Color.white,
				im).fill(g);
	}
	
	private Dimension getSize() {
		if(o instanceof HeroInfo) {
			return bild.getHeroSize();
		}
		else if(o instanceof MonsterInfo) {
			return bild.getMonsterSize((MonsterInfo)o);
		}
		else {
			return null;
		}
		
	}
	
	protected JDPoint getPoint(RoomInfo r) {
		int x = r.getPoint().getX();
		int j = r.getPoint().getY();
		int xcoord = bild.getOffset() + (roomSize * x);
		int ycoord = bild.getOffset() + (roomSize * j);
		return JDPoint.getPoint(xcoord,ycoord);
	}
	
	
}
