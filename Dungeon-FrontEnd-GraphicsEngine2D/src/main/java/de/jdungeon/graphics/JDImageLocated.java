package de.jdungeon.graphics;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.graphics.util.DrawingRectangle;
import de.jdungeon.graphics.util.RelativeRectangle;

public class JDImageLocated implements DrawingRectangle {

	private final JDImageProxy<?> image;
	private RelativeRectangle relativeRectangle;
	private final int sizeX;
	private final int sizeY;
	private int posY;
	private int posX;

	public JDImageLocated(JDImageProxy<?> i, int posX, int posY, int sizeX, int sizeY) {
		image = i;
		this.posX = posX;
		this.posY = posY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}

	public JDImageLocated(JDImageProxy<?> i, RelativeRectangle relativeRectangle) {
		image = i;

		this.relativeRectangle = relativeRectangle;
		this.sizeX = relativeRectangle.getWidth();
		this.sizeY = relativeRectangle.getHeight();
	}

	@Override
	public boolean containsPoint(JDPoint p, int roomOffsetX, int roomOffsetY) {
		// TODO
		return false;
	}

	/**
	 * RENDER THREAD
	 */
	@Override
	public int getWidth() {
		return sizeX;
	}

	/**
	 * RENDER THREAD
	 */
	@Override
	public int getHeight() {
		return sizeY;
	}

	/**
	 * RENDER THREAD
	 */
	@Override
	public int getX(int roomOffsetX) {

		int xValue;
		if (relativeRectangle != null) {
			xValue = relativeRectangle.getX(roomOffsetX);
		}
		else {
			xValue = posX;
		}
		return xValue;
	}

	/**
	 * RENDER THREAD
	 */
	@Override
	public int getY(int roomOffsetY) {

		int yValue;
		if (relativeRectangle != null) {
			yValue = relativeRectangle.getY(roomOffsetY);
		}
		else {
			yValue = posY;
		}
		return yValue;
	}

	public JDImageProxy<?> getImage() {
		return image;
	}
}
