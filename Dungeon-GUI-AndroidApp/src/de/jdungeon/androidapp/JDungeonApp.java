package de.jdungeon.androidapp;

import android.view.Menu;

import com.example.dungeon_gui_androidapp.R;

import de.jdungeon.game.Screen;
import de.jdungeon.implementation.AndroidGame;

public class JDungeonApp extends AndroidGame {


	private boolean firstTimeCreate = true;
	
	enum GameState {
		Ready, Running, Paused, GameOver
	}

	GameState state = GameState.Ready;

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public Configuration getConfiguration() {
		return new DefaultConfiguration();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.jdungeon, menu);
		return true;
	}

	@Override
	public Screen getInitScreen() {
		 if (firstTimeCreate ) {
	            Assets.load(this);
	            firstTimeCreate = false;
	        }
		return new StartScreen(this);
	}

}
