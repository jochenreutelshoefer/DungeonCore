package shrine;

import fight.Slap;
import figure.DungeonVisibilityMap;
import figure.Figure;
import figure.hero.Hero;
import figure.percept.Percept;
import figure.percept.TextPercept;
import figure.percept.UsePercept;
import game.JDEnv;
import item.Item;
import item.ItemInfo;
import item.ItemPool;
import item.interfaces.ItemOwner;
import item.quest.LuziasBall;
import item.quest.Thing;

/*
 * Created on 04.08.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import java.util.Iterator;
/**
 * @author Jochen
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import java.util.LinkedList;
import java.util.List;

import util.JDColor;
import dungeon.Room;

public class Luzia extends Shrine implements ItemOwner {

	/**
	 * @param actualP
	 * 
	 */
	Item requestedItem;

	/**
	 * 
	 */
	LuziasBall ball;

	Figure figure;

	/**
	 * 
	 */
	LinkedList goodItems = new LinkedList();

	/**
	 * 
	 */
	LinkedList items = new LinkedList();

	public static final int UNSOLVED = 1;

	public static final int SOLVED = 2;

	public static final int DEAD = 3;

	/**
	 * 
	 */
	boolean solved = false;

	public Luzia(Room p, LuziasBall ball) {
		super(p);
		this.ball = ball;
		goodItems.add(ItemPool.getHigherItem(40, 2));
		goodItems.add(ItemPool.getHigherItem(50, 2));
		goodItems.add(ItemPool.getHigherItem(60, 2));
		type = Luzia.UNSOLVED;
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canBeUsedBy(Figure f) {
		return f instanceof Hero;
	}

	@Override
	public ItemInfo[] getItemInfos(DungeonVisibilityMap map) {

		return null;
	}

	@Override
	public Item getItemNumber(int i) {
		return (Item) items.get(i);
	}

	public void setReqItem(Item req) {
		requestedItem = req;
	}

	@Override
	public boolean needsTarget() {
		return false;
	}

	@Override
	public boolean takeItem(Item i, ItemOwner o) {
		items.add(i);
		return true;
	}

	@Override
	public Item getItem(ItemInfo it) {
		for (Iterator iter = items.iterator(); iter.hasNext();) {
			Item element = (Item) iter.next();
			if (ItemInfo.makeItemInfo(element, null).equals(it)) {
				return element;
			}

		}
		return null;
	}

	@Override
	public Room getRoom() {
		return this.location;
	}

	@Override
	public int getShrineIndex() {
		return Shrine.SHRINE_LUZIA;
	}

	@Override
	public boolean removeItem(Item i) {
		if (items.remove(i)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean addItems(List l, ItemOwner o) {
		for (int i = 0; i < l.size(); i++) {
			Item it = (Item) (l.get(i));
			this.takeItem(it, o);
		}
		return true;
	}

	/**
	 * 
	 */
	public Luzia() {
		super();
		// TODO Auto-generated constructor stub
	}

	private boolean hasBall() {
		for (int i = 0; i < items.size(); i++) {
			if (((Item) items.get(i)) instanceof LuziasBall) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see shrine#turn(int)
	 */
	@Override
	public void turn(int round) {
		if (solved) {
			return;
		}
		if (dead) {
			return;
		}
		if (this.location.monstersThere() && !hasBall()) {
			destroy();
		}

	}

	private void destroy() {
		System.out.println("Luzia destroys!");
		ItemOwner ballOwner = ball.getOwner();
		ballOwner.removeItem(ball);
		if (ballOwner instanceof Figure) {
			((Thing) requestedItem).getOwner().removeItem(requestedItem);
			Figure owner = ((Figure) ballOwner);
			Slap sl = new Slap(null, 0, 0, 150);
			sl.setValueMagic(25);
			owner.getSlap(sl);

			String s = JDEnv.getResourceBundle().getString(
					"shrine_luzia_accurse");
			owner.tellPercept(new TextPercept(s));
			String s2 = JDEnv.getResourceBundle().getString(
					"shrine_luzia_explodes");
			owner.tellPercept(new TextPercept(s2));
		}

		this.solved = true;
		dead = true;
		this.type = Luzia.DEAD;
	}

	private boolean dead = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see shrine#getColor()
	 */
	@Override
	public JDColor getColor() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see shrine#getStory()
	 */
	@Override
	public String getStory() {
		// TODO Auto-generated method stub
		return JDEnv.getResourceBundle().getString("shrine_luzia_story");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Luzia";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see shrine#getText()
	 */
	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return JDEnv.getResourceBundle().getString("shrine_luzia_text");
	}

	public boolean metaClick(Figure f, Object target) {
		if (solved || dead) {
			String s = JDEnv.getString("shrine_luzia_done");
			f.tellPercept(new TextPercept(s));
			return false;
		}

		if (hasBall()) {
			if (target != null && target == requestedItem) {

				((Hero) f).removeItem(this.requestedItem);
				Percept p = new UsePercept(f, this);
				f.getRoom().distributePercept(p);
				f.takeItem(ball, null);
				solve(f);
				return true;
			} else {
				
				items.remove(ball);
				figure = f;
				f.takeItem(ball,null);
				Percept p = new UsePercept(f, this);
				f.getRoom().distributePercept(p);
				String s = JDEnv.getResourceBundle().getString(
						"shrine_luzia_taken");
				f.tellPercept(new TextPercept(s));
				return true;
			}
		} else {
			if (f instanceof Hero) {
				if (target != null && target == requestedItem) {

					((Hero) f).removeItem(this.requestedItem);
					Percept p = new UsePercept(f, this);
					f.getRoom().distributePercept(p);
					solve(f);
					return true;
				} else {

					if (((Hero) f).hasItem(ball)) {
						((Hero) f).removeItem(ball);
						Percept p = new UsePercept(f, this);
						f.getRoom().distributePercept(p);
						items.add(ball);
						String s = JDEnv.getResourceBundle().getString(
								"shrine_luzia_returned");
						f.tellPercept(new TextPercept(s));
						return true;
					}
				}
			}
		}
		return false;
	}

	private void solve(Figure f) {
		solved = true;
		type = Luzia.SOLVED;
		ball.solved();
		String s = JDEnv.getResourceBundle().getString("shrine_luzia_solved");
		f.tellPercept(new TextPercept(s));
		this.location.addItems(goodItems, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see shrine#getStatus()
	 */
	@Override
	public String getStatus() {
		if (dead) {
			return JDEnv.getString("shrine_luzia_dead");
		}
		if (solved) {
			return JDEnv.getString("shrine_luzia_gone");
		}
		if (hasBall()) {
			return JDEnv.getResourceBundle().getString("action") + ": "
					+ JDEnv.getResourceBundle().getString("shrine_luzia_take");
		} else {
			return JDEnv.getResourceBundle().getString("action")
					+ ": "
					+ JDEnv.getResourceBundle()
							.getString("shrine_luzia_return");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see usable#use(fighter)
	 */
	@Override
	public boolean use(Figure f, Object target, boolean meta) {

		if (solved || dead) {
			String s = JDEnv.getString("shrine_luzia_done");
			f.tellPercept(new TextPercept(s));
			return false;
		}

		if (meta || target != null) {
			return metaClick(f, target);
		} else {
			if (hasBall()) {
				// briefing
				String s = JDEnv.getString("shrine_luzia_briefing");
				f.tellPercept(new TextPercept(s));
			} else {
				String s = JDEnv.getString("shrine_luzia_searching");
				f.tellPercept(new TextPercept(s));
			}
			return true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see usable#usableOnce()
	 */
	@Override
	public boolean usableOnce() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @return
	 * 
	 */
	public Item getRequestedItem() {
		return requestedItem;
	}

	/**
	 * @return
	 * 
	 */
	public boolean isSolved() {
		return solved;
	}

}
