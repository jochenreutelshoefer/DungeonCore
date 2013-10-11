package shrine;

import item.*;
import item.interfaces.Usable;
import item.quest.Thing;

import java.awt.Color;

import figure.Figure;
import figure.FigureInfo;
import figure.hero.Hero;
import figure.percept.Percept;
import figure.percept.UsePercept;
import game.JDEnv;
import gui.Texts;


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
	public void turn(int round) {
	}
	
	public boolean needsTarget() {
		return false;
	}
	
	public boolean canBeUsedBy(Figure f) {
		   return f instanceof Hero;
	   }

	/**
	 * @see Shrine#getColor()
	 */
	public Color getColor() {
		return Color.black;
	}

	/**
	 * @see Shrine#getStory()
	 */
	public String getStory() {
		return null;
	}
	
	public int getShrineIndex() {
		return Shrine.SHRINE_QUEST;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name +": "+getStatus();
	}

	/**
	 * @see Shrine#getText()
	 */
	public String getText() {
		return name;
	}

	

	public void metaClick(Figure f) {
		
	}
	/**
	 * @see Shrine#getStatus()
	 */
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
