package animation;

import graphics.JDImageProxy;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 17.01.18.
 */
public interface AnimationSet {

	JDImageProxy getImageAtTime(long millisecondsPassed);

	int getTotalDuration();
}
