package de.jdungeon.gui.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.jdungeon.dungeon.RoomInfo;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.action.Action;
import de.jdungeon.figure.action.result.ActionResult;

import de.jdungeon.app.ActionAssembler;
import de.jdungeon.app.gui.smartcontrol.UIFeedback;
import de.jdungeon.gui.LibgdxFocusManager;
import de.jdungeon.world.PlayerController;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.02.20.
 */
public class AttackActivity extends AbstractExecutableActivity {

	public AttackActivity(PlayerController controller) {
		super(controller);
	}

	@Override
	public ActivityPlan createExecutionPlan(boolean doIt, Object targetObject) {
		List<Action> actions = new ArrayList<>();
		ActionAssembler actionAssembler = playerController.getActionAssembler();
		LibgdxFocusManager focusManager = playerController.getGameScreen().getFocusManager();
		Object highlightedEntity = focusManager.getWorldFocusObject();
		FigureInfo figure = actionAssembler.getFigure();
		if(figure == null) return null; // de.jdungeon.level exit problem
		RoomInfo roomInfo = figure.getRoomInfo();
		if(roomInfo == null) return null;  // de.jdungeon.level exit problem
		List<FigureInfo> hostileFigures = getHostileFiguresList();
		if (highlightedEntity instanceof FigureInfo) {
			actions.addAll(actionAssembler.getActionAssemblerHelper().wannaAttack((FigureInfo) highlightedEntity));
		} else if (hostileFigures.size() == 1) {
			FigureInfo target = hostileFigures.get(0);
			actions.addAll(actionAssembler.getActionAssemblerHelper().wannaAttack(target));
			if(doIt) {
				focusManager.setWorldFocusObject(target);
			}
		} else {
			this.playerController.getGameScreen().getGuiRenderer().setMessage(UIFeedback.SelectEnemy.getMessage());
		}

		return new SimpleActivityPlan(this, actions);
	}

	private List<FigureInfo> getHostileFiguresList() {
		RoomInfo roomInfo = playerController.getFigure().getRoomInfo();
		if(roomInfo == null) return Collections.emptyList();
		List<FigureInfo> figureInfos = roomInfo.getFigureInfos();
		List<FigureInfo> hostileFigures = new ArrayList<>();
		for (FigureInfo figureInfo : figureInfos) {
			if (figureInfo.isHostile(playerController.getFigure())) {
				hostileFigures.add(figureInfo);
			}
		}
		return hostileFigures;
	}

	@Override
	public ActionResult possible(Object targetObject) {
		if(playerController.getFigure() == null) return ActionResult.UNKNOWN;
		if(getHostileFiguresList().isEmpty()) {
			return ActionResult.NO_TARGET;
		}
		RoomInfo roomInfo = playerController.getActionAssembler().getFigure().getRoomInfo();
		if(roomInfo.fightRunning() == null) {
			// todo: happens, but this is really weird, hence the de.jdungeon.figure has no visibility of its current room
			return ActionResult.UNKNOWN;
		}
		boolean fightRunning = roomInfo.fightRunning();
		if(!fightRunning) {
			return ActionResult.MODE;
		} else {
			return ActionResult.POSSIBLE;
		}
	}

	@Override
	public Object getObject() {
		return this;
	}

}
