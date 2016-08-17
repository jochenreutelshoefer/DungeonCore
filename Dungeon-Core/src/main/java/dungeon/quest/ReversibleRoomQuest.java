package dungeon.quest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import dungeon.Dungeon;
import dungeon.JDPoint;
import dungeon.generate.DungeonFiller;
import dungeon.generate.undo.DungeonChangeAction;
import item.Item;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 03.08.16.
 */
public abstract class ReversibleRoomQuest extends RoomQuest {

	private List<DungeonChangeAction> actions;
	public ReversibleRoomQuest(JDPoint p, DungeonFiller df, int x, int y) {
		super(p, df, x, y);
	}
	public ReversibleRoomQuest(DungeonFiller df, int x, int y) {
		super(df, x, y);
	}

	boolean insert() {
		actions = createActionList();
		if(actions == null) return false;
		for (DungeonChangeAction action : actions) {
			action.doAction();
		}
		return true;
	}

	abstract List<DungeonChangeAction> createActionList();

	public void undo() {
		List<DungeonChangeAction> undoList = new ArrayList<>(actions);
		Collections.reverse(undoList);
		for (DungeonChangeAction dungeonChangeAction : undoList) {
			dungeonChangeAction.undo();
		}

	}

	/**
	 * Finalizes the RoomQuest
	 * Note: Can not be reverted
	 * Returns a collection of items (e.g. the key) that
	 * need to be distributed outside the RoomQuest
	 *
	 * @return a collection of items that need to be distributed outside the RoomQuest
	 */
	public abstract Collection<Item> finalizeRoomQuest();

}
