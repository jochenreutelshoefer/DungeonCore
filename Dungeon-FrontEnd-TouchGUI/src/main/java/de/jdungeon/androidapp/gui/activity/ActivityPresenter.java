package de.jdungeon.androidapp.gui.activity;

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

	protected final ActivityProvider provider;
	protected boolean visible = true;
	protected boolean highlightOn = false;
	protected int markedPointIndex;



	public ActivityPresenter(JDPoint position, JDDimension dimension, StandardScreen screen, Game game, ActivityProvider provider) {
		super(position, dimension, screen, game);
		this.provider = provider;
	}

	public ActivityPresenter(JDPoint position, JDDimension dimension, ActivityProvider provider) {
		super(position, dimension);
		this.provider = provider;
	}

	public ActivityPresenter(JDPoint position, JDDimension dimension, GUIElement parent, ActivityProvider provider) {
		super(position, dimension, parent);
		this.provider = provider;
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

	protected abstract void centerOnIndex(Activity activity);

	public void setHighlightOn(boolean val) {
		this.highlightOn = val;
	}

	protected abstract void iconTouched(Activity activity);



}
