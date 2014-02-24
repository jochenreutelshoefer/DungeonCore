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

	private FigureInfo f;

	private final AI brain;

	private List<Percept> perceptList = new LinkedList<Percept>();

	public FigureControl(FigureInfo f, AI ai) {
		this.f = f;
		this.brain = ai;

	}

	@Override
	public void setFigure(FigureInfo f) {
		this.f = f;
	}

	@Override
	public void resetingRoomVisibility(JDPoint p) {
		brain.resetingRoomVisibility(p);
	}

	@Override
	public void onTurn() {

	}

	@Override
	public boolean isHostileTo(FigureInfo f) {
		return brain.isHostileTo(f);
	}

	@Override
	public void actionDone(Action a, ActionResult res) {

	}

	@Override
	public void gameOver() {

	}

	public void repaint() {

	}

	@Override
	public void tellPercept(Percept p) {
		brain.tellPercept(p);
		perceptList.add(p);
	}

	public List<Percept> getPerceptList() {
		return perceptList;
	}

	@Override
	public Action getAction() {
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
