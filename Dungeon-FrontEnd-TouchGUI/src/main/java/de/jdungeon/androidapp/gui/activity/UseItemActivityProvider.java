package de.jdungeon.androidapp.gui.activity;

import control.ActionAssembler;
import dungeon.ItemInfoOwner;

import de.jdungeon.androidapp.Control;
import de.jdungeon.androidapp.audio.AudioManagerTouchGUI;
import de.jdungeon.androidapp.gui.FocusManager;
import de.jdungeon.androidapp.gui.activity.Activity;
import de.jdungeon.androidapp.gui.activity.ItemActivityItemProvider;
import de.jdungeon.androidapp.screen.GameScreen;
import de.jdungeon.game.Game;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 04.01.17.
 */
public class UseItemActivityProvider extends ItemActivityItemProvider {

	private final Control actionAssembler;
	private final FocusManager focusManager;

	public UseItemActivityProvider(ItemInfoOwner info, Game game, Control actionAssembler, FocusManager focusManager) {
		super(info, game);
		this.actionAssembler = actionAssembler;
		this.focusManager = focusManager;
	}

	@Override
	public void activityPressed(Activity infoEntity) {
		AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
		// TODO: refactor, remove control, direct event processing by ActionAssembler

		actionAssembler.itemWheelActivityClicked(infoEntity,
				focusManager.getWorldFocusObject());
	}
}
