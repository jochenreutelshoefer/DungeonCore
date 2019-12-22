package de.jdungeon.app.screen.start;

import dungeon.JDPoint;

import de.jdungeon.app.audio.MusicManager;
import de.jdungeon.app.event.QuitGameEvent;
import de.jdungeon.app.event.StartNewGameEvent;
import de.jdungeon.app.gui.SimpleButton;
import de.jdungeon.game.Game;
import de.jdungeon.game.Music;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 27.12.16.
 */
public class WelcomeScreen extends MenuScreen {

	public WelcomeScreen(Game game) {
		super(game);
		int x = game.getScreenWidth() / 2 - SimpleButton.getDefaultDimension().getWidth()/2;
		int screenHeight = game.getScreenHeight();
		int startHeight = screenHeight/2 - screenHeight/8;
		this.guiElements.add(new SimpleButton("Neues Spiel", new StartNewGameEvent(), new JDPoint(x, startHeight), game));
		this.guiElements.add(new SimpleButton("Beenden", new QuitGameEvent(), new JDPoint(x, startHeight + 110), game));
	}

	@Override
	protected String getHeaderString() {
		return "Willkommen bei <todo: find game title>";
	}

	@Override
	public void init() {
		Music music = this.game.getAudio().createMusic("music/" + "Exciting_Trailer.mp3");
		MusicManager.getInstance().playMusic(music);
	}
}
