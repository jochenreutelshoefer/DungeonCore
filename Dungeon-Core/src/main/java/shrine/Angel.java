package shrine;

import figure.Figure;
import figure.hero.Hero;
import figure.percept.TextPercept;
import game.JDEnv;
import item.Item;
import item.paper.Scroll;
import item.quest.Feather;

import java.util.LinkedList;
import java.util.List;

import spell.Prayer;
import util.JDColor;

public class Angel extends Shrine {
	
	List<Item> requestedItems = new LinkedList<Item>();
	List<Item> ownedItems = new LinkedList<Item>();
	List<Item> rewardItems = new LinkedList<Item>();
	boolean solved = false;
	
	public static final int UNSOLVED = 1;

	public static final int SOLVED = 2;
	
	public List<Item> getRequestedItems() {
		return requestedItems;
	}

	public Angel() {
		Feather f1 = new Feather(JDEnv.getString("feather_golden"), this);
		Feather f2 = new Feather(JDEnv.getString("feather_silver"), this);
		requestedItems.add(f1);
		requestedItems.add(f2);
		rewardItems.add(new Scroll(new Prayer(),0));
		this.type = Angel.UNSOLVED;
	}

	@Override
	public int getShrineIndex() {
		// TODO Auto-generated method stub
		return Shrine.SHRINE_ANGEL;
	}

	@Override
	public void turn(int round) {
		// TODO Auto-generated method stub

	}

	@Override
	public JDColor getColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStory() {
		// TODO Auto-generated method stub
		return JDEnv.getString("shrine_angel_story");
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return JDEnv.getString("shrine_angel_name");
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStatus() {
		if(solved) {
			return "Gelöst";
		}else {
			return "Ungelöst";
		}
	}

	@Override
	public boolean use(Figure f, Object target, boolean meta) {
		if(target instanceof Feather) {
			ownedItems.add((Feather) target);
			f.removeItem((Item)target);
			f.tellPercept(new TextPercept(JDEnv.getString("thanks")));
			checkCompleted();
			return true;
		}else {
			if(target == null) {
				
			}
		}
		return false;
	}
	
	
	public boolean isSolved() {
		return solved;
	}

	private void checkCompleted() {
		if(ownedItems.containsAll(requestedItems)) {
			solved = true;
			this.getRoom().addItems(rewardItems, null);
			rewardItems.clear();
			this.type = Angel.SOLVED;
		}
	}

	@Override
	public boolean usableOnce() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canBeUsedBy(Figure f) {
		// TODO Auto-generated method stub
		return f instanceof Hero;
	}

	@Override
	public boolean needsTarget() {
		// TODO Auto-generated method stub
		return false;
	}

	

}
