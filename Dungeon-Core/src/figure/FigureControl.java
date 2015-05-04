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

import ai.GuiAI;
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

	private final GuiAI brain;

	private List<Percept> perceptList = new LinkedList<Percept>();

	public FigureControl(FigureInfo f, GuiAI ai) {
		this.f = f;
		this.brain = ai;

	}

	@Override
	public void setFigure(FigureInfo f) {
		this.f = f;
	}

	@Override
	public void notifyVisbilityStatusDecrease(JDPoint p) {
		brain.notifyVisbilityStatusDecrease(p);
	}


	@Override
	public boolean isHostileTo(FigureInfo f) {
		return brain.isHostileTo(f);
	}

	@Override
	public void actionDone(Action a, ActionResult res) {

	}

	@Override
	public void tellPercept(Percept p) {
		brain.tellPercept(p);
		perceptList.add(p);
	}

	public List<Percept> getPerceptList() {
		return perceptList;
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
		if (f.getRoomInfo().fightRunning().booleanValue()) {
			a = brain.chooseFightAction();
		} else {
			a = brain.chooseMovementAction();
		}
		perceptList = new LinkedList<Percept>();
		return a;
	}

}
