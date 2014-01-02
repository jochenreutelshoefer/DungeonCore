package de.jdungeon.androidapp;

import figure.FigureInfo;
import figure.action.Action;
import figure.action.result.ActionResult;
import figure.monster.MonsterInfo;
import figure.other.Fir;
import figure.percept.Percept;
import game.JDGUI;
import item.ItemInfo;

import java.util.Vector;

import spell.SpellInfo;
import dungeon.JDPoint;
import dungeon.RoomInfo;

public class AndroidScreenJDGUI implements JDGUI {

	private final Vector<Action> actionQueue = new Vector<Action>();

	private final GameScreen screen;
	private final PerceptHandler perceptHandler;

	public AndroidScreenJDGUI(GameScreen screen) {
		this.screen = screen;
		this.perceptHandler = new PerceptHandler(screen);

	}

	@Override
	public void actionDone(Action a, ActionResult res) {
		// TODO Auto-generated method stub

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
	public void resetingRoomVisibility(JDPoint p) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub

	}
}
