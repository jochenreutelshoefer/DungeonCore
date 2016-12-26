package de.jdungeon.androidapp.animation;

/**
 * An animation tasks that does not show any animation,
 * but is used as a time blocker to delay subsequent animations.
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 26.12.16.
 */
public class DelayAnimationTask implements AnimationTask {

	private long startTime;
	private boolean started = false;
	private long duration;

	public DelayAnimationTask(long duration) {
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
		// we always return 'null' as this is no real animation, but rather only a time blocker
		return null;
	}
}
