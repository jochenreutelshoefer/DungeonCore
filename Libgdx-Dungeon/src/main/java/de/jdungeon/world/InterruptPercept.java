package de.jdungeon.world;

import java.util.Collections;
import java.util.List;

import figure.FigureInfo;
import figure.percept.OpticalPercept;

/**
 * This is a GUI-Util-Percept only - not part of the game world but only used to visualize a interruption of the
 * players plan.
 *
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 25.04.20.
 */
public class InterruptPercept extends OpticalPercept {
	public InterruptPercept(FigureInfo figure, int round) {
		super(figure.getRoomInfo().getNumber(), round);
	}

	@Override
	public List<FigureInfo> getInvolvedFigures() {
		return Collections.emptyList();
	}
}
