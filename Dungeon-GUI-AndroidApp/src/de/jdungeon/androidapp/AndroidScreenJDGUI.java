package de.jdungeon.androidapp;

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
import graphics.AbstractImageLoader;

public class AndroidScreenJDGUI implements JDGUIEngine2D {

	private final Vector<Action> actionQueue = new Vector<Action>();

	private final GameScreen screen;
	private final PerceptHandler perceptHandler;

	public AndroidScreenJDGUI(GameScreen screen) {
		this.screen = screen;
		this.perceptHandler = new PerceptHandler(screen);

	}

	@SuppressWarnings("rawtypes")
	@Override
	public AbstractImageLoader getImageSource() {
		return Assets.getLoader();
	}

	@Override
	public void actionDone(Action a, ActionResult res) {
		if (res.getKey1() == ActionResult.KEY_IMPOSSIBLE) {
			perceptHandler.newStatement(StatementManager.getStatement(res));
		}
	}

	@Override
	public void tellPercept(Percept p) {
		perceptHandler.handlePercept(p);
	}

	@Override
	public void onTurn() {
		// TODO Auto-generated method stub

	}

	@Override
	public void gameOver() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isHostileTo(FigureInfo f) {
		if (f.getFigureClass().equals(Fir.class)) {
			return false;
		}
		if (f instanceof MonsterInfo) {
			return true;
		}
		return false;
	}


	@Override
	public Action getAction() {
		if (actionQueue.size() > 0) {
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
		return this.screen.getFigureInfo();
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
		this.screen.setFigure(f);

	}

	@Override
	public void notifyVisbilityStatusDecrease(JDPoint p) {
		// TODO Auto-generated method stub

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
}
