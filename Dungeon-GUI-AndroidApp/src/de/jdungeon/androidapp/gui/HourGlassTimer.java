package de.jdungeon.androidapp.gui;

import util.JDDimension;
import de.jdungeon.androidapp.screen.GameScreen;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input.TouchEvent;
import dungeon.JDPoint;
import figure.hero.HeroInfo;

public class HourGlassTimer extends AbstractGUIElement {

	private final HeroInfo hero;

	public HourGlassTimer(JDPoint position, JDDimension dimension,
			GameScreen screen, HeroInfo hero) {
		super(position, dimension, screen);
		this.hero = hero;
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void handleTouchEvent(TouchEvent touch) {
		screen.getControl().endRound();
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		Image image = null;
		int actionPoints = hero.getActionPoints();
		if (actionPoints == 0) {
			image = GUIImageManager.getImage(GUIImageManager.HOUR_GLASS_EMPTY,
					screen.getGame());
			// } else if (actionPoints == 1) {
			// image =
			// GUIImageManager.getImage(GUIImageManager.HOUR_GLASS_THIRD,
			// screen.getGame());
		} else if (actionPoints == 1) {
			image = GUIImageManager.getImage(GUIImageManager.HOUR_GLASS_HALF,
					screen.getGame());
		} else if (actionPoints == 2) {
			image = GUIImageManager.getImage(GUIImageManager.HOUR_GLASS_FULL,
					screen.getGame());
		}
		if (image != null) {

		g.drawScaledImage(image, this.position.getX(), this
				.getPositionOnScreen().getY(), this.getDimension().getWidth(),
				this.getDimension().getHeight(), 0, 0, image.getWidth(), image
						.getHeight());
		}
	}

}
