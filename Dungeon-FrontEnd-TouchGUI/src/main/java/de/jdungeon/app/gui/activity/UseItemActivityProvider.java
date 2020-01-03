package de.jdungeon.app.gui.activity;

import dungeon.ItemInfoOwner;

import de.jdungeon.app.ActionController;
import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.gui.FocusManager;
import de.jdungeon.game.Game;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 04.01.17.
 */
public class UseItemActivityProvider extends ItemActivityItemProvider {

	private final ActionController actionAssembler;
	private final FocusManager focusManager;

	public UseItemActivityProvider(ItemInfoOwner info, Game game, ActionController actionAssembler, FocusManager focusManager) {
		super(info, game, actionAssembler);
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

	@Override
	public boolean isCurrentlyPossible(Activity infoEntity) {
		return true;
	}
}
