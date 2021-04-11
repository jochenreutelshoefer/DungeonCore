package de.jdungeon.item;
import de.jdungeon.item.interfaces.Usable;
import de.jdungeon.figure.attribute.Attribute;

public class HealPotion extends AttrPotion implements Usable{


    public HealPotion(int value){
    	super(Attribute.Type.Health, value);
	}

	public HealPotion(){
		super(Attribute.Type.Health, 15);
	}
    
    @Override
	public int getItemKey() {
    	return Item.ITEM_KEY_HEALPOTION;
    }

}
