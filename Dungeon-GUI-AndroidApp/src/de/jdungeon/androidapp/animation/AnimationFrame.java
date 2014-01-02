package de.jdungeon.androidapp.animation;

import graphics.JDImageProxy;
import dungeon.JDPoint;

public class AnimationFrame {

	private JDImageProxy<?> image;
	private String text;
	private JDPoint textCoordinatesOffset;

	public JDImageProxy<?> getImage() {
		return image;
	}

	public void setImage(JDImageProxy<?> image) {
		this.image = image;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public JDPoint getTextCoordinatesOffset() {
		return textCoordinatesOffset;
	}

	public void setTextCoordinatesOffset(JDPoint textCoordinatesOffset) {
		this.textCoordinatesOffset = textCoordinatesOffset;
	}

	public AnimationFrame(JDImageProxy<?> image) {
		super();
		this.image = image;
	}

	public AnimationFrame(JDImageProxy<?> image, String text,
			JDPoint textCoordinatesOffset) {
		super();
		this.image = image;
		this.text = text;
		this.textCoordinatesOffset = textCoordinatesOffset;
	}

}
