package de.jdungeon;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import control.JDGUIEngine2D;
import dungeon.JDPoint;
import event.EventManager;
import figure.FigureInfo;
import figure.action.Action;
import figure.action.EndRoundAction;
import figure.action.result.ActionResult;
import figure.other.Fir;
import figure.other.Lioness;
import figure.percept.Percept;
import figure.percept.TextPercept;
import game.PerceptHandler;
import item.ItemInfo;
import log.Log;
import spell.SpellInfo;
import text.StatementManager;

import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.event.VisibilityIncreasedEvent;
import de.jdungeon.app.screen.GameScreen;
import de.jdungeon.game.Screen;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.12.19.
 */
public class LibgdxJDGUI implements JDGUIEngine2D {

	private final Vector<Action> actionQueue = new Vector<Action>();
	private final DungeonApp app;

	private FigureInfo figure;

	private PerceptHandler perceptHandler;

	private static LibgdxJDGUI instance;

	public static LibgdxJDGUI getInstance(DungeonApp app) {
		if(instance == null) {
			instance = new LibgdxJDGUI(app);
		} else {
			if(! (instance.app == app)) {
				Log.severe("Duplicate AndroidScreenJDGUI instance creation with different App objects");
				System.exit(0);
			}
		}
		return instance;
	}

	private LibgdxJDGUI(DungeonApp app) {
		this.app = app;

	}


	@Override
	public int getSelectedItemIndex() {
		return 0;
	}

	@Override
	public ItemInfo getSelectedItem() {
		return null;
	}

	@Override
	public SpellInfo getSelectedSpellInfo() {
		return null;
	}

	@Override
	public void setSpellMetaDown(boolean b) {

	}

	@Override
	public void setUseWithTarget(boolean b) {

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

	@Override
	public void onTurn() {
		if(!visibilityIncreasedRooms.isEmpty()) {
			EventManager.getInstance().fireEvent(new VisibilityIncreasedEvent(visibilityIncreasedRooms));
			visibilityIncreasedRooms.clear();
		}


		// if player does not have an action point left,
		// preemptively cause an EndRoundAction
		if(this.getFigure().getActionPoints() == 0) {
			plugAction(new EndRoundAction());
		}
	}

	@Override
	public FigureInfo getFigure() {
		return figure;
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
		figure = f;
	}

	@Override
	public void actionProcessed(Action a, ActionResult res) {
		if (res.getValue() == ActionResult.VALUE_IMPOSSIBLE) {
			perceptHandler.tellPercept(new TextPercept(StatementManager.getStatement(res).getText()));
			AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.JAM);
		}
	}

	@Override
	public boolean isHostileTo(FigureInfo otherFigure) {
		if(otherFigure.equals(this.figure)) {
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
		if (otherFigure.isHostile(this.figure)) {
			return true;
		}
		return false;
	}

	@Override
	public void notifyVisibilityStatusDecrease(JDPoint p) {

	}

	private final List<JDPoint> visibilityIncreasedRooms = new ArrayList<>();

	@Override
	public void notifyVisibilityStatusIncrease(JDPoint p) {
		visibilityIncreasedRooms.add(p);
	}

	@Override
	public Action getAction() {
		synchronized (actionQueue) {
			if (!actionQueue.isEmpty()) {
				final Action action = actionQueue.remove(0);
				return action;
			} else {
				Screen currentScreen = this.app.getCurrentScreen();
				if(currentScreen instanceof GameScreen) {
					((GameScreen)currentScreen).getActionAssembler().triggerPlannedActions();
				}
			}
		}
		return null;
	}

	@Override
	public void tellPercept(Percept p) {
		perceptHandler.tellPercept(p);
	}

	public void setPerceptHandler(PerceptHandler perceptHandler) {
		this.perceptHandler = perceptHandler;
	}
}
