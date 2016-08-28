package de.jdungeon.androidapp.screen.start;

import event.EventManager;
import figure.hero.Hero;
import user.DefaultDungeonSession;
import util.JDDimension;
import de.jdungeon.androidapp.gui.AbstractGUIElement;
import de.jdungeon.androidapp.gui.ImageGUIElement;
import de.jdungeon.androidapp.screen.GameScreen;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input.TouchEvent;
import de.jdungeon.user.Session;

import dungeon.JDPoint;

public class HeroChoiceButton extends ImageGUIElement {

	private final int heroType;

	@Deprecated
	public HeroChoiceButton(JDPoint position, int heroType, Image im, Game game) {
		super(position, new JDDimension(120, 120), im, game);
		this.heroType = heroType;
	}

	public HeroChoiceButton(JDPoint position, Hero.HeroCategory category, Image im, Game game) {
		super(position, new JDDimension(120, 120), im, game);
		this.heroType = category.getCode();
	}

	@Override
	public void handleTouchEvent(TouchEvent touch) {
		EventManager.getInstance().fireEvent(new HeroCategorySelectedEvent(heroType));
	}

}
