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
import de.jdungeon.world.GUIRenderer;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 22.02.20.
 */
public class AttackActivity extends AbstractExecutableActivity {

	private final LibgdxFocusManager focusManager;
	private final ActionAssembler guiControl;
	private final GUIRenderer guiRenderer;

	public AttackActivity(LibgdxFocusManager focusManager, ActionAssembler actionAssembler, GUIRenderer guiRenderer) {
		this.focusManager = focusManager;
		this.guiControl = actionAssembler;
		this.guiRenderer = guiRenderer;
	}

	@Override
	public void execute() {
		Object highlightedEntity = focusManager.getWorldFocusObject();
		if(highlightedEntity instanceof FigureInfo && !((FigureInfo)highlightedEntity).getRoomInfo().getPoint().equals(guiControl.getFigure().getRoomInfo().getLocation())) {
			// moved out of room since last figure focus hence reset focus
			focusManager.setWorldFocusObject((RoomInfoEntity)null);
			highlightedEntity = null;
		}

		List<FigureInfo> hostileFigures = getHostileFiguresList();
		if (highlightedEntity instanceof FigureInfo) {
			AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
			guiControl.plugActions(guiControl.getActionAssembler().wannaAttack((FigureInfo) highlightedEntity));
		} else if (hostileFigures.size() == 1) {
			FigureInfo target = hostileFigures.get(0);
			guiControl.plugActions(guiControl.getActionAssembler().wannaAttack(target));
			focusManager.setWorldFocusObject(target);
		} else {
			guiRenderer.setMessage(UIFeedback.SelectEnemy);
		}
	}

	private List<FigureInfo> getHostileFiguresList() {
		RoomInfo roomInfo = guiControl.getFigure().getRoomInfo();
		if(roomInfo == null) return Collections.emptyList();
		List<FigureInfo> figureInfos = roomInfo.getFigureInfos();
		List<FigureInfo> hostileFigures = new ArrayList<>();
		for (FigureInfo figureInfo : figureInfos) {
			if (figureInfo.isHostile(guiControl.getFigure())) {
				hostileFigures.add(figureInfo);
			}
		}
		return hostileFigures;
	}

	@Override
	public boolean isCurrentlyPossible() {
		if(guiControl.getFigure() == null) return false;
		if(getHostileFiguresList().isEmpty()) {
			return false;
		}
		return guiControl.getFigure().getRoomInfo().fightRunning();
	}

	@Override
	public Object getObject() {
		return this;
	}

}
