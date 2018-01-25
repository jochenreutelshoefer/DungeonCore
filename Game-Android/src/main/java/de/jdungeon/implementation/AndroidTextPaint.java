package de.jdungeon.implementation;

import de.jdungeon.game.Paint;
import de.jdungeon.game.TextPaint;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 23.01.18.
 */
public class AndroidTextPaint implements TextPaint {

	public android.text.TextPaint getPaint() {
		return paint;
	}

	private final android.text.TextPaint paint;

	public AndroidTextPaint(android.text.TextPaint paint) {
		this.paint = paint;
	}




}