package shrine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import dungeon.Room;
import dungeon.RoomEntity;
import event.EventManager;
import event.ExitUsedEvent;
import figure.Figure;
import figure.hero.Hero;
import figure.percept.TextPercept;
import game.JDEnv;
import item.Item;
import util.JDColor;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 20.03.16.
 */
public class LevelExit extends Shrine {

	private List<Figure> requiredFigures = new ArrayList<>();
	private List<Item> requiredItems = new ArrayList<>();

	public LevelExit() {

	}

	/**
	 * Constructor to create exits that required some special item(s) to access the exit
	 *
	 * @param items that player needs to have to trigger exit
	 */
	public LevelExit(Item... items) {
		requiredItems = Arrays.asList(items);
	}


	/**
	 * Constructor to create exits that required presence of escorted NPCs
	 *
	 * @param figures that need to be in the room to trigger exit
	 */
	public LevelExit(Figure... figures) {
		requiredFigures = Arrays.asList(figures);
	}

	@Override
	public int getShrineIndex() {
		return SHRINE_EXIT;
	}

	@Override
	public void turn(int round) {

	}

	@Override
	public JDColor getColor() {
		return null;
	}

	@Override
	public String getStory() {
		return "";
	}

	@Override
	public String toString() {
		return JDEnv.getResourceBundle().getString("shrine_exit_name");

	}

	@Override
	public String getText() {
		return toString() + "\n"+JDEnv.getResourceBundle().getString("shrine_exit_text");
	}

	@Override
	public String getStatus() {
		return "";
	}

	@Override
	public int dustCosts() {
		return 0;
	}

	@Override
	public boolean use(Figure f, RoomEntity target, boolean meta) {
		// TODO: factor out text
		if(requiredFigureMissing()) {
			// some figure to be escorted to exit is not here -> refuse
			f.getRoomInfo().distributePercept(new TextPercept("Folgende Charaktere benötigt, um Dungeon zu verlassen: "+requiredItems.toString()));
			return false;
		}

		if(requiredItemMissing(f)) {
			// some item to be found to exit is not here -> refuse
			Room roomInfo = f.getRoomInfo();
			if(roomInfo != null) {
				roomInfo.distributePercept(new TextPercept("Folgende Gegenstände benötigt, um Dungeon zu verlassen: "+requiredItems.toString()));
			}
			return false;
		}

		// items are given away when passing the exit
		for (Item requiredItem : requiredItems) {
			f.removeItem(requiredItem);
		}

		f.setLocation((Room)null);
		EventManager.getInstanceDungeon().fireEvent(new ExitUsedEvent(f, this));
		EventManager.getInstanceMenu().fireEvent(new ExitUsedEvent(f, this));
		return true;
	}

	private boolean requiredFigureMissing() {
		for (Figure requiredFigure : requiredFigures) {
			if(!this.getRoom().getRoomFigures().contains(requiredFigure)) {
				return true;
			}
		}
		return false;
	}

	private boolean requiredItemMissing(Figure figure) {
		for (Item item : requiredItems) {
			if(!figure.getItems().contains(item)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean usableOnce() {
		return true;
	}

	@Override
	public boolean canBeUsedBy(Figure f) {
		return f instanceof Hero;
	}

	@Override
	public boolean needsTarget() {
		return false;
	}
}
