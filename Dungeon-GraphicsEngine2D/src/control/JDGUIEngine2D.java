package control;

import figure.FigureInfo;
import figure.action.Action;
import game.JDGUI;
import item.ItemInfo;
import spell.SpellInfo;
import dungeon.RoomInfo;

public interface JDGUIEngine2D extends JDGUI {

	/**
	 * Sets the figure's action which will be processed next by the game.
	 * 
	 * @param a
	 */
	public void plugAction(Action a);

	public boolean currentAnimationThreadRunning(RoomInfo r);

	int getSelectedItemIndex();

	void setSelectedItemIndex(int i);

	public ItemInfo getSelectedItem();

	public SpellInfo getSelectedSpellInfo();

	public void setSpellMetaDown(boolean b);

	public FigureInfo getFigure();

	public void setUseWithTarget(boolean b);

	public void stopAllAnimation();

	public ActionAssembler getControl();

	MainFrameI getMainFrame();

}
