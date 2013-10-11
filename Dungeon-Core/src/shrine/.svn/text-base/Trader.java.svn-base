package shrine;

import item.*;
import item.interfaces.Usable;

import java.awt.Color;

/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
import java.util.*;

import dungeon.*;

import util.Arith;

import figure.Figure;
import figure.hero.Hero;
import game.JDEnv;


public class Trader extends Shrine /*implements itemOwner*/{
	
	double rate = 1.6;
	int rounds_to_change = 10;
	int itemsCnt = 5;

	
	LinkedList items = new LinkedList();

	int AvWorth;

	
	int rounds = 0;

	
	boolean ok = false;

	
	Dungeon d;

	
	
	
	public Trader(int AvWorth, Dungeon d) {
		super();
		this.AvWorth = AvWorth;
		this.d = d;
		for(int i = 0;	i < itemsCnt; i++) {
			addNewItem();	
		}
	}
	
	public void metaClick(Figure f) {
		
	}
	
	public boolean use(Figure f,Object target,boolean meta) {
		return false;
	}
	
	
	private void addNewItem() {
		int value = (int)Arith.gauss((int)(10 + (int)(Math.random() * AvWorth)), 2);	
		Item i = ItemPool.getRandomItem(value, 0.8+Math.random());
		while((i instanceof DustItem)) {
			i = ItemPool.getRandomItem(value, 0.8+Math.random());
			
		}
		items.add(i);
	}

	public boolean canBeUsedBy(Figure f) {
		   return f instanceof Hero;
	   }
	public void turn(int round) {
		rounds++;
		if (rounds > rounds_to_change) {
			rounds = 0;
			removeItem();
			while (items.size() < itemsCnt) {
				addNewItem();
			}
		}
	}

	public boolean getOk() {
		return ok;
	}
	
	public boolean needsTarget() {
		return false;
	}
	
	public int getShrineIndex() {
		return Shrine.SHRINE_TRADER;
	}

	
	private void removeItem() {
		items.remove((int)(Math.random()*items.size()));	
	}

	/**
	 * @see Shrine#getColor()
	 */
	public Color getColor() {
		return Color.cyan;
	}

	/**
	 * @see Shrine#getStory()
	 */
	public String getStory() {
		return JDEnv.getResourceBundle().getString("shrine_trader_story");
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return JDEnv.getResourceBundle().getString("shrine_trader_name");
	}

	/**
	 * @see Shrine#getText()
	 */
	public String getText() {
		return JDEnv.getResourceBundle().getString("shrine_trader_text");
	}
	
	public String getBeginText() {
		return JDEnv.getResourceBundle().getString("shrine_trader_begin");	
	}
	
	public String getWhatText() {
		return JDEnv.getResourceBundle().getString("shrine_trader_want");
	}
	
	public String getWantText() {
		return JDEnv.getResourceBundle().getString("shrine_trader_offer");
	}	
	
	public String getEmptyText() {
		return JDEnv.getResourceBundle().getString("shrine_trader_trade");	
	}
	
	
	public String setTrade(List heroItems, List traderItems) {
		int heroGives = summItems(heroItems);
		//System.out.println(heroItems.size()+ " biete : "+heroGives);
		
		int heroWants = summItems(traderItems);
		//System.out.println(traderItems.size()+"möchte : "+heroWants);
		
		if(heroGives < heroWants * rate) {
			ok = false;
		return JDEnv.getResourceBundle().getString("shrine_trader_no");	
		}
		else {
			ok = true;
			return  JDEnv.getResourceBundle().getString("shrine_trader_yes");
		}
	}
	
	public void makeTrade(List heroItems,List traderItems) {
		for(int i = 0; i< heroItems.size(); i++) {
			Item it = (Item)heroItems.get(i);
			//[TODO] Gegenstände wegnehmen 
			//System.out.println("held gibt ab; "+it.toString());
			//boolean b = game.getHero().removeItem(it);	
			//System.out.println(b);
		}
		for(int i = 0; i< traderItems.size(); i++) {
//			[TODO] Gegenstände geben
			//game.getHero().addItem((Item)traderItems.get(i),location);	
			items.remove(traderItems.get(i));
		}
			
	}
	
//	public boolean addItem(item it,itemOwner o) {
//		location.addItem(it);
//		return true;	
//	}
	
	private int summItems(List l) {
		int sum = 0;
		for(int i = 0; i < l.size(); i++) {
			sum += ((Item)l.get(i)).getWorth();	
		}	
		return sum;
	}

	/**
	 * @see Shrine#clicked(fighter)
	 */
	public void clicked(Figure f, boolean right) {
//		if(f.isGuiControlled()) {
//			//Trader_window win = new Trader_window(f.getControl().getGui().getMainFrame(),this);
//		}
	}

	
	public LinkedList getItems() {
		return items;
	}

	/**
	 * @see Shrine#getStatus()
	 */
	public String getStatus() {
		return null;
	}



	/**
	 * @see Usable#usableOnce()
	 */
	public boolean usableOnce() {
		return false;
	}

}
