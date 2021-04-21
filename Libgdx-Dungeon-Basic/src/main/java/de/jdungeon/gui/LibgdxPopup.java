package de.jdungeon.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.event.Event;
import de.jdungeon.event.EventManager;
import de.jdungeon.util.JDDimension;

import de.jdungeon.asset.Assets;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 04.01.19.
 */
public class LibgdxPopup extends AbstractLibgdxGUIElement {

	protected String text;
	private Event event = null;
	;
	private GlyphLayout layout;

	public LibgdxPopup(JDPoint position, JDDimension dimension, Event fireWhenTouched) {
		super(position, dimension);
		this.event = fireWhenTouched;
		initPopup();
	}

	public LibgdxPopup(JDPoint position, JDDimension dimension, String text) {
		super(position, dimension);
		this.text = text;
		initPopup();
	}

	public LibgdxPopup(JDPoint position, JDDimension dimension, String text, Event fireWhenTouched) {
		this(position, dimension, text);
		this.event = fireWhenTouched;
		initPopup();
	}

	public void initPopup() {
		layout = new GlyphLayout();
	}

	public void setText(String text) {
		this.text = text;
	}

	private boolean show = false;

	public void setShow(boolean show) {
		this.show = show;
	}

	@Override
	public boolean isVisible() {
		return show;
	}

	@Override
	public boolean handleClickEvent(int x, int y) {
		if (event != null) {
			EventManager.getInstance().fireEvent(event);
		}
		show = false;
		return true;
	}

	@Override
	public void paint(SpriteBatch batch, float deltaTime) {

		drawBackground(batch, this.position.getX(), this.position.getY());

		BitmapFont headerFont = Assets.instance.fonts.defaultBigFlipped;
		headerFont.setColor(Color.RED);
		int headerWidth = 140;
		layout.setText(headerFont, text, Color.WHITE, headerWidth, Align.center, true);
		int headerStartX = position.getX() + dimension.getWidth() / 2 - headerWidth / 2;
		headerFont.draw(batch, layout, headerStartX, position.getY() + 50);
	}
}
