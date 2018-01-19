package de.jdungeon.androidapp.gui.itemWheel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemWheelBindingSetSimple implements ItemWheelBindingSet {

	private final int itemWheelSize;
	private final int initValue;
	private final Map<Integer, ItemWheelActivity> mapping = new HashMap<>();
	private final Map<Integer, ItemWheelActivity> completedMapping = new HashMap<>();
	private final ItemWheelActivityProvider provider;
	ItemWheelActivity addedLast = null;

	@Override
	public ItemWheelActivityProvider getProvider() {
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
	public ItemWheelActivity getAndClearLastAdded() {
		ItemWheelActivity result = addedLast;
		addedLast = null;
		return result;
	}

	public ItemWheelBindingSetSimple(int initialValue, int itemWheelSize,
									 ItemWheelActivityProvider provider) {
		this.initValue = initialValue;
		this.itemWheelSize = itemWheelSize;
		this.provider = provider;

		initMapping();
	}

	private void initMapping() {
		List<ItemWheelActivity> activities = provider.getActivities();
		if (activities.size() > this.itemWheelSize) {
			throw new IllegalArgumentException(
					"Item wheel binding set not implemented for this case: too many items!");
		}
		mapping.clear();
		int index = initValue;
		for (ItemWheelActivity itemInfo : activities) {
			mapping.put(index, itemInfo);
			index = (index + 1) % this.itemWheelSize;
		}
		completeMapping();

	}

	private void completeMapping() {
		// complete mapping to simplify drawing of itemwheel
		List<ItemWheelActivity> activities = provider.getActivities();
		completedMapping.clear();
		int completionIndex = initValue + activities.size();
		completedMapping.putAll(mapping);
		int activityCounter = activities.size();
		int reoccurrenceCycleSize = 3;
		// its less than 3 items, we fill up mapping with empty entries
		// for not rendering an item multiple times on the display
		if (activities.size() > reoccurrenceCycleSize) {
			reoccurrenceCycleSize = activities.size();
		}
		while (!completedMapping.containsKey(completionIndex % itemWheelSize)) {
			int activityIndex = activityCounter % reoccurrenceCycleSize;
			ItemWheelActivity activity = null;
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
	public ItemWheelActivity getActivity(int index) {
		//return mapping.get(index);
		return completedMapping.get(index);
	}

	@Override
	public void update(float time) {
		List<ItemWheelActivity> figureItemList = provider.getActivities();
		Collection<ItemWheelActivity> values = mapping.values();
		if (figureItemList.containsAll(values)
				&& values.containsAll(figureItemList)) {
			return;
		}
		/*
		 * remove items not possessed any more
		 */
		Iterator<ItemWheelActivity> iterator = mapping.values().iterator();
		List<ItemWheelActivity> toRemove = new ArrayList<ItemWheelActivity>();
		while (iterator.hasNext()) {
			ItemWheelActivity itemInfo = iterator.next();
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
		for (ItemWheelActivity itemInfo : figureItemList) {
			if (!mapping.containsValue(itemInfo)) {
				insertItem(itemInfo);

			}
		}
		completeMapping();
	}

	private void insertItem(ItemWheelActivity itemInfo) {

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

	private void removeBinding(ItemWheelActivity itemInfo) {
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
