package de.jdungeon.item;

import java.util.Collections;
import java.util.List;

import de.jdungeon.figure.Figure;
import de.jdungeon.figure.attribute.Attribute;
import de.jdungeon.item.interfaces.Usable;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 26.03.20.
 */
public class OxygenPotion extends AttrPotion implements Usable {

	public OxygenPotion(){
		super(Attribute.Type.Oxygen , 10);
	}

	@Override
	public int getItemKey() {
		return Item.ITEM_KEY_ATTRPOTION;
	}

	@Override
	public List<PotionMod> getModifications(Figure held) {
		return Collections.singletonList(new PotionMod((int) held.getAttribute(Attribute.Type.Oxygen).getBasic(), 0));
	}


}
