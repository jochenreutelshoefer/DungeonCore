package item;
import item.interfaces.Usable;
import figure.Figure;
import figure.attribute.Attribute;
import figure.attribute.TimedAttributeModification;
import figure.hero.Character;
import figure.percept.TextPercept;
import game.JDEnv;
import gui.Texts;

public class HealPotion extends AttrPotion implements Usable{

    //String Type;

    public HealPotion(int value){
    	super(Attribute.HEALTH,value);
	
	}
    
    public int getItemKey() {
    	return Item.ITEM_KEY_HEALPOTION;
    }
	
}
