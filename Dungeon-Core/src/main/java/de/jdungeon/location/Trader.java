package de.jdungeon.location;

import java.util.LinkedList;
import java.util.List;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.game.JDEnv;
import de.jdungeon.item.DustItem;
import de.jdungeon.item.Item;
import de.jdungeon.item.ItemPool;
import de.jdungeon.item.interfaces.Usable;
import de.jdungeon.util.Arith;

/**
 * @author Duke1
 * <p>
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

@Deprecated
public class Trader extends Location /*implements itemOwner*/ {

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
		for (int i = 0; i < itemsCnt; i++) {
			addNewItem();
		}
	}

	public void metaClick(Figure f) {

	}

	@Override
	public int dustCosts() {
		return 0;
	}

	@Override
	public ActionResult use(Figure f, RoomEntity target, boolean meta, int round, boolean doIt) {
		// todo: re-implement entirely
		return ActionResult.OTHER;
	}

	private void addNewItem() {
		int value = (int) Arith.gauss(10 + (int) (Math.random() * AvWorth), 2);
		Item i = ItemPool.getRandomItem(value, 0.8 + Math.random());
		while ((i instanceof DustItem)) {
			i = ItemPool.getRandomItem(value, 0.8 + Math.random());
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

	private void removeItem() {
		items.remove((int) (Math.random() * items.size()));
	}

	/**
	 * @see Location#getStory()
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
	 * @see Location#getText()
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

		if (heroGives < heroWants * rate) {
			ok = false;
			return JDEnv.getResourceBundle().getString("shrine_trader_no");
		}
		else {
			ok = true;
			return JDEnv.getResourceBundle().getString("shrine_trader_yes");
		}
	}

	public void makeTrade(List<Item> heroItems, List<Item> traderItems) {
		for (int i = 0; i < heroItems.size(); i++) {
			Item it = heroItems.get(i);
			//[TODO] Gegenst�nde wegnehmen 
			//System.out.println("held gibt ab; "+it.toString());
			//boolean b = de.jdungeon.game.getHero().removeItem(it);
			//System.out.println(b);
		}
		for (int i = 0; i < traderItems.size(); i++) {
//			[TODO] Gegenst�nde geben
			//de.jdungeon.game.getHero().addItem((Item)traderItems.get(i),de.jdungeon.location);
			items.remove(traderItems.get(i));
		}
	}

//	public boolean addItem(de.jdungeon.item it,itemOwner o) {
//		de.jdungeon.location.addItem(it);
//		return true;	
//	}

	private int summItems(List<Item> l) {
		int sum = 0;
		for (int i = 0; i < l.size(); i++) {
			sum += l.get(i).getWorth();
		}
		return sum;
	}

	@Override
	public List<Item> getItems() {
		return items;
	}

	/**
	 * @see Location#getStatus()
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
