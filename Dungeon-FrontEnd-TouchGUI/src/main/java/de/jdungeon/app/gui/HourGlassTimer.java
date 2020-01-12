package de.jdungeon.app.gui;

import dungeon.JDPoint;
import event.EventManager;
import figure.hero.HeroInfo;
import game.DungeonGame;
import util.JDDimension;

import de.jdungeon.app.event.EndRoundEvent;
import de.jdungeon.app.screen.StandardScreen;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input.TouchEvent;
import de.jdungeon.util.PaintBuilder;

public class HourGlassTimer extends AbstractGUIElement {

	private final HeroInfo hero;
	private final PaintBuilder paint;

	public HourGlassTimer(JDPoint position, JDDimension dimension, StandardScreen screen, HeroInfo hero, Game game) {
		super(position, dimension, screen, game);
		this.hero = hero;
		paint = new PaintBuilder();
		paint.setFontSize(14);
		paint.setColor(Colors.BLUE);

	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public boolean handleTouchEvent(TouchEvent touch) {
		EventManager.getInstance().fireEvent(new EndRoundEvent());
		return true;
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
		}
		else if (actionPoints == 1) {
			image = screen.getGuiImageManager().getImage(GUIImageManager.HOUR_GLASS_HALF);
		}
		else if (actionPoints == 2) {
			image = screen.getGuiImageManager().getImage(GUIImageManager.HOUR_GLASS_FULL);
		}
		if (image != null) {

			g.drawScaledImage(image, this.position.getX(), this
							.getPositionOnScreen().getY(), this.getDimension().getWidth(),
					this.getDimension().getHeight(), 0, 0, image.getWidth(), image
							.getHeight());
			g.drawString("" + DungeonGame.getInstance().getRound(), this.position.getX() + this.getDimension()
					.getWidth() / 2, this
					.getPositionOnScreen().getY() + this.getDimension().getHeight() / 2, paint);
		}
	}

}
