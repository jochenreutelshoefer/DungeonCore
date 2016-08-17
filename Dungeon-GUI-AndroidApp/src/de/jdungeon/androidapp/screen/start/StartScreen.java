package de.jdungeon.androidapp.screen.start;

import dungeon.JDPoint;
import figure.hero.Hero;
import graphics.ImageManager;

import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;

public class StartScreen extends StandardScreen {

	public StartScreen(Game game) {
		super(game);

		HeroChoiceButton warriorButton = new HeroChoiceButton(new JDPoint(150,
				200), Hero.HEROCODE_WARRIOR, (Image) ImageManager
				.getWarrior_walking(3).getImagesNr(0).getImage(), this.game);
		this.guiElements.add(warriorButton);

		HeroChoiceButton thiefButton = new HeroChoiceButton(new JDPoint(300,
				200), Hero.HEROCODE_HUNTER, (Image) ImageManager
				.getThief_walking(3).getImagesNr(0).getImage(), this.game);
		this.guiElements.add(thiefButton);

		HeroChoiceButton druidButton = new HeroChoiceButton(new JDPoint(500,
				200), Hero.HEROCODE_DRUID, (Image) ImageManager
				.getDruid_walking(3).getImagesNr(0).getImage(), this.game);
		this.guiElements.add(druidButton);

		HeroChoiceButton mageButton = new HeroChoiceButton(
				new JDPoint(650, 200), Hero.HEROCODE_MAGE, (Image) ImageManager
						.getMage_walking(3).getImagesNr(0).getImage(), this.game);
		this.guiElements.add(mageButton);
	}

	@Override
	public void paint(float deltaTime) {
		Graphics gr = game.getGraphics();

		super.paint(deltaTime);

		// Darken the entire screen so you can display the Paused screen.
		gr.drawARGB(155, 0, 0, 0);
		gr.drawString("Welcome to Java Dungeon", 165, 165, defaultPaint);

	}

}
