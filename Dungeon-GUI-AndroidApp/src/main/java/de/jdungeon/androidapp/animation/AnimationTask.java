package de.jdungeon.androidapp.animation;

/**
 *
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 26.12.16.
 */
public interface AnimationTask {


	boolean isFinished();

	boolean isUrgent() ;

	AnimationFrame getCurrentAnimationFrame();

}
