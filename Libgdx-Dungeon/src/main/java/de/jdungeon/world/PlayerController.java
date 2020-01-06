package de.jdungeon.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import dungeon.JDPoint;
import figure.FigureInfo;
import figure.action.Action;
import figure.action.EndRoundAction;
import figure.action.result.ActionResult;
import figure.hero.HeroInfo;
import figure.other.Fir;
import figure.other.Lioness;
import figure.percept.Percept;
import figure.percept.TextPercept;
import game.JDGUI;
import text.StatementManager;

import de.jdungeon.app.ActionController;
import de.jdungeon.app.audio.AudioManagerTouchGUI;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 03.01.20.
 */
public class PlayerController implements JDGUI {

	private final HeroInfo heroInfo;

	private final ActionController actionController;

	private final List<JDPoint> visibilityIncreasedRooms = new ArrayList<>();
	private final List<JDPoint> visibilityDecreasedRooms = new ArrayList<>();
	private final Vector<Action> actionQueue = new Vector<>();
	private final Vector<Percept> perceptQueue = new Vector<>();
	private ViewModel viewModel;

	public PlayerController(HeroInfo heroInfo) {
		this.heroInfo = heroInfo;
		this.actionController = new ActionController(heroInfo, this);
	}

	@Override
	public void plugAction(Action a) {
		AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
		actionQueue.add(a);
	}

	@Override
	public void plugActions(List<Action> actions) {
		AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
		actionQueue.addAll(actions);
	}

	@Override
	public void gameOver() {

	}

	public ActionController getActionController() {
		return actionController;
	}

	@Override
	public void onTurn() {
		// if player does not have an action point left,
		// preemptively cause an EndRoundAction
		if (this.getFigure().getActionPoints() == 0) {
			plugAction(new EndRoundAction());
		}
	}

	@Override
	public FigureInfo getFigure() {
		return heroInfo;
	}

	@Override
	public void gameRoundEnded() {

	}

	@Override
	public void fightEnded() {
		this.visibilityIncreasedRooms.clear();
	}

	@Override
	public void setFigure(FigureInfo f) {

	}

	@Override
	public void actionProcessed(Action a, ActionResult res) {
		if (res.getValue() == ActionResult.VALUE_IMPOSSIBLE) {
			perceptQueue.add(new TextPercept(StatementManager.getStatement(res).getText()));
			AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.JAM);
		}
	}

	@Override
	public boolean isHostileTo(FigureInfo otherFigure) {
		if (otherFigure.equals(heroInfo)) {
			// hopefully not called, but you never know..
			return false;
		}
		// TODO: how to treat self conjured figures better?
		if (otherFigure.getFigureClass().equals(Fir.class)
				|| otherFigure.getFigureClass().equals(Lioness.class)) {
			return false;
		}

		// player is hostile to those figures which are hostile to play
		// TODO: find way to prevent infinite loop if other figure also plays like this..
		if (otherFigure.isHostile(heroInfo)) {
			return true;
		}
		return false;
	}

	@Override
	public void notifyVisibilityStatusDecrease(JDPoint p) {
		updateRoomViewModel(p);
		//visibilityDecreasedRooms.add(p);
	}

	@Override
	public void notifyVisibilityStatusIncrease(JDPoint p) {
		updateRoomViewModel(p);
		//visibilityIncreasedRooms.add(p);
	}

	private void updateRoomViewModel(JDPoint p) {
		this.viewModel.updateRoom(p.getX(), p.getY());
	}

	@Override
	public Action getAction() {
		synchronized (actionQueue) {
			if (!actionQueue.isEmpty()) {
				return actionQueue.remove(0);
			}
			else {
				actionController.triggerPlannedActions();
			}
		}
		return null;
	}

	@Override
	public void tellPercept(Percept p) {
		JDPoint number = null;
		List<FigureInfo> involvedFigures = p.getInvolvedFigures();
		for (FigureInfo involvedFigure : involvedFigures) {
			JDPoint pos = involvedFigure.getRoomInfo().getNumber();
			if(pos != null) {
				number = pos;
				break;
			}

		}
		if(number!= null) {
			updateRoomViewModel(number);
		}
		perceptQueue.add(p);
	}

	public List<Percept> getPercepts() {
		List<Percept> result = Collections.unmodifiableList(perceptQueue);
		perceptQueue.clear();
		return result;
	}

	public List<JDPoint> getVisibilityDecreasedRooms() {
		List<JDPoint> result = Collections.unmodifiableList(this.visibilityDecreasedRooms);
		this.visibilityDecreasedRooms.clear();
		return result;
	}

	public List<JDPoint> getVisibilityIncreasedRooms() {
		List<JDPoint> result = Collections.unmodifiableList(this.visibilityIncreasedRooms);
		this.visibilityIncreasedRooms.clear();
		return result;
	}

	public void setViewModel(ViewModel viewModel) {
		this.viewModel = viewModel;
	}
}
