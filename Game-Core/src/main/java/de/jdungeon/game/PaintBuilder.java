package de.jdungeon.game;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 01.01.17.
 */
public class PaintBuilder {

	private int fontSize = 15;
	private Color color = Colors.BLACK;

	public Paint.Alignment getAlignment() {
		return alignment;
	}

	public void setAlignment(Paint.Alignment alignment) {
		this.alignment = alignment;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	private Paint.Alignment alignment = Paint.Alignment.CENTER;

}
