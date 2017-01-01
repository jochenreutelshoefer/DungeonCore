package de.jdungeon.androidapp;

import event.EventManager;
import figure.action.EndRoundAction;
import figure.other.Lioness;
import figure.percept.TextPercept;
import game.PerceptHandler;
import item.ItemInfo;

import java.util.Vector;

import spell.SpellInfo;
import text.StatementManager;
import control.AbstractSwingMainFrame;
import control.ActionAssembler;
import control.JDGUIEngine2D;
import dungeon.JDPoint;
import dungeon.RoomInfo;
import figure.FigureInfo;
import figure.action.Action;
import figure.action.result.ActionResult;
import figure.monster.MonsterInfo;
import figure.other.Fir;
import figure.percept.Percept;
import de.jdungeon.game.AbstractImageLoader;

import de.jdungeon.androidapp.event.VisibilityIncreasedEvent;

public class AndroidScreenJDGUI implements JDGUIEngine2D {

	private final Vector<Action> actionQueue = new Vector<Action>();

	private FigureInfo figure;

	private PerceptHandler perceptHandler;

	public AndroidScreenJDGUI() {
	}

	@SuppressWarnings("rawtypes")
	@Override
	public AbstractImageLoader getImageSource() {
		return Assets.getLoader();
	}

	@Override
	public void actionDone(Action a, ActionResult res) {
		if (res.getValue() == ActionResult.VALUE_IMPOSSIBLE) {
			perceptHandler.tellPercept(new TextPercept(StatementManager.getStatement(res).getText()));
		}
	}

	@Override
	public void tellPercept(Percept p) {
		perceptHandler.tellPercept(p);
	}

	@Override
	public void onTurn() {
		// if player does not have an action point left,
		// preemptively cause an EndRoundAction
		// TODO: can this ever happen
		if(this.getFigure().getActionPoints() == 0) {
			plugAction(new EndRoundAction());
		}
	}

	@Override
	public void gameOver() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isHostileTo(FigureInfo f) {
		// TODO: how to treat self conjured figures better?
		if (f.getFigureClass().equals(Fir.class)
				|| f.getFigureClass().equals(Lioness.class)) {
			return false;
		}
		if (f instanceof MonsterInfo) {
			return true;
		}
		return false;
	}


	@Override
	public Action getAction() {
		if (!actionQueue.isEmpty()) {
			return actionQueue.remove(0);
		}
		return null;
	}

	@Override
	public boolean currentAnimationThreadRunning(RoomInfo r) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void plugAction(Action a) {
		actionQueue.add(a);
	}

	@Override
	public int getSelectedItemIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ItemInfo getSelectedItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SpellInfo getSelectedSpellInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSpellMetaDown(boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public FigureInfo getFigure() {
		return figure;
	}

	@Override
	public void setUseWithTarget(boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void gameRoundEnded() {
		// TODO Auto-generated method stub

	}


	@Override
	public void setFigure(FigureInfo f) {
		this.figure = f;
	}

	@Override
	public void notifyVisibilityStatusDecrease(JDPoint p) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyVisibilityStatusIncrease(JDPoint p) {
		EventManager.getInstance().fireEvent(new VisibilityIncreasedEvent(p));
	}

	@Override
	public void setSelectedItemIndex(int i) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopAllAnimation() {
		// TODO Auto-generated method stub

	}

	@Override
	public ActionAssembler getActionAssembler() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractSwingMainFrame getMainFrame() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPerceptHandler(PerceptHandler perceptHandler) {
		this.perceptHandler = perceptHandler;
	}
}
