package de.jdungeon.location;

import de.jdungeon.dungeon.RoomEntity;
import de.jdungeon.figure.action.result.ActionResult;
import de.jdungeon.game.GameLoopMode;
import de.jdungeon.item.AttrPotion;
import de.jdungeon.item.DustItem;
import de.jdungeon.item.HealPotion;
import de.jdungeon.item.Item;

/*
 * Created on 04.08.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import java.util.LinkedList;
import java.util.List;

import de.jdungeon.spell.AbstractSpell;
import de.jdungeon.dungeon.Room;
import de.jdungeon.figure.Figure;
import de.jdungeon.figure.RoomObservationStatus;
import de.jdungeon.figure.VisibilityModifier;
import de.jdungeon.figure.attribute.Attribute;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.figure.percept.Percept;
import de.jdungeon.figure.percept.TextPercept;
import de.jdungeon.figure.percept.UsePercept;
import de.jdungeon.game.JDEnv;
import de.jdungeon.gui.Texts;

@Deprecated
public class SorcerLab extends Location implements VisibilityModifier {

	boolean firstTime = true;

	double dustRate = 0.4;

	boolean activated = false;

	double dust;

	int actionPointer;

	int heroLevel = 0;

	Figure owner;

	List<SorcerLabAction> actions = new LinkedList<SorcerLabAction>();

	public SorcerLab(Room p) {

		super(p);
		for (int i = 0; i < 2; i++) {
			actions.add(new SorcerLabAction(i));
		}
		actions.add(new SorcerLabAction(4));
		actions.add(new SorcerLabAction(5));
	}

	@Override
	public boolean canBeUsedBy(Figure f) {
		return f instanceof Hero;
	}

	/**
	 * 
	 */
	public SorcerLab() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getVisibilityStatus() {
		if (activated) {
			return RoomObservationStatus.VISIBILITY_ITEMS;
		} else {
			return RoomObservationStatus.VISIBILITY_SHRINE;
		}
	}

	@Override
	public boolean stillValid() {
		return activated;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see shrine#turn(int)
	 */
	@Override
	public void turn(int round, GameLoopMode mode) {
		int k = location.getRoomFigures().size();
		boolean containsOwner = false;
		if (owner != null) {
			containsOwner = location.getRoomFigures().contains(owner);
		}
		int threshold = 0;
		if (containsOwner) {
			threshold = 1;
		}
		if (k > threshold && activated && !containsOwner) {

			// System.out.println("sorcLab verwüstet!");
			List<Room> rooms = location.getScoutableNeighbours();

			rooms.add(location);
			for (int i = 0; i < rooms.size(); i++) {
				Room toView = rooms.get(i);

				owner.getRoomObservationStatus(toView.getRoomNumber())
						.removeVisibilityModifier(this);
				owner.getRoomObservationStatus(toView.getRoomNumber()).resetVisibilityStatus();

		
			}

			String s = JDEnv.getResourceBundle().getString("sorcLab_desolated");

			owner.tellPercept(new TextPercept(s, round));
			owner = null;
			activated = false;
		}
		if (activated) {
			dust += dustRate;
		}

		if (dust > 6) {
			if (Math.random() < 0.5) {

				int pot = 3 + (int) (Math.random() * 4);
				this.location.addItem(new DustItem(pot));
				dust -= pot;
			}
		}

	}


	@Override
	public boolean needsTarget() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see shrine#getStory()
	 */
	@Override
	public String getStory() {
		String s = "";
		if (activated) {
			s = JDEnv.getResourceBundle().getString("sorcLab_back_home");
		} else {
			s = JDEnv.getResourceBundle().getString("sorcLab_found");
		}
		return s;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return JDEnv.getResourceBundle().getString("sorcLab");
	}

	
	@Override
	public String getText() {
		return JDEnv.getResourceBundle().getString("sorcLab");
	}

	

	public void metaClick(Figure f, Object target) {
		/*
		// AKTION MACHEN
		if (activated) {
			Percept p = new UsePercept(f, this, -1);
			this.getRoom()
					.distributePercept(p);
			(actions.get(actionPointer)).fire((Hero) f, -1);
		} else {
			use(f, null, false, -1);
		}
		*/

	}

	private void activate(Figure f) {
		Percept p = new UsePercept(f, this, -1);
		f.getRoom().distributePercept(p);
		activated = true;
		owner = f;
		List<Room> rooms = location.getScoutableNeighbours();
		rooms.add(location);
		for (int i = 0; i < rooms.size(); i++) {
			Room toView = rooms.get(i);
			// TODO: maybe better do this scouting as done at scout shrine, via ScoutResult objects?
			f.getRoomVisibility().addVisibilityModifier(toView.getRoomNumber(), this);

		}
		String s = JDEnv.getResourceBundle().getString("sorcLab_setup");
		f.tellPercept(new TextPercept(s, -1));
	}

	@Override
	public int dustCosts() {
		return 0;
	}

	@Override
	public ActionResult use(Figure f, RoomEntity target, boolean meta, int round, boolean doIt) {

		if (!activated) {
			Attribute dust = ((Hero) f).getDust();
			if (firstTime) {

				if (dust.getBasic() == dust.getValue()) {

					firstTime = false;
					dust.setValue(0);
					activate(f);

				} else {

					String s = JDEnv.getResourceBundle().getString(
							"sorcLab_no_dust");
					f.tellPercept(new TextPercept(s, round));
				}
			} else {

				if (dust.getBasic() / 2 <= dust.getValue()) {
					activate(f);
					dust.modValue((-1) * dust.getBasic() / 2);

				} else {
					String s = JDEnv.getResourceBundle().getString(
							"sorcLab_no_dust");
					f.tellPercept(new TextPercept(s, round));
				}
			}

		} else {
			if (meta) {
				metaClick(f, target);
			} else {
				testHeroLevel(((Hero) f));

				actionPointer = (actionPointer + 1) % actions.size();
			}

		}
		// todo: re-implement entirely
		return ActionResult.OTHER;
	}

	private void testHeroLevel(Hero h) {
		int l = 1;
		if (l > heroLevel) {
			if (l >= 1 && heroLevel < 1) {
				actions.add(new SorcerLabAction(2));
				actions.add(new SorcerLabAction(3));
				actions.add(new SorcerLabAction(4));
				actions.add(new SorcerLabAction(5));
			}
			heroLevel = l;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see shrine#getStatus()
	 */
	@Override
	public String getStatus() {

		String s = "";
		if (activated) {
			s += JDEnv.getResourceBundle().getString("sorcLab_ready") + " \n";
		} else {
			s += JDEnv.getResourceBundle().getString("sorcLab_unready") + " \n";
		}
		s += generateActionString();
		return s;
	}

	private String generateActionString() {
		if (!activated) {
			return JDEnv.getResourceBundle().getString("action")
					+ ": "
					+ JDEnv.getResourceBundle().getString(
							"sorcLab_setup_action");
		} else {
			SorcerLabAction ac = (actions
					.get(actionPointer));
			return JDEnv.getResourceBundle().getString("action") + ": "
					+ ac.getString();
		}
	}


	@Override
	public boolean usableOnce() {
		// TODO Auto-generated method stub
		return false;
	}

}

class SorcerLabAction {

	int key;

	boolean spell;

	Item it;

	AbstractSpell sp;

	int cost;

	String[] strings = { "Heiltrank", "Elexir: Geschicklichkeit",
			"Elexir: Stärke", "Elexir: Psyche",  "Zauberrolle: Entdecken" };

	public SorcerLabAction(int key) {
		this.key = key;
		switch (key) {

		case 0: {
			cost = 6;
			break;
		}
		case 1: {
			cost = 10;
			break;
		}
		case 2: {
			cost = 10;
			break;
		}
		case 3: {
			cost = 10;
			break;
		}
		case 5: {
			cost = 10;
			break;
		}
		default: {

		}

		}
	}

	public String getString() {
		if (key < strings.length) {

			return strings[key] + " \n"
					+ JDEnv.getResourceBundle().getString("cost") + ": " + cost;
		} else {
			return JDEnv.getResourceBundle().getString("nothing");
		}
	}

	public void fire(Hero h, int round) {
		if (h.payDust(cost)) {

			if (spell) {
				sp.fire(h, null, true, round);
			} else {
				if (key == 0) {
					h.takeItem(new HealPotion(20));
				}
				if (key == 1) {
					h.takeItem(
							new AttrPotion(Attribute.Type.Dexterity, 25));
				}
				if (key == 2) {
					h.takeItem(
							new AttrPotion(Attribute.Type.Strength, 25));
				}
				if (key == 3) {
					h.takeItem(new AttrPotion(Attribute.Type.Psycho, 25));
				}
			}
			String s = JDEnv.getResourceBundle().getString(
					"sorcLab_action_done");
			h.tellPercept(new TextPercept(s, round));

		} else {
			h.tellPercept(new TextPercept(Texts.noDust(), round));
		}
	}
}
