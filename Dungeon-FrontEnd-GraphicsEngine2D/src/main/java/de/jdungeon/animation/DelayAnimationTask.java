package de.jdungeon.animation;

import de.jdungeon.figure.percept.Percept;

/**
 * An de.jdungeon.animation tasks that does not show any de.jdungeon.animation,
 * but is used as a time blocker to delay subsequent animations.
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 26.12.16.
 */
public class DelayAnimationTask implements AnimationTask {

	long startTime;
	protected boolean started = false;
	private final long duration;

	DelayAnimationTask(long duration) {
		this.duration = duration;
	}

	@Override
	public boolean isFinished() {
		if(started) {
			return System.currentTimeMillis() > startTime + duration;
		}
		return false;
	}

	@Override
	public boolean isUrgent() {
		return false;
	}

	@Override
	public AnimationFrame getCurrentAnimationFrame() {
		if(!started) {
			started = true;
			startTime = System.currentTimeMillis();
		}
		// we always return 'null' as this is no real de.jdungeon.animation, but rather only a time blocker
		return null;
	}

	@Override
	public Percept getPercept() {
		// we have none
		return null;
	}
}
