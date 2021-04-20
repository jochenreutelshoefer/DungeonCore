package de.jdungeon.animation;

import de.jdungeon.figure.percept.Percept;

/**
 *
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 26.12.16.
 */
public interface AnimationTask {


	boolean isFinished();

	boolean isUrgent() ;

	AnimationFrame getCurrentAnimationFrame();

	Percept getPercept();
}
