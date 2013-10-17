package de.jdungeon.androidapp;

import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Input.TouchEvent;
import de.jdungeon.game.Screen;

public class StartScreen extends Screen{

	private Paint paint;

	public StartScreen(Game game) {
		super(game);
		// Defining a paint object
		paint = new Paint();
		paint.setTextSize(25);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setAntiAlias(true);
		paint.setColor(Color.RED);
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = g.getInput().getTouchEvents();
		if(touchEvents.size() > 0) {
			g.setScreen(new GameScreen(g));
		}
		
	}

	@Override
	public void paint(float deltaTime) {
		Graphics gr = g.getGraphics();
		// Darken the entire screen so you can display the Paused screen.
		gr.drawARGB(155, 0, 0, 0);
		gr.drawString("Welcome to Java Dungeon", 165, 165, paint);
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void backButton() {
		// TODO Auto-generated method stub
		
	}

}
