package de.jdungeon.androidapp.screen.start;

import event.EventManager;
import figure.hero.Hero;
import util.JDDimension;

import de.jdungeon.androidapp.audio.AudioManagerTouchGUI;
import de.jdungeon.androidapp.gui.ImageGUIElement;
import de.jdungeon.game.Game;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input.TouchEvent;

import dungeon.JDPoint;

public class HeroChoiceButton extends ImageGUIElement {

	private final int heroType;

	private static final int SIZE = 120;

	@Deprecated
	public HeroChoiceButton(JDPoint position, int heroType, Image im) {
		super(position, new JDDimension(SIZE, SIZE), im);
		this.heroType = heroType;
	}

	public HeroChoiceButton(JDPoint position, Hero.HeroCategory category, Image im) {
		super(position, new JDDimension(SIZE, SIZE), im);
		this.heroType = category.getCode();
	}

	@Override
	public boolean handleTouchEvent(TouchEvent touch) {
		EventManager.getInstanceDungeon().fireEvent(new HeroCategorySelectedEvent(heroType));
		AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
		return true;
	}

}
