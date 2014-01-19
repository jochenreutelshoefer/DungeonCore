package de.jdungeon.androidapp.gui.itemWheel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemWheelBindingSetSimple implements ItemWheelBindingSet {

	private final int size;
	private final int initValue;
	private final Map<Integer, ItemWheelActivity> mapping = new HashMap<Integer, ItemWheelActivity>();
	private final ItemWheelActivityProvider provider;

	@Override
	public ItemWheelActivityProvider getProvider() {
		return provider;
	}

	public ItemWheelBindingSetSimple(int initialValue, int size,
			ItemWheelActivityProvider provider) {
		this.initValue = initialValue;
		this.size = size;
		this.provider = provider;

		List<ItemWheelActivity> activities = provider.getActivities();
		if (activities.size() > this.size) {
			throw new IllegalArgumentException(
					"Item wheel binding set not implemented for this case: too many items!");
		}
		int index = initValue;
		for (ItemWheelActivity itemInfo : activities) {
			mapping.put(index, itemInfo);
			index = (index + 1) % this.size;
		}
	}

	@Override
	public ItemWheelActivity getActivity(int index) {
		return mapping.get(index);
	}

	@Override
	public void update(float time) {
		List<ItemWheelActivity> figureItemList = provider.getActivities();
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
		for (ItemWheelActivity itemInfo : toRemove) {
			removeBinding(itemInfo);
		}

		/*
		 * bind new items
		 */
		for (ItemWheelActivity itemInfo : figureItemList) {
			if (!mapping.containsValue(itemInfo)) {
				insertItem(itemInfo);
			}
		}

	}

	private void insertItem(ItemWheelActivity itemInfo) {

		boolean inserted = false;
		int index = initValue;
		int iteration = 0;
		while (iteration < this.size) {
			if (!mapping.containsKey(index)) {
				mapping.put(index, itemInfo);
				inserted = true;
				break;
			}
			iteration++;
			index = (index + 1) % this.size;
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
