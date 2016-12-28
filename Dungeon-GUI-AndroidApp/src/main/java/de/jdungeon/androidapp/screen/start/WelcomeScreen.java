package de.jdungeon.androidapp.screen.start;

import com.apple.eawt.AppEvent;
import dungeon.JDPoint;

import de.jdungeon.androidapp.event.QuitGameEvent;
import de.jdungeon.androidapp.event.StartNewGameEvent;
import de.jdungeon.androidapp.gui.SimpleButton;
import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;

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
		gr.drawString("Willkommen bei <Untitled Dungeon Game>", 300, 120, StandardScreen.defaultPaint);
	}
}
