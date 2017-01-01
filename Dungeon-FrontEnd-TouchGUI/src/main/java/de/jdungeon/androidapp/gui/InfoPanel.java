package de.jdungeon.androidapp.gui;

import dungeon.DoorInfo;
import dungeon.JDPoint;
import dungeon.util.RouteInstruction;
import figure.FigureInfo;
import graphics.ImageManager;
import graphics.JDImageProxy;
import gui.Paragraph;
import gui.Paragraphable;
import item.ItemInfo;
import shrine.ShrineInfo;
import util.JDDimension;

import de.jdungeon.androidapp.gui.itemWheel.ItemWheelActivity;
import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input.TouchEvent;
import de.jdungeon.game.PaintBuilder;
import de.jdungeon.game.ScrollMotion;

public class InfoPanel extends SlidingGUIElement {

	private final InventoryImageManager inventoryImageManager;
	private final SkillImageManager skillImageManager;

	public InfoPanel(JDPoint position, JDDimension dimension, StandardScreen screen, Game game) {
		super(position, dimension, new JDPoint(position.getX()
				+ dimension.getWidth() - 10, position.getY()), screen, game);
		inventoryImageManager = new InventoryImageManager(screen.getGuiImageManager());
		skillImageManager = new SkillImageManager(screen.getGuiImageManager());

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
				dimension.getHeight(), Colors.GRAY);

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

					PaintBuilder paintBuilder = new PaintBuilder();
					paintBuilder.setColor(ColorConverter.getColor(paragraph.getColor()));
					paintBuilder.setAlignment(de.jdungeon.game.Paint.Alignment.CENTER);
					paintBuilder.setFontSize(13);
					// paragraph.getFont();
					String text = paragraph.getText();
					if(text == null) {
						text = "null";
					}
					g.drawString(text,
								x + (this.dimension.getWidth() / 2),
								position.getY() + posCounterY, g.createPaint(paintBuilder));
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
				if (((FigureInfo) content).isDead()) {
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
			Image image = inventoryImageManager.getImage((ItemInfo) content);
			if (image.equals(screen.getGuiImageManager().getImage(GUIImageManager.NO_IMAGE))) {
				image = (Image) ImageManager.getImage((ItemInfo) content)
						.getImage();
			}
			return image;
		}
		if (content instanceof ItemWheelActivity) {
			ItemWheelActivity activity = (ItemWheelActivity) content;
			if (activity.getObject() instanceof ItemInfo) {
				Image image = inventoryImageManager.getImage(
						(ItemInfo) activity.getObject());
				if (image.equals(screen.getGuiImageManager().getImage(
						GUIImageManager.NO_IMAGE))) {
					image = (Image) ImageManager.getImage((ItemInfo) content)
							.getImage();
				}
				return image;
			} else {
				return skillImageManager.getSkillImage(activity);

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