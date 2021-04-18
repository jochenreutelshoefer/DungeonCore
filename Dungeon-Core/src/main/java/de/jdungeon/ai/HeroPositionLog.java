package de.jdungeon.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.RoomInfo;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.hero.HeroInfo;
import de.jdungeon.figure.percept.DoorSmashPercept;
import de.jdungeon.figure.percept.FleePercept;
import de.jdungeon.figure.percept.HitPercept;
import de.jdungeon.figure.percept.LeavesPercept;
import de.jdungeon.figure.percept.MissPercept;
import de.jdungeon.figure.percept.EntersPercept;
import de.jdungeon.figure.percept.Percept;
import de.jdungeon.figure.percept.ScoutPercept;
import de.jdungeon.figure.percept.StepPercept;

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
        //Log.info("Setting hero position de.jdungeon.log last pos to: "+lastHeroLocation + " (round "+ lastHeroLocationInfoRound + ") ["+element.getClass().getName()+"]");
        this.lastHeroLocation = lastHeroLocation;
        this.lastHeroLocationInfoRound = lastHeroLocationInfoRound;
    }

    private final Map<JDPoint, Integer> lastVisits = new HashMap<>();

    public JDPoint getLastHeroPosition() {
        return lastHeroLocation;
    }

    public void tellPecept(Percept p) {
        synchronized (perceptList) {
            perceptList.add(p);
        }
    }

    public boolean lastEnemyPositionVisited() {
        if (lastVisits.containsKey(lastHeroLocation)) {
            if (lastVisits.get(lastHeroLocation) >= lastHeroLocationInfoRound) {
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
                if (((EntersPercept) element).getFigure(this.owner) instanceof HeroInfo && !(element.getRound() < lastHeroLocationInfoRound)) {
                    setLastHeroLocationInfoRound(element.getRound(), ((EntersPercept) element).getTo(this.owner).getPoint(), element);
                }

                // de.jdungeon.log self movement
                FigureInfo perceptFighter = ((EntersPercept) element).getFigure(this.owner);
                if (perceptFighter == null) {
                    // happens when players leaves the de.jdungeon.level
                    return;
                }
                int ownerID = owner.getFigureID();
                if (perceptFighter.getFigureID() == ownerID) {
                    lastVisits.put(((EntersPercept) element).getTo(this.owner).getPoint(), element.getRound());
                }

            }
            if (element instanceof LeavesPercept) {
                if (((LeavesPercept) element).getFigure(this.owner) instanceof HeroInfo && !(element.getRound() < lastHeroLocationInfoRound)) {
                    setLastHeroLocationInfoRound(element.getRound(), ((LeavesPercept) element).getTo(this.owner).getRoomNumber(), element);
                }
            }
            if (element instanceof FleePercept) {
                if (((FleePercept) element).getFigure(this.owner) instanceof HeroInfo && !(element.getRound() < lastHeroLocationInfoRound)) {
                    int dir = ((FleePercept) element).getDir();
                    RoomInfo r = ((FleePercept) element).getRoom(this.owner);
                    setLastHeroLocationInfoRound(element.getRound(), r.getNeighbourRoom(dir).getNumber(), element);
                }
            }
            if (element instanceof HitPercept) {
                if (((HitPercept) element).getAttacker(this.owner) instanceof HeroInfo && !(element.getRound() < lastHeroLocationInfoRound)) {
                    RoomInfo r = ((HitPercept) element).getAttacker(this.owner).getRoomInfo();
                    setLastHeroLocationInfoRound(element.getRound(), r.getNumber(), element);
                }
            }

            if (element instanceof DoorSmashPercept) {
                if (((DoorSmashPercept) element).getVictim(this.owner) instanceof HeroInfo && !(element.getRound() < lastHeroLocationInfoRound)) {
                    setLastHeroLocationInfoRound(element.getRound(), this.lastHeroLocation = ((DoorSmashPercept) element).getVictimPosition(), element);
                }
                if (((DoorSmashPercept) element).getOpponent(this.owner) instanceof HeroInfo && !(element.getRound() < lastHeroLocationInfoRound)) {
                    setLastHeroLocationInfoRound(element.getRound(), this.lastHeroLocation = ((DoorSmashPercept) element).getOpponentPosition(), element);
                }
            }
            if (element instanceof MissPercept) {
                if (((MissPercept) element).getAttacker(this.owner) instanceof HeroInfo && !(element.getRound() < lastHeroLocationInfoRound)) {
                    RoomInfo r = ((MissPercept) element).getAttacker(this.owner).getRoomInfo();
                    setLastHeroLocationInfoRound(element.getRound(), r.getNumber(), element);
                }
            }
            // TODO: refactor
            if (element instanceof StepPercept) {
                if (((StepPercept) element).getFigure(this.owner) instanceof HeroInfo && !(element.getRound() < lastHeroLocationInfoRound)) {
                    RoomInfo r = ((StepPercept) element).getFigure(this.owner).getRoomInfo();
                    // exit used, de.jdungeon.level ends -> r might be null;
                    if (r != null) {
                        setLastHeroLocationInfoRound(element.getRound(), r.getNumber(), element);
                    }
                }
            }
            if (element instanceof ScoutPercept) {
                if (((ScoutPercept) element).getFigure(this.owner) instanceof HeroInfo && !(element.getRound() < lastHeroLocationInfoRound)) {
                    RoomInfo r = ((ScoutPercept) element).getRoom(this.owner);
                    setLastHeroLocationInfoRound(element.getRound(), r.getNumber(), element);
                }
            }
        }
    }


    static class PerceptComparator implements Comparator<Percept> {

        @Override
        public int compare(Percept o1, Percept o2) {
            if (o1 != null && o2 != null) {
                if ((o1).getRound() < (o2).getRound()) {
                    return 1;
                } else if ((o1).getRound() > (o2).getRound()) {
                    return -1;
                }

            }
            return 0;
        }

    }
}
