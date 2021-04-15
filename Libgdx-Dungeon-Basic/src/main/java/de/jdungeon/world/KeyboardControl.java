package de.jdungeon.world;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import de.jdungeon.dungeon.PositionInRoomInfo;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.action.Action;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.figure.percept.TextPercept;
import de.jdungeon.item.ItemInfo;
import de.jdungeon.text.Statement;
import de.jdungeon.text.StatementManager;

import de.jdungeon.app.ActionAssembler;
import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.gui.activity.ScoutActivity;

import static com.badlogic.gdx.Gdx.input;
import static com.badlogic.gdx.Input.Keys;

/**
 * Implements keyboard control for the de.jdungeon.game (relevant for desktop version only).
 *
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 29.02.20.
 */
public class KeyboardControl {

	private final PlayerController playerController;
	private final CameraHelper cameraHelper;

	public KeyboardControl(PlayerController playerController, CameraHelper cameraHelper) {
		this.playerController = playerController;
		this.cameraHelper = cameraHelper;

		// setting each used key here this is necessary for GWT mode, as otherwise the browser will catch the key event
		Gdx.input.setCatchKey(Keys.SPACE, true);
		Gdx.input.setCatchKey(Keys.C, true);
		Gdx.input.setCatchKey(Keys.L, true);
		Gdx.input.setCatchKey(Keys.I, true);
		Gdx.input.setCatchKey(Keys.D, true);
		Gdx.input.setCatchKey(Keys.A, true);
		Gdx.input.setCatchKey(Keys.S, true);
		Gdx.input.setCatchKey(Keys.D, true);
		Gdx.input.setCatchKey(Keys.W, true);
		Gdx.input.setCatchKey(Keys.ENTER, true);
		Gdx.input.setCatchKey(Keys.TAB, true);
		Gdx.input.setCatchKey(Keys.BACKSPACE, true);
		Gdx.input.setCatchKey(Keys.ESCAPE, true);
		Gdx.input.setCatchKey(Keys.SHIFT_LEFT, true);
		Gdx.input.setCatchKey(Keys.SHIFT_RIGHT, true);
		Gdx.input.setCatchKey(Keys.COMMA, true);
		Gdx.input.setCatchKey(Keys.PERIOD, true);
		Gdx.input.setCatchKey(Keys.SLASH, true);
		Gdx.input.setCatchKey(Keys.UP, true);
		Gdx.input.setCatchKey(Keys.DOWN, true);
		Gdx.input.setCatchKey(Keys.LEFT, true);
		Gdx.input.setCatchKey(Keys.RIGHT, true);
		Gdx.input.setCatchKey(Keys.CONTROL_LEFT, true);
		Gdx.input.setCatchKey(Keys.CONTROL_RIGHT, true);
		Gdx.input.setCatchKey(Keys.ALT_LEFT, true);
		Gdx.input.setCatchKey(Keys.ALT_RIGHT, true);
	}

	public boolean handleKeyEvents(float deltaTime) {

		long lastEvent = System.currentTimeMillis() - last_key_pressed_event;
		if (lastEvent < 200) {
			// we do not process move input events faster than any 200 msec
			// as there are coming many events with every key press
			//Log.info("Ignoring key event because last was "+lastEvent +"ago");
			return false;
		}

		Boolean fightRunning = playerController.getFigure().getRoomInfo().fightRunning();
		boolean isFight = fightRunning != null && fightRunning;
		ActionAssembler actionAssembler = playerController.getActionAssembler();

		if (isFight) {
			if (input.isKeyPressed(Keys.SPACE)) {
				ActionResult actionResult = playerController.plugActivity(playerController.getAttackActivity(), playerController
						.getGameScreen()
						.getFocusManager()
						.getWorldFocusObject());
				if (actionResult.getSituation() == ActionResult.Situation.possible) {
					return eventProcessed();
				}
				else {
					Statement statement = StatementManager.getStatement(actionResult, playerController.getRound());
					this.playerController.tellPercept(new TextPercept(statement.getText(), playerController.getRound()));
					AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.JAM);
				}
			}
		}


		/*
		handle chest event
		 */
		if (input.isKeyPressed(Keys.C)) {
			playerController.getActionAssembler().wannaUseChest();
			return eventProcessed();
		}

		/*
		handle use location event
		 */
		if (input.isKeyPressed(Keys.L)) {
			playerController.getActionAssembler().wannaUseLocation();
			return eventProcessed();
		}

		/*
		handle take item event
		 */
		if (input.isKeyPressed(Keys.I)) {
			playerController.getActionAssembler().wannaTakeItem();
			return eventProcessed();
		}

		/*
		handle door lock/unlock event
		 */
		if (input.isKeyPressed(Keys.D)) {
			playerController.getActionAssembler().wannaLockUnlockDoor();
			return eventProcessed();
		}

		/*
		handle use item event
		 */
		if (input.isKeyPressed(Keys.ENTER)) {
			ItemInfo selectedInventoryItem = playerController.getGameScreen()
					.getGuiRenderer()
					.getItemWheel()
					.getSelectedInventoryItem();
			playerController.getActionAssembler()
					.wannaUseItem(selectedInventoryItem);
			return eventProcessed();
		}

		/*
		shift selected inventory item
		 */
		if (input.isKeyPressed((Keys.TAB))) {
			playerController.getGameScreen().getGuiRenderer().getItemWheel().shiftInventoryItemSelection();
			return eventProcessed();
		}

		Boolean x = handleCursorEvents(isFight, actionAssembler);
		if (x != null) return x;

		// camera controls move
		float camMoveSpeed = 500 * deltaTime;
		//float cameraMoveSpeedAcceleratorFactor = 5;
		//if (Gdx.input.isKeyPressed((Input.Keys.SHIFT_LEFT))) camMoveSpeed *= cameraMoveSpeedAcceleratorFactor;

		if (input.isKeyPressed((Keys.D))) moveCamera(camMoveSpeed, 0);
		if (input.isKeyPressed((Keys.S))) moveCamera(0, camMoveSpeed);
		if (input.isKeyPressed((Keys.A))) moveCamera(-camMoveSpeed, 0);
		if (input.isKeyPressed((Keys.W))) moveCamera(0, -camMoveSpeed);

		if (input.isKeyPressed((Keys.BACKSPACE))) cameraHelper.setPosition(0, 0);
		// camera controls zoom
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccel = 5;
		if (input.isKeyPressed(Keys.SHIFT_LEFT)) camZoomSpeed *= camZoomSpeedAccel;
		if (input.isKeyPressed(Keys.COMMA)) cameraHelper.addZoom(camZoomSpeed);
		if (input.isKeyPressed(Keys.PERIOD)) cameraHelper.addZoom(-camZoomSpeed);
		if (input.isKeyPressed(Keys.SLASH)) cameraHelper.setZoom(1);

		return false;
	}

	private static final Map<Integer, RouteInstruction.Direction> keyDirectionMap = new HashMap<>();

	static {
		keyDirectionMap.put(Keys.RIGHT, RouteInstruction.Direction.East);
		keyDirectionMap.put(Keys.LEFT, RouteInstruction.Direction.West);
		keyDirectionMap.put(Keys.DOWN, RouteInstruction.Direction.South);
		keyDirectionMap.put(Keys.UP, RouteInstruction.Direction.North);
	}

	private Boolean handleCursorEvents(boolean isFight, ActionAssembler actionAssembler) {
		if (altPressed()) {
			// cursor keys with alt modifier do scouting
			ScoutActivity scoutActivity = playerController.getScoutActivity();
			for (Integer cursorKey : keyDirectionMap.keySet()) {
				if (input.isKeyPressed(cursorKey)) {
					if (isFight) AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.JAM); // no use for shift in de.jdungeon.fight
					RouteInstruction.Direction dir = keyDirectionMap.get(cursorKey);
					ActionResult actionResult = scoutActivity.isCurrentlyPossible(dir);
					if (actionResult.getSituation() == ActionResult.Situation.possible) {
						playerController.plugActivity(scoutActivity, dir);
						return eventProcessed();
					}
					else {
						tellUserEventFail(actionResult);
					}
				}
			}
		}
		else if (shiftPressed()) {
			// cursor keys with shift modifier trigger steps
			return plugStepDirectionAction();
		}
		else {
			// cursor keys without modifier
			if (isFight) {
				PositionInRoomInfo pos = actionAssembler.getFigure().getPos();
				RouteInstruction.Direction possibleFleeDirection = pos.getPossibleFleeDirection();
				if (possibleFleeDirection != null) {
					for (Integer cursorKey : keyDirectionMap.keySet()) {
						if (input.isKeyPressed(cursorKey)) {
							RouteInstruction.Direction dir = keyDirectionMap.get(cursorKey);
							if (possibleFleeDirection == dir) {
								ActionResult actionResult = playerController.getFleeActivity()
										.plugToController(RouteInstruction.Direction.East);
								if (!(actionResult.getSituation() == ActionResult.Situation.possible)) {
									tellUserEventFail(actionResult);
								}
								return eventProcessed();
							}
						}
					}

					// cannot flee from this pos -> step further
					return plugStepDirectionAction();
				}
				else {
					return plugStepDirectionAction();
				}
			}
			else {
				// cursor keys be default moving to next room
				for (Integer cursorKey : keyDirectionMap.keySet()) {
					if (input.isKeyPressed(cursorKey)) {
						RouteInstruction.Direction dir = keyDirectionMap.get(cursorKey);
						actionAssembler.wannaWalk(dir.getValue());
						return eventProcessed();
					}
				}
			}
		}
		return null;
	}

	private void tellUserEventFail(ActionResult actionResult) {
		Statement statement = StatementManager.getStatement(actionResult, playerController.getRound());
		playerController.tellPercept(new TextPercept(statement.getText(), playerController.getRound()));
		AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.JAM);
	}

	private boolean eventProcessed() {
		//Log.info("Reseting last key event timer");
		// we log a time stamp to be able to filter duplicate processing
		last_key_pressed_event = System.currentTimeMillis();
		AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
		return true;
	}

	private boolean plugStepDirectionAction() {
		if (input.isKeyPressed((Keys.RIGHT))) {
			plugNonEmpty(playerController.getActionAssembler().getActionAssemblerHelper().wannaStepEast());
			return eventProcessed();
		}
		if (input.isKeyPressed((Keys.DOWN))) {
			plugNonEmpty(playerController.getActionAssembler().getActionAssemblerHelper().wannaStepSouth());
			return eventProcessed();
		}
		if (input.isKeyPressed((Keys.LEFT))) {
			plugNonEmpty(playerController.getActionAssembler().getActionAssemblerHelper().wannaStepWest());
			return eventProcessed();
		}
		if (input.isKeyPressed((Keys.UP))) {
			plugNonEmpty(playerController.getActionAssembler().getActionAssemblerHelper().wannaStepNorth());
			return eventProcessed();
		}
		return false;
	}

	private void plugNonEmpty(List<Action> actions) {
		if (actions != null && !actions.isEmpty()) {
			playerController.plugActions(actions);
		}
	}

	private boolean altPressed() {
		return input.isKeyPressed(Keys.ALT_LEFT) || input.isKeyPressed(Keys.ALT_RIGHT);
	}

	private boolean shiftPressed() {
		return input.isKeyPressed(Keys.SHIFT_RIGHT) || input.isKeyPressed(Keys.SHIFT_LEFT);
	}

	@Deprecated // on mac os not possible in combination with cursors
	private boolean controlPressed() {
		return input.isKeyPressed(Keys.CONTROL_LEFT) || input.isKeyPressed(Keys.CONTROL_RIGHT);
	}

	private long last_key_pressed_event;

	private void moveCamera(float x, float y) {
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}
}
