package de.jdungeon.androidapp.screen.start;

import animation.Motion;
import dungeon.JDPoint;
import dungeon.util.RouteInstruction;
import figure.hero.Hero;
import graphics.ImageManager;

import de.jdungeon.game.Game;
import de.jdungeon.game.Image;
import de.jdungeon.game.Music;

public class HeroSelectionScreen extends MenuScreen {


	public HeroSelectionScreen(Game game) {
		super(game);

		RouteInstruction.Direction south = RouteInstruction.Direction.South;

		int border = 0;
		int screenWidth = game.getScreenWidth();
		int screenWidthBy5 = (screenWidth - 2 * border) / 5;

		HeroChoiceButton warriorButton = new HeroChoiceButton(new JDPoint(border + screenWidthBy5,
				200), Hero.HeroCategory.Warrior, (Image) ImageManager.getAnimationSet(Hero.HeroCategory.Warrior, Motion.Walking, south).getImagesNr(0).getImage(), game);
		this.guiElements.add(warriorButton);

		HeroChoiceButton thiefButton = new HeroChoiceButton(new JDPoint(border + 2 * screenWidthBy5,
				200), Hero.HeroCategory.Thief, (Image) ImageManager.getAnimationSet(Hero.HeroCategory.Thief, Motion.Walking, south).getImagesNr(0).getImage(), game);
		this.guiElements.add(thiefButton);

		HeroChoiceButton druidButton = new HeroChoiceButton(new JDPoint(border + 3 * screenWidthBy5,
				200), Hero.HeroCategory.Druid, (Image) ImageManager.getAnimationSet(Hero.HeroCategory.Druid, Motion.Walking, south).getImagesNr(0).getImage(), game);
		this.guiElements.add(druidButton);

		HeroChoiceButton mageButton = new HeroChoiceButton(
				new JDPoint(border + 4 * screenWidthBy5, 200), Hero.HeroCategory.Mage, (Image) ImageManager.getAnimationSet(Hero.HeroCategory.Mage, Motion.Walking, south).getImagesNr(0).getImage(), game);
		this.guiElements.add(mageButton);
	}

	@Override
	protected String getHeaderString() {
		return "Held wählen";
	}

	@Override
	public void init() {
		Music music = this.game.getAudio().createMusic("music/" + "Dark_Times.mp3");
		music.play();
	}
}
