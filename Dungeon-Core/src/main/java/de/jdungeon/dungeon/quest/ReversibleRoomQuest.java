package de.jdungeon.dungeon.quest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.generate.DungeonFiller;
import de.jdungeon.dungeon.generate.undo.DungeonChangeAction;
import de.jdungeon.item.Item;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 03.08.16.
 */
public abstract class ReversibleRoomQuest extends RoomQuest {

	private List<DungeonChangeAction> actions;
	private List<DungeonChangeAction> performedActions = new ArrayList<>();
	public ReversibleRoomQuest(JDPoint p, DungeonFiller df, int x, int y) {
		super(p, df, x, y);
	}
	public ReversibleRoomQuest(DungeonFiller df, int x, int y) {
		super(df, x, y);
	}

	protected boolean insert() {
		performedActions.clear();
		actions = createActionList();
		if(actions == null) return false;
		for (DungeonChangeAction action : actions) {
			boolean works = action.doAction();
			performedActions.add(action);
			if(!works) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean setUp() {
		final boolean possible = insert();
		if(!possible) {
			undo();
		}
		return possible;
	}

	abstract List<DungeonChangeAction> createActionList();

	public void undo() {
		if(actions == null) {
			//action list could not be created, therefore nothing to undo
			return;
		}
		List<DungeonChangeAction> undoList = new ArrayList<>(performedActions);
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
