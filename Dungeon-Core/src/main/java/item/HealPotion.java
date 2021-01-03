package item;
import dungeon.RoomEntity;
import item.interfaces.Usable;
import figure.Figure;
import figure.attribute.Attribute;
import figure.attribute.TimedAttributeModification;
import figure.hero.Character;
import figure.percept.TextPercept;
import game.JDEnv;
import gui.Texts;

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
