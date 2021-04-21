package de.jdungeon.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import de.jdungeon.dungeon.ChestInfo;
import de.jdungeon.dungeon.DoorInfo;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.util.RouteInstruction;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.graphics.ImageManager;
import de.jdungeon.graphics.JDImageProxy;
import de.jdungeon.item.ItemInfo;
import de.jdungeon.location.LocationInfo;
import de.jdungeon.log.Log;
import de.jdungeon.util.JDColor;
import de.jdungeon.util.JDDimension;

import de.jdungeon.app.gui.ColorConverter;
import de.jdungeon.app.gui.GUIImageManager;
import de.jdungeon.app.gui.InventoryImageManager;
import de.jdungeon.app.gui.skillselection.SkillImageManager;
import de.jdungeon.asset.Assets;
import de.jdungeon.game.Color;
import de.jdungeon.game.LibgdxGraphics;
import de.jdungeon.util.PaintBuilder;
import de.jdungeon.util.Pair;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 10.02.20.
 */
public class LibgdxInfoPanel extends LibgdxSlidingInOutGUIElement {

	private static final String WINDOW_BUBBLE = "win-bubble";

	private final InventoryImageManager inventoryImageManager;
	private final SkillImageManager skillImageManager;
	private final GUIImageManager guiImageManager;
	private final TextureAtlas.AtlasRegion bubble;
	private final GlyphLayout layout;

	public LibgdxInfoPanel(JDPoint position, JDDimension dimension, GUIImageManager guiImageManager) {
		super(position, dimension, new JDPoint(position.getX()
				- dimension.getWidth() + 10, position.getY()));
		inventoryImageManager = new InventoryImageManager(guiImageManager);
		skillImageManager = new SkillImageManager(guiImageManager);
		this.guiImageManager = guiImageManager;
		layout = new GlyphLayout();

		// init border
		bubble = Assets.instance.getAtlasRegion(WINDOW_BUBBLE, Assets.instance.getGuiAtlas());
	}

	private Paragraphable content;

	public void setContent(Paragraphable entity) {
		this.content = entity;
		if (content == null) {
			slideOut();
		}
		else {
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
	public boolean isAnimated() {
		return true;
	}

	@Override
	public void paint(SpriteBatch batch, float deltaTime) {

		// trigger animation update
		super.paint(batch, deltaTime);

		/*
		 * draw background
		 */
		int x = getCurrentX();
		this.drawBackground(batch, x, position.getY());

		Pair<String, RenderInfo> im = getImage();
		if (im != null) {
			int bubbleSizeX = (int) (this.getDimension().getWidth() / 1.5);
			int bubbleSizeY = (int) (this.getDimension().getHeight() / 2);
			int bubblePosX = this.getCurrentX() + this.getDimension().getWidth() / 2 - bubbleSizeX / 2;
			int bubblePosY = this.position.getY() - bubbleSizeY / 4;
			batch.draw(bubble, bubblePosX, bubblePosY, 0, 0, bubbleSizeX, bubbleSizeY, 1f, 1f, 0);

			TextureAtlas.AtlasRegion atlasRegion = Assets.instance.findTexture(im.getA(), true);
			if (atlasRegion != null) {
				RenderInfo renderInfo = im.getB();
				int imageSize = (int) ((bubbleSizeX / 2.5) * renderInfo.getScaleFactor());
				int imagePosX = this.getCurrentX() + this.getDimension().getWidth() / 2 - imageSize / 2;
				int imagePosY = this.position.getY() - imageSize / 2 + bubbleSizeY / 10;
				//batch.draw(atlasRegion, imagePosX, imagePosY, imageSize, imageSize);
				float scaleY = 1.0f;
				if(renderInfo.isFlipY()) {
					scaleY = -1.0f;
					imagePosY += imageSize;
				}
				batch.draw(atlasRegion, imagePosX, imagePosY, 0, 0, imageSize, imageSize, 1f, scaleY, 0);
			}
		}

		/*
		 * print information
		 */
		if (this.content != null) {
			Paragraph[] paragraphs = this.content.getParagraphs();
			if (paragraphs != null) {
				int posCounterY = 35;
				int lineIndex = 0;
				for (Paragraph paragraph : paragraphs) {
					// TODO: refactor Paragraph thing...
					JDColor color = color = JDColor.WHITE;

					PaintBuilder paintBuilder = new PaintBuilder();
					paintBuilder.setColor(ColorConverter.getColor(color));
					paintBuilder.setAlignment(de.jdungeon.game.Paint.Alignment.CENTER);
					paintBuilder.setFontSize(13);
					String text = paragraph.getText();
					if (text == null) {
						text = "null";
					}

					BitmapFont font = Assets.instance.fonts.defaultSmallFlipped;
					com.badlogic.gdx.graphics.Color oldColor = font.getColor();
					Color paintBuilderColor = paintBuilder.getColor();
					com.badlogic.gdx.graphics.Color gdxColor = LibgdxGraphics.colorMap.get(paintBuilderColor);
					if (gdxColor != null) {
						font.setColor(gdxColor);
					}

					if(lineIndex == 0) {
						// this is the headline
						font = Assets.instance.fonts.defaultTitle;
					}
					layout.setText(font, text, com.badlogic.gdx.graphics.Color.WHITE, this.dimension.getWidth()*0.8f, Align.center, true);
					font.draw(batch, layout, x + this.dimension.getWidth() * 0.1f , position.getY() + (this.dimension.getHeight() / 6) + posCounterY);

					if (gdxColor != null) {
						font.setColor(oldColor);
					}
					posCounterY += 20;
					if(lineIndex == 1) {
						// more space after headline
						posCounterY += 10;
					}
					lineIndex++;
				}
			}
		}
	}

	static class RenderInfo {
		float scaleFactor;

		boolean flipY = false;
		public RenderInfo(float scaleFactor) {
			this.scaleFactor = scaleFactor;
		}

		public RenderInfo(float scaleFactor, boolean flipY) {
			this.scaleFactor = scaleFactor;
			this.flipY = flipY;
		}

		public float getScaleFactor() {
			return scaleFactor;
		}

		public boolean isFlipY() {
			return flipY;
		}
	}

	private Pair<String, RenderInfo> getImage() {
		if (content == null) {
			return null;
		}
		// wtf, I have no idea why some entities need to be flippedY to be shown correctly and others not...
		if (content instanceof DoorInfo) {
			return new Pair<>(ImageManager.getImage((DoorInfo) content).getFilenameBlank(), new RenderInfo(1.0f, true));
		}
		if (content instanceof LocationInfo) {
			return new Pair<>(ImageManager.getImage(((LocationInfo) content).getShrineClass()).getFilenameBlank(), new RenderInfo(1.0f));
		}
		if (content instanceof ChestInfo) {
			return new Pair<>(ImageManager.getImage((ChestInfo) content).getFilenameBlank(), new RenderInfo(1.0f, false));
		}
		if (content instanceof FigureInfo) {
			try {
				if (((FigureInfo) content).isDead()) {
					return new Pair<>(ImageManager.deathImage.getFilenameBlank(), new RenderInfo(1.5f, true));
				}
			}
			catch (NullPointerException e) {
				return null;
			}
			JDImageProxy<?> image = ImageManager.getImage((FigureInfo) content, RouteInstruction.Direction.South);
			if (image == null) {
				Log.severe("Image was null for de.jdungeon.figure: " + content + " dir: " + RouteInstruction.Direction.South.name());
				return null;
			}
			return new Pair<>(image.getFilenameBlank(), new RenderInfo(1.5f, true));
		}
		if (content instanceof ItemInfo) {
			String image = inventoryImageManager.getJDImage((ItemInfo) content).getFilenameBlank();
			if (image.equals(guiImageManager.getJDImage(GUIImageManager.NO_IMAGE).getFilenameBlank())) {
				image = ImageManager.getImage((ItemInfo) content).getFilenameBlank();
			}
			return new Pair<>(image, new RenderInfo(1.0f));
		}
		return null;
	}
}
