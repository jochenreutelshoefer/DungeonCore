package de.jdungeon.app.gui;

import dungeon.JDPoint;
import event.PlayerDiedEvent;
import util.JDDimension;

import de.jdungeon.app.screen.StandardScreen;
import de.jdungeon.game.Game;

public class GameOverView extends Popup {

	public GameOverView(JDPoint position, JDDimension dimension,
						StandardScreen screen, Game game) {
		super(position, dimension, game, "Game Over", new PlayerDiedEvent());

	}
}
