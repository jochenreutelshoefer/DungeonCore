package de.jdungeon.androidapp.gui;

import graphics.ImageManager;
import graphics.JDImageProxy;
import gui.Paragraph;
import util.JDDimension;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input.TouchEvent;
import de.jdungeon.util.FloatDimension;
import dungeon.JDPoint;

public class InfoPanel implements GUIElement {

	private final JDPoint position;
	private final JDDimension dimension;
	private Paragraph[] content;
	private boolean visible = true;
	private float timer = 0;
	private final static float DISPLAY_TIME = 1000f;
	private final static int SLIDE_OUT_STEPS = 20;
	private int slideStep = -1;

	public InfoPanel(JDPoint position, JDDimension dimension) {
		super();
		this.position = position;
		this.dimension = dimension;
	}

	public void setContent(Paragraph[] content) {
		this.content = content;
		visible = true;
		timer = DISPLAY_TIME;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public JDPoint getPositionOnScreen() {
		return position;
	}

	@Override
	public JDDimension getDimension() {
		return dimension;
	}

	@Override
	public void handleTouchEvent(TouchEvent touch) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleScrollEvent(FloatDimension scrolling) {
		// TODO Auto-generated method stub

	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		/*
		 * draw background
		 */
		int x = position.getX();
		if (slideStep >= 0) {
			x = x
					+ ((SLIDE_OUT_STEPS - slideStep)
							* this.getDimension().getWidth() / SLIDE_OUT_STEPS);
		}
		g.drawRect(x, position.getY(), dimension.getWidth(),
				dimension.getHeight(), Color.GRAY);

		JDImageProxy<?> background = ImageManager.paperBackground;
		Image image = (Image) background.getImage();
		g.drawScaledImage(image, x, position.getY(),
				dimension.getWidth(), dimension.getHeight(), 0, 0,
				image.getWidth(), image.getHeight());

		/*
		 * print information
		 */
		Paragraph[] paragraphs = this.content;
		if (paragraphs != null) {

			int posCounterY = 35;
			for (Paragraph paragraph : paragraphs) {
				Paint p = new Paint();
				p.setColor(ColorConverter.getColor(paragraph.getC()));
				p.setTextAlign(Align.CENTER);
				p.setStyle(p.getStyle());
				p.setTextSize(13);
				// paragraph.getFont();
				g.drawString(paragraph.getText(), x
						+ (this.dimension.getWidth() / 2), position.getY()
						+ posCounterY, p);
				posCounterY += 30;
			}
		}

		int borderWidth = 20;

		/*
		 * paint border
		 */
		g.drawScaledImage(
				(Image) ImageManager.border_double_left_upper_corner.getImage(),
				x, position.getY(), borderWidth, borderWidth, 0, 0, 20, 20);
		g.drawScaledImage(
				(Image) ImageManager.border_double_left_lower_corner.getImage(),
				x, position.getY() + dimension.getHeight() - borderWidth,
				borderWidth, borderWidth, 0, 0, 20, 20);
		g.drawScaledImage((Image) ImageManager.border_double_right_upper_corner
				.getImage(), x + dimension.getWidth() - borderWidth, position
				.getY(), borderWidth, borderWidth, 0, 0, 20, 20);
		g.drawScaledImage((Image) ImageManager.border_double_right_lower_corner
				.getImage(), x + dimension.getWidth() - borderWidth,
				position.getY() + dimension.getHeight() - borderWidth,
				borderWidth, borderWidth, 0, 0, 20, 20);

		g.drawScaledImage((Image) ImageManager.border_double_top.getImage(), x
				+ borderWidth, position.getY(), dimension.getWidth() - 40,
				borderWidth, 0, 0, 72, 20);

		g.drawScaledImage((Image) ImageManager.border_double_left.getImage(),
				x, position.getY() + borderWidth, borderWidth,
				dimension.getHeight() - 40 + 6, 0, 0, 20, 56);

		g.drawScaledImage((Image) ImageManager.border_double_bottom.getImage(),
				x + borderWidth, position.getY() + dimension.getHeight()
						- borderWidth, dimension.getWidth() - 40, borderWidth,
				0, 0, 72, 20);

		g.drawScaledImage((Image) ImageManager.border_double_right.getImage(),
				x + dimension.getWidth() - 20, position.getY() + borderWidth,
				borderWidth, dimension.getHeight() - 40 + 4, 0, 0, 20, 56);

	}

	@Override
	public void update(float time) {
		timer -= time;
		if (timer < 0 && visible) {
			if (this.slideStep == -1) {
				this.slideStep = SLIDE_OUT_STEPS;
			}
			if (slideStep > 0) {
				slideStep -= 1;
			} else {
				this.visible = false;
				this.slideStep = -1;
			}
		}
	}

}
