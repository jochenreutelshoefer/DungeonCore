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

public class GameOverView extends Popup {

	public GameOverView(JDPoint position, JDDimension dimension,
						StandardScreen screen, Game game) {
		super(position, dimension, screen, game, "Game Over", new PlayerDiedEvent());

	}
}
