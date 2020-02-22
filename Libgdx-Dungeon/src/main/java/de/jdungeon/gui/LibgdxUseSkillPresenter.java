package de.jdungeon.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dungeon.JDPoint;
import event.Event;
import event.EventListener;
import event.WorldChangedEvent;
import util.JDDimension;

import de.jdungeon.app.gui.activity.Activity;
import de.jdungeon.app.gui.activity.ExecutableActivity;
import de.jdungeon.gui.thumb.LibgdxActivityGUIElement;
import de.jdungeon.ui.LibgdxGUIElement;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 09.02.20.
 */
public class LibgdxUseSkillPresenter extends LibgdxActivityPresenter implements EventListener {

	private static final int defaultImageWidth = 50;
	private final List<LibgdxActivityGUIElement> activities = new ArrayList<>();
	private final Map<Activity, LibgdxActivityGUIElement> activityMap = new HashMap<>();
	private final JDPoint[] itemTilePositions = new JDPoint[5];
	private final JDDimension tileDimension;
	private final String imageBg;

	public LibgdxUseSkillPresenter(JDPoint point, JDDimension dimension, LibgdxActivityProvider useItemActivityProvider, String imageBg) {
		super(point, dimension, useItemActivityProvider, imageBg, defaultImageWidth);
		this.imageBg = imageBg;

		tileDimension = new JDDimension(defaultImageWidth, defaultImageWidth);

		int screenWidth = Gdx.app.getGraphics().getWidth();

		JDDimension parentDim = getDimension();
		int posY = parentDim.getHeight() * 2 / 3;
		// parent X and parent Y are not required here, as we have relative coordinates (this is calculated in the SubGUIElement)
		int widht = parentDim.getWidth();
		itemTilePositions[0] = new JDPoint(widht - widht * 1 / 5, posY);
		itemTilePositions[1] = new JDPoint(widht - widht * 2 / 5, posY);
		itemTilePositions[2] = new JDPoint(widht - widht * 3 / 5, posY);
		itemTilePositions[3] = new JDPoint(widht - widht * 4 / 5, posY);
		itemTilePositions[4] = new JDPoint(widht - widht * 5 / 5, posY);

	}

	@Override
	protected List<? extends LibgdxGUIElement> getAllSubElements() {
		return activities;
	}

	@Override
	public void update(float time) {
		updateActivities();
	}

	@Override
	public void highlightEntity(Object object) {
		Activity objectActivity = getObjectActivity(object);
		if (objectActivity != null) {
			centerOnIndex(objectActivity);
		}
	}

	private Activity getObjectActivity(Object object) {
		return null;
	}

	@Override
	public Object highlightFirst() {
		return null;
	}

	@Override
	protected void centerOnIndex(Activity activity) {

	}

	@Override
	public void paint(SpriteBatch batch) {
		for(Map.Entry<Activity, LibgdxActivityGUIElement> entry : activityMap.entrySet()) {
			JDPoint positionOnScreen = entry.getValue().getPositionOnScreen();
			drawActivityRelative(batch, positionOnScreen.getX(), positionOnScreen.getY(), entry.getKey());
		}
	}

	@Override
	public Collection<Class<? extends Event>> getEvents() {
		Collection<Class<? extends Event>> events = new HashSet<>();
		events.add(WorldChangedEvent.class);
		return events;
	}

	@Override
	public void notify(Event event) {
		if (event instanceof WorldChangedEvent) {
			updateActivities();
		}
	}

	private void updateActivities() {
		activities.clear();
		// Todo: refactor in a way that the activity provider return list of ExecutableActivity
		List<Activity> newActivities = provider.getActivities();
		int tileIndex = 0;
		for (Activity activity : newActivities) {
			if (tileIndex < this.itemTilePositions.length) {
				String activityImage = this.provider.getActivityImage(activity);
				LibgdxActivityGUIElement libgdxActivityGUIElement = new LibgdxActivityGUIElement(itemTilePositions[tileIndex], tileDimension, this, (ExecutableActivity) activity, activityImage, imageBg);
				activities.add(libgdxActivityGUIElement);
				activityMap.put(activity, libgdxActivityGUIElement);
			}
			tileIndex++;
		}
	}
}
