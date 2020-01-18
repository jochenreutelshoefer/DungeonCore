package graphics.util;

import util.JDDimension;
import dungeon.JDPoint;

public class JDRectangle implements DrawingRectangle {

	private final int x;
	private final int y;
	private final int width;
	private final int height;
	public JDRectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
/*
	public JDRectangle(JDPoint p, int width, int height) {
		this.x = p.getX();
		this.y = p.getY();
		this.width = width;
		this.height = height;
	}
	
	public JDRectangle(JDPoint p, JDDimension d) {
		this.x = p.getX();
		this.y = p.getY();
		this.width = d.getWidth();
		this.height = d.getHeight();
	}

*/

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public int getX(int roomOffsetX) {
		return getX();
	}

	@Override
	public int getY(int roomOffsetY) {
		return getY();
	}

	public boolean containsPoint(JDPoint p) {
		return p.getX() >= x && p.getX() <= x + width && p.getY() >= y
				&& p.getY() <= y + height;
	}

	@Override
	public boolean containsPoint(JDPoint p, int roomOffsetX, int roomOffsetY) {
		return containsPoint(p);
	}
}
