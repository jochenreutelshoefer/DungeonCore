package com.example.dungeon_gui_androidapp;

import android.view.Menu;
import de.jdungeon.game.Screen;
import de.jdungeon.implementation.AndroidGame;

public class JDungeon extends AndroidGame {


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.jdungeon, menu);
		return true;
	}

	@Override
	public Screen getInitScreen() {
		return new StartScreen(this);
	}

}
