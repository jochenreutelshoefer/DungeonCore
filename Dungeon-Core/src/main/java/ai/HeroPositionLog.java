package ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dungeon.JDPoint;
import dungeon.RoomInfo;
import figure.FigureInfo;
import figure.hero.HeroInfo;
import figure.percept.DoorSmashPercept;
import figure.percept.FleePercept;
import figure.percept.HitPercept;
import figure.percept.LeavesPercept;
import figure.percept.MissPercept;
import figure.percept.EntersPercept;
import figure.percept.Percept;
import figure.percept.ScoutPercept;
import figure.percept.StepPercept;
import log.Log;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.01.18.
 */
public class HeroPositionLog {

	private final FigureInfo owner;
	private JDPoint lastHeroLocation = null;

	private int lastHeroLocationInfoRound;

	public HeroPositionLog(FigureInfo owner) {
		this.owner = owner;
	}

	private final List<Percept> perceptList = new LinkedList<>();

	private void setLastHeroLocationInfoRound(int lastHeroLocationInfoRound, JDPoint lastHeroLocation, Percept element) {
		//Log.info("Setting hero position log last pos to: "+lastHeroLocation + " (round "+ lastHeroLocationInfoRound + ") ["+element.getClass().getName()+"]");
		this.lastHeroLocation = lastHeroLocation;
		this.lastHeroLocationInfoRound = lastHeroLocationInfoRound;
	}

	private final Map<JDPoint, Integer> lastVisits = new HashMap<>();

	public JDPoint getLastHeroPosition(){
		return lastHeroLocation;
	}

	public void tellPecept(Percept p) {
		synchronized (perceptList) {
			perceptList.add(p);
		}
	}

	public boolean lastEnemyPositionVisited() {
		if(lastVisits.containsKey(lastHeroLocation)) {
			if(lastVisits.get(lastHeroLocation) >= lastHeroLocationInfoRound) {
				return true;
			}
		}
		return false;
	}

	public synchronized void processPercepts() {
		List<Percept> sortedList = null;
		synchronized (perceptList) {
			sortedList = new ArrayList<>(perceptList);
			Collections.sort(sortedList, new PerceptComparator());
			perceptList.clear();
		}
		for (Percept element : sortedList) {
			if (element instanceof EntersPercept) {
				if (((EntersPercept) element).getFigure() instanceof HeroInfo && !(element.getRound() < lastHeroLocationInfoRound)) {
					setLastHeroLocationInfoRound(element.getRound(), ((EntersPercept) element).getTo().getPoint(), element);
				}

				// log self movement
				FigureInfo perceptFighter = ((EntersPercept) element).getFigure();
				int ownerID = owner.getFighterID();
				if(perceptFighter.getFighterID() == ownerID) {
					lastVisits.put(((EntersPercept) element).getTo().getPoint(), element.getRound());
				}

			}
			if (element instanceof LeavesPercept) {
				if (((LeavesPercept) element).getFigure() instanceof HeroInfo && !(element.getRound() < lastHeroLocationInfoRound)) {
					setLastHeroLocationInfoRound(element.getRound(), ((LeavesPercept) element).getTo().getLocation(), element);
				}
			}
			if (element instanceof FleePercept) {
				if (((FleePercept) element).getFigure() instanceof HeroInfo && !(element.getRound() < lastHeroLocationInfoRound)) {
					int dir = ((FleePercept) element).getDir();
					RoomInfo r = ((FleePercept) element).getRoom();
					setLastHeroLocationInfoRound(element.getRound(), r.getNeighbourRoom(dir).getNumber(), element);
				}
			}
			if (element instanceof HitPercept) {
				if (((HitPercept) element).getAttacker() instanceof HeroInfo && !(element.getRound() < lastHeroLocationInfoRound)) {
					RoomInfo r = ((HitPercept) element).getAttacker().getRoomInfo();
					setLastHeroLocationInfoRound(element.getRound(), r.getNumber(), element);
				}
			}

			if (element instanceof DoorSmashPercept) {
				if (((DoorSmashPercept) element).getVictim() instanceof HeroInfo && !(element.getRound() < lastHeroLocationInfoRound)) {
					setLastHeroLocationInfoRound(element.getRound(), this.lastHeroLocation = ((DoorSmashPercept) element).getVictimPosition(), element);
				}
				if (((DoorSmashPercept) element).getOpponent() instanceof HeroInfo && !(element.getRound() < lastHeroLocationInfoRound)) {
					setLastHeroLocationInfoRound(element.getRound(), this.lastHeroLocation = ((DoorSmashPercept) element).getOpponentPosition(), element);
				}
			}
			if (element instanceof MissPercept) {
				if (((MissPercept) element).getAttacker() instanceof HeroInfo && !(element.getRound() < lastHeroLocationInfoRound)) {
					RoomInfo r = ((MissPercept) element).getAttacker().getRoomInfo();
					setLastHeroLocationInfoRound(element.getRound(),  r.getNumber(), element);
				}
			}
			// TODO: refactor
			if (element instanceof StepPercept) {
				if (((StepPercept) element).getFigure() instanceof HeroInfo && !(element.getRound() < lastHeroLocationInfoRound)) {
					RoomInfo r = ((StepPercept) element).getFigure().getRoomInfo();
					setLastHeroLocationInfoRound(element.getRound(), r.getNumber(), element);
				}
			}
			if (element instanceof ScoutPercept) {
				if (((ScoutPercept) element).getFigure() instanceof HeroInfo && !(element.getRound() < lastHeroLocationInfoRound)) {
					RoomInfo r = ((ScoutPercept) element).getRoom();
					setLastHeroLocationInfoRound(element.getRound(), r.getNumber(), element);
				}
			}
		}
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
