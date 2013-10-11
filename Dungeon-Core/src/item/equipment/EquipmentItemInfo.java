/*
 * Created on 07.11.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package item.equipment;

import java.awt.Color;

import figure.DungeonVisibilityMap;

import item.ItemInfo;;

/**
 * Klasse
 *
 */
public class EquipmentItemInfo extends item.ItemInfo {
	
	public EquipmentItemInfo(EquipmentItem i, DungeonVisibilityMap map) {
		super(i,map);
	}
	
	public Color getStatusColor(){
		return ((EquipmentItem)it).getStatusColor();
	}

}
