package de.jdungeon.androidapp.gui.itemWheel;

import java.util.Collections;
import java.util.List;

import dungeon.JDPoint;
import event.EventManager;
import figure.hero.HeroInfo;
import util.JDDimension;

import de.jdungeon.androidapp.event.FocusEvent;
import de.jdungeon.androidapp.gui.GUIElement;
import de.jdungeon.androidapp.gui.activity.Activity;
import de.jdungeon.androidapp.gui.activity.ActivityPresenter;
import de.jdungeon.androidapp.gui.activity.ActivityProvider;
import de.jdungeon.androidapp.screen.StandardScreen;
import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;
import de.jdungeon.game.Input.TouchEvent;
import de.jdungeon.game.ScrollMotion;

public class ItemWheel extends ActivityPresenter {

	public enum CenterPositionMode {
		topLeft, center;
	}

	private static  double PI_EIGHTEENTH;
	private static  double PI_THIRTHYSIXTH;
	private final JDPoint[] points;
	private static final double TWO_PI = Math.PI * 2;
	private final int hightlightItemPosition;
	private int markedPointIndex;
	private final Image wheelBackgroundImage;
	private final String title;
	private final JDPoint drawBoundPositon;
	private final JDDimension drawBoundsDimension;
	private final CenterPositionMode centerMode;
	private float currentRotationState = (float) TWO_PI;
	private final int radius;
	private final int itemPositions;
	private boolean justRotated = true;

	private static final int defaultImageWidth = 50;


	private final int screenPlusDefaultImageWidth = screenWidth
			+ defaultImageWidth;
	private final int screenPlusDefaultImageHeight = screenHeight
			+ defaultImageHeight;
	private float timer = 0;

	private float velocity = 0;
	private float startVelocity = 0;
	private final float maxVelocity = 50;
	//private final int posY;

	//private final int xLeft;

	private final ItemWheelBindingSet binding;

	public ItemWheel(JDPoint wheelCenterPosition, JDDimension dim, HeroInfo info,
					 StandardScreen screen, Game game, ActivityProvider provider, int selectedIndex,
					 Image itemBackground, Image wheelBackgroundImage, String title) {
			this(wheelCenterPosition, dim, info, screen, game, provider, selectedIndex, itemBackground, wheelBackgroundImage, title, new JDPoint(wheelCenterPosition.getX()-dim.getWidth(), wheelCenterPosition.getY()-dim.getHeight() ), new JDDimension(dim.getWidth()  * 2 , dim.getHeight() * 2), CenterPositionMode.topLeft, dim.getWidth(), 36);
	}

		public ItemWheel(JDPoint wheelCenterPosition, JDDimension dim, HeroInfo info,
					 StandardScreen screen, Game game, ActivityProvider provider, int selectedIndex,
					 Image itemBackground, Image wheelBackgroundImage, String title, JDPoint drawBoundPositon, JDDimension drawBoundsDimension, CenterPositionMode centerMode, int radius, int itemPositions) {
		super(wheelCenterPosition, dim, screen, game, provider, itemBackground, defaultImageWidth);


			PI_EIGHTEENTH = Math.PI / 18;
			PI_THIRTHYSIXTH = Math.PI / 36;
			points = new JDPoint[36];

		this.hightlightItemPosition = selectedIndex;
		this.wheelBackgroundImage = wheelBackgroundImage;
		this.title = title;
			this.drawBoundPositon = drawBoundPositon;
			this.drawBoundsDimension = drawBoundsDimension;
			this.centerMode = centerMode;
			this.radius = radius;
			this.itemPositions = itemPositions;
			info.getSpellBuffer();
		this.binding = new ItemWheelBindingSetSimple(selectedIndex, itemPositions, provider);
		//markedPointIndex = selectedIndex;

		/*
		 * init points
		 */
		for (int i = 0; i < points.length; i++) {
			double degreeRad = i * PI_EIGHTEENTH + currentRotationState;
			int x = calcXCoordinate(degreeRad);
			int y = calcYCoordinate(degreeRad);
			points[i] = new JDPoint(x, y);
		}

		positionCorrection = true;
	}

	long lastEvent = 0;

	@Override
	public boolean handleScrollEvent(ScrollMotion scrolling) {
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
		return true;
	}

	@Override
	protected List<? extends GUIElement> getAllSubElements() {
		// TODO (currently ItemWheel does not use GUIElements for acitivity tiles)
		return Collections.emptyList();
	}

	@Override
	public boolean handleTouchEvent(TouchEvent touch) {
		for (int i = 0; i < points.length; i++) {
			double distance = Math.hypot(touch.x - points[i].getX(), touch.y - points[i].getY());
			if (distance < 25) {
				Activity activity = getActivityForIndex(i);
				if(activity != null) {
					iconTouched(activity);
				}
				break;
			}
		}
		velocity = 0;
		return super.handleTouchEvent(touch);
	}

	@Override
	public void highlightEntity(Object object) {
		// we need to update the binding set to have the new item included
		binding.update(0);
		Activity objectActivity = getObjectActivity(object);
		if(objectActivity != null) {
			centerOnIndex(objectActivity);
		}
	}

	private int getObjectIndex(Object object) {
		for (int i = 0; i < binding.getBindingSize(); i++) {
			Activity activity = binding.getActivity(i);
			if (activity != null) {
				if (activity.getObject().equals(object)) {
					return i;
				}
			}
		}
		return -1;
	}

	private Activity getObjectActivity(Object object) {
		for (int i = 0; i < binding.getBindingSize(); i++) {
			Activity activity = binding.getActivity(i);
			if (activity != null) {
				if (activity.getObject().equals(object)) {
					return activity;
				}
			}
		}
		return null;
	}

	private int getActivityIndex(Activity activity) {
		Object object = activity.getObject();
		if(object != null) {
			return getObjectIndex(object);
		}
		return -1;
	}

	private Activity getActivityForIndex(int index) {
		return binding.getActivity(index);
	}

	@Override
	protected void iconTouched(Activity activity) {
		int i = getActivityIndex(activity);
		if(i == -1) return;
		if (i == markedPointIndex) {
			Activity infoEntity = binding.getActivity(i);
			if (infoEntity != null) {
				provider.activityPressed(infoEntity);
			}
		}
		else {
			centerOnIndex(activity);
		}
	}

	@Override
	public Object highlightFirst() {
		// we need to update the binding set to have the new item included

		List<Activity> activities = provider.getActivities();
		if (!activities.isEmpty()) {
			Activity activity = activities.get(0);
			Object object = activity.getObject();
			highlightEntity(object);
			return object;
		}
		return null;
	}

	@Override
	protected void centerOnIndex(Activity activity) {

		int activityIndex = getActivityIndex(activity);
		setMarkedIndex(activityIndex);

		// scroll element to center position
		int diff = hightlightItemPosition - activityIndex;
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
		Activity activity = binding.getActivity(markedPointIndex);
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
		Activity lastAdded = binding.getAndClearLastAdded();
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
		int y = this.position.getY();

		int result = (int) (y + (Math.cos(degreeRad) * radius));
		if(centerMode == CenterPositionMode.center) {
			result = result + (int)(this.getDimension().getWidth()/2);
		}
		return result;
	}

	private int calcXCoordinate(double degreeRad) {
		int x = this.position.getX();

		int result =  (int) (x + (Math.sin(degreeRad) * radius));
		if(centerMode == CenterPositionMode.center) {
			result = result + (int)(this.getDimension().getWidth()/2);
		}
		return result;
	}

	protected boolean isInDrawBounds(int x, int y) {
		return x >= drawBoundPositon.getX() && x <= drawBoundPositon.getX() + drawBoundsDimension.getWidth() &&
				y >= drawBoundPositon.getY() && y <= drawBoundPositon.getY() + drawBoundsDimension.getHeight();
	}
	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {

		for (int i = 0; i < points.length; i++) {
			int toDraw = (markedPointIndex + i + 1) % points.length;
			int x = points[toDraw].getX();
			int y = points[toDraw].getY();



			/*
			PaintBuilder numberPaint = new PaintBuilder();
			numberPaint.setColor(Colors.RED);
			//g.drawOval(x, y, 2, 2, Colors.GRAY);
			g.drawString("" + toDraw, x, y, g.createTextPaint(numberPaint) );
			g.drawRect(drawBoundPositon.getX(), drawBoundPositon.getY(), drawBoundsDimension.getWidth(), drawBoundsDimension.getHeight(), Colors.BLUE);
			*/

			if (x > screenPlusDefaultImageWidth || x < 0 - doubleImageWidth
					|| y > screenPlusDefaultImageHeight
					|| y < 0 - doubleImageHeight || !isInDrawBounds(x,y)) {
				continue;
			}
			Activity activity = this.binding.getActivity(toDraw);
			if (activity != null) {
				if (toDraw == this.markedPointIndex) {
					drawActivityLarge(g, x, y, activity);
				}
				else {
					drawActivity(g, x, y, activity);
				}
			}

		}

	}



}
