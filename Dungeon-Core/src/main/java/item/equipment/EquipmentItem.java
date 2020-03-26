/*
 * Created on 07.11.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package item.equipment;


import util.JDColor;
import figure.attribute.Attribute;

/**
 * Klasse
 *
 */
public abstract class EquipmentItem extends item.Item {
	

	public EquipmentItem(int value,boolean m) {
		super(value,m);
	}
	
	
	@Override
	public abstract String getText();

	public JDColor getStatusColor() {
		JDColor color = JDColor.black;
		return color;
	}


}
