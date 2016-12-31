package de.jdungeon.androidapp.screen.start;

import android.media.MediaPlayer;
import dungeon.JDPoint;

import de.jdungeon.androidapp.event.QuitGameEvent;
import de.jdungeon.androidapp.event.StartNewGameEvent;
import de.jdungeon.androidapp.gui.SimpleButton;
import de.jdungeon.androidapp.io.MusicUtils;
import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Music;
import de.jdungeon.implementation.AndroidGame;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.12.16.
 */
public class WelcomeScreen extends StandardScreen {

	public WelcomeScreen(Game game) {
		super(game);
		int x = game.getScreenWidth() / 2 - SimpleButton.getDefaultDimension().getWidth()/2;
		this.guiElements.add(new SimpleButton("Neues Spiel", new StartNewGameEvent(), new JDPoint(x, 200)));
		this.guiElements.add(new SimpleButton("Beenden", new QuitGameEvent(), new JDPoint(x, 300)));
	}

	@Override
	public void paint(float deltaTime) {
		Graphics gr = game.getGraphics();

		super.paint(deltaTime);

		gr.drawARGB(155, 0, 0, 0);
		gr.drawString("Willkommen bei <Untitled Dungeon Game>", game.getScreenWidth() / 2, 120, gr.getDefaultPaint());
	}

	@Override
	public void init() {
		Music music = this.game.getAudio().createMusic("music/" + "Exciting_Trailer.mp3");
		music.play();
	}
}
