package de.jdungeon.world;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import dungeon.JDPoint;
import figure.Figure;
import figure.FigureInfo;
import figure.action.Action;
import figure.action.result.ActionResult;
import figure.hero.HeroInfo;
import figure.other.Fir;
import figure.other.Lioness;
import figure.percept.EntersPercept;
import figure.percept.OpticalPercept;
import figure.percept.Percept;
import figure.percept.TextPercept;
import game.JDGUI;
import log.Log;
import location.LevelExit;
import text.StatementManager;
import user.DungeonSession;

import de.jdungeon.app.ActionAssembler;
import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.gui.activity.Activity;
import de.jdungeon.gui.activity.ActivityPlan;
import de.jdungeon.gui.activity.AttackActivity;
import de.jdungeon.gui.activity.FleeActivity;
import de.jdungeon.gui.activity.ScoutActivity;

/**
 * This class basically controls the interaction from the world (world loop)
 * and the user interface (gui actions).
 * <p>
 * It is a thread dashboard, where objects are put by one thread and collected by another.
 * - game thread puts percepts, that the player perceives about the world and that need to be visualized in some way on
 * the UI
 * - game thread puts visibility increase/decrease notifications, saying that the player can see parts of the world that
 * he could not before or vice versa
 * - the black board for player action handling is done within the action controller, which is part of the player
 * controller
 *
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 03.01.20.
 */
public class PlayerController implements JDGUI {

	private final DungeonSession dungeonSession;
	private HeroInfo heroInfo;

	private ActionAssembler actionAssembler;

	private final List<JDPoint> visibilityIncreasedRooms = new Vector<>();
	private final Set<JDPoint> roomRenderUpdateLaundry = new HashSet<>();
	private final Set<JDPoint> visibilityIncreasedRoomsTransport = new HashSet<>();
	private final List<JDPoint> visibilityDecreasedRooms = new Vector<>();
	private final List<Percept> perceptQueue = new CopyOnWriteArrayList<>();
	private final List<Percept> perceptQueueTransport = new CopyOnWriteArrayList<>();
	private final Vector<Action> actionQueue = new Vector<>();
	private ActivityPlan currentActivityPlan;

	private ViewModel viewModel;

	private final AttackActivity attackActivity;
	private final FleeActivity fleeActivity;
	private final ScoutActivity scoutActivity;

	private GameScreen gameScreen;

	public PlayerController(DungeonSession dungeonSession) {
		this.dungeonSession = dungeonSession;
		attackActivity = new AttackActivity(this);
		fleeActivity = new FleeActivity(this);
		scoutActivity = new ScoutActivity(this);
	}

	public void plugActivityPlan(ActivityPlan currentActivityPlan) {
		// todo: is this okay to just silently override any plan if a new one comes along?
		this.currentActivityPlan = currentActivityPlan;
	}

	public boolean plugActivity(Activity activity, Object target) {
		if (activity.isCurrentlyPossible(target)) {
			plugActivityPlan(activity.createExecutionPlan(true, target));
			return true;
		}
		else {
			return false;
		}
	}

	public ScoutActivity getScoutActivity() {
		return scoutActivity;
	}

	public FleeActivity getFleeActivity() {
		return fleeActivity;
	}

	public AttackActivity getAttackActivity() {
		return attackActivity;
	}

	@Override
	public void setFigure(FigureInfo f) {
		if (f instanceof HeroInfo) {
			this.heroInfo = (HeroInfo) f;
		}
		else {
			String message = "Figure type not matching in player controller initilaization: " + f;
			Log.severe(message);
			throw new IllegalStateException(message);
		}
		this.actionAssembler = new ActionAssembler(heroInfo, this);
	}

	public void setGameScreen(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	public HeroInfo getHeroInfo() {
		return heroInfo;
	}

	public GameScreen getGameScreen() {
		return gameScreen;
	}

	/**
	 * Thread-blackboard put method (called by UI-Thread)
	 * <p>
	 * The gui thread plugs an action to be performed by the UI-controlled figure.
	 * <p>
	 * Adds a particular Action to the action sequence to be executed
	 *
	 * @param action action to be executed
	 */
	@Override
	public void plugAction(Action action) {
		actionQueue.add(action);
	}

	/**
	 * Thread-blackboard put method (called by UI-Thread)
	 * <p>
	 * The gui thread plugs a sequence of actions to be performed by the UI-controlled figure.
	 * <p>
	 * Adds a sequences of Actions to the characters' action sequence to be executed
	 *
	 * @param actions actions to be executed
	 */
	@Override
	public void plugActions(List<Action> actions) {
		actionQueue.addAll(actions);
	}

	@Override
	public void gameOver() {

	}

	public ActionAssembler getActionAssembler() {
		return actionAssembler;
	}

	@Override
	public void onTurn() {
		JDPoint roomNumber = this.heroInfo.getRoomNumber();
		//viewModel.updateRoom(roomNumber.getX(), roomNumber.getY());

		for (JDPoint point : roomRenderUpdateLaundry) {
			viewModel.updateRoom(point.getX(), point.getY());
		}
		roomRenderUpdateLaundry.clear();

	}

	@Override
	public FigureInfo getFigure() {
		return heroInfo;
	}

	@Override
	public void gameRoundEnded() {

	}

	@Override
	public void actionProcessed(Action a, ActionResult res) {
		if (res.getSituation() == ActionResult.Situation.impossible) {
			perceptQueue.add(new TextPercept(StatementManager.getStatement(res).getText(), -1));
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
		roomRenderUpdateLaundry.add(p);
		visibilityDecreasedRooms.add(p);
	}

	@Override
	public void notifyVisibilityStatusIncrease(JDPoint p) {
		roomRenderUpdateLaundry.add(p);
		synchronized (visibilityIncreasedRooms) {
			visibilityIncreasedRooms.add(p);
		}
	}

	@Override
	public void exitUsed(LevelExit exit, Figure f) {
		gameScreen.pause();
		dungeonSession.notifyExit(exit, f);
	}

	private void updateRoomViewModel(JDPoint p) {
		// might be null during GUI initialization
		if (viewModel != null) {
			this.viewModel.updateRoom(p.getX(), p.getY());
		}
	}

	@Override
	public Action getAction() {
		synchronized (actionQueue) {
			if (!actionQueue.isEmpty()) {
				return actionQueue.remove(0);
			}
		}
		if (currentActivityPlan != null) {
			synchronized (currentActivityPlan) {  // todo: make thread safe
				if (currentActivityPlan != null && !currentActivityPlan.isCompleted()) {
					return currentActivityPlan.getNextAction();
				}
			}
		}
		return null;
	}

	@Override
	public void tellPercept(Percept p) {
		JDPoint number = null;
		if (p instanceof OpticalPercept) {
			number = ((OpticalPercept) p).getLocation();
		}
		/*
		List<FigureInfo> involvedFigures = p.getInvolvedFigures();
		for (FigureInfo involvedFigure : involvedFigures) {
			JDPoint pos = involvedFigure.getRoomInfo().getNumber();
			if(pos != null) {
				number = pos;
				break;
			}

		}
		*/
		if (p instanceof EntersPercept  // someone enters
			&&(((EntersPercept) p).getTo().equals(this.getFigure().getRoomInfo())) // into the room of this figure
				&& (!((EntersPercept) p).getFigure().equals(this.getFigure()))) { // who is not this figure
			// we interrupt the current plan to allow the player to react
			interrupt(p);
		}
		if (number != null) {
			roomRenderUpdateLaundry.add(number);
			//updateRoomViewModel(number);
		}

		synchronized (perceptQueue) {
			if (!perceptQueue.contains(p)) {
				// we need to be aware of duplicates, if percept is distributed to multiple rooms (with vis state)
				perceptQueue.add(p);
			}
		}
		if (gameScreen != null) { // initialization issue at level start
			gameScreen.checkCameraPosition();
		}
	}

	private void interrupt(Percept p) {
		if(!this.actionQueue.isEmpty()) {
			// we interrupt the current sequence of actions
			this.actionQueue.clear();
			this.perceptQueue.add(new InterruptPercept(this.getFigure(), p.getRound()));
		}
	}

	public List<Percept> getPercepts() {
		perceptQueueTransport.clear();
		perceptQueueTransport.addAll(perceptQueue);
		perceptQueue.clear();
		return perceptQueueTransport;
	}

	public List<JDPoint> getVisibilityDecreasedRooms() {
		List<JDPoint> result = Collections.unmodifiableList(this.visibilityDecreasedRooms);
		this.visibilityDecreasedRooms.clear();
		return result;
	}

	public synchronized Set<JDPoint> getVisibilityIncreasedRooms() {
		synchronized (visibilityIncreasedRooms) {
			visibilityIncreasedRoomsTransport.clear();
			visibilityIncreasedRoomsTransport.addAll(visibilityIncreasedRooms);
			this.visibilityIncreasedRooms.clear();
		}
		return visibilityIncreasedRoomsTransport;
	}

	public void setViewModel(ViewModel viewModel) {
		this.viewModel = viewModel;
	}
}
