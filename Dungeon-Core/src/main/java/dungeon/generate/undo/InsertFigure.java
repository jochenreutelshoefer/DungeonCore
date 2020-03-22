package dungeon.generate.undo;

import dungeon.Dungeon;
import dungeon.Room;
import figure.Figure;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 03.08.16.
 */
public class InsertFigure implements DungeonChangeAction {

	private final Figure figure;
	private final Room room;

	public InsertFigure(Figure figure, Room room) {
		this.figure = figure;
		this.room = room;
	}

	@Override
	public boolean doAction() {
		room.figureEnters(figure, 0, -1);
		return true;
	}

	@Override
	public void undo() {
		boolean removed = room.figureLeaves(figure);
		assert removed;
	}
}
