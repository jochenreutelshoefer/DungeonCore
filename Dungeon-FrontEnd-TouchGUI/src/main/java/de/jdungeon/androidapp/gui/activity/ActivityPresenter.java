package de.jdungeon.androidapp.gui.activity;

import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.androidapp.gui.AbstractGUIElement;
import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 25.01.18.
 */
public abstract class ActivityPresenter extends AbstractGUIElement {

	protected final ActivityProvider provider;
	protected boolean visible = true;
	protected boolean highlightOn = false;
	protected Activity markedActivity;

	protected final int backgroundPanelOffset = 7;

	protected int defaultImageWidth;
	protected int defaultImageHeight;

	protected  int defaultImageWidthHalf;
	protected  int defaultImageHeightHalf;
	protected  int doubleImageWidth;
	protected  int doubleImageHeight;
	protected  int doubleBackgroundPanelOffset;
	private final int doubleWidthPlusOffset;
	private final int doubleHeightPlusOffset;

	private final int positionCorrectionLarge;
	private final int positionCorrectionSmall;
	protected boolean positionCorrection = false;

	protected final Image itemBackgroundImage;


	protected final int screenWidth = getGame().getScreenWidth();
	protected final int screenHeight = getGame().getScreenHeight();


	public ActivityPresenter(JDPoint position, JDDimension dimension, StandardScreen screen, Game game, ActivityProvider provider, Image itemBg, int defaultTileSize) {
		super(position, dimension, screen, game);
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

	protected void drawHighlighting(Graphics g, int yMinusDefaultHeight, int xMinusDefaultWidth) {
		if(highlightOn) {
			g.drawOval(xMinusDefaultWidth, yMinusDefaultHeight, doubleImageWidth, doubleImageHeight, Colors.YELLOW);
		}
	}
	protected void drawActivityLarge(Graphics g, int x, int y, Activity activity) {
		Image im = provider.getActivityImage(activity);
		int posX = x;
		int posY = y;
		if(positionCorrection) {
			posX = x - positionCorrectionLarge;
			posY = y - positionCorrectionLarge;
		}
					/*
					draw highlighting circle
					 */
		drawHighlighting(g, posY, posX);

					/*
					 * draw background if existing
					 */
		drawActivityBackgroundLarge(g, posY, posX);

					/*
					 * draw actual item
					 */
		g.drawScaledImage(im, posX,
				posY, doubleImageWidth,
				doubleImageHeight, 0, 0, im.getWidth(),
				im.getHeight());
	}

	protected void drawActivity(Graphics g, int x, int y, Activity activity) {
		Image im = provider.getActivityImage(activity);

		int posX = x;
		int posY = y;
		if(positionCorrection) {
			posX = x - positionCorrectionSmall;
			posY = y - positionCorrectionSmall;
		}


					/*
					 * draw background if existing
					 */
		drawActivityBackground(g, posX, posY);

					/*
					 * draw actual item
					 */
		g.drawScaledImage(im, posX,
				posY, defaultImageWidth,
				defaultImageHeight, 0, 0, im.getWidth(),
				im.getHeight());
	}

	protected void drawActivityBackground(Graphics g, int xMinusDefaultWidthHalf, int yMinusDefaultHeightHalf) {
		if (itemBackgroundImage != null) {
			g.drawScaledImage(
					itemBackgroundImage,
					xMinusDefaultWidthHalf - backgroundPanelOffset,
					yMinusDefaultHeightHalf - backgroundPanelOffset,
					defaultImageWidth + doubleBackgroundPanelOffset,
					defaultImageHeight + doubleBackgroundPanelOffset,
					0, 0,
					itemBackgroundImage.getWidth(),
					itemBackgroundImage.getHeight());
		}
	}

	protected void drawActivityBackgroundLarge(Graphics g, int yMinusDefaultHeight, int xMinusDefaultWidth) {
		if (itemBackgroundImage != null) {
			g.drawScaledImage(
					itemBackgroundImage,
					xMinusDefaultWidth - backgroundPanelOffset,
					yMinusDefaultHeight - backgroundPanelOffset,
					doubleWidthPlusOffset,
					doubleHeightPlusOffset,
					0, 0,
					itemBackgroundImage.getWidth(),
					itemBackgroundImage.getHeight());
		}
	}



}
