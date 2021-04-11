package de.jdungeon.app.screen.start;

import de.jdungeon.event.EventManager;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.util.JDDimension;

import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.gui.ImageGUIElement;
import de.jdungeon.game.Game;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input.TouchEvent;

import de.jdungeon.dungeon.JDPoint;

public class HeroChoiceButton extends ImageGUIElement {

	private final int heroType;

	private static final int SIZE = 120;

	@Deprecated
	public HeroChoiceButton(JDPoint position, int heroType, Image im, Game game) {
		super(position, new JDDimension(SIZE, SIZE), im, game);
		this.heroType = heroType;
	}

	public HeroChoiceButton(JDPoint position, Hero.HeroCategory category, Image im, Game game) {
		super(position, new JDDimension(SIZE, SIZE), im, game);
		this.heroType = category.getCode();
	}

	@Override
	public boolean handleTouchEvent(TouchEvent touch) {
		EventManager.getInstance().fireEvent(new HeroCategorySelectedEvent(heroType));
		AudioManagerTouchGUI.playSound(AudioManagerTouchGUI.TOUCH1);
		return true;
	}

}
