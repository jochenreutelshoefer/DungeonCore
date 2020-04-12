package de.jdungeon.gui.thumb;

import dungeon.JDPoint;
import figure.action.result.ActionResult;
import util.JDDimension;

import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.gui.activity.Activity;
import de.jdungeon.gui.ImageLibgdxGUIElement;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.01.18.
 */
public class LibgdxActivityGUIElement extends ImageLibgdxGUIElement {

	private final Activity activity;
	private final String bgInactiveImage;

	public LibgdxActivityGUIElement(JDPoint position, JDDimension dimension, Activity activity, String activityImage, String bgImage, String bgInactiveImage) {
		super(position, dimension, activityImage, bgImage);
		if(activity == null) {
			throw new IllegalArgumentException("activity may not be null!");
		}
		this.activity = activity;
		this.bgInactiveImage = bgInactiveImage;
	}


	@Override
	protected String getBackgroundImage() {
		if(activity.possible().equals(ActionResult.POSSIBLE)) {
			return backGround;
		}
		else return bgInactiveImage;
	}


	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public boolean handleClickEvent(int x, int y) {
		boolean possible = activity.plugToController();
		if(possible) {
			AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
		}
		else {
			AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.JAM);
		}

		return true;
	}
}
