/*
 * Created on 17.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure;

import figure.action.Action;
import figure.action.result.ActionResult;
import figure.percept.Percept;
import game.ControlUnit;

import java.util.LinkedList;
import java.util.List;

import ai.AI;
import dungeon.JDPoint;

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

	private List<Percept> perceptList = new LinkedList<Percept>();

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
	public boolean isHostileTo(FigureInfo otherFigure) {
		if(otherFigure.equals(this.f)) {
			// hopefully not called, but you never know..
			return false;
		}
		return brain.isHostileTo(otherFigure);
	}

	@Override
	public void actionProcessed(Action a, ActionResult res) {

	}

	@Override
	public void tellPercept(Percept p) {
		brain.tellPercept(p);
		perceptList.add(p);
	}


	private void delay() {
		try {
			Thread.sleep(this.delay);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Action getAction() {
		/*
		 * artificial delay to enable smooth visual display of action in the gui
		 */
		delay();

		while (this.paused) {
			delay();
		}

		Action a = null;
		// TODO: test that fight actions really are fight actions etc, to find errors in AIs quicker
		Boolean fightRunning = f.getRoomInfo().fightRunning();
		if (fightRunning != null && fightRunning) {
			a = brain.chooseFightAction();
		} else {
			a = brain.chooseMovementAction();
		}
		perceptList = new LinkedList<Percept>();
		return a;
	}

}
