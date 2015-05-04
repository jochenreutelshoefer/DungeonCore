package control;

import figure.action.Action;
import game.JDGUI;
import graphics.AbstractImageLoader;
import item.ItemInfo;
import spell.SpellInfo;
import dungeon.RoomInfo;

public interface JDGUIEngine2D extends JDGUI {

	/**
	 * Sets the figure's action which will be processed next by the game.
	 * 
	 * @param a
	 */
	void plugAction(Action a);

	boolean currentAnimationThreadRunning(RoomInfo r);

	int getSelectedItemIndex();

	void setSelectedItemIndex(int i);

	ItemInfo getSelectedItem();

	SpellInfo getSelectedSpellInfo();

	void setSpellMetaDown(boolean b);



	void setUseWithTarget(boolean b);

	void stopAllAnimation();

	ActionAssembler getActionAssembler();

	AbstractSwingMainFrame getMainFrame();

	AbstractImageLoader getImageSource();


}
