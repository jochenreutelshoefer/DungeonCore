package de.jdungeon.user;

import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.ControlUnit;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 01.02.20.
 */
public interface ControlUnitFactory {

	ControlUnit create(FigureInfo figure);

}
