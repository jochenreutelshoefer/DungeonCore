package de.jdungeon.androidapp.gui.itemWheel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.jdungeon.androidapp.gui.activity.Activity;
import de.jdungeon.androidapp.gui.activity.DefaultActivity;
import de.jdungeon.androidapp.gui.activity.ActivityProvider;

public class ItemWheelBindingSetSimple implements ItemWheelBindingSet {

	public static final int DEFAULT_REOCURRENCE_CYCLE_SIZE = 3;

	private final int itemWheelSize;
	private final int initValue;
	private final Map<Integer, Activity> mapping = new HashMap<>();
	private final Map<Integer, Activity> completedMapping = new HashMap<>();
	private final ActivityProvider provider;
	Activity addedLast = null;
	private int reoccurrenceCycleSize;

	@Override
	public ActivityProvider getProvider() {
		return provider;
	}

	@Override
	public int getNumberOfObjects() {
		return mapping.size();
	}

	@Override
	public int getBindingSize() {
		return itemWheelSize;
	}

	@Override
	public Activity getAndClearLastAdded() {
		Activity result = addedLast;
		addedLast = null;
		return result;
	}

	public ItemWheelBindingSetSimple(int initialValue, int itemWheelSize,
									 ActivityProvider provider, int reoccurrenceCycleSize) {
		this.initValue = initialValue;
		this.itemWheelSize = itemWheelSize;
		this.provider = provider;
		this.reoccurrenceCycleSize = reoccurrenceCycleSize;

		initMapping();
	}

	public ItemWheelBindingSetSimple(int initialValue, int itemWheelSize,
									 ActivityProvider provider) {
		this(initialValue, itemWheelSize, provider, 3);
	}

	private void initMapping() {
		List<Activity> activities = provider.getActivities();
		if (activities.size() > this.itemWheelSize) {
			throw new IllegalArgumentException(
					"Item wheel binding set not implemented for this case: too many items!");
		}
		mapping.clear();
		int index = initValue;
		for (Activity itemInfo : activities) {
			mapping.put(index, itemInfo);
			index = (index + 1) % this.itemWheelSize;
		}
		completeMapping();

	}

	private void completeMapping() {
		// complete mapping to simplify drawing of itemwheel
		List<Activity> activities = provider.getActivities();
		completedMapping.clear();
		int completionIndex = initValue + activities.size();
		completedMapping.putAll(mapping);
		int activityCounter = activities.size();

		// its less than 3 items, we fill up mapping with empty entries
		// for not rendering an item multiple times on the display
		if (activities.size() > reoccurrenceCycleSize) {
			reoccurrenceCycleSize = activities.size();
		}
		while (!completedMapping.containsKey(completionIndex % itemWheelSize)) {
			int activityIndex = activityCounter % reoccurrenceCycleSize;
			Activity activity = null;
			if (activityIndex < activities.size()) {
				activity = activities.get(activityIndex);
			}
			// else: we insert null as placeholder into mapping
			int itemwheelIndex = completionIndex % itemWheelSize;
			completedMapping.put(itemwheelIndex, activity);
			activityCounter++;
			completionIndex++;
		}
	}

	@Override
	public Activity getActivity(int index) {
		//return mapping.get(index);
		return completedMapping.get(index);
	}

	@Override
	public void update(float time) {
		List<Activity> figureItemList = provider.getActivities();
		Collection<Activity> values = mapping.values();
		if (figureItemList.containsAll(values)
				&& values.containsAll(figureItemList)) {
			return;
		}
		/*
		 * remove items not possessed any more
		 */
		Iterator<Activity> iterator = mapping.values().iterator();
		List<Activity> toRemove = new ArrayList<>();
		while (iterator.hasNext()) {
			Activity itemInfo = iterator.next();
			if (!figureItemList.contains(itemInfo)) {
				toRemove.add(itemInfo);
			}
		}
		if (!toRemove.isEmpty()) {
			initMapping();
			return;
		}
		/*
		for (ItemWheelActivity itemInfo : toRemove) {
			removeBinding(itemInfo);
		}
		*/

		/*
		 * bind new items
		 */
		for (Activity itemInfo : figureItemList) {
			if (!mapping.containsValue(itemInfo)) {
				insertItem(itemInfo);

			}
		}
		completeMapping();
	}

	private void insertItem(Activity itemInfo) {

		boolean inserted = false;
		int index = initValue;
		int iteration = 0;
		while (iteration < this.itemWheelSize) {
			if (!mapping.containsKey(index)) {
				mapping.put(index, itemInfo);
				this.addedLast = itemInfo;
				inserted = true;
				break;
			}
			iteration++;
			index = (index + 1) % this.itemWheelSize;
		}

		if (!inserted) {
			throw new IllegalArgumentException(
					"Item wheel binding set not implemented for this case: too many items!");
		}

	}

	private void removeBinding(DefaultActivity itemInfo) {
		Set<Integer> keySet = mapping.keySet();
		/*
		 * TODO: optimize this using bidirectional map
		 */
		for (Integer integer : keySet) {
			if (mapping.get(integer).equals(itemInfo)) {
				mapping.remove(integer);
				break;
			}
		}

	}

}
