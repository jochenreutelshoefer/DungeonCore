package de.jdungeon.androidapp.screen.start;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import animation.Motion;
import dungeon.JDPoint;
import dungeon.util.RouteInstruction;
import figure.hero.Hero;
import figure.hero.HeroInfo;
import graphics.ImageManager;

import de.jdungeon.androidapp.io.MusicUtils;
import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Music;

public class HeroSelectionScreen extends StandardScreen {

	private MediaPlayer mediaPlayer;

	public HeroSelectionScreen(Game game) {
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
		gr.drawString("Held wählen", 165, 165, gr.getDefaultPaint());

	}

	@Override
	public void pause() {
		// TOOD : implement fade out
	}

	@Override
	public void init() {
		Music music = this.game.getAudio().createMusic("music/" + "Dark_Times.mp3");
		music.play();

		//MediaPlayer mediaPlayer = MusicUtils.playMusicFile(, context.getAssets());
	}
}
