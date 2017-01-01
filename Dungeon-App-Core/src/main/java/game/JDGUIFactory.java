package game;

import figure.FigureInfo;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 01.01.17.
 */
public interface JDGUIFactory {

	JDGUI create(FigureInfo figureInfo);
}
