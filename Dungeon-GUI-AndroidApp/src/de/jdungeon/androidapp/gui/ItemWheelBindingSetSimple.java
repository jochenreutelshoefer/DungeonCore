package de.jdungeon.androidapp.gui;

import figure.hero.HeroInfo;
import item.ItemInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemWheelBindingSetSimple implements
		ItemWheelBindingSet<ItemInfo> {

	private final HeroInfo hero;
	private final int size;
	private final int initValue;
	private final Map<Integer, ItemInfo> mapping = new HashMap<Integer, ItemInfo>();
	
	public ItemWheelBindingSetSimple(HeroInfo hero, int initialValue, int size) {
		this.hero = hero;
		this.initValue = initialValue;
		this.size = size;

		List<ItemInfo> figureItemList = hero.getFigureItemList();
		if (figureItemList.size() > this.size) {
			throw new IllegalArgumentException(
					"Item wheel binding set not implemented for this case: too many items!");
		}

		int index = initValue;
		for (ItemInfo itemInfo : figureItemList) {
			mapping.put(index, itemInfo);
			index = (index + 1) % this.size;
		}
	}

	@Override
	public ItemInfo getInfoEntity(int index) {
		return mapping.get(index);
	}

	@Override
	public void update(float time) {
		List<ItemInfo> figureItemList = hero.getFigureItemList();
		/*
		 * remove items not possessed any more
		 */
		Iterator<ItemInfo> iterator = mapping.values().iterator();
		List<ItemInfo> toRemove = new ArrayList<ItemInfo>();
		while (iterator.hasNext()) {
			ItemInfo itemInfo = iterator.next();
			if (!figureItemList.contains(itemInfo)) {
				toRemove.add(itemInfo);
			}
		}
		for (ItemInfo itemInfo : toRemove) {
			removeBinding(itemInfo);
		}

		/*
		 * bind new items
		 */
		for (ItemInfo itemInfo : figureItemList) {
			if (!mapping.containsValue(itemInfo)) {
				insertItem(itemInfo);
			}
		}


	}

	private void insertItem(ItemInfo itemInfo) {

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

	private void removeBinding(ItemInfo itemInfo) {
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
