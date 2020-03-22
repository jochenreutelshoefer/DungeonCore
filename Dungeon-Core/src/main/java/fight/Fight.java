package fight;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import dungeon.Room;
import figure.Figure;
import figure.FigureInfo;
import game.ControlUnit;

/**
 *
 */
public class Fight {

	private final Room fightRoom;
	private final List<Figure> fighterList;

	public Fight(Room r, int round) {
		List<Figure> figuresSorted = new LinkedList<Figure>(r.getRoomFigures());
		Collections.sort(figuresSorted, new MyFightOrderComparator());
		fighterList = figuresSorted;
		fightRoom = r;

		List<Figure> l = r.getRoomFigures();
		for (int i = 0; i < l.size(); i++) {
			Figure mon = (l.get(i));
			mon.fightBegins(l, round);
		}
	}

	static class MyFightOrderComparator implements Comparator<Figure> {

		@Override
		public int compare(Figure o1, Figure o2) {
			if (o1 != null && o2 != null) {
				double readiness1 = o1.getReadiness();
				double readiness2 = o2.getReadiness();
				if (o1.isRaiding()) {
					return -1;
				}
				if (o2.isRaiding()) {
					return 1;
				}
				if (readiness1 > readiness2) {
					return -1;
				}
				else if (readiness1 < readiness2) {
					return 1;
				}
			}
			else {
				return 0;
			}
			return 0;
		}
	}

	public void figureLeaves(Figure f) {
		fighterList.remove(f);
	}

	public void figureJoins(Figure f) {
		fighterList.add(f);
	}

	public void doFight(int round) {

		boolean endFight = false;
		List<Figure> tempList = new LinkedList<>(fighterList);
		if (tempList.size() <= 1) {
			fightRoom.endFight();
			return;
		}
		for (Figure element : tempList) {
			if (!element.isDead()) {
				element.setActionPoints(1, round);
				element.doActions(round, true);

				if (!checkFightOn()) {
					endFight = true;
					break;
				}
			}
		}
		if (endFight) {
			fightRoom.endFight();
		}
	}

	private boolean checkFightOn() {

		boolean fightOn = false;
		for (Iterator<Figure> iter = fighterList.iterator(); iter.hasNext(); ) {
			Figure element = iter.next();
			ControlUnit c = element.getControl();
			if (c == null) {
				return false;
			}
			for (Iterator<Figure> iter2 = fighterList.iterator(); iter2.hasNext(); ) {
				Figure element2 = iter2.next();
				if (element != element2) {
					boolean b = element.getControl()
							.isHostileTo(FigureInfo.makeFigureInfo(element2, element.getRoomVisibility()));
					if (b) {
						fightOn = true;
						break;
					}
				}
				if (fightOn) {
					break;
				}
			}
		}
		return fightOn;
	}

	public List<Figure> getFightFigures() {
		return fightRoom.getRoomFigures();
	}
}