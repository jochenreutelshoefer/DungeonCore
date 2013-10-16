package graphics.util;

import util.JDDimension;
import dungeon.JDPoint;

public class JDRectangle {

	private int x;
	private int y;
	private int width;
	private int height;

	public JDRectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
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

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean containsPoint(JDPoint p) {
		return p.getX() >= x && p.getX() <= x + width && p.getY() >= y
				&& p.getY() <= y + height;
	}
}
