package de.jdungeon.dungeon.generate.undo;

import de.jdungeon.dungeon.Room;
import de.jdungeon.figure.Figure;

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
