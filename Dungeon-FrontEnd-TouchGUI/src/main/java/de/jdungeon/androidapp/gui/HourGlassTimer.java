package de.jdungeon.androidapp.gui;

import event.EventManager;
import util.JDDimension;

import de.jdungeon.androidapp.event.EndRoundEvent;
import de.jdungeon.androidapp.screen.GameScreen;
import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input.TouchEvent;
import dungeon.JDPoint;
import figure.hero.HeroInfo;

public class HourGlassTimer extends AbstractGUIElement {

	private final HeroInfo hero;

	public HourGlassTimer(JDPoint position, JDDimension dimension, StandardScreen screen, HeroInfo hero, Game game) {
		super(position, dimension, screen, game);
		this.hero = hero;
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void handleTouchEvent(TouchEvent touch) {
		EventManager.getInstance().fireEvent(new EndRoundEvent());

	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		Image image = null;
		int actionPoints = hero.getActionPoints();
		if (actionPoints == 0) {
			image = screen.getGuiImageManager().getImage(GUIImageManager.HOUR_GLASS_EMPTY);
			// } else if (actionPoints == 1) {
			// image =
			// GUIImageManager.getImage(GUIImageManager.HOUR_GLASS_THIRD,
			// screen.getGame());
		} else if (actionPoints == 1) {
			image = screen.getGuiImageManager().getImage(GUIImageManager.HOUR_GLASS_HALF);
		} else if (actionPoints == 2) {
			image = screen.getGuiImageManager().getImage(GUIImageManager.HOUR_GLASS_FULL);
		}
		if (image != null) {

		g.drawScaledImage(image, this.position.getX(), this
				.getPositionOnScreen().getY(), this.getDimension().getWidth(),
				this.getDimension().getHeight(), 0, 0, image.getWidth(), image
						.getHeight());
		}
	}

}
