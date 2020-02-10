package de.jdungeon.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dungeon.JDPoint;
import event.Event;
import event.EventListener;
import event.WorldChangedEvent;
import item.ItemInfo;
import util.JDDimension;

import de.jdungeon.LibgdxDungeonMain;
import de.jdungeon.app.gui.GUIImageManager;
import de.jdungeon.app.gui.InventoryImageManager;
import de.jdungeon.app.gui.activity.Activity;
import de.jdungeon.app.gui.activity.ExecutableActivity;
import de.jdungeon.gui.thumb.LibgdxActivityGUIElement;
import de.jdungeon.ui.LibgdxGUIElement;
import de.jdungeon.world.ScreenAdapter;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 09.02.20.
 */
public class LibgdxUseItemPresenter extends LibgdxActivityPresenter implements EventListener {

	private static final int defaultImageWidth = 50;
	private final List<LibgdxActivityGUIElement> activities = new ArrayList<>();
	private final JDPoint[] itemTilePositions = new JDPoint[5];
	private final InventoryImageManager inventoryImageManager;
	private final JDDimension tileDimension;


	public LibgdxUseItemPresenter(JDPoint point, JDDimension dimension,  LibgdxUseItemActivityProvider useItemActivityProvider, String imageBg, InventoryImageManager inventoryImageManager) {
		super(point, dimension,  useItemActivityProvider, imageBg, defaultImageWidth);

		tileDimension = new JDDimension(defaultImageWidth, defaultImageWidth);

		JDDimension parentDim = getDimension();
		int posY = parentDim.getHeight() * 2 / 3;
		// parent X and parent Y are not required here, as we have relative coordinates (this is calculated in the SubGUIElement)
		itemTilePositions[0] = new JDPoint(parentDim.getWidth() * 0/5, posY);
		itemTilePositions[1] = new JDPoint(parentDim.getWidth() * 1/5, posY);
		itemTilePositions[2] = new JDPoint(parentDim.getWidth() * 2/5, posY);
		itemTilePositions[3] = new JDPoint(parentDim.getWidth() * 3/5, posY);
		itemTilePositions[4] = new JDPoint(parentDim.getWidth() * 4/5, posY);

		this.inventoryImageManager = inventoryImageManager;

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
		if(objectActivity != null) {
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
		for (LibgdxActivityGUIElement activity : activities) {
			activity.paint(batch);
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
		if(event instanceof WorldChangedEvent) {
			updateActivities();
		}
	}

	private void updateActivities() {
		activities.clear();
		List<Activity> newActivities = provider.getActivities();
		int tileIndex = 0;
		for (Activity activity : newActivities) {
			if(tileIndex < this.itemTilePositions.length) {
				String image = inventoryImageManager.getJDImage((ItemInfo) activity.getObject()).getFilenameBlank();
				activities.add(new LibgdxActivityGUIElement(itemTilePositions[tileIndex], tileDimension, this, (ExecutableActivity)activity, image, null ));
			}
			tileIndex++;
		}
	}}
