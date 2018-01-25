package de.jdungeon.androidapp.gui.activity;

import dungeon.JDPoint;
import util.JDDimension;

import de.jdungeon.androidapp.gui.AbstractGUIElement;
import de.jdungeon.androidapp.gui.GUIElement;
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

	protected final int defaultImageWidth = 50;
	protected final int defaultImageHeight = 50;
	protected final int defaultImageWidthHalf = defaultImageWidth / 2;
	protected final int defaultImageHeightHalf = defaultImageHeight / 2;
	protected final int doubleImageWidth = defaultImageWidth * 2;
	protected final int doubleImageHeight = defaultImageHeight * 2;
	protected final int backgroundPanelOffset = 7;
	protected final int doubleBackgroundPanelOffset = 2 * backgroundPanelOffset;
	protected final int screenWidth = getGame().getScreenWidth();
	protected final int screenHeight = getGame().getScreenHeight();
	private final int doubleWidthPlusOffset = doubleImageWidth
			+ doubleBackgroundPanelOffset;
	private final int doubleHeightPlusOffset = doubleImageHeight
			+ doubleBackgroundPanelOffset;


	protected final Image itemBackgroundImage;


	public ActivityPresenter(JDPoint position, JDDimension dimension, StandardScreen screen, Game game, ActivityProvider provider, Image itemBg) {
		super(position, dimension, screen, game);
		this.provider = provider;
		this.itemBackgroundImage = itemBg;
	}

	public ActivityPresenter(JDPoint position, JDDimension dimension, ActivityProvider provider, Image itemBg) {
		super(position, dimension);
		this.provider = provider;
		this.itemBackgroundImage = itemBg;
	}

	public ActivityPresenter(JDPoint position, JDDimension dimension, GUIElement parent, ActivityProvider provider, Image itemBg) {
		super(position, dimension, parent);
		this.provider = provider;
		this.itemBackgroundImage = itemBg;
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
		int yMinusDefaultHeight = y - defaultImageHeight;
		int xMinusDefaultWidth = x - defaultImageWidth;
					/*
					draw highlighting circle
					 */
		drawHighlighting(g, yMinusDefaultHeight, xMinusDefaultWidth);

					/*
					 * draw background if existing
					 */
		drawActivityBackgroundLarge(g, yMinusDefaultHeight, xMinusDefaultWidth);

					/*
					 * draw actual item
					 */
		g.drawScaledImage(im, xMinusDefaultWidth,
				yMinusDefaultHeight, doubleImageWidth,
				doubleImageHeight, 0, 0, im.getWidth(),
				im.getHeight());
	}

	protected void drawActivity(Graphics g, int x, int y, Activity activity) {
		Image im = provider.getActivityImage(activity);
		int xMinusDefaultWidthHalf = x - defaultImageWidthHalf;
		int yMinusDefaultHeightHalf = y - defaultImageHeightHalf;

					/*
					 * draw background if existing
					 */
		drawActivityBackground(g, xMinusDefaultWidthHalf, yMinusDefaultHeightHalf);

					/*
					 * draw actual item
					 */
		g.drawScaledImage(im, xMinusDefaultWidthHalf,
				yMinusDefaultHeightHalf, defaultImageWidth,
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
