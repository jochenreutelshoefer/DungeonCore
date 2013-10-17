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
import graphics.GraphicObject;
import graphics.JDImageProxy;
import graphics.util.JDRectangle;
import gui.MyJDGui;
import gui.engine2D.DrawUtils;

import java.awt.Graphics;

import util.JDColor;
import util.JDDimension;
import dungeon.JDPoint;
import dungeon.RoomInfo;

/**
 * @author Jochen
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AnimationFake extends Animation {

	private JDImageProxy<?> im;
	private int roomSize;

	public AnimationFake(JDImageProxy<?> i, FigureInfo o, int type, RoomInfo r,
			MyJDGui gui) {
		super(r);
		im = i;
		aniType = type;
		this.o = o;
		bild = gui.getMainFrame().getSpielfeld().getSpielfeldBild();
		roomSize = bild.getRoomSize();
	}

	public int getLength() {
		return 5;
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
