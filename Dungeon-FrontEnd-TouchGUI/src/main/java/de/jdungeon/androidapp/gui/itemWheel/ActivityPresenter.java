package de.jdungeon.androidapp.gui.itemWheel;

import java.util.List;

import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.androidapp.gui.AbstractGUIElement;
import de.jdungeon.androidapp.gui.GUIElement;
import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Game;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 25.01.18.
 */
public abstract class ActivityPresenter extends AbstractGUIElement {

	protected boolean visible = true;
	protected boolean highlightOn = false;
	protected int markedPointIndex;


	public ActivityPresenter(JDPoint position, JDDimension dimension, StandardScreen screen, Game game) {
		super(position, dimension, screen, game);
	}

	public ActivityPresenter(JDPoint position, JDDimension dimension) {
		super(position, dimension);
	}

	public ActivityPresenter(JDPoint position, JDDimension dimension, GUIElement parent) {
		super(position, dimension, parent);
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public abstract void highlightEntity(Object object);

	public abstract Object highlightFirst();

	protected abstract void centerOnIndex(int i);

	public void setHighlightOn(boolean val) {
		this.highlightOn = val;
	}

	protected abstract void iconTouched(Activity activity);



}
