package de.jdungeon.androidapp.screen.start;

import animation.Motion;
import dungeon.JDPoint;
import dungeon.util.RouteInstruction;
import figure.hero.Hero;
import figure.hero.HeroInfo;
import graphics.ImageManager;

import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;

public class StartScreen extends StandardScreen {

	public StartScreen(Game game) {
		super(game);

		RouteInstruction.Direction south = RouteInstruction.Direction.South;

		HeroChoiceButton warriorButton = new HeroChoiceButton(new JDPoint(150,
				200), Hero.HeroCategory.Warrior, (Image) ImageManager.getAnimationSet(Hero.HeroCategory.Warrior, Motion.Walking, south).getImagesNr(0).getImage(), this.game);
		this.guiElements.add(warriorButton);

		HeroChoiceButton thiefButton = new HeroChoiceButton(new JDPoint(300,
				200), Hero.HeroCategory.Thief, (Image) ImageManager.getAnimationSet(Hero.HeroCategory.Thief, Motion.Walking, south).getImagesNr(0).getImage(), this.game);
		this.guiElements.add(thiefButton);

		HeroChoiceButton druidButton = new HeroChoiceButton(new JDPoint(500,
				200), Hero.HeroCategory.Druid, (Image) ImageManager.getAnimationSet(Hero.HeroCategory.Druid, Motion.Walking, south).getImagesNr(0).getImage(), this.game);
		this.guiElements.add(druidButton);

		HeroChoiceButton mageButton = new HeroChoiceButton(
				new JDPoint(650, 200), Hero.HeroCategory.Mage, (Image) ImageManager.getAnimationSet(Hero.HeroCategory.Mage, Motion.Walking, south).getImagesNr(0).getImage(), this.game);
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
