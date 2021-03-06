/*
 * Created on 09.06.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.ai;

import java.util.ArrayList;
import java.util.List;

import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.Action;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.figure.hero.HeroInfo;
import de.jdungeon.figure.monster.MonsterInfo;
import de.jdungeon.figure.percept.Percept;
import de.jdungeon.figure.ControlUnit;
import de.jdungeon.dungeon.Door;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.location.LevelExit;
import de.jdungeon.skill.FleeSkill;

/**
 * Einfachste rein zufallsbasierte Steuerung eines Helden. Nur fuer Testzwecke.
 */
public class SimpleHeroBehavior extends AbstractAI implements ControlUnit {

	private HeroInfo h;
	private ActionAssemblerHelper actionAssembler;
	private final List<Action> moveActionQueue = new ArrayList<>();
	private final List<Action> fightActionQueue = new ArrayList<>();

	public SimpleHeroBehavior() {
		super(new AttitudeDefaultHero());
	}

	@Override
	public void setFigure(FigureInfo f) {
		if (f instanceof HeroInfo) {
			h = (HeroInfo) f;
		}
		actionAssembler = new ActionAssemblerHelper(h);
	}

	@Override
	public void onTurn() {

	}

	@Override
	public void gameOver() {
		// nothing to do
	}

	@Override
	protected void processPercept(Percept p) {

	}

	@Override
	public void notifyVisibilityStatusDecrease(JDPoint p) {

	}

	@Override
	public void exitUsed(LevelExit exit, Figure f) {
		// nothing
	}

	@Override
	public boolean isHostileTo(FigureInfo f) {
		if (f instanceof MonsterInfo) {
			return true;
		}
		return false;
	}

	@Override
	public void actionProcessed(Action action, ActionResult res, int round) {

	}

	@Override
	public Action getAction() {
		if (h.getRoomInfo().fightRunning() != null
				&& h.getRoomInfo().fightRunning().booleanValue()) {
			Action action = chooseFightAction();
			return action;
		} else {
			Action action = chooseMovementAction();
			return action;
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
		if(!moveActionQueue.isEmpty()) {
			return moveActionQueue.remove(0);
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

		moveActionQueue.addAll(actionAssembler.wannaWalk(dir));

		return moveActionQueue.remove(0);
	}

	@Override
	public Action chooseFightAction() {
		Action a = null;
		if (this.h.getHealthLevel().getValue() <= 2 && Math.random() < 0.3) {
			a = this.h.getSkill(FleeSkill.class).newActionFor(h).get();
			ActionResult actionResult = this.h.checkAction(a);
			if(actionResult.getSituation() == ActionResult.Situation.possible) {
				return a;
			}
		}

		if(!fightActionQueue.isEmpty()) {
			return fightActionQueue.remove(0);
		}

		List<FigureInfo> figureInfos = h.getRoomInfo().getFigureInfos();
		FigureInfo hostileFigure = null;
		for (FigureInfo figureInfo : figureInfos) {
			if(figureInfo.isHostile(this.h)) {
				hostileFigure = figureInfo;
				continue;
			}
		}
		if(hostileFigure != null) {
			fightActionQueue.clear();
			fightActionQueue.addAll(actionAssembler.wannaAttack(hostileFigure));

		}

		if(!fightActionQueue.isEmpty()) {
			return fightActionQueue.remove(0);

		} else {
			return actionAssembler.wannaFlee().remove(0);
		}
	}

}
