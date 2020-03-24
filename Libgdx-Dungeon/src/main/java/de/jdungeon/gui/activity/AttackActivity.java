package de.jdungeon.gui.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dungeon.RoomInfo;
import figure.FigureInfo;
import game.RoomInfoEntity;

import de.jdungeon.app.ActionAssembler;
import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.gui.activity.AbstractExecutableActivity;
import de.jdungeon.app.gui.smartcontrol.UIFeedback;
import de.jdungeon.gui.LibgdxFocusManager;
import de.jdungeon.world.PlayerController;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.02.20.
 */
public class AttackActivity extends AbstractExecutableActivity {

	private final PlayerController controller;

	public AttackActivity(PlayerController controller) {
		this.controller = controller;
	}

	@Override
	public void execute() {

		ActionAssembler actionAssembler = controller.getActionAssembler();
		LibgdxFocusManager focusManager = controller.getGameScreen().getFocusManager();
		Object highlightedEntity = focusManager.getWorldFocusObject();
		if(highlightedEntity instanceof FigureInfo && !((FigureInfo)highlightedEntity).getRoomInfo().getPoint().equals(actionAssembler
				.getFigure().getRoomInfo().getLocation())) {
			// moved out of room since last figure focus hence reset focus
			focusManager.setWorldFocusObject((RoomInfoEntity)null);
			highlightedEntity = null;
		}
		List<FigureInfo> hostileFigures = getHostileFiguresList();
		if (highlightedEntity instanceof FigureInfo) {
			AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
			actionAssembler.plugActions(actionAssembler.getActionAssemblerHelper().wannaAttack((FigureInfo) highlightedEntity));
		} else if (hostileFigures.size() == 1) {
			FigureInfo target = hostileFigures.get(0);
			actionAssembler.plugActions(actionAssembler.getActionAssemblerHelper().wannaAttack(target));
			focusManager.setWorldFocusObject(target);
		} else {
			controller.getGameScreen().getGuiRenderer().setMessage(UIFeedback.SelectEnemy.getMessage());
		}
	}

	private List<FigureInfo> getHostileFiguresList() {
		RoomInfo roomInfo = controller.getActionAssembler().getFigure().getRoomInfo();
		if(roomInfo == null) return Collections.emptyList();
		List<FigureInfo> figureInfos = roomInfo.getFigureInfos();
		List<FigureInfo> hostileFigures = new ArrayList<>();
		for (FigureInfo figureInfo : figureInfos) {
			if (figureInfo.isHostile(controller.getActionAssembler().getFigure())) {
				hostileFigures.add(figureInfo);
			}
		}
		return hostileFigures;
	}

	@Override
	public boolean isCurrentlyPossible() {
		if(controller.getActionAssembler().getFigure() == null) return false;
		if(getHostileFiguresList().isEmpty()) {
			return false;
		}
		RoomInfo roomInfo = controller.getActionAssembler().getFigure().getRoomInfo();
		if(roomInfo.fightRunning() == null) {
			// todo: happens, but this is really weird, hence the figure has no visibility of its current room
			return false;
		}
		return roomInfo.fightRunning();
	}

	@Override
	public Object getObject() {
		return this;
	}

}
