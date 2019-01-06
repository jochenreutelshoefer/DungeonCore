package de.jdungeon.androidapp.gui;

import dungeon.JDPoint;
import event.Event;
import event.EventManager;
import util.JDDimension;

import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input;
import de.jdungeon.util.PaintBuilder;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 04.01.19.
 */
public class Popup extends AbstractGUIElement {

	protected String text;
	private  Event event = null;;

	public Popup(JDPoint position, JDDimension dimension,
				 StandardScreen screen, Game game, Event fireWhenTouched) {
		super(position, dimension, screen, game);
		this.event = fireWhenTouched;
	}

	public Popup(JDPoint position, JDDimension dimension,
					  StandardScreen screen, Game game, String text) {
		super(position, dimension, screen, game);
		this.text = text;
	}

	public Popup(JDPoint position, JDDimension dimension,
				 StandardScreen screen, Game game, String text, Event fireWhenTouched) {
		this(position, dimension, screen, game,text);
		this.event = fireWhenTouched;
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
	public boolean handleTouchEvent(Input.TouchEvent touch) {
		if(event != null) {
			EventManager.getInstance().fireEvent(event);
		}
		show = false;
		return true;
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {

		drawBackground(g);

		int textStartRow = this.position.getY()
				+ this.getDimension().getHeight() / 4;

		PaintBuilder red = new PaintBuilder();
		red.setColor(Colors.RED);
		red.setFontSize(36);
		g.drawString(text, this.position.getX() + 100,
				textStartRow - 10, g.getTextPaintWhite25());

		drawBorder(g);
	}

}
