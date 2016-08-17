package shrine;

import item.DustItem;
import item.Item;
import item.ItemPool;
import item.interfaces.Usable;

/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
import java.util.LinkedList;
import java.util.List;

import util.Arith;
import util.JDColor;
import dungeon.Dungeon;
import figure.Figure;
import figure.hero.Hero;
import game.JDEnv;


public class Trader extends Shrine /*implements itemOwner*/{
	
	double rate = 1.6;
	int rounds_to_change = 10;
	int itemsCnt = 5;

	
	List<Item> items = new LinkedList<Item>();

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
	
	@Override
	public boolean use(Figure f,Object target,boolean meta) {
		return false;
	}
	
	
	private void addNewItem() {
		int value = (int)Arith.gauss(10 + (int)(Math.random() * AvWorth), 2);	
		Item i = ItemPool.getRandomItem(value, 0.8+Math.random());
		while((i instanceof DustItem)) {
			i = ItemPool.getRandomItem(value, 0.8+Math.random());
			
		}
		items.add(i);
	}

	@Override
	public boolean canBeUsedBy(Figure f) {
		   return f instanceof Hero;
	   }
	@Override
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
	
	@Override
	public boolean needsTarget() {
		return false;
	}
	
	@Override
	public int getShrineIndex() {
		return Shrine.SHRINE_TRADER;
	}

	
	private void removeItem() {
		items.remove((int)(Math.random()*items.size()));	
	}

	/**
	 * @see Shrine#getColor()
	 */
	@Override
	public JDColor getColor() {
		return JDColor.blue;
	}

	/**
	 * @see Shrine#getStory()
	 */
	@Override
	public String getStory() {
		return JDEnv.getResourceBundle().getString("shrine_trader_story");
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return JDEnv.getResourceBundle().getString("shrine_trader_name");
	}

	/**
	 * @see Shrine#getText()
	 */
	@Override
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
	
	
	public String setTrade(List<Item> heroItems, List<Item> traderItems) {
		int heroGives = summItems(heroItems);
		//System.out.println(heroItems.size()+ " biete : "+heroGives);
		
		int heroWants = summItems(traderItems);
		//System.out.println(traderItems.size()+"m�chte : "+heroWants);
		
		if(heroGives < heroWants * rate) {
			ok = false;
		return JDEnv.getResourceBundle().getString("shrine_trader_no");	
		}
		else {
			ok = true;
			return  JDEnv.getResourceBundle().getString("shrine_trader_yes");
		}
	}
	
	public void makeTrade(List<Item> heroItems, List<Item> traderItems) {
		for(int i = 0; i< heroItems.size(); i++) {
			Item it = heroItems.get(i);
			//[TODO] Gegenst�nde wegnehmen 
			//System.out.println("held gibt ab; "+it.toString());
			//boolean b = game.getHero().removeItem(it);	
			//System.out.println(b);
		}
		for(int i = 0; i< traderItems.size(); i++) {
//			[TODO] Gegenst�nde geben
			//game.getHero().addItem((Item)traderItems.get(i),location);	
			items.remove(traderItems.get(i));
		}
			
	}
	
//	public boolean addItem(item it,itemOwner o) {
//		location.addItem(it);
//		return true;	
//	}
	
	private int summItems(List<Item> l) {
		int sum = 0;
		for(int i = 0; i < l.size(); i++) {
			sum += l.get(i).getWorth();	
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

	
	public List<Item> getItems() {
		return items;
	}

	/**
	 * @see Shrine#getStatus()
	 */
	@Override
	public String getStatus() {
		return null;
	}



	/**
	 * @see Usable#usableOnce()
	 */
	@Override
	public boolean usableOnce() {
		return false;
	}

}
