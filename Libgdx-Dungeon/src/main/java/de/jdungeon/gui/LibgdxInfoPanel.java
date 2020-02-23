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
import graphics.JDImageProxy;
import gui.Paragraph;
import gui.Paragraphable;
import item.ItemInfo;
import log.Log;
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
import de.jdungeon.libgdx.LibgdxGraphics;
import de.jdungeon.util.PaintBuilder;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 10.02.20.
 */
public class LibgdxInfoPanel extends LibgdxSlidingGUIElement {

	private static final String WINDOW_BUBBLE = "win-bubble";

	private final InventoryImageManager inventoryImageManager;
	private final SkillImageManager skillImageManager;
	private final GUIImageManager guiImageManager;
	private final TextureAtlas.AtlasRegion bubble;

	public LibgdxInfoPanel(JDPoint position, JDDimension dimension, GUIImageManager guiImageManager) {
		super(position, dimension, new JDPoint(position.getX()
				- dimension.getWidth() + 10, position.getY()));
		inventoryImageManager = new InventoryImageManager(guiImageManager);
		skillImageManager = new SkillImageManager(guiImageManager);
		this.guiImageManager = guiImageManager;

		// init border
		bubble = Assets.instance.getAtlasRegion(WINDOW_BUBBLE, Assets.instance.getGuiAtlas());
	}

	private Paragraphable content;


	public void setContent(Paragraphable entity) {
		this.content = entity;
		if(content == null) {
			slideOut();
		} else {
			slideIn();
		}
	}

	public Paragraphable getContent() {
		return content;
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void paint(ShapeRenderer shapeRenderer) {
		// noting to do here
	}

	/*
	@Override
	public boolean handlePanEvent(float x, float y, float dx, float dy) {
		if (dx < 0) {
			timer = 0;
			return true;
		}
		return false;
	}
	/*

	/*
	@Override
	public boolean handleClickEvent(int x, int y) {
		return false;
	}
*/
	@Override
	public void paint(SpriteBatch batch) {

		/*
		 * draw background
		 */
		int x = getCurrentX();
		this.drawBackground(batch, x, position.getY());

		String im = getImage();
		if (im != null) {
			int bubbleSizeX = (int) (this.getDimension().getWidth() / 1.5);
			int bubbleSizeY = (int) (this.getDimension().getHeight() / 2);
			int bubblePosX = this.getCurrentX() + this.getDimension().getWidth() / 2 - bubbleSizeX / 2;
			int bubblePosY = this.position.getY() - bubbleSizeY / 4;
			batch.draw(bubble,  bubblePosX, bubblePosY, 0 , 0 , bubbleSizeX, bubbleSizeY, 1f , 1f, 0);

			TextureAtlas.AtlasRegion atlasRegion = Assets.instance.findTexture(im);
			if(atlasRegion != null) {
				int imageSize = (int) (bubbleSizeX / 2.5);
				int imagePosX = this.getCurrentX()  + this.getDimension().getWidth() / 2 - imageSize / 2;
				int imagePosY = this.position.getY() - imageSize / 2 + bubbleSizeY / 10;
				batch.draw(atlasRegion, imagePosX, imagePosY, imageSize, imageSize);
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
			JDImageProxy<?> image = ImageManager.getImage((FigureInfo) content, RouteInstruction.Direction.South);
			if(image == null) {
				Log.severe("Image was null for figure: "+ content + " dir: "+ RouteInstruction.Direction.South.name());
				return null;
			}
			return image.getFilenameBlank();
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

	/*
	@Override
	public void update(float time) {
		timer -= time;
		if (timer < 0 && visible) {

			if (this.slideStepCounter == -1) {
				this.slideStepCounter = SLIDING_STEPS;
			}

			if (slideStepCounter > 0) {
				slideStepCounter -= 1;
			}
			else {
				//this.visible = false;
				this.slideStepCounter = -1;
			}
		}
	}
	*/
}
