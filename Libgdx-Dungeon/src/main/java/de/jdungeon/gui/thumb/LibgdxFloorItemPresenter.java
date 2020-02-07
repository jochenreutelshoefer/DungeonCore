package de.jdungeon.gui.thumb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dungeon.JDPoint;
import event.Event;
import event.EventListener;
import event.WorldChangedEvent;
import graphics.JDImageProxy;
import item.ItemInfo;
import util.JDDimension;

import de.jdungeon.app.gui.GUIElement;
import de.jdungeon.app.gui.GUIImageManager;
import de.jdungeon.app.gui.InventoryImageManager;
import de.jdungeon.app.gui.activity.Activity;
import de.jdungeon.app.gui.activity.ActivityProvider;
import de.jdungeon.app.gui.activity.ExecutableActivity;
import de.jdungeon.app.screen.StandardScreen;
import de.jdungeon.game.Game;
import de.jdungeon.game.Image;
import de.jdungeon.ui.LibgdxGUIElement;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.01.18.
 */
public class LibgdxFloorItemPresenter extends LibgdxActivityPresenter implements EventListener {

	private final List<LibgdxActivityGUIElement> activities = new ArrayList<>();
	private final JDPoint[] itemTilePositions = new JDPoint[2];
	private final InventoryImageManager inventoryImageManager;
	private final JDDimension tileDimension;

	public LibgdxFloorItemPresenter(JDPoint position, JDDimension dimension, LibgdxGUIElement parent, Game game, ActivityProvider provider, Image itemBg, int tileSize) {
		super(position, dimension, game, provider, itemBg, tileSize);

		tileDimension = new JDDimension(tileSize, tileSize);

		JDDimension parentDim = parent.getDimension();
		int posY = parentDim.getHeight() * 2 / 3;
		// parent X and parent Y are not required here, as we have relative coordinates (this is calculated in the SubGUIElement)
		itemTilePositions[0] = new JDPoint(parentDim.getWidth() * 1 / 5, posY);
		itemTilePositions[1] = new JDPoint(parentDim.getWidth() * 2 / 3, posY);

		inventoryImageManager = new InventoryImageManager(new GUIImageManager(game.getFileIO().getImageLoader()));
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
				activities.add(new LibgdxActivityGUIElement(itemTilePositions[tileIndex], tileDimension, this, (ExecutableActivity) activity, image
						.getFilenameBlank(), null));
			}
			tileIndex++;
		}
	}
}
