package de.jdungeon.androidapp.screen.start;

import java.awt.*;

import dungeon.JDPoint;

import de.jdungeon.androidapp.event.QuitGameEvent;
import de.jdungeon.androidapp.event.StartNewGameEvent;
import de.jdungeon.androidapp.gui.SimpleButton;
import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
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
		this.guiElements.add(new SimpleButton("Neues Spiel", new StartNewGameEvent(), new JDPoint(x, startHeight)));
		this.guiElements.add(new SimpleButton("Beenden", new QuitGameEvent(), new JDPoint(x, startHeight + 110)));
	}

	@Override
	protected String getHeaderString() {
		return "Willkommen bei <Untitled Dungeon Game>";
	}

	@Override
	public void init() {
		Music music = this.game.getAudio().createMusic("music/" + "Exciting_Trailer.mp3");
		music.play();
	}
}
