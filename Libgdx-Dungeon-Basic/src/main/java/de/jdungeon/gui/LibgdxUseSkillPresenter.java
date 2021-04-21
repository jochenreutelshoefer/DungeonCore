package de.jdungeon.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.event.Event;
import de.jdungeon.event.EventListener;
import de.jdungeon.event.WorldChangedEvent;
import de.jdungeon.util.JDDimension;

import de.jdungeon.gui.activity.Activity;
import de.jdungeon.gui.thumb.LibgdxActivityGUIElement;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 09.02.20.
 */
public class LibgdxUseSkillPresenter extends LibgdxActivityPresenter implements EventListener {

	//private int defaultImageWidth;
	private final List<LibgdxActivityGUIElement> activities = new ArrayList<>();
	private static final int NUMBER_OF_SLOTS = 6;
	private final JDPoint[] relativeItemTilePositions = new JDPoint[NUMBER_OF_SLOTS];
	private final JDDimension tileDimension;
	private final String imageBg;
	private final String imageInactiveBg;

	public LibgdxUseSkillPresenter(JDPoint presenterPos, JDDimension dimension, LibgdxActivityProvider useItemActivityProvider, String imageBg, String imageInactiveBg) {
		super(presenterPos, dimension, useItemActivityProvider, imageBg, (int) (dimension.getHeight() * 0.6));
		this.imageBg = imageBg;
		this.imageInactiveBg = imageInactiveBg;
		//defaultImageWidth = (int) (this.getDimension().getHeight() * 0.6);
		tileDimension = new JDDimension(defaultImageWidth, defaultImageWidth);


		JDDimension parentDim = getDimension();
		int posY = parentDim.getHeight() * 2 / 3;
		// parent X and parent Y are not required here, as we have relative coordinates (this is calculated in the SubGUIElement)
		int width = parentDim.getWidth();
		for(int i = 0; i < NUMBER_OF_SLOTS; i++) {
			relativeItemTilePositions[i] = new JDPoint(presenterPos.getX() + width - width * (i+1) / NUMBER_OF_SLOTS, presenterPos.getY() + posY);
		}

	}

	@Override
	protected List<? extends LibgdxGUIElement> getAllSubElements() {
		return activities;
	}

	@Override
	public void update(float deltaTime, int round) {
		updateActivities();
	}

	@Override
	public void highlightEntity(Object object) {
		// do nothing
	}

	@Override
	public Object highlightFirst() {
		return null;
	}

	@Override
	protected void centerOnIndex(Activity activity) {

	}

	/*
	@Override
	public void paint(SpriteBatch batch) {
		for(Map.Entry<Activity, LibgdxActivityGUIElement> entry : activityMap.entrySet()) {
			JDPoint positionOnScreen = entry.getValue().getPositionOnScreen();
			drawActivityRelative(batch, positionOnScreen.getX(), positionOnScreen.getY(), entry.getKey());
		}
	}
	*/

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
			if (tileIndex < this.relativeItemTilePositions.length) {
				String activityImage = this.provider.getActivityImage(activity);
				LibgdxActivityGUIElement libgdxActivityGUIElement = new LibgdxActivityGUIElement(relativeItemTilePositions[tileIndex], tileDimension,  activity, activityImage, imageBg, imageInactiveBg);
				activities.add(libgdxActivityGUIElement);
			}
			tileIndex++;
		}
	}
}
