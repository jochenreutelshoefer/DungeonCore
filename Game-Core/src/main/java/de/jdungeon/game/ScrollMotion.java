package de.jdungeon.game;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 31.12.16.
 */
public interface ScrollMotion {

	MotionEvent getStartEvent();

	FloatDimension getMovement();


	class FloatDimension {
		public float getX() {
			return width;
		}

		public float getY() {
			return height;
		}

		float width;
		float height;

		public FloatDimension(float width, float height) {
			this.width = width;
			this.height = height;
		}
	}
}
