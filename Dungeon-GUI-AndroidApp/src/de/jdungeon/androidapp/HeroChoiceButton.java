package de.jdungeon.androidapp;

import util.JDDimension;
import de.jdungeon.androidapp.gui.AbstractGUIElement;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input.TouchEvent;
import dungeon.JDPoint;

public class HeroChoiceButton extends AbstractGUIElement {

	private final Image im;
	private final int heroType;
	private final Game g;

	public HeroChoiceButton(JDPoint position, int heroType, Image im, Game g) {
		super(position, new JDDimension(120, 120));
		this.heroType = heroType;
		this.im = im;
		this.g = g;
	}

	@Override
	public void handleTouchEvent(TouchEvent touch) {
		g.setScreen(new GameScreen(g, heroType));
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		g.drawScaledImage(im, this.position.getX(), this.position.getY(),
				this.dimension.getWidth(), this.dimension.getHeight(), 0, 0,
				im.getHeight(), im.getWidth());

	}

}
