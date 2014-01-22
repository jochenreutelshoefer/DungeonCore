package de.jdungeon.androidapp.gui.itemWheel;

import util.JDDimension;
import android.graphics.Color;
import android.graphics.Paint;
import de.jdungeon.androidapp.GameScreen;
import de.jdungeon.androidapp.gui.AbstractGUIElement;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input.TouchEvent;
import de.jdungeon.util.FloatDimension;
import de.jdungeon.util.ScrollMotion;
import dungeon.JDPoint;
import figure.hero.HeroInfo;

public class ItemWheel extends AbstractGUIElement {

	private static final double PI_EIGHTEENTH = Math.PI / 18;
	private static final double TWO_PI = Math.PI * 2;
	private final JDPoint[] points = new JDPoint[36];
	private float rotationState = (float) TWO_PI;
	private final int radius;
	private int markedPointIndex;
	private final Image backgroundImage;
	private final ItemWheelBindingSet binding;
	private boolean justRotated = true;
	private final int defaultImageWidth = 50;
	private final int defaultImageHeight = 50;
	private final int defaultImageWidthHalf = defaultImageWidth / 2;
	private final int defaultImageHeightHalf = defaultImageHeight / 2;
	private final int doubleImageWidth = defaultImageWidth * 2;
	private final int doubleImageHeight = defaultImageHeight * 2;
	private final int backgroundPanelOffset = 7;
	private final int doubleBackgroundPanelOffset = 2 * backgroundPanelOffset;
	private final int screenWidth = screen.getScreenSize().getWidth();
	private final int screenHeight = screen.getScreenSize().getHeight();
	private final int screenPlusDefaultImageWidth = screenWidth
			+ defaultImageWidth;
	private final int screenPlusDefaultImageHeight = screenHeight
			+ defaultImageHeight;
	private final int doubleWidthPlusOffset = doubleImageWidth
			+ doubleBackgroundPanelOffset;
	private final int doubleHeightPlusOffset = doubleImageHeight
			+ doubleBackgroundPanelOffset;

	public ItemWheel(JDPoint position, JDDimension dim, HeroInfo info,
			GameScreen screen, ItemWheelBindingSet binding, int selectedIndex,
			Image background) {
		super(position, dim, screen);
		radius = dimension.getWidth();
		info.getSpellBuffer();
		this.binding = binding;
		markedPointIndex = selectedIndex;
		this.backgroundImage = background;

		/*
		 * init points
		 */
		for (int i = 0; i < points.length; i++) {
			double degreeRad = i * PI_EIGHTEENTH + rotationState;
			int x = (int) (this.position.getX() + (Math.sin(degreeRad) * radius));
			int y = (int) (this.position.getY() + (Math.cos(degreeRad) * radius));
			points[i] = new JDPoint(x, y);
		}
	}

	@Override
	public boolean isVisible() {
		return true;
	}

	@Override
	public void handleScrollEvent(ScrollMotion scrolling) {
		FloatDimension movement = scrolling.getMovement();
		float movementX = movement.getX();
		changeRotation(movementX / 500);
		justRotated = true;
	}

	@Override
	public void handleTouchEvent(TouchEvent touch) {
		for (int i = 0; i < points.length; i++) {
			double distance = Math.hypot(touch.x - points[i].getX(), touch.y
					- points[i].getY());
			if (distance < 25) {
				iconTouched(i);
				break;
			}
		}
		super.handleTouchEvent(touch);
	}

	private void iconTouched(int i) {
		if (i == markedPointIndex) {
			ItemWheelActivity infoEntity = binding.getActivity(i);
			binding.getProvider().activityPressed(infoEntity);
		} else {
			centerOnIndex(i);
		}
	}

	private void centerOnIndex(int i) {
		int previousIndex = this.markedPointIndex;
		int diff = previousIndex - i;
		rotationState = (float) (rotationState + (diff * PI_EIGHTEENTH));
		setMarkedIndex(i);
	}

	private void changeRotation(float rotationChange) {

		this.rotationState += rotationChange;

	}

	private void setMarkedIndex(int i) {
		markedPointIndex = i;
		ItemWheelActivity activity = binding.getActivity(markedPointIndex);
		if (activity != null) {
			this.getScreen().setInfoEntity(activity);
		}
	}

	@Override
	public boolean hasPoint(JDPoint p) {
		int diffX = p.getX() - this.position.getX();
		int diffY = p.getY() - this.position.getY();
		double hypot = Math.hypot(diffX, diffY);
		return hypot < radius + 50;
	}

	@Override
	public void update(float time) {

		if (justRotated) {

			if (rotationState >= TWO_PI) {
				rotationState = (float) ((rotationState) % TWO_PI);
			}
			for (int i = 0; i < points.length; i++) {
				double degreeRad = i * PI_EIGHTEENTH + rotationState;
				int x = (int) (this.position.getX() + (Math.sin(degreeRad) * radius));
				int y = (int) (this.position.getY() + (Math.cos(degreeRad) * radius));
				points[i].setX(x);
				points[i].setY(y);

			}
			justRotated = false;
		}

		binding.update(time);
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		g.drawOval(position.getX(), position.getY(), dimension.getWidth(),
				dimension.getHeight(), Color.BLUE);
		for (int i = 0; i < points.length; i++) {
			int toDraw = (markedPointIndex + i + 1) % points.length;
			int x = points[toDraw].getX();
			int y = points[toDraw].getY();
			if (x > screenPlusDefaultImageWidth || x < 0 - doubleImageWidth
					|| y > screenPlusDefaultImageHeight
					|| y < 0 - doubleImageHeight) {
				continue;
			}
			ItemWheelActivity activity = this.binding.getActivity(toDraw);
			if (activity != null) {
				Image im = binding.getProvider().getActivityImage(activity);
				if (im == null) {
					System.out.println("Activity image is null: "
							+ activity.toString());
				}
				int yMinusDefaultHeight = y - defaultImageHeight;
				int xMinusDefaultWidth = x - defaultImageWidth;
				int xMinusDefaultWidthHalf = x - defaultImageWidthHalf;
				int yMinusDefaultHeightHalf = y - defaultImageHeightHalf;
				if (toDraw == this.markedPointIndex) {
					/*
					 * draw background if existing
					 */
					if (backgroundImage != null) {
						g.drawScaledImage(backgroundImage, xMinusDefaultWidth
								- backgroundPanelOffset, yMinusDefaultHeight
								- backgroundPanelOffset, doubleWidthPlusOffset,
								doubleHeightPlusOffset, 0, 0,
								backgroundImage.getWidth(),
								backgroundImage.getHeight());
					}

					/*
					 * draw actual item
					 */
					g.drawScaledImage(im, xMinusDefaultWidth,
							yMinusDefaultHeight, doubleImageWidth,
							doubleImageHeight, 0, 0, im.getWidth(),
							im.getHeight());
				} else {

					/*
					 * draw background if existing
					 */
					if (backgroundImage != null) {
						g.drawScaledImage(
								backgroundImage,
								xMinusDefaultWidthHalf - backgroundPanelOffset,
								yMinusDefaultHeightHalf - backgroundPanelOffset,
								defaultImageWidth + doubleBackgroundPanelOffset,
								defaultImageHeight
										+ doubleBackgroundPanelOffset, 0, 0,
								backgroundImage.getWidth(),
								backgroundImage.getHeight());
					}

					/*
					 * draw actual item
					 */
					g.drawScaledImage(im, xMinusDefaultWidthHalf,
							yMinusDefaultHeightHalf, defaultImageWidth,
							defaultImageHeight, 0, 0, im.getWidth(),
							im.getHeight());
				}
			}

			/*
			 * dev mode only
			 */
			Paint numberPain = new Paint();
			numberPain.setColor(Color.YELLOW);
			g.drawString("" + toDraw, x, y, numberPain);

		}

	}
}
