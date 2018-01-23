package de.jdungeon.androidapp.gui.itemWheel;

import java.util.List;

import android.util.Log;
import dungeon.JDPoint;
import event.EventManager;
import figure.hero.HeroInfo;
import util.JDDimension;

import de.jdungeon.androidapp.event.FocusEvent;
import de.jdungeon.androidapp.gui.AbstractGUIElement;
import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Colors;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input.TouchEvent;
import de.jdungeon.game.ScrollMotion;

public class ItemWheel extends AbstractGUIElement {

	private static final double PI_EIGHTEENTH = Math.PI / 18;
	private static final double PI_THIRTHYSIXTH = Math.PI / 36;
	private static final double TWO_PI = Math.PI * 2;
	private final JDPoint[] points = new JDPoint[36];
	private final int hightlightItemPosition;
	private final Image wheelBackgroundImage;
	private final String title;
	private float currentRotationState = (float) TWO_PI;
	private final int radius;
	private int markedPointIndex;
	private final Image itemBackgroundImage;
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
	private final int posY;
	private final int xLeft;
	private final int heightFullArea;
	private final int backgroundX;
	private final int backgroundWidth;
	private final int xRight;
	private final int stepDown;
	private final int stepLength;
	private boolean highlightOn = false;

	public ItemWheel(JDPoint position, JDDimension dim, HeroInfo info,
					 StandardScreen screen, Game game, ItemWheelBindingSet binding, int selectedIndex,
					 Image itemBackground, Image wheelBackgroundImage, String title) {
		super(position, dim, screen, game);
		this.hightlightItemPosition = selectedIndex;
		this.wheelBackgroundImage = wheelBackgroundImage;
		this.title = title;
		radius = dimension.getWidth();
		info.getSpellBuffer();
		this.binding = binding;
		//markedPointIndex = selectedIndex;
		this.itemBackgroundImage = itemBackground;

		/*
		 * init points
		 */
		for (int i = 0; i < points.length; i++) {
			double degreeRad = i * PI_EIGHTEENTH + currentRotationState;
			int x = calcXCoordinate(degreeRad);
			int y = calcYCoordinate(degreeRad);
			points[i] = new JDPoint(x, y);
		}

		posY = position.getY() - (this.getDimension().getHeight()) - this.getDimension().getHeight() / 12;
		xLeft = position.getX() - this.getDimension().getWidth() * 2 / 5 - 20;

		// calc background image size
		int widthFullArea = game.getScreenWidth() - xLeft;
		heightFullArea = game.getScreenHeight() - posY;
		backgroundWidth = widthFullArea * 2 / 3;
		backgroundX = xLeft + ((widthFullArea - backgroundWidth) / 2);

		// top border line
		xRight = position.getX() + this.getDimension().getWidth() * 2 / 5 + 20;
		stepDown = 10;
		stepLength = 50;

	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	long lastEvent = 0;

	@Override
	public void handleScrollEvent(ScrollMotion scrolling) {
		long timeSinceLastEvent = System.currentTimeMillis() - lastEvent;
		ScrollMotion.FloatDimension movement = scrolling.getMovement();
		float movementX = movement.getX();
		float rotation = movementX / 400;

		// instant rotation
		changeRotationState(rotation);

		float initialRotationVelocity = movementX / 50;
		if (initialRotationVelocity > maxVelocity) {
			velocity = maxVelocity;
		}
		else {
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

	public void highlightEntity(Object object) {
		// we need to update the binding set to have the new item included
		binding.update(0);

		for (int i = 0; i < binding.getBindingSize(); i++) {
			ItemWheelActivity activity = binding.getActivity(i);
			if (activity != null) {
				if (activity.getObject().equals(object)) {
					centerOnIndex(i);
					break;
				}
			}
		}
	}

	public Object highlightFirst() {
		// we need to update the binding set to have the new item included

		ItemWheelActivityProvider provider = binding.getProvider();
		List<ItemWheelActivity> activities = provider.getActivities();
		if(!activities.isEmpty()) {
			ItemWheelActivity activity = activities.get(0);
			Object object = activity.getObject();
			highlightEntity(object);
			return object;
		}
		return null;
	}

	public void setHighlightOn(boolean val) {
		this.highlightOn = val;
	}

	private void iconTouched(int i) {
		if (i == markedPointIndex) {
			ItemWheelActivity infoEntity = binding.getActivity(i);
			if (infoEntity != null) {
				binding.getProvider().activityPressed(infoEntity);
			}
		}
		else {
			centerOnIndex(i);
		}
	}

	private void centerOnIndex(int i) {
		setMarkedIndex(i);

		// scroll element to center position
		int diff = hightlightItemPosition - i;
		this.currentRotationState = (float) PI_EIGHTEENTH * diff;
		updatePointCoordinates();

	}

	private synchronized void changeRotationState(float rotationChange) {
		this.currentRotationState = (float) ((this.currentRotationState + rotationChange + TWO_PI) % TWO_PI);
		updatePointCoordinates();

		// we jump with highlighting to the next element during rotation
		int diff = ((hightlightItemPosition - markedPointIndex) + 36) % 36;
		float highlightPosition = (float) PI_EIGHTEENTH * diff;
		double rotationFromHighlightPoint = (this.currentRotationState - highlightPosition);
		if (rotationFromHighlightPoint > PI_THIRTHYSIXTH) {
			setMarkedIndex((markedPointIndex - 1 + 36) % 36);
		}
		if (rotationFromHighlightPoint < -1 * PI_THIRTHYSIXTH) {
			setMarkedIndex((markedPointIndex + 1) % 36);
		}

	}

	private void unmark() {
		markedPointIndex = -1;
		highlightOn = false;
	}

	private void setMarkedIndex(int i) {
		// set touched element as highlighted element
		markedPointIndex = i;
		highlightOn = true;

		// show info about element
		ItemWheelActivity activity = binding.getActivity(markedPointIndex);
		if (activity != null) {
			EventManager.getInstance().fireEvent(new FocusEvent(activity));
		}
	}

	@Override
	public boolean hasPoint(JDPoint p) {
		int diffX = p.getX() - this.position.getX();
		int diffY = p.getY() - this.position.getY();
		double hypot = Math.hypot(diffX, diffY);
		return hypot < radius + 40;
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
		}
		else {
			/*
			 * calc movement due to inertia-velocity
			 */
			if (Math.abs(velocity) > 0) {
				if (timer > 100f) {
					// slow down on 70% velocity every 100ms
					velocity *= 0.7;
					timer = 0;
				}
				changeRotationState(velocity / 10);

				// at some very low velocity -> stop
				if (Math.abs(velocity) < 0.07) {
					velocity = 0;
				}
			}
		}
		binding.update(time);
		ItemWheelActivity lastAdded = binding.getAndClearLastAdded();
		if (lastAdded != null) {
			this.highlightEntity(lastAdded.getObject());
		}
	}

	private void updatePointCoordinates() {
		for (int i = 0; i < points.length; i++) {
			double degreeRad = i * PI_EIGHTEENTH + currentRotationState;
			points[i].setX(calcXCoordinate(degreeRad));
			points[i].setY(calcYCoordinate(degreeRad));
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


		/*
		 * draw item wheel background if existing
		 */
		if (wheelBackgroundImage != null) {
			g.drawScaledImage(
					wheelBackgroundImage,
					backgroundX,
					posY,
					backgroundWidth,
					heightFullArea,
					0, 0,
					wheelBackgroundImage.getWidth(),
					wheelBackgroundImage.getHeight());
		}

		// headline and title

		g.drawLine(xLeft, posY, xRight, posY, Colors.WHITE);

		g.drawLine(xLeft - stepLength, posY + stepDown, xLeft, posY + stepDown, Colors.WHITE);
		g.drawLine(xLeft, posY, xLeft, posY + stepDown, Colors.WHITE);
		g.drawLine(xRight, posY + stepDown, xRight + stepLength, posY + stepDown, Colors.WHITE);
		g.drawLine(xRight, posY, xRight, posY + stepDown, Colors.WHITE);
		g.drawString(title, xLeft - 25, posY + stepDown - 1, g.getTextPaintWhite());
		g.drawString(title, xRight + 25, posY + stepDown - 1, g.getTextPaintWhite());


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


				Image im = binding.getProvider().getActivityImage(activity);
				if (im == null) {
					Log.w("Warning", "Activity image is null: "
							+ activity);
				}

				if (toDraw == this.markedPointIndex) {
					int yMinusDefaultHeight = y - defaultImageHeight;
					int xMinusDefaultWidth = x - defaultImageWidth;
					/*
					draw highlighting circle
					 */
					if(highlightOn) {
						g.drawOval(xMinusDefaultWidth, yMinusDefaultHeight, doubleImageWidth, doubleImageHeight, Colors.YELLOW);
					}

					/*
					 * draw background if existing
					 */
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

					/*
					 * draw actual item
					 */
					g.drawScaledImage(im, xMinusDefaultWidth,
							yMinusDefaultHeight, doubleImageWidth,
							doubleImageHeight, 0, 0, im.getWidth(),
							im.getHeight());
				}
				else {
					int xMinusDefaultWidthHalf = x - defaultImageWidthHalf;
					int yMinusDefaultHeightHalf = y - defaultImageHeightHalf;

					/*
					 * draw background if existing
					 */
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
