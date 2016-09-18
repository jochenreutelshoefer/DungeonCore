package de.jdungeon.androidapp.gui;

import dungeon.util.RouteInstruction;
import figure.FigureInfo;
import graphics.ImageManager;
import graphics.JDImageProxy;
import gui.Paragraph;
import gui.Paragraphable;
import item.ItemInfo;
import shrine.ShrineInfo;
import util.JDDimension;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import de.jdungeon.androidapp.screen.GameScreen;
import de.jdungeon.androidapp.gui.itemWheel.ItemWheelActivity;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input.TouchEvent;
import de.jdungeon.util.ScrollMotion;
import dungeon.DoorInfo;
import dungeon.JDPoint;

public class InfoPanel extends SlidingGUIElement {

	public InfoPanel(JDPoint position, JDDimension dimension, GameScreen screen) {
		super(position, dimension, new JDPoint(position.getX()
				+ dimension.getWidth() - 10, position.getY()), screen);
	}

	private Paragraphable content;
	private boolean visible = true;
	private float timer = 0;
	private final static float DISPLAY_TIME = 1000f;
	private final static int SLIDE_OUT_STEPS = 20;

	public void setContent(Paragraphable entity) {
		this.content = entity;
		visible = true;
		timer = DISPLAY_TIME;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void handleScrollEvent(ScrollMotion scrolling) {
		if (scrolling.getMovement().getX() < 0) {
			timer = 0;
		}
	}

	@Override
	public void handleTouchEvent(TouchEvent touch) {
		setContent(content);
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		/*
		 * draw background
		 */
		int x = getCurrentX();
		g.drawRect(x, position.getY(), dimension.getWidth(),
				dimension.getHeight(), Color.GRAY);

		GUIUtils.drawBackground(g, x, position.getY(), dimension);

		Image im = getImage();
		if (im != null) {
			g.drawScaledImage(im, getCurrentX() + 20, position.getY() + 20, 60,
					60, 0, 0, im.getWidth(), im.getHeight());
		}

		/*
		 * print information
		 */
		if (this.content != null) {
			Paragraph[] paragraphs = this.content.getParagraphs();
			if (paragraphs != null) {
				int posCounterY = 35;
				for (Paragraph paragraph : paragraphs) {
					Paint p = new Paint();
					p.setColor(ColorConverter.getColor(paragraph.getColor()));
					p.setTextAlign(Align.CENTER);
					p.setStyle(p.getStyle());
					p.setTextSize(13);
					// paragraph.getFont();
					String text = paragraph.getText();
					if(text == null) {
						text = "null";
					}
					g.drawString(text,
								x + (this.dimension.getWidth() / 2),
								position.getY() + posCounterY, p);
					posCounterY += 30;
				}
			}
		}

		/*
		 * paint border
		 */
		GUIUtils.drawDoubleBorder(g, x, position.getY(), dimension, 20);

	}

	private Image getImage() {
		if (content == null)
			return null;
		if (content instanceof DoorInfo) {
			return (Image) ImageManager.getImage((DoorInfo) content).getImage();
		}
		if (content instanceof ShrineInfo) {
			return (Image) ImageManager.getImage((ShrineInfo) content)
					.getImage();
		}
		if (content instanceof FigureInfo) {
			try {
				if (content != null && ((FigureInfo) content).isDead()) {
					return (Image) ImageManager.deathImage.getImage();
				}
			} catch (NullPointerException e) {
				return null;
			}
			JDImageProxy<?> image = ImageManager.getImage((FigureInfo) content, RouteInstruction.Direction.South);
			return image != null ? (Image) image
					.getImage() : null;
		}
		if (content instanceof ItemInfo) {
			Image image = InventoryImageManager.getImage((ItemInfo) content,
					screen);
			if (image.equals(GUIImageManager.getImage(GUIImageManager.NO_IMAGE,
					screen.getGame()))) {
				image = (Image) ImageManager.getImage((ItemInfo) content)
						.getImage();
			}
			return image;
		}
		if (content instanceof ItemWheelActivity) {
			ItemWheelActivity activity = (ItemWheelActivity) content;
			if (activity.getObject() instanceof ItemInfo) {
				Image image = InventoryImageManager.getImage(
						(ItemInfo) activity.getObject(), screen);
				if (image.equals(GUIImageManager.getImage(
						GUIImageManager.NO_IMAGE, screen.getGame()))) {
					image = (Image) ImageManager.getImage((ItemInfo) content)
							.getImage();
				}
				return image;
			} else {
				return SkillImageManager.getSkillImage(activity, screen);

			}
		}
		return null;
	}

	@Override
	public void update(float time) {
		timer -= time;
		if (timer < 0 && visible) {
			if (this.slideStep == -1) {
				this.slideStep = SLIDE_OUT_STEPS;
			}
			if (slideStep > 0) {
				slideStep -= 1;
			} else {
				this.visible = false;
				this.slideStep = -1;
			}
		}
	}

}
