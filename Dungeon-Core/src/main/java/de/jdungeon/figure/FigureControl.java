/*
 * Created on 17.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.figure;

import de.jdungeon.figure.action.Action;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.figure.percept.Percept;

import de.jdungeon.ai.AI;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.location.LevelExit;

public class FigureControl implements ControlUnit {

	private boolean paused = false;

	public void pause() {
		paused = true;

	}

	public void run() {
		paused = false;

	}

	private int delay = 0;

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	private FigureInfo f;

	private final AI brain;


	public FigureControl(FigureInfo f, AI ai) {
		if(f == null) {
			throw new IllegalArgumentException("FigureInfo may not be null");
		}
		this.f = f;
		this.brain = ai;

	}

	@Override
	public void setFigure(FigureInfo f) {
		this.f = f;
	}

	@Override
	public void notifyVisibilityStatusDecrease(JDPoint p) {
		brain.notifyVisibilityStatusDecrease(p);
	}

	@Override
	public void notifyVisibilityStatusIncrease(JDPoint p) {
		brain.notifyVisibilityStatusIncrease(p);
	}

	@Override
	public void exitUsed(LevelExit exit, Figure f) {
		// nothing
	}

	@Override
	public boolean isHostileTo(FigureInfo otherFigure) {
		if(otherFigure.equals(this.f)) {
			// hopefully not called, but you never know..
			return false;
		}
		return brain.isHostileTo(otherFigure);
	}

	@Override
	public void actionProcessed(Action action, ActionResult res, int round) {

	}

	@Override
	public void tellPercept(Percept p) {
		if(p != null) {
			brain.tellPercept(p);
		}
	}



	@Override
	public Action getAction() {
		Action a = null;
		// TODO: test that de.jdungeon.fight actions really are de.jdungeon.fight actions etc, to find errors in AIs quicker
		Boolean fightRunning = f.getRoomInfo().fightRunning();
		if (fightRunning != null && fightRunning) {
			a = brain.chooseFightAction();
		} else {
			a = brain.chooseMovementAction();
		}
		return a;
	}

}
