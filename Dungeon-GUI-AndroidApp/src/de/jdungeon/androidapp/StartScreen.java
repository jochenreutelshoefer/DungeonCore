package de.jdungeon.androidapp;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import android.graphics.Color;
import android.graphics.Paint;
import de.jdungeon.androidapp.gui.GUIElement;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input.TouchEvent;
import de.jdungeon.game.Screen;
import dungeon.JDPoint;
import figure.hero.Hero;
import graphics.ImageManager;

public class StartScreen extends Screen {

	private final Paint paint;

	private final List<GUIElement> guiElements = new LinkedList<GUIElement>();

	public StartScreen(Game game) {
		super(game);
		// Defining a paint object
		paint = new Paint();
		paint.setTextSize(25);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setAntiAlias(true);
		paint.setColor(Color.RED);

		HeroChoiceButton warriorButton = new HeroChoiceButton(new JDPoint(150,
				200), Hero.HEROCODE_WARRIOR, (Image) ImageManager
				.getWarrior_walking(3).getImagesNr(0).getImage(), g);
		this.guiElements.add(warriorButton);

		HeroChoiceButton thiefButton = new HeroChoiceButton(new JDPoint(300,
				200), Hero.HEROCODE_HUNTER, (Image) ImageManager
				.getThief_walking(3).getImagesNr(0).getImage(), g);
		this.guiElements.add(thiefButton);

		HeroChoiceButton druidButton = new HeroChoiceButton(new JDPoint(500,
				200), Hero.HEROCODE_DRUID, (Image) ImageManager
				.getDruid_walking(3).getImagesNr(0).getImage(), g);
		this.guiElements.add(druidButton);

		HeroChoiceButton mageButton = new HeroChoiceButton(
				new JDPoint(650, 200), Hero.HEROCODE_MAGE, (Image) ImageManager
						.getMage_walking(3).getImagesNr(0).getImage(), g);
		this.guiElements.add(mageButton);
	}

	@Override
	public void update(float deltaTime) {
		// List<TouchEvent> touchEvents = g.getInput().getTouchEvents();
		// if(touchEvents.size() > 0) {
		// g.setScreen(new GameScreen(g));
		// }

		/*
		 * handle touch events
		 */
		List<TouchEvent> touchEvents = g.getInput().getTouchEvents();
		TouchEvent touchDownEvent = null;
		for (int i = 0; i < touchEvents.size(); i++) {
			TouchEvent touchEvent = touchEvents.get(i);
			boolean isTouchDownEvent = g.getInput().isTouchDown(i);
			if (isTouchDownEvent) {
				touchDownEvent = touchEvent;

			}
		}

		if (touchDownEvent != null) {
			boolean guiOP = false;
			JDPoint coordinates = new JDPoint(touchDownEvent.x,
					touchDownEvent.y);
			ListIterator<GUIElement> listIterator = guiElements
					.listIterator(guiElements.size());
			while (listIterator.hasPrevious()) {
				GUIElement guiElement = listIterator.previous();
				if (guiElement.hasPoint(coordinates) && guiElement.isVisible()) {
					guiElement.handleTouchEvent(touchDownEvent);
					guiOP = true;
					break;
				}
			}
		}

	}

	@Override
	public void paint(float deltaTime) {
		Graphics gr = g.getGraphics();
		// Darken the entire screen so you can display the Paused screen.
		gr.drawARGB(155, 0, 0, 0);
		gr.drawString("Welcome to Java Dungeon", 165, 165, paint);

		/*
		 * draw gui elements
		 */
		List<GUIElement> elements = this.guiElements;
		for (GUIElement guiElement : elements) {
			if (guiElement.isVisible()) {
				guiElement.paint(gr, null);
			}
		}

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
