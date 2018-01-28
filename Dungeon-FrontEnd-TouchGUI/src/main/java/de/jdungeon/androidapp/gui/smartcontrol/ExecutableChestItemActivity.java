package de.jdungeon.androidapp.gui.smartcontrol;

import audio.AudioEffectsManager;
import figure.action.TakeItemAction;
import figure.action.TakeItemFromChestAction;
import item.ItemInfo;

import de.jdungeon.androidapp.GUIControl;
import de.jdungeon.androidapp.audio.AudioManagerTouchGUI;

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
