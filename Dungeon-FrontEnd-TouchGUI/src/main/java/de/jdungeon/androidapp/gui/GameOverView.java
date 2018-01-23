package de.jdungeon.androidapp.gui;

import dungeon.JDPoint;
import event.EventManager;
import event.PlayerDiedEvent;
import util.JDDimension;

import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input.TouchEvent;
import de.jdungeon.util.PaintBuilder;

public class GameOverView extends AbstractGUIElement {

	public GameOverView(JDPoint position, JDDimension dimension,
						StandardScreen screen, Game game) {
		super(position, dimension, screen, game);

	}

	private boolean show = false;

	public void setShow(boolean show) {
		this.show = show;
	}

	public boolean isVisible() {
		return show;
	}

	@Override
	public boolean handleTouchEvent(TouchEvent touch) {
		EventManager.getInstance().fireEvent(new PlayerDiedEvent());
		return true;
	}

	public void paint(Graphics g, JDPoint viewportPosition) {

		GUIUtils.drawBackground(g, this);

		int textStartRow = this.position.getY()
				+ this.getDimension().getHeight() / 4;
		int textStartX = this.position.getX() + 15;
		int lineHeight = 20;


		PaintBuilder red = new PaintBuilder();
		red.setColor(Colors.RED);
		red.setFontSize(20);
		g.drawString("Game Over", this.position.getX() + 100,
				textStartRow - 10, g.getTextPaintBlack());

				/*

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

*/

		GUIUtils.drawDoubleBorder(g, this, 20);
	}

}
