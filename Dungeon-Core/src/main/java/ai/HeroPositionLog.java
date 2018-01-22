package ai;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import dungeon.JDPoint;
import dungeon.RoomInfo;
import figure.hero.HeroInfo;
import figure.percept.FleePercept;
import figure.percept.MovePercept;
import figure.percept.Percept;
import figure.percept.ScoutPercept;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.01.18.
 */
public class HeroPositionLog {

	private JDPoint lastHeroLocation = null;
	private int lastHeroLocationInfoRound;
	private final List<Percept> perceptList = new LinkedList<>();

	JDPoint getLastHeroPosition(){
		return lastHeroLocation;
	}

	public void tellPecept(Percept p) {
		perceptList.add(p);
	}

	public void processPercepts() {
		Collections.sort(perceptList, new PerceptComparator());
		for (Iterator<Percept> iter = perceptList.iterator(); iter.hasNext();) {
			Percept element = iter.next();
			if(element instanceof MovePercept) {
				if(((MovePercept)element).getFigure() instanceof HeroInfo && !(element.getRound() < lastHeroLocationInfoRound)) {
					this.lastHeroLocation = ((MovePercept)element).getTo().getPoint();
					lastHeroLocationInfoRound = element.getRound();
				}
			}
			if(element instanceof FleePercept) {
				if(((FleePercept)element).getFigure() instanceof HeroInfo && !(element.getRound() < lastHeroLocationInfoRound)) {
					int dir = ((FleePercept)element).getDir();
					RoomInfo r = ((FleePercept)element).getRoom();
					this.lastHeroLocation = r.getNeighbourRoom(dir).getNumber();
					lastHeroLocationInfoRound = element.getRound();
				}
			}
			if(element instanceof ScoutPercept) {
				if(((ScoutPercept)element).getFigure() instanceof HeroInfo && !(element.getRound() < lastHeroLocationInfoRound)) {
					int dir = ((ScoutPercept)element).getDir();
					RoomInfo r = ((ScoutPercept)element).getRoom();
					this.lastHeroLocation = r.getNeighbourRoom(dir).getNumber();
					lastHeroLocationInfoRound = element.getRound();
				}
			}
		}
		perceptList.clear();

	}


	static class PerceptComparator implements Comparator<Percept> {

		@Override
		public int compare(Percept o1, Percept o2) {
			if(o1 != null && o2 != null) {
				if((o1).getRound() < (o2).getRound()) {
					return 1;
				} else if((o1).getRound() > (o2).getRound()) {
					return -1;
				}

			}
			return 0;
		}

	}
}
