package user;

import figure.FigureInfo;
import game.ControlUnit;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 01.02.20.
 */
public interface ControlUnitFactory {

	ControlUnit create(FigureInfo figure);

}
