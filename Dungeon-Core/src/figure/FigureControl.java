/*
 * Created on 17.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure;

import figure.action.Action;
import figure.action.result.ActionResult;
import figure.monster.MonsterInfo;
import figure.percept.Percept;
import game.ControlUnit;

import java.util.LinkedList;
import java.util.List;

import dungeon.JDPoint;

import ai.AI;
import ai.DefaultMonsterIntelligence;

public class FigureControl implements ControlUnit {

	FigureInfo f;

	AI brain;

	List perceptList = new LinkedList();

	public FigureControl(FigureInfo f, AI ai) {
		this.f = f;
		this.brain = ai;

	}

	public void setFigure(FigureInfo f) {
		this.f = f;
	}

	public void resetingRoomVisibility(JDPoint p) {
		brain.resetingRoomVisibility(p);
	}

	public void onTurn() {

	}

	public boolean isHostileTo(FigureInfo f) {
		return brain.isHostileTo(f);
	}

	public void actionDone(Action a, ActionResult res) {

	}

	public void gameOver() {

	}

	public void repaint() {

	}

	public void tellPercept(Percept p) {
		brain.tellPercept(p);
		perceptList.add(p);
	}

	public List getPerceptList() {
		return perceptList;
	}

	public Action getAction() {
		Action a = null;
		if (f.getRoomInfo().fightRunning().booleanValue()) {
			a = brain.chooseFightAction();
		} else {
			a = brain.chooseMovementAction();
		}
		perceptList = new LinkedList();
		return a;
	}

	// public Action getMovementAction() {
	//
	// Action a = moveBrain.chooseMovementAction();
	// perceptList = new LinkedList();
	// return a;
	// }

	// protected abstract Action getForcedMovementAction();
	//
	// protected abstract Action getForcedFightAction();

}
