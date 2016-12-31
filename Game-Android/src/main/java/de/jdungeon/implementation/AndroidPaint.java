package de.jdungeon.implementation;

import de.jdungeon.game.Paint;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 31.12.16.
 */
public class AndroidPaint implements Paint {

	public android.graphics.Paint getPaint() {
		return paint;
	}

	private final android.graphics.Paint paint;

	public AndroidPaint(android.graphics.Paint paint) {
		this.paint = paint;
	}
}
