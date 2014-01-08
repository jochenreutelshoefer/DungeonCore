package de.jdungeon.androidapp.gui;

import util.JDDimension;
import android.graphics.Color;
import android.graphics.Paint;
import de.jdungeon.androidapp.GameScreen;
import de.jdungeon.androidapp.StartScreen;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input.TouchEvent;
import dungeon.JDPoint;

public class GameOverView extends AbstractGUIElement {

	public GameOverView(JDPoint position, JDDimension dimension,
			GameScreen screen) {
		super(position, dimension, screen);
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
	public void handleTouchEvent(TouchEvent touch) {
		this.getScreen().getGame()
				.setScreen(new StartScreen(this.getScreen().getGame()));
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {

		GUIUtils.drawBackground(g, this);

		int textStartRow = this.position.getY()
				+ this.getDimension().getHeight() / 4;
		int textStartX = this.position.getX() + 15;
		int lineHeight = 20;

		Paint red = new Paint();
		red.setColor(Color.RED);
		red.setTextSize(20);
		g.drawString("Game Over", this.position.getX() + 100,
				textStartRow - 10, red);

		Paint yellow = new Paint();
		yellow.setColor(Color.YELLOW);
		yellow.setTextSize(15);
		g.drawString("Game by: Jochen Reutelshöfer", textStartX, textStartRow
				+ lineHeight - 4, yellow);

		Paint black = new Paint();
		black.setColor(Color.BLACK);
		black.setTextSize(12);
		g.drawString("Credits:", textStartX, textStartRow + 2 * lineHeight,
				black);

		g.drawString("Game Graphics Sprites: Reiner Prokein", textStartX,
				textStartRow + 3 * lineHeight, black);

		g.drawString("GUI Sprites: Lamoot, blarumyrran, Clint Bellanger",
				textStartX, textStartRow + 4 * lineHeight, black);

		g.drawString(
"Scrittl, Tempest in the Aether and Ryzom Core,",
				textStartX, textStartRow + 5 * lineHeight, black);

		g.drawString("yinakoSGA", textStartX, textStartRow + 6 * lineHeight,
				black);

		GUIUtils.drawDoubleBorder(g, this, 20);
	}

}
