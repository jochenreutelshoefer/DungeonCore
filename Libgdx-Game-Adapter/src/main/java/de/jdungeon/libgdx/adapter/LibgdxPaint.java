package de.jdungeon.libgdx.adapter;

import de.jdungeon.game.Paint;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 31.12.16.
 */
public class LibgdxPaint implements Paint {

	public android.graphics.Paint getPaint() {
		return paint;
	}

	private final android.graphics.Paint paint;

	public LibgdxPaint(android.graphics.Paint paint) {
		this.paint = paint;
	}


}
