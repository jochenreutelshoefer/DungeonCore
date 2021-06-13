package de.jdungeon.world;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.Action;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.figure.hero.HeroInfo;
import de.jdungeon.figure.other.Fir;
import de.jdungeon.figure.other.Lioness;
import de.jdungeon.figure.percept.EntersPercept;
import de.jdungeon.figure.percept.OpticalPercept;
import de.jdungeon.figure.percept.Percept;
import de.jdungeon.figure.percept.TextPercept;
import de.jdungeon.game.Configuration;
import de.jdungeon.game.Game;
import de.jdungeon.user.LossCriterion;
import de.jdungeon.user.PlayerControllerI;
import de.jdungeon.location.LevelExit;
import de.jdungeon.log.Log;
import de.jdungeon.text.StatementManager;
import de.jdungeon.user.DungeonSession;

import de.jdungeon.app.ActionAssembler;
import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.gui.activity.Activity;
import de.jdungeon.gui.activity.ActivityPlan;
import de.jdungeon.gui.activity.AttackActivity;
import de.jdungeon.gui.activity.FleeActivity;
import de.jdungeon.gui.activity.ScoutActivity;
import de.jdungeon.util.CopyOnWriteArrayList;

/**
 * This class basically controls the interaction from the world (world loop)
 * and the user interface (de.jdungeon.gui actions).
 * <p>
 * It is a thread dashboard, where objects are put by one thread and collected by another.
 * - de.jdungeon.game thread puts percepts, that the player perceives about the world and that need to be visualized in some way on
 * the UI
 * - de.jdungeon.game thread puts visibility increase/decrease notifications, saying that the player can see parts of the world that
 * he could not before or vice versa
 * - the black board for player action handling is done within the action controller, which is part of the player
 * controller
 *
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 03.01.20.
 */
public class PlayerController implements PlayerControllerI {

    private final DungeonSession dungeonSession;
    private Game game;
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

    public PlayerController(DungeonSession dungeonSession, Game game) {
        this.dungeonSession = dungeonSession;
        this.game = game;
        attackActivity = new AttackActivity(this);
        fleeActivity = new FleeActivity(this);
        scoutActivity = new ScoutActivity(this);
    }

    /**
     * The number of rounds played in this dungeon.
     *
     * @return de.jdungeon.game round
     */
    public int getRound() {
        return this.dungeonSession.getDungeonRound();
    }

    public void plugActivityPlan(ActivityPlan currentActivityPlan) {
        // todo: is this okay to just silently override any plan if a new one comes along?
        this.currentActivityPlan = currentActivityPlan;
    }

    public ActionResult plugActivity(Activity activity, Object target) {
        ActionResult result = activity.isCurrentlyPossible(target);
        if (result.getSituation() == ActionResult.Situation.possible) {
            plugActivityPlan(activity.createExecutionPlan(true, target));
        }
        return result;
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
        } else {
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
     * The de.jdungeon.gui thread plugs an action to be performed by the UI-controlled de.jdungeon.figure.
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
     * The de.jdungeon.gui thread plugs a sequence of actions to be performed by the UI-controlled de.jdungeon.figure.
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
    public Configuration getConfiguration() {
        return game.getConfiguration();
    }

    @Override
    public void gameOver() {
        JDPoint roomNumber = this.heroInfo.getRoomNumber();
        viewModel.updateRoom(roomNumber.getX(), roomNumber.getY());
    }

    public ActionAssembler getActionAssembler() {
        return actionAssembler;
    }

    @Override
    public void onTurn() {
        if (viewModel == null) return; // can happen during initialisation
        boolean backgroundDrawingUpdateRequired = !roomRenderUpdateLaundry.isEmpty();
        for (JDPoint point : roomRenderUpdateLaundry) {
            viewModel.updateRoom(point.getX(), point.getY());
        }
        if (backgroundDrawingUpdateRequired) {
            viewModel.setBackgroundUpdateRequired();
        }
        roomRenderUpdateLaundry.clear();
    }

    @Override
    public FigureInfo getFigure() {
        return heroInfo;
    }

    @Override
    public LossCriterion gameOverCriterionMet() {
        return this.dungeonSession.lossCriterionMet();
    }

    @Override
    public void actionProcessed(Action action, ActionResult res, int round) {
        if (res.getSituation() == ActionResult.Situation.impossible) {
            perceptQueue.add(new TextPercept(StatementManager.getStatement(res, round).getText(), round));
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
        // TODO: find way to prevent infinite loop if other de.jdungeon.figure also plays like this..
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
            number = ((OpticalPercept) p).getPoint();
        }
        if (p instanceof EntersPercept  // someone enters
                && (((EntersPercept) p).getTo(this.heroInfo).equals(this.getFigure().getRoomInfo())) // into the room of this de.jdungeon.figure
                && (!((EntersPercept) p).getFigure(this.heroInfo).equals(this.getFigure()))) { // who is not this de.jdungeon.figure
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
        if (gameScreen != null) { // initialization issue at de.jdungeon.level start
            gameScreen.checkCameraPosition();
        }
    }

    private void interrupt(Percept p) {
        if (!this.actionQueue.isEmpty()) {
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

    public boolean isDungeonTransactionLocked() {
        return this.dungeonSession.getCurrentDungeon().isTransactionLocked();
    }
}
