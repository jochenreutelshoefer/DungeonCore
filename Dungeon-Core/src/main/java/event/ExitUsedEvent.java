package event;

import figure.Figure;
import shrine.LevelExit;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.03.16.
 */
public class ExitUsedEvent extends Event{

	public LevelExit getExit() {
		return exit;
	}

	public Figure getFigure() {
		return figure;
	}

	private final Figure figure;
	private final LevelExit exit;

	public ExitUsedEvent(Figure figure, LevelExit exit) {
		this.figure = figure;
		this.exit = exit;
	}
}
