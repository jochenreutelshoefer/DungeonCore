/*
 * Created on 07.11.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package de.jdungeon.item.equipment;

import de.jdungeon.util.JDColor;
import de.jdungeon.figure.DungeonVisibilityMap;

/**
 * Klasse
 *
 */
public class EquipmentItemInfo extends de.jdungeon.item.ItemInfo {
	
	public EquipmentItemInfo(EquipmentItem i, DungeonVisibilityMap map) {
		super(i,map);
	}
	
	public JDColor getStatusColor() {
		return ((EquipmentItem)it).getStatusColor();
	}

}
