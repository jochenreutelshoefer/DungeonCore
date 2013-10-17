package shrine;

import figure.Figure;
import figure.hero.Hero;
import figure.percept.Percept;
import figure.percept.UsePercept;
import game.JDEnv;
import gui.Texts;
import item.Item;
import item.ItemPool;
import item.interfaces.Usable;
import item.quest.Thing;
import util.JDColor;


/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class QuestShrine extends Shrine {

	
	Item requestedItem;

	
	Item goodItem;

	
	public static  String[] names = null;
	public static  String[] items = null;
	
	public QuestShrine(String name, String text, String story, Item toFind, Item toGive) {
		this.name = name;
		this.text = text;
		this.story = story;
		requestedItem = toFind;
		goodItem = toGive;
	}
	
	public QuestShrine(Item i) {
		if(names == null || items == null) {
			names = Texts.makeStrings("shrine_quest_name");
			items = Texts.makeStrings("shrine_quest_item");
		}
		int k = (int)(Math.random()*names.length);
		name = names[k];
		requestedItem = new Thing(items[k],this);
		goodItem = i;	
	}
	
	public QuestShrine(int value) {
		if(names == null || items == null) {
			names = Texts.makeStrings("shrine_quest_name");
			items = Texts.makeStrings("shrine_quest_item");
		}
		int k = (int)(Math.random()*names.length);
		name = names[k];
		requestedItem = new Thing(items[k],this);
		goodItem = ItemPool.getGoodItem(value,3);	
		//System.out.println(goodItem.toString());
	}

	/**
	 * @see Shrine#turn(int)
	 */
	@Override
	public void turn(int round) {
	}
	
	@Override
	public boolean needsTarget() {
		return false;
	}
	
	@Override
	public boolean canBeUsedBy(Figure f) {
		   return f instanceof Hero;
	   }

	/**
	 * @see Shrine#getColor()
	 */
	@Override
	public JDColor getColor() {
		return JDColor.black;
	}

	/**
	 * @see Shrine#getStory()
	 */
	@Override
	public String getStory() {
		return null;
	}
	
	@Override
	public int getShrineIndex() {
		return Shrine.SHRINE_QUEST;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name +": "+getStatus();
	}

	/**
	 * @see Shrine#getText()
	 */
	@Override
	public String getText() {
		return name;
	}

	

	public void metaClick(Figure f) {
		
	}
	/**
	 * @see Shrine#getStatus()
	 */
	@Override
	public String getStatus() {
		if(goodItem == null) {
			return JDEnv.getResourceBundle().getString("shrine_quest_solved");
		}
		else {
			return JDEnv.getResourceBundle().getString("shrine_quest_unsolved");	
		}
	}

	/**
	 * @see Usable#use(fighter)
	 */
	@Override
	public boolean use(Figure f,Object target,boolean meta) {
		if(f.hasItem(this.requestedItem) && (goodItem != null)) {
			 Percept p = new UsePercept(f,this);
				f.getRoom().distributePercept(p);
			//game.getGui().figureUsingAnimation(FigureInfo.makeFigureInfo(f,game.getGui().getFigure().getVisMap()));
			f.removeItem(requestedItem);
			location.takeItem(goodItem,null);	
			goodItem = null;
			return true;
		}
		return false;
	}

	/**
	 * @see Usable#usableOnce()
	 */
	@Override
	public boolean usableOnce() {
		return false;
	}

	/**
	 * Returns the requestedItem.
	 * @return item
	 * 
	 */
	public Item getRequestedItem() {
		return requestedItem;
	}

}
