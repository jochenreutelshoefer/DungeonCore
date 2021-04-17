package de.jdungeon.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.util.JDDimension;

import de.jdungeon.app.gui.GUIImageManager;
import de.jdungeon.gui.activity.Activity;
import de.jdungeon.asset.Assets;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 09.02.20.
 */
public abstract class LibgdxActivityPresenter extends LibgdxContainerGUIElement {

	protected final LibgdxActivityProvider provider;
	protected boolean visible = true;
	protected boolean highlightOn = false;
	protected Activity markedActivity;

	protected final int backgroundPanelOffset = 7;

	protected int defaultImageWidth;
	protected int defaultImageHeight;

	protected int defaultImageWidthHalf;
	protected int defaultImageHeightHalf;
	protected int doubleImageWidth;
	protected int doubleImageHeight;
	protected int doubleBackgroundPanelOffset;
	private final int doubleWidthPlusOffset;
	private final int doubleHeightPlusOffset;

	private final int positionCorrectionLarge;
	private final int positionCorrectionSmall;
	// TODO : rework this thing
	protected boolean positionCorrection = false;

	protected final String itemBackgroundImage;

	public LibgdxActivityPresenter(JDPoint position, JDDimension dimension, LibgdxActivityProvider provider, String itemBg, int defaultTileSize) {
		super(position, dimension);
		this.provider = provider;
		this.itemBackgroundImage = itemBg;
		defaultImageWidth = defaultTileSize;
		defaultImageHeight = defaultTileSize;
		defaultImageWidthHalf = defaultImageWidth / 2;
		defaultImageHeightHalf = defaultImageHeight / 2;
		doubleImageWidth = defaultImageWidth * 2;
		doubleImageHeight = defaultImageHeight * 2;
		doubleBackgroundPanelOffset = 2 * backgroundPanelOffset;
		doubleWidthPlusOffset = doubleImageWidth
				+ doubleBackgroundPanelOffset;
		doubleHeightPlusOffset = doubleImageHeight
				+ doubleBackgroundPanelOffset;

		positionCorrectionLarge = defaultImageHeight;
		positionCorrectionSmall = defaultImageHeightHalf;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public abstract void highlightEntity(Object object);

	public abstract Object highlightFirst();

	protected abstract void centerOnIndex(Activity activity);

	public void setHighlightOn(boolean val) {
		this.highlightOn = val;
	}

	protected void iconTouched(Activity activity) {
		if (activity == markedActivity) {
			provider.activityPressed(activity);
		}
		else {
			markedActivity = activity;
			centerOnIndex(activity);
		}
	}

	private void drawHighlighting(SpriteBatch batch, int yMinusDefaultHeight, int xMinusDefaultWidth) {
		if (highlightOn) {
			TextureAtlas.AtlasRegion atlasRegion = Assets.instance.getAtlasRegion(GUIImageManager.CIRCLE_HIGHLIGHT, Assets.instance.getGuiAtlas());
			batch.draw(atlasRegion, xMinusDefaultWidth, yMinusDefaultHeight, doubleImageWidth, doubleImageHeight);
		}
	}

	protected void drawActivityLarge(SpriteBatch batch, int x, int y, Activity activity) {
		String im = provider.getActivityImage(activity);
		int posX = x;
		int posY = y;
		if (positionCorrection) {
			posX = x - positionCorrectionLarge;
			posY = y - positionCorrectionLarge;
		}
					/*
					draw highlighting circle
					 */
		drawHighlighting(batch, posY, posX);

		/*
		 * draw background if existing
		 */
		drawActivityBackgroundLarge(batch, posY, posX);

		/*
		 * draw actual de.jdungeon.item
		 */
		TextureAtlas.AtlasRegion atlasRegion = Assets.instance.getAtlasRegion(im, Assets.instance.getGuiAtlas());
		batch.draw(atlasRegion, posX + doubleImageWidth / 8,
				posY + doubleImageWidth / 8, doubleImageWidth * 3 / 4,
				doubleImageHeight * 3 / 4);
	}

	public void drawActivityAbsolute(SpriteBatch batch, int x, int y, Activity activity) {
		String im = provider.getActivityImage(activity);

		int posX = x;
		int posY = y;
		if (positionCorrection) {
			posX = x - positionCorrectionSmall;
			posY = y - positionCorrectionSmall;
		}


		/*
		 * draw background if existing
		 */
		drawActivityBackground(batch, posX, posY);

		/*
		 * draw actual item
		 */
		TextureAtlas.AtlasRegion atlasRegion = Assets.instance.getAtlasRegion(im, Assets.instance.getGuiAtlas());
		batch.draw(atlasRegion,
				posX + defaultImageWidth / 8,
				posY + defaultImageWidth / 8,
				defaultImageWidth * 3 / 4,
				defaultImageHeight * 3 / 4);
	}

	public void drawActivityBackground(SpriteBatch batch, int xMinusDefaultWidthHalf, int yMinusDefaultHeightHalf) {
		if (itemBackgroundImage != null) {
			TextureAtlas.AtlasRegion atlasRegion = Assets.instance.getAtlasRegion(itemBackgroundImage, Assets.instance.getGuiAtlas());
			batch.draw(atlasRegion,
					xMinusDefaultWidthHalf - backgroundPanelOffset,
					yMinusDefaultHeightHalf - backgroundPanelOffset,
					defaultImageWidth + doubleBackgroundPanelOffset,
					defaultImageHeight + doubleBackgroundPanelOffset);
		}
	}

	public void drawActivityBackgroundLarge(SpriteBatch batch, int yMinusDefaultHeight, int xMinusDefaultWidth) {
		if (itemBackgroundImage != null) {
			TextureAtlas.AtlasRegion atlasRegion = Assets.instance.getAtlasRegion(itemBackgroundImage, Assets.instance.getGuiAtlas());
			batch.draw(atlasRegion,
					xMinusDefaultWidth - backgroundPanelOffset,
					yMinusDefaultHeight - backgroundPanelOffset,
					doubleWidthPlusOffset,
					doubleHeightPlusOffset);
		}
	}

	public Activity getSelected() {
		return markedActivity;
	}
}
