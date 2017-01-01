package de.jdungeon.androidapp.gui;

import graphics.ImageManager;
import graphics.JDImageProxy;
import util.JDDimension;

import de.jdungeon.androidapp.gui.GUIElement;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;

public class GUIUtils {

	public static void drawBackground(Graphics g, GUIElement e) {
		drawBackground(g, e.getPositionOnScreen().getX(), e
				.getPositionOnScreen().getY(), e.getDimension());
	}

	public static void drawBackground(Graphics g, int x, int posY,
			JDDimension dimension) {
		JDImageProxy<?> background = ImageManager.paperBackground;
		Image image = (Image) background.getImage();
		g.drawScaledImage(image, x, posY, dimension.getWidth(),
				dimension.getHeight(), 0, 0, image.getWidth(),
				image.getHeight());
	}

	public static void drawDoubleBorder(Graphics g, GUIElement e,
			int borderWidth) {
		drawDoubleBorder(g, e.getPositionOnScreen().getX(), e
				.getPositionOnScreen().getY(), e.getDimension(), borderWidth);
	}

	public static void drawDoubleBorder(Graphics g, int x, int posY,
			JDDimension dimension, int borderWidth) {

		g.drawScaledImage(
				(Image) ImageManager.border_double_left_upper_corner.getImage(),
				x, posY, borderWidth, borderWidth, 0, 0, 20, 20);
		g.drawScaledImage(
				(Image) ImageManager.border_double_left_lower_corner.getImage(),
				x, posY + dimension.getHeight() - borderWidth, borderWidth,
				borderWidth, 0, 0, 20, 20);
		g.drawScaledImage((Image) ImageManager.border_double_right_upper_corner
				.getImage(), x + dimension.getWidth() - borderWidth, posY,
				borderWidth, borderWidth, 0, 0, 20, 20);

		g.drawScaledImage((Image) ImageManager.border_double_right_lower_corner
				.getImage(), x + dimension.getWidth() - borderWidth, posY
				+ dimension.getHeight() - borderWidth, borderWidth,
				borderWidth, 0, 0, 20, 20);

		g.drawScaledImage((Image) ImageManager.border_double_top.getImage(), x
				+ borderWidth, posY, dimension.getWidth() - 40, borderWidth, 0,
				0, 72, 20);

		g.drawScaledImage((Image) ImageManager.border_double_left.getImage(),
				x, posY + borderWidth, borderWidth,
				dimension.getHeight() - 40 + 6, 0, 0, 20, 56);

		g.drawScaledImage((Image) ImageManager.border_double_bottom.getImage(),
				x + borderWidth, posY + dimension.getHeight() - borderWidth,
				dimension.getWidth() - 40, borderWidth, 0, 0, 72, 20);

		g.drawScaledImage((Image) ImageManager.border_double_right.getImage(),
				x + dimension.getWidth() - 20, posY + borderWidth, borderWidth,
				dimension.getHeight() - 40 + 4, 0, 0, 20, 56);
	}

}

