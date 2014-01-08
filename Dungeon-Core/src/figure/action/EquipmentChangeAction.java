/*
 * Created on 04.01.2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package figure.action;


public class EquipmentChangeAction extends Action {
	
	public final static int EQUIPMENT_TYPE_WEAPON = 1;
	public final static int EQUIPMENT_TYPE_ARMOR = 2;
	public final static int EQUIPMENT_TYPE_HELMET = 3;
	public final static int EQUIPMENT_TYPE_SHIELD = 4;

	
	
	private final int equpipmentType;
	private final int index;
	
	public EquipmentChangeAction(int eType, int index) {
		super();
		this.equpipmentType = eType;
		this.index = index;
	}

	/**
	 * @return Returns the equpipmentType.
	 */
	public int getEqupipmentType() {
		return equpipmentType;
	}

	/**
	 * @return Returns the index.
	 */
	public int getIndex() {
		return index;
	}
	
	

}
