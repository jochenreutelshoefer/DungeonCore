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
	private final HeroInfo info;
	private final JDPoint[] points = new JDPoint[36];
	private float rotationState = (float) TWO_PI;
	private final int radius;
	private int markedPointIndex;
	private final int startPointerIndex;
	private int itemRotationPointer = 0;
	private final Image backgroundImage;
	ItemWheelBindingSet binding;

	public ItemWheel(JDPoint position, JDDimension dim, HeroInfo info,
			GameScreen screen, ItemWheelBindingSet binding, int selectedIndex,
			Image background) {
		super(position, dim, screen);
		this.info = info;
		radius = dimension.getWidth();
		info.getSpellBuffer();
		this.binding = binding;
		markedPointIndex = selectedIndex;
		startPointerIndex = selectedIndex;
		this.backgroundImage = background;
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
		int itemsRotated = (int) (rotationState / PI_EIGHTEENTH);
		if (itemsRotated != itemRotationPointer) {
			if (itemsRotated > itemRotationPointer) {
				int newMarkedPointIndex = (markedPointIndex - 1)
						% points.length;
				if (newMarkedPointIndex < 0) {
					newMarkedPointIndex += points.length;
				}
				setMarkedIndex(newMarkedPointIndex);
			}
			if (itemsRotated < itemRotationPointer) {
				setMarkedIndex((markedPointIndex + 1) % points.length);
				
			}

			itemRotationPointer = itemsRotated;
		}
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
		return hypot < radius + 100;
	}

	@Override
	public void update(float time) {

		if (rotationState >= TWO_PI) {
			rotationState = (float) ((rotationState) % TWO_PI);
		}
		for (int i = 0; i < points.length; i++) {
			double degreeRad = i * PI_EIGHTEENTH + rotationState;
			int x = (int) (this.position.getX() + (Math.sin(degreeRad) * radius));
			int y = (int) (this.position.getY() + (Math.cos(degreeRad) * radius));
			points[i] = new JDPoint(x, y);

		}

		binding.update(time);
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		g.drawOval(position.getX(), position.getY(), dimension.getWidth(),
				dimension.getHeight(), Color.BLUE);

		for (int i = 0; i < points.length; i++) {
			int imageWidth = 50;
			int imageHeight = 50;
			int toDraw = (markedPointIndex + i + 1) % points.length;
			ItemWheelActivity activity = this.binding.getActivity(toDraw);
			if (activity != null) {
				Image im = binding.getProvider().getActivityImage(activity);
				if (im == null) {
					System.out.println("Actiivty image is null: "
							+ activity.toString());
				}
				if (toDraw == this.markedPointIndex) {
					imageWidth *= 2;
					imageHeight *= 2;
				}

				/*
				 * draw background if existing
				 */
				int offset = 7;
				if (backgroundImage != null) {
					g.drawScaledImage(backgroundImage, points[toDraw].getX()
							- imageWidth / 2 - offset, points[toDraw].getY()
							- imageHeight / 2 - offset,
							imageWidth + 2 * offset, imageHeight + 2 * offset,
							0, 0,
							backgroundImage.getWidth(),
							backgroundImage.getHeight());
				}

				/*
				 * draw actual item
				 */
				g.drawScaledImage(im, points[toDraw].getX() - imageWidth / 2,
						points[toDraw].getY() - imageHeight / 2, imageWidth,
						imageHeight, 0, 0, im.getWidth(), im.getHeight());
			}

			/*
			 * dev mode only
			 */
			Paint numberPain = new Paint();
			numberPain.setColor(Color.YELLOW);
			g.drawString("" + toDraw, points[toDraw].getX(),
					points[toDraw].getY(), numberPain);

		}

	}

}
