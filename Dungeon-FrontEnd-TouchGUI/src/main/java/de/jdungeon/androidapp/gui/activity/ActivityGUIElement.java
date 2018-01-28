package de.jdungeon.androidapp.gui.activity;

import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.androidapp.gui.GUIElement;
import de.jdungeon.androidapp.gui.SubGUIElement;
import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.01.18.
 */
public class ActivityGUIElement extends SubGUIElement {

	private final ActivityPresenter parent;
	private final ExecutableActivity activity;
	private final Image activityImage;
	private final Image itemBg;

	public ActivityGUIElement(JDPoint position, JDDimension dimension, ActivityPresenter parent, ExecutableActivity activity, Image activityImage, Image itemBg) {
		super(position, dimension, parent);
		this.parent = parent;
		this.activity = activity;
		this.activityImage = activityImage;
		this.itemBg = itemBg;
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		if(itemBg != null) {
			g.drawScaledImage(itemBg, getX(), getY(), this.getDimension().getWidth(), this.getDimension().getHeight(), 0 ,0 ,itemBg.getWidth(), itemBg.getHeight());
		}
		g.drawScaledImage(activityImage, getX(), getY(), this.getDimension().getWidth(), this.getDimension().getHeight(), 0 ,0 ,activityImage.getWidth(), activityImage.getHeight());

	}

	@Override
	public boolean isVisible() {
		return activity.isCurrentlyPossible();
	}

	@Override
	public boolean handleTouchEvent(Input.TouchEvent touch) {
		activity.execute();
		return true;
	}

}
