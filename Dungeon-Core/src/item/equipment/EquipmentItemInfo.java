/*
 * Created on 07.11.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package item.equipment;

import util.JDColor;
import figure.DungeonVisibilityMap;

/**
 * Klasse
 *
 */
public class EquipmentItemInfo extends item.ItemInfo {
	
	public EquipmentItemInfo(EquipmentItem i, DungeonVisibilityMap map) {
		super(i,map);
	}
	
	public JDColor getStatusColor() {
		return ((EquipmentItem)it).getStatusColor();
	}

}
