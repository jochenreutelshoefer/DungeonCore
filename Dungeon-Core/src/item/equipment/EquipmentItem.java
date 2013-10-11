/*
 * Created on 07.11.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package item.equipment;

import java.awt.Color;

import figure.attribute.Attribute;

import item.Item;

/**
 * Klasse
 *
 */
public abstract class EquipmentItem extends item.Item {
	
	protected Attribute hitPoints;

	public EquipmentItem(int value,boolean m) {
		super(value,m);
	}
	
	
	public abstract String getText();

	public Color getStatusColor() {
			Color color = Color.black;
				if (hitPoints.perCent() <= 50) {
						 color = Color.red;
						} else if (hitPoints.perCent() <= 70) {
							color = Color.yellow;
					} 
					
		return color;
	}


	/**
	 * Returns the hit_points.
	 * @return attribute
	 * 
	 */
	public Attribute getHitPoints() {
		return hitPoints;
	}


	public void repairPerCent(int value) {
		int all = (int)hitPoints.getBasic();
		int amount = (all * value)/100;
		repair(amount);
	}


	/**
	 * Sets the hit_points.
	 * @param hitPoints The hit_points to set
	 */
	public void setHitPoints(int basic) {
		this.hitPoints = new Attribute(Attribute.HIT_POINTS,2);
	}


	public void repair(int value) {
		Attribute a = getHitPoints();
		if (a != null) {
			if(a.getValue() + value <= a.getBasic()){
	    
	    		a.modValue(value);
			}
			else{
	    		a.setValue((a.getBasic()));
	    	}
		}
	
	}

}
