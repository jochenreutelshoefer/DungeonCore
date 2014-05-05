/*
 * 
 * Created on 08.02.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gui.engine2D.animation;

import java.awt.Graphics;
import java.util.Set;

import util.JDColor;
import util.JDDimension;
import animation.AnimationSet;
import audio.AbstractAudioSet;
import dungeon.JDPoint;
import dungeon.RoomInfo;
import figure.FigureInfo;
import figure.hero.HeroInfo;
import figure.monster.MonsterInfo;
import graphics.GraphicObject;
import graphics.JDImageProxy;
import graphics.util.JDRectangle;
import gui.AbstractJDGUIEngine2D;
import gui.engine2D.DrawUtils;

/**
 * @author Jochen
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AnimationReal extends Animation {

	private final AnimationSet set;
	private int fromPosIndex = -1;
	private int toPosIndex = -1;
	private final JDPoint startFigurePositionInRoom;

	public AnimationReal(AnimationSet set, FigureInfo o, int type, RoomInfo r,
			AbstractJDGUIEngine2D gui) {
		super(r);
		if (o == null) {
			throw new NullPointerException("FigureInfo was null");
		}
		this.set = set;
		time = 35;
		aniType = type;
		this.o = o;
		bild = gui.getBoard().getSpielfeldBild();
		startFigurePositionInRoom = bild.getPositionCoordModified(o
				.getPositionInRoomIndex());
		roomSize = bild.getRoomSize();
	}

	@Override
	public int getLength() {
		if (set == null) {
			return 0;
		}
		return set.getLength();
	}

	@Override
	public void paintPic(int num, Graphics g) {
		if (o.isDead() != null && o.isDead().booleanValue()
				&& !this.deathAnimation)
			return;
		JDImageProxy<?> im = set.getImagesNr(num);
		JDDimension d = getSize();

		int positionInRoomIndex = o.getPositionInRoomIndex();
		if (positionInRoomIndex == -1)
			return;
		JDPoint point = bild.getPositionCoordModified(positionInRoomIndex);
		RoomInfo roomInfo = o.getRoomInfo();
		if (!roomInfo.fightRunning().booleanValue()) {
			point = this.startFigurePositionInRoom;
		}
		int x = (point.getX()) + getMoveModX(num);
		int y = (point.getY()) + getMoveModY(num);
		if (toPosIndex == -1 || fromPosIndex == -1) {
			x = (getPointInRoom().getX());
			y = (getPointInRoom().getY());
		}

		/*
		 * play sound during animation
		 */
		Set<AbstractAudioSet> sounds = set.getSound(num);
		if (sounds != null) {
			for (AbstractAudioSet audioSet : sounds) {
				if (audioSet != null) {
					audioSet.playRandomSound();
				}
			}
		}

		JDPoint p = new JDPoint(x - (d.getWidth() / 2),
				y - (d.getHeight() / 2));
		GraphicObject c = new GraphicObject(null, new JDRectangle(p, getSize()),
				JDColor.WHITE, im);
		DrawUtils.fillGraphicObject(c, g);
	}

	private int getMoveModX(int num) {
		if (toPosIndex == -1 || fromPosIndex == -1) {
			return 0;
		}
		int xFrom = bild.getPositionCoord(fromPosIndex).x;
		int xTo = bild.getPositionCoord(toPosIndex).x;
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
		int yFrom = bild.getPositionCoord(fromPosIndex).y;
		int yTo = bild.getPositionCoord(toPosIndex).y;
		int diff = yTo - yFrom;
		int setSize = set.getLength();
		float factor = ((float) num) / setSize;
		int offset = (int) (factor * diff);
		return offset;
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