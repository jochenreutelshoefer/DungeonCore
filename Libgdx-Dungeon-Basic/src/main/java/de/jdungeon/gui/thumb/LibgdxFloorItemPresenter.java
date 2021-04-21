package de.jdungeon.gui.thumb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.jdungeon.app.gui.InventoryImageManager;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.event.Event;
import de.jdungeon.event.EventListener;
import de.jdungeon.event.WorldChangedEvent;
import de.jdungeon.graphics.JDImageProxy;
import de.jdungeon.item.ItemInfo;
import de.jdungeon.util.JDDimension;

import de.jdungeon.gui.activity.Activity;
import de.jdungeon.gui.LibgdxActivityPresenter;
import de.jdungeon.gui.LibgdxActivityProvider;
import de.jdungeon.gui.LibgdxGUIElement;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.01.18.
 */
public class LibgdxFloorItemPresenter extends LibgdxActivityPresenter implements EventListener {

	private final List<LibgdxActivityGUIElement> activities = new ArrayList<>();
	private final JDPoint[] itemTilePositions = new JDPoint[2];
	private final InventoryImageManager inventoryImageManager;
	private final JDDimension tileDimension;

	public LibgdxFloorItemPresenter(JDPoint position, JDDimension dimension, LibgdxGUIElement parent, InventoryImageManager inventoryImageManager, LibgdxActivityProvider provider, String itemBg, int tileSize) {
		super(position, dimension, provider, itemBg, tileSize);

		tileDimension = new JDDimension(tileSize, tileSize);

		JDDimension parentDim = parent.getDimension();
		int posYOffset = parentDim.getHeight() * 2 / 3;
		// parent X and parent Y are not required here, as we have relative coordinates (this is calculated in the SubGUIElement)
		itemTilePositions[0] = new JDPoint(parent.getPositionOnScreen().getX() + parentDim.getWidth() * 1 / 5, parent.getPositionOnScreen().getY() + posYOffset);
		itemTilePositions[1] = new JDPoint(parent.getPositionOnScreen().getX() + parentDim.getWidth() * 2 / 3, parent.getPositionOnScreen().getY() + posYOffset);

		this.inventoryImageManager = inventoryImageManager;
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

	}

	@Override
	public Object highlightFirst() {
		return null;
	}

	@Override
	protected void centerOnIndex(Activity activity) {

	}

	@Override
	public void paint(SpriteBatch batch, float deltaTime) {
		for (LibgdxActivityGUIElement activity : activities) {
			activity.paint(batch, deltaTime);
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
		List<Activity> newActivities = provider.getActivities();
		int tileIndex = 0;
		for (Activity activity : newActivities) {
			if (tileIndex < this.itemTilePositions.length) {
				JDImageProxy image = inventoryImageManager.getJDImage((ItemInfo) activity.getObject());
				activities.add(new LibgdxActivityGUIElement(itemTilePositions[tileIndex], tileDimension, activity, image
						.getFilenameBlank(), null, null));
			}
			tileIndex++;
		}
	}
}
