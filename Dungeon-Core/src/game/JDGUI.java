/*
 * Created on 13.11.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package game;


import item.ItemInfo;
import spell.SpellInfo;
import dungeon.RoomInfo;
import figure.FigureInfo;
import figure.action.Action;

public interface JDGUI extends ControlUnit{
	
	public boolean currentAnimationThreadRunning(RoomInfo r);
	
	public void plugAction(Action a);
	
	public int getSelectedItemIndex();
	
	public ItemInfo getSelectedItem();
	
	public SpellInfo getSelectedSpellInfo();
	
	public void setSpellMetaDown(boolean b);
	
	public FigureInfo getFigure();
	
	public void setUseWithTarget(boolean b);
	
	public void gameRoundEnded();
}
