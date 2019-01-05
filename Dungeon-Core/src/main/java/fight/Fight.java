package fight;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import dungeon.Room;
import figure.Figure;
import figure.FigureInfo;
import game.ControlUnit;

/**
 * Ein Kampf enthaelt zwei Parteien, den Helden und die Monster. Wird der Kampf
 * gestartet kommen beide immer abwechseln zum Zug, bis der Kampf ended.
 * 
 */
public class Fight {

	private final Room fightRoom;
	private final List<Figure> fighterList;


	public Fight(Room r, List<Figure> figures) {
		fighterList = figures;
		fightRoom = r;

		List<Figure> l = r.getRoomFigures();
		for (int i = 0; i < l.size(); i++) {
			Figure mon = (l.get(i));
			mon.fightBegins(l);
		}

	}

	public void figureLeaves(Figure f) {
		fighterList.remove(f);
	}
	
	public void figureJoins(Figure f) {
		fighterList.add(f);
	}
	
	public void doFight() {
		
		boolean endFight = false;
		for(int i = 0; i < 3; i++) {
			List<Figure> tempList = new LinkedList<>(fighterList);
			if(tempList.size() <= 1) {
				fightRoom.endFight();
				break;
			}
			for (Figure element : tempList) {
				if (!element.isDead()) {
					boolean disappears = element.fight();
					if (disappears) {
						this.fighterList.remove(element);
						this.fightRoom.figureLeaves(element);
					}

					if (!checkFightOn()) {
						endFight = true;
						break;
					}
				}
			}
			if(endFight) {
				break;
			}
		}
		if(endFight) {
			fightRoom.endFight();
		}
	}
	
	private boolean checkFightOn() {
		
		boolean fightOn = false;
		for (Iterator<Figure> iter = fighterList.iterator(); iter.hasNext();) {
			Figure element = iter.next();
			ControlUnit c = element.getControl();
			if(c == null) {
				return false;
			}
			for (Iterator<Figure> iter2 = fighterList.iterator(); iter2.hasNext();) {
				Figure element2 = iter2.next();
				if(element != element2) {
					boolean b = element.getControl().isHostileTo(FigureInfo.makeFigureInfo(element2,element.getRoomVisibility()));
					if(b) {
						fightOn = true;
						break;
					}
				}
				if(fightOn) {
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