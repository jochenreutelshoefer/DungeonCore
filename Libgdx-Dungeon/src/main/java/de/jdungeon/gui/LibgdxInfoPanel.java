package de.jdungeon.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dungeon.DoorInfo;
import dungeon.JDPoint;
import dungeon.util.RouteInstruction;
import figure.FigureInfo;
import graphics.ImageManager;
import gui.Paragraph;
import gui.Paragraphable;
import item.ItemInfo;
import shrine.ShrineInfo;
import util.JDColor;
import util.JDDimension;

import de.jdungeon.app.gui.ColorConverter;
import de.jdungeon.app.gui.GUIImageManager;
import de.jdungeon.app.gui.InventoryImageManager;
import de.jdungeon.app.gui.activity.DefaultActivity;
import de.jdungeon.app.gui.skillselection.SkillImageManager;
import de.jdungeon.asset.Assets;
import de.jdungeon.game.Color;
import de.jdungeon.game.Game;
import de.jdungeon.libgdx.LibgdxGraphics;
import de.jdungeon.ui.LibgdxSlidingGUIElement;
import de.jdungeon.util.PaintBuilder;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 10.02.20.
 */
public class LibgdxInfoPanel extends LibgdxSlidingGUIElement {

	private final InventoryImageManager inventoryImageManager;
	private final SkillImageManager skillImageManager;
	private final GUIImageManager guiImageManager;

	public LibgdxInfoPanel(JDPoint position, JDDimension dimension, GUIImageManager guiImageManager, Game game) {
		super(position, dimension, new JDPoint(position.getX()
				+ dimension.getWidth() - 10, position.getY()), game);
		inventoryImageManager = new InventoryImageManager(guiImageManager);
		skillImageManager = new SkillImageManager(guiImageManager);
		this.guiImageManager = guiImageManager;
	}

	private Paragraphable content;

	private boolean visible = true;
	private float timer = 0;
	private final static float DISPLAY_TIME = 1000f;
	private final static int SLIDE_OUT_STEPS = 20;

	public void setContent(Paragraphable entity) {
		this.content = entity;
		visible = true;
		if (entity == null) {
			timer = 100f;
		}
		else {
			timer = DISPLAY_TIME;
		}
	}

	public Paragraphable getContent() {
		return content;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void paint(ShapeRenderer shapeRenderer) {
		// noting to do here
	}

	@Override
	public boolean handlePanEvent(float x, float y, float dx, float dy) {
		if (dx < 0) {
			timer = 0;
			return true;
		}
		return false;
	}

	@Override
	public boolean handleClickEvent(int x, int y) {
		return false;
	}

	@Override
	public void paint(SpriteBatch batch) {

		/*
		 * draw background
		 */
		int x = getCurrentX();
		this.drawBackground(batch, x, position.getY());

		String im = getImage();
		if (im != null) {
			TextureAtlas.AtlasRegion atlasRegion = Assets.instance.findTexture(im);
			if(atlasRegion != null) {
				batch.draw(atlasRegion, getCurrentX() + 2, position.getY() + 20, 60, 60);
			}
		}

		/*
		 * print information
		 */
		if (this.content != null) {
			Paragraph[] paragraphs = this.content.getParagraphs();
			if (paragraphs != null) {
				int posCounterY = 35;
				boolean first = true;
				for (Paragraph paragraph : paragraphs) {
					// TODO: refactor Paragraph thing...
					JDColor color = paragraph.getColor();
					if (first) {
						first = false;
						color = JDColor.WHITE;
					}

					PaintBuilder paintBuilder = new PaintBuilder();
					paintBuilder.setColor(ColorConverter.getColor(color));
					paintBuilder.setAlignment(de.jdungeon.game.Paint.Alignment.CENTER);
					paintBuilder.setFontSize(13);
					String text = paragraph.getText();
					if (text == null) {
						text = "null";
					}

					BitmapFont defaultSmallFlipped = Assets.instance.fonts.defaultSmallFlipped;
					com.badlogic.gdx.graphics.Color oldColor = defaultSmallFlipped.getColor();
					Color paintBuilderColor = paintBuilder.getColor();
					com.badlogic.gdx.graphics.Color gdxColor = LibgdxGraphics.colorMap.get(paintBuilderColor);
					if (gdxColor != null) {
						defaultSmallFlipped.setColor(gdxColor);
					}
					//fpsFont.setColor(textpaint.getFont().getColor());
					defaultSmallFlipped.draw(batch, text, x + (this.dimension.getWidth() / 3),
							position.getY() + posCounterY);
					if (gdxColor != null) {
						defaultSmallFlipped.setColor(oldColor);
					}
					posCounterY += 30;
				}
			}
		}
	}

	private String getImage() {
		if (content == null) {
			return null;
		}
		if (content instanceof DoorInfo) {
			return ImageManager.getImage((DoorInfo) content).getFilenameBlank();
		}
		if (content instanceof ShrineInfo) {
			return ImageManager.getImage((ShrineInfo) content).getFilenameBlank();
		}
		if (content instanceof FigureInfo) {
			try {
				if (((FigureInfo) content).isDead()) {
					return ImageManager.deathImage.getFilenameBlank();
				}
			}
			catch (NullPointerException e) {
				return null;
			}
			return ImageManager.getImage((FigureInfo) content, RouteInstruction.Direction.South).getFilenameBlank();
		}
		if (content instanceof ItemInfo) {
			String image = inventoryImageManager.getJDImage((ItemInfo) content).getFilenameBlank();
			if (image.equals(guiImageManager.getJDImage(GUIImageManager.NO_IMAGE).getFilenameBlank())) {
				image = ImageManager.getImage((ItemInfo) content).getFilenameBlank();
			}
			return image;
		}
		if (content instanceof DefaultActivity) {
			DefaultActivity activity = (DefaultActivity) content;
			if (activity.getObject() instanceof ItemInfo) {
				String image = inventoryImageManager.getJDImage(
						(ItemInfo) activity.getObject()).getFilenameBlank();
				if (image.equals(guiImageManager.getJDImage(
						GUIImageManager.NO_IMAGE).getFilenameBlank())) {
					image = ImageManager.getImage((ItemInfo) content).getFilenameBlank();
				}
				return image;
			}
			else {
				return skillImageManager.getImage(activity.getObject());
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
			}
			else {
				this.visible = false;
				this.slideStep = -1;
			}
		}
	}
}
