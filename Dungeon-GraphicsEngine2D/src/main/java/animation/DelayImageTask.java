package animation;

import dungeon.Position;
import graphics.JDImageProxy;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.12.16.
 */
public class DelayImageTask extends DelayAnimationTask {

	private final JDImageProxy<?> image;
	private final Position.Pos to;

	public DelayImageTask(long duration, JDImageProxy<?> image, Position.Pos to) {
		super(duration);
		this.image = image;
		this.to = to;
	}

	@Override
	public AnimationFrame getCurrentAnimationFrame() {
		if(!started) {
			started = true;
			startTime = System.currentTimeMillis();
		}
		return new AnimationFrame(image, 1.0, null, to);
	}
}
