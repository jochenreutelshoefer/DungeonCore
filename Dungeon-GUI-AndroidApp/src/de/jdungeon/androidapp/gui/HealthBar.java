package de.jdungeon.androidapp.gui;

import util.JDDimension;
import de.jdungeon.androidapp.GameScreen;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import dungeon.JDPoint;
import figure.attribute.Attribute;
import figure.hero.HeroInfo;
import graphics.ImageManager;
import graphics.JDImageProxy;

public class HealthBar extends AbstractGUIElement {

	private final HeroInfo figure;
	private final Kind kind;
	
	public enum Kind {
		health, dust
	};

	public HealthBar(JDPoint position, JDDimension dimension, HeroInfo info,
			Kind kind, GameScreen screen) {
		super(position, dimension, screen);
		this.figure = info;
		this.kind = kind;
	}

	@Override
	public boolean isVisible() {
		return true;
	}



	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {

		double baseValue = 1;
		double actualValue = 1;
		Image coloredBar = null;
		if (kind == Kind.health) {
			baseValue = figure.getAttributeBasic(Attribute.HEALTH);
			actualValue = figure.getAttributeValue(Attribute.HEALTH);
			coloredBar = (Image) ImageManager.health_bar_red.getImage();

		} else if (kind == Kind.dust) {
			baseValue = figure.getAttributeBasic(Attribute.DUST);
			actualValue = figure.getAttributeValue(Attribute.DUST);
			coloredBar = (Image) ImageManager.health_bar_yellow.getImage();
		}

		int totalWidthPixels = this.getDimension().getWidth();
		double percentage = (actualValue / baseValue);
		int coloredWidthPixels = (int) (percentage * totalWidthPixels);

		JDImageProxy<?> background = ImageManager.health_bar_empty;
		Image image = (Image) background.getImage();
		g.drawScaledImage(image, position.getX(), position.getY(),
				dimension.getWidth(),
				dimension.getHeight(), 0, 0, image.getWidth(),
				image.getHeight());

		g.drawScaledImage(coloredBar, position.getX(), position.getY(),
				coloredWidthPixels, dimension.getHeight(), 0, 0,
				(int) (coloredBar.getWidth() * percentage),
				coloredBar.getHeight());

	}


}
