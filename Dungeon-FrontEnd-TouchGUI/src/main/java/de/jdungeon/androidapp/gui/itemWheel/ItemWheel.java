package de.jdungeon.androidapp.gui.itemWheel;

import java.util.Collection;
import java.util.HashSet;

import event.EventManager;
import util.JDDimension;

import de.jdungeon.androidapp.event.ShowInfoEntityEvent;
import de.jdungeon.androidapp.gui.AbstractGUIElement;
import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input.TouchEvent;
import de.jdungeon.game.ScrollMotion;
import de.jdungeon.util.PaintBuilder;

import dungeon.JDPoint;
import figure.hero.HeroInfo;

public class ItemWheel extends AbstractGUIElement {

	private static final double PI_EIGHTEENTH = Math.PI / 18;
	private static final double TWO_PI = Math.PI * 2;
	private final JDPoint[] points = new JDPoint[36];
	private final int hightlightItemPosition;
	private float currentRotationState = (float) TWO_PI;
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
	private final int screenWidth = getGame().getScreenWidth();
	private final int screenHeight = getGame().getScreenHeight();
	private final int screenPlusDefaultImageWidth = screenWidth
			+ defaultImageWidth;
	private final int screenPlusDefaultImageHeight = screenHeight
			+ defaultImageHeight;
	private final int doubleWidthPlusOffset = doubleImageWidth
			+ doubleBackgroundPanelOffset;
	private final int doubleHeightPlusOffset = doubleImageHeight
			+ doubleBackgroundPanelOffset;

	private float timer = 0;
	private float velocity = 0;
	private float startVelocity = 0;
	private final float maxVelocity = 50;


	private boolean visible = true;

	public ItemWheel(JDPoint position, JDDimension dim, HeroInfo info,
					 StandardScreen screen, Game game, ItemWheelBindingSet binding, int selectedIndex,
					 Image background) {
		super(position, dim, screen, game);
		this.hightlightItemPosition = selectedIndex;
		radius = dimension.getWidth();
		info.getSpellBuffer();
		this.binding = binding;
		markedPointIndex = selectedIndex;
		this.backgroundImage = background;

		/*
		 * init points
		 */
		for (int i = 0; i < points.length; i++) {
			double degreeRad = i * PI_EIGHTEENTH + currentRotationState;
			int x = calcXCoordinate(degreeRad);
			int y = calcYCoordinate(degreeRad);
			points[i] = new JDPoint(x, y);
		}
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}


	long lastEvent =0;

	@Override
	public void handleScrollEvent(ScrollMotion scrolling) {
		long timeSinceLastEvent = System.currentTimeMillis() - lastEvent;
		ScrollMotion.FloatDimension movement = scrolling.getMovement();
		float movementX = movement.getX();
		float rotation = movementX / 400;

		// instant rotation
		changeRotation(rotation);

		float initialRotationVelocity = movementX / 50;
		if (initialRotationVelocity > maxVelocity) {
			velocity = maxVelocity;
		} else {
			velocity = initialRotationVelocity;
		}
		startVelocity = velocity;
		justRotated = true;

	}

	@Override
	public boolean handleTouchEvent(TouchEvent touch) {
		for (int i = 0; i < points.length; i++) {
			double distance = Math.hypot(touch.x - points[i].getX(), touch.y
					- points[i].getY());
			if (distance < 25) {
				iconTouched(i);
				break;
			}
		}
		velocity = 0;
		return super.handleTouchEvent(touch);
	}

	private void iconTouched(int i) {
		if (i == markedPointIndex) {
			ItemWheelActivity infoEntity = binding.getActivity(i);
			if(infoEntity != null) {
				binding.getProvider().activityPressed(infoEntity);
			}
		} else {
			centerOnIndex(i);
		}
	}

	private void centerOnIndex(int i) {
		setMarkedIndex(i);
	}

	private synchronized void changeRotation(float rotationChange) {
		this.currentRotationState += rotationChange;
		updatePointCoordinates();
	}

	private void setMarkedIndex(int i) {
		// set touched element as highlighted element
		markedPointIndex = i;

		// scroll element to center position
		int diff = hightlightItemPosition - i;
		this.currentRotationState = (float) PI_EIGHTEENTH * diff;
		updatePointCoordinates();

		// show info about element
		ItemWheelActivity activity = binding.getActivity(markedPointIndex);
		if (activity != null) {
			EventManager.getInstance().fireEvent(new ShowInfoEntityEvent(activity));
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
		timer += time;
		if (justRotated) {
			/*
			 * calc user motion
			 */
			updatePointCoordinates();
			justRotated = false;
		} else {
			/*
			 * calc movement due to inertia-velocity
			 */
			if (Math.abs(velocity) > 0) {
				if (timer > 100f) {
					// slow down on 70% velocity every 100ms
					velocity *= 0.7;
					timer = 0;
				}
				changeRotation(velocity/10);

				// at some very low velocity -> stop
				if (Math.abs(velocity) < 0.07) {
					velocity = 0;
				}
			}
		}
		binding.update(time);
	}

	private void updatePointCoordinates() {
		for (int i = 0; i < points.length; i++) {
			double degreeRad = i * PI_EIGHTEENTH + currentRotationState;
			int x = calcXCoordinate(degreeRad);
			int y = calcYCoordinate(degreeRad);
			points[i].setX(x);
			points[i].setY(y);

		}
	}

	private int calcYCoordinate(double degreeRad) {
		return (int) (this.position.getY() + (Math.cos(degreeRad) * radius));
	}

	private int calcXCoordinate(double degreeRad) {
		return (int) (this.position.getX() + (Math.sin(degreeRad) * radius));
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		g.drawOval(position.getX(), position.getY(), dimension.getWidth(),
				dimension.getHeight(), Colors.BLUE);
		this.binding.getSize();
		for (int i = 0; i < points.length; i++) {
			int toDraw = (markedPointIndex + i + 1) % points.length;
			int x = points[toDraw].getX();
			int y = points[toDraw].getY();


			/*
			PaintBuilder numberPaint = new PaintBuilder();
			numberPaint.setColor(Colors.RED);
			//g.drawOval(x, y, 2, 2, Colors.GRAY);
			g.drawString("" + toDraw, x, y, g.createPaint(numberPaint) );
			*/

			if (x > screenPlusDefaultImageWidth || x < 0 - doubleImageWidth
					|| y > screenPlusDefaultImageHeight
					|| y < 0 - doubleImageHeight) {
				continue;
			}



			ItemWheelActivity activity = this.binding.getActivity(toDraw);
			if (activity != null) {
				int yMinusDefaultHeight = y - defaultImageHeight;
				int xMinusDefaultWidth = x - defaultImageWidth;
				int xMinusDefaultWidthHalf = x - defaultImageWidthHalf;
				int yMinusDefaultHeightHalf = y - defaultImageHeightHalf;

				Image im = binding.getProvider().getActivityImage(activity);
				if (im == null) {
					System.out.println("Activity image is null: "
							+ activity);
				}

				if (toDraw == this.markedPointIndex) {
					/*
					 * draw background if existing
					 */
					if (backgroundImage != null) {
						g.drawScaledImage(
								backgroundImage,
								xMinusDefaultWidth - backgroundPanelOffset,
								yMinusDefaultHeight - backgroundPanelOffset,
								doubleWidthPlusOffset,
								doubleHeightPlusOffset,
								0, 0,
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
								defaultImageHeight + doubleBackgroundPanelOffset,
								0, 0,
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


		}

	}
}
