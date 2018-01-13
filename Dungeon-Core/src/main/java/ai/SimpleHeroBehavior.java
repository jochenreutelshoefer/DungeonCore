/*
 * Created on 09.06.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ai;

import figure.FigureInfo;
import figure.action.Action;
import figure.action.result.ActionResult;
import figure.hero.HeroInfo;
import figure.monster.MonsterInfo;
import figure.percept.Percept;
import game.ControlUnit;
import item.DustItem;
import dungeon.Door;
import dungeon.JDPoint;

/**
 * Einfachste rein zufallsbasierte Steuerung eines Helden. Nur fuer Testzwecke.
 */
public class SimpleHeroBehavior extends AbstractAI implements ControlUnit {

	private HeroInfo h;

	public SimpleHeroBehavior() {
		super(new AttitudeDefaultHero());
	}

	@Override
	public void setFigure(FigureInfo f) {
		if (f instanceof HeroInfo) {
			h = (HeroInfo) f;
		}
	}



	@Override
	protected void processPercept(Percept p) {

	}

	@Override
	public void notifyVisibilityStatusDecrease(JDPoint p) {

	}

	@Override
	public boolean isHostileTo(FigureInfo f) {
		if (f instanceof MonsterInfo) {
			return true;
		}
		return false;
	}

	@Override
	public void actionProcessed(Action a, ActionResult res) {

	}

	@Override
	public Action getAction() {
		if (h.getRoomInfo().fightRunning() != null
				&& h.getRoomInfo().fightRunning().booleanValue()) {
			return chooseFightAction();
		} else {
			return chooseMovementAction();
		}
	}

	protected int getRandomFleeDir() {
		int doors[] = h.getRoomDoors();
		boolean poss[] = new boolean[4];
		boolean dirExisting = false;
		for (int i = 0; i < doors.length; i++) {
			if (doors[i] == Door.DOOR || doors[i] == Door.DOOR_LOCK_OPEN) {
				poss[i] = true;
				dirExisting = true;
			} else {
				poss[i] = false;
			}
		}

		if (!dirExisting) {
			return 0;
		}
		int k = -1;
		int dir = ((int) (Math.random() * 4));
		while (k == -1) {
			if (poss[dir]) {
				k = dir;
			}
			dir = ((int) (Math.random() * 4));
		}
		return k;
	}

	@Override
	public Action chooseMovementAction() {
		if (h.getRoomItems().length > 0) {
			if ((h.getRoomItems())[0].getItemClass() != DustItem.class) {

			} else {
				// return Action.makeActionTakeItem(h.getFighterID(),i);
			}
		}
		if (h.getActionPoints() < 2) {
			return Action.makeEndRoundAction();
		}

		boolean possible = false;
		int cnt = 0;
		int dir = 0;
		int doors[] = h.getRoomDoors();
		while (!possible) {
			cnt++;
			if (cnt > 10)
				return null;
			dir = ((int) (Math.random() * 4)) + 1;

			possible = (doors[dir - 1] == 1 || doors[dir - 1] == 2);
		}

		Action a = Action.makeActionMove(h.getFighterID(), dir);

		return a;
	}

	@Override
	public Action chooseFightAction() {
		Action a = null;
		if (this.h.getHealthLevel() <= 2 && Math.random() < 0.3) {
			a = Action.makeActionFlee();
			this.h.checkAction(a);

			return a;
		}
		a = Action.makeActionAttack(0);
		this.h.checkAction(a);

		return a;
	}

}
