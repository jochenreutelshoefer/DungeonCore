package control;

import figure.action.Action;
import game.JDGUI;
import de.jdungeon.game.AbstractImageLoader;
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

	int getSelectedItemIndex();

	ItemInfo getSelectedItem();

	SpellInfo getSelectedSpellInfo();

	void setSpellMetaDown(boolean b);

	void setUseWithTarget(boolean b);

}
