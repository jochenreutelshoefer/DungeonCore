package control;

import figure.action.Action;
import game.JDGUI;
import de.jdungeon.game.AbstractImageLoader;
import item.ItemInfo;
import spell.SpellInfo;
import dungeon.RoomInfo;

public interface JDGUIEngine2D extends JDGUI {



	int getSelectedItemIndex();

	ItemInfo getSelectedItem();

	SpellInfo getSelectedSpellInfo();

	void setSpellMetaDown(boolean b);

	void setUseWithTarget(boolean b);


}
