package de.jdungeon.androidapp.gui;

import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import dungeon.JDPoint;
import event.EventManager;
import event.PlayerDiedEvent;
import util.JDDimension;

import de.jdungeon.androidapp.io.MusicUtils;
import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input.TouchEvent;
import de.jdungeon.implementation.AndroidGame;

public class GameOverView extends AbstractGUIElement {

	public GameOverView(JDPoint position, JDDimension dimension,
						StandardScreen screen, Game game) {
		super(position, dimension, screen, game);

	}

	private boolean show = false;

	public void setShow(boolean show) {
		this.show = show;
		if(show) {
			MediaPlayer mediaPlayer = MusicUtils.playMusicFileRandom( ((AndroidGame)game).getAssets(), "Dark_Times.mp3", "For_the_Fallen.mp3");
		}
	}

	@Override
	public boolean isVisible() {
		return show;
	}

	@Override
	public void handleTouchEvent(TouchEvent touch) {
		EventManager.getInstance().fireEvent(new PlayerDiedEvent());
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
				textStartRow - 10, g.getPaintBlack());

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
