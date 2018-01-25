package de.jdungeon.androidapp.gui.itemWheel;

import dungeon.ItemInfoOwner;

import de.jdungeon.androidapp.Control;
import de.jdungeon.androidapp.audio.AudioManagerTouchGUI;
import de.jdungeon.androidapp.screen.GameScreen;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 04.01.17.
 */
public class UseItemActivityProvider extends ItemActivityItemProvider {

	public UseItemActivityProvider(ItemInfoOwner info, GameScreen screen) {
		super(info, screen);
	}

	@Override
	public void activityPressed(Activity infoEntity) {
		AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
		// TODO: refactor, remove control, direct event processing by ActionAssembler
		Control control = screen.getControl();
		control.itemWheelActivityClicked(infoEntity,
				screen.getFocusManager().getWorldFocusObject());
	}
}
