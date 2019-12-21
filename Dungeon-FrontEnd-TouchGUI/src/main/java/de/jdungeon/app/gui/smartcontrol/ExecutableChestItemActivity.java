package de.jdungeon.app.gui.smartcontrol;

import audio.AudioEffectsManager;
import figure.action.TakeItemFromChestAction;
import item.ItemInfo;

import de.jdungeon.app.GUIControl;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 28.01.18.
 */
public class ExecutableChestItemActivity extends ExecutableTakeItemActivity {

	public ExecutableChestItemActivity(GUIControl control, ItemInfo item) {
		super(control, item);
	}

	@Override
	public void execute() {
		AudioEffectsManager.playSound(AudioEffectsManager.TAKE_ITEM);
		guiControl.plugAction(new TakeItemFromChestAction((ItemInfo) getObject()));
	}

}
