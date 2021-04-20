package de.jdungeon.util;

import java.util.Objects;

public class JDDimension {

	private final int width;
	private final int height;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		JDDimension that = (JDDimension) o;
		return width == that.width && height == that.height;
	}

	@Override
	public int hashCode() {
		return Objects.hash(width, height);
	}

	public JDDimension(int w, int h) {
		this.width = w;
		this.height = h;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
