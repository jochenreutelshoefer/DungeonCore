package shrine;

import item.*;
import item.interfaces.Usable;
import item.paper.Scroll;
import figure.hero.Character;

import java.awt.Color;

/*
 * Created on 04.08.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author Jochen
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import java.util.*;

import dungeon.*;

import spell.*;

import figure.Figure;
import figure.FigureInfo;
import figure.RoomObservationStatus;
import figure.VisibilityModifier;
import figure.attribute.Attribute;
import figure.hero.Hero;
import figure.percept.Percept;
import figure.percept.TextPercept;
import figure.percept.UsePercept;
import game.JDEnv;
import gui.Texts;

public class SorcerLab extends Shrine implements VisibilityModifier {

	/**
	 * @param actualP
	 * 
	 */
	boolean firstTime = true;

	double dustRate = 0.4;

	boolean activated = false;

	double dust;

	int actionPointer;

	int heroLevel = 0;

	Figure owner;

	LinkedList actions = new LinkedList();

	public SorcerLab(Room p) {

		super(p);
		for (int i = 0; i < 2; i++) {
			actions.add(new sorcer_lab_action(i));
		}
		actions.add(new sorcer_lab_action(4));
		actions.add(new sorcer_lab_action(5));
	}

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

	public int getVisibilityStatus() {
		if (activated) {
			return RoomObservationStatus.VISIBILITY_ITEMS;
		} else {
			return RoomObservationStatus.VISIBILITY_SHRINE;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see shrine#turn(int)
	 */
	public void turn(int round) {
		int k = location.getRoomFigures().size();
		if(k >= 2) {
			int klsd = 235;
			klsd--;
			
		}
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

				owner.getRoomObservationStatus(toView.getLocation())
						.removeVisibilityModifier(this);
				owner.getRoomObservationStatus(toView.getLocation()).resetVisibilityStatus();

		
			}

			String s = JDEnv.getResourceBundle().getString("sorcLab_desolated");

			owner.tellPercept(new TextPercept(s));
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see shrine#getColor()
	 */
	public Color getColor() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean needsTarget() {
		return false;
	}

	public int getShrineIndex() {
		return Shrine.SHRINE_SORCER_LAB;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see shrine#getStory()
	 */
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
	public String toString() {
		return JDEnv.getResourceBundle().getString("sorcLab");
	}

	
	public String getText() {
		return JDEnv.getResourceBundle().getString("sorcLab");
	}

	

	public void metaClick(Figure f, Object target) {
		// AKTION MACHEN
		if (activated) {
			Percept p = new UsePercept(f, (Usable) this);
			this.getRoom()
					.distributePercept(p);
			((sorcer_lab_action) (actions.get(actionPointer))).fire((Hero) f);
		} else {
			use(f, null, false);
		}
	}

	private void activate(Figure f) {
		Percept p = new UsePercept(f, this);
		f.getRoom().distributePercept(p);
		activated = true;
		owner = f;
		List<Room> rooms = location.getScoutableNeighbours();
		rooms.add(location);
		for (int i = 0; i < rooms.size(); i++) {
			Room toView = (Room) rooms.get(i);
			f.getRoomObservationStatus(toView.getLocation())
					.addVisibilityModifier(this);

		}
		String s = JDEnv.getResourceBundle().getString("sorcLab_setup");
		f.tellPercept(new TextPercept(s));
	}

	public boolean use(Figure f, Object target, boolean meta) {

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
					f.tellPercept(new TextPercept(s));
				}
			} else {

				if (dust.getBasic() / 2 <= dust.getValue()) {
					activate(f);
					dust.modValue((-1) * dust.getBasic() / 2);

				} else {
					String s = JDEnv.getResourceBundle().getString(
							"sorcLab_no_dust");
					f.tellPercept(new TextPercept(s));
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

		return true;
	}

	private void testHeroLevel(Hero h) {
		int l = h.getCharacter().getLevel();
		if (l > heroLevel) {
			if (l >= 1 && heroLevel < 1) {
				actions.add(new sorcer_lab_action(2));
				actions.add(new sorcer_lab_action(3));
				actions.add(new sorcer_lab_action(4));
				actions.add(new sorcer_lab_action(5));
			}
			heroLevel = l;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see shrine#getStatus()
	 */
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
			sorcer_lab_action ac = ((sorcer_lab_action) actions
					.get(actionPointer));
			return JDEnv.getResourceBundle().getString("action") + ": "
					+ ac.getString();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see usable#use(fighter)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see usable#usableOnce()
	 */
	public boolean usableOnce() {
		// TODO Auto-generated method stub
		return false;
	}

}

class sorcer_lab_action {

	int key;

	boolean spell;

	Item it;

	Spell sp;

	int cost;

	String[] strings = { "Heiltrank", "Elexir: Geschicklichkeit",
			"Elexir: Stärke", "Elexir: Psyche", "Zauberrolle: Reparieren", "Zauberrolle: Entdecken" };

	public sorcer_lab_action(int key) {
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
		case 4: {
			cost = 3;
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

	public void fire(Hero h) {
		if (h.payDust(cost)) {

			if (spell) {
				sp.fire(h, null, true);
			} else {
				if (key == 0) {
					h.takeItem(new HealPotion(20), null);
				}
				if (key == 1) {
					h.takeItem(
							new AttrPotion(Attribute.DEXTERITY, 25), null);
				}
				if (key == 2) {
					h.takeItem(
							new AttrPotion(Attribute.STRENGTH, 25), null);
				}
				if (key == 3) {
					h.takeItem(new AttrPotion(Attribute.PSYCHO, 25),
							null);
				}
				if (key == 4) {
					h.takeItem(new Scroll(new Repair(1), 5), null);
				}
				if (key == 5) {
					h.takeItem(new Scroll(new Discover(1), 5), null);
				}
			}
			String s = JDEnv.getResourceBundle().getString(
					"sorcLab_action_done");
			h.tellPercept(new TextPercept(s));

		} else {
			h.tellPercept(new TextPercept(Texts.noDust()));
		}
	}
}
