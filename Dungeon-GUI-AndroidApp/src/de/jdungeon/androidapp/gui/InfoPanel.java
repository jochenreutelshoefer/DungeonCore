package de.jdungeon.androidapp.gui;

import gui.Paragraph;
import gui.Paragraphable;
import util.JDDimension;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import de.jdungeon.androidapp.GameScreen;
import de.jdungeon.game.Graphics;
import de.jdungeon.util.ScrollMotion;
import dungeon.JDPoint;

public class InfoPanel extends SlidingGUIElement {

	public InfoPanel(JDPoint position, JDDimension dimension, GameScreen screen) {
		super(position, dimension, new JDPoint(position.getX()
				+ dimension.getWidth(), position.getY()), screen);
	}

	private Paragraphable content;
	private boolean visible = true;
	private float timer = 0;
	private final static float DISPLAY_TIME = 1000f;
	private final static int SLIDE_OUT_STEPS = 20;

	public void setContent(Paragraphable entity) {
		this.content = entity;
		visible = true;
		timer = DISPLAY_TIME;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void handleScrollEvent(ScrollMotion scrolling) {
		if (scrolling.getMovement().getX() < 0) {
			timer = 0;
		}
	}


	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		/*
		 * draw background
		 */
		int x = getCurrentX();
		g.drawRect(x, position.getY(), dimension.getWidth(),
				dimension.getHeight(), Color.GRAY);

		GUIUtils.drawBackground(g, x, position.getY(), dimension);

		/*
		 * print information
		 */
		if (this.content != null) {
			Paragraph[] paragraphs = this.content.getParagraphs();
			if (paragraphs != null) {
				int posCounterY = 35;
				for (Paragraph paragraph : paragraphs) {
					Paint p = new Paint();
					p.setColor(ColorConverter.getColor(paragraph.getC()));
					p.setTextAlign(Align.CENTER);
					p.setStyle(p.getStyle());
					p.setTextSize(13);
					// paragraph.getFont();
					g.drawString(paragraph.getText(),
							x + (this.dimension.getWidth() / 2),
							position.getY() + posCounterY, p);
					posCounterY += 30;
				}
			}
		}


		/*
		 * paint border
		 */
		GUIUtils.drawDoubleBorder(g, x, position.getY(), dimension, 20);

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
