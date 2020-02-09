package de.jdungeon.gui.itemwheel;

import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dungeon.JDPoint;
import event.EventManager;
import figure.hero.HeroInfo;
import util.JDDimension;

import de.jdungeon.app.event.FocusEvent;
import de.jdungeon.app.gui.activity.Activity;
import de.jdungeon.app.gui.itemWheel.ItemWheelBindingSetSimple;
import de.jdungeon.game.Game;
import de.jdungeon.gui.LibgdxActivityPresenter;
import de.jdungeon.gui.LibgdxActivityProvider;
import de.jdungeon.ui.LibgdxGUIElement;

public class LibgdxItemWheel extends LibgdxActivityPresenter {

	public enum CenterPositionMode {
		topLeft, center;
	}

	private static  double PI_EIGHTEENTH;
	private static  double PI_THIRTHYSIXTH;
	private final JDPoint[] points;
	private static final double TWO_PI = Math.PI * 2;
	private final int hightlightItemPosition;
	private int markedPointIndex;
	private final JDPoint drawBoundPositon;
	private final JDDimension drawBoundsDimension;
	private final CenterPositionMode centerMode;
	private float currentRotationState = (float) TWO_PI;
	private final int radius;
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

	private final LibgdxItemWheelBindingSet binding;

	public LibgdxItemWheel(JDPoint wheelCenterPosition,
						   JDDimension dim,
						   HeroInfo info,
						   Game game,
						   LibgdxActivityProvider provider,
						   int selectedIndex,
						   String itemBackground,
						   float rotationOffset) {
		this(wheelCenterPosition,
				dim,
				info,
				game,
				provider,
				selectedIndex,
				itemBackground,
				new JDPoint(wheelCenterPosition.getX()-dim.getWidth(), wheelCenterPosition.getY()-dim.getHeight() ),
				new JDDimension(dim.getWidth()  * 2 , dim.getHeight() * 2),
				CenterPositionMode.topLeft,
				dim.getWidth(),
				36,
				ItemWheelBindingSetSimple.DEFAULT_REOCURRENCE_CYCLE_SIZE);
		currentRotationState = (float)TWO_PI - rotationOffset;
	}

	public LibgdxItemWheel(JDPoint wheelCenterPosition,
						   JDDimension dim,
						   HeroInfo info,
						   Game game,
						   LibgdxActivityProvider provider,
						   int selectedIndex,
						   String itemBackground
						   ) {
			this(wheelCenterPosition,
					dim,
					info,
					game,
					provider,
					selectedIndex,
					itemBackground,
					new JDPoint(wheelCenterPosition.getX()-dim.getWidth(), wheelCenterPosition.getY()-dim.getHeight() ),
					new JDDimension(dim.getWidth()  * 2 , dim.getHeight() * 2),
					CenterPositionMode.topLeft,
					dim.getWidth(),
					36,
					ItemWheelBindingSetSimple.DEFAULT_REOCURRENCE_CYCLE_SIZE);
	}

		public LibgdxItemWheel(JDPoint wheelCenterPosition,
							   JDDimension dim,
							   HeroInfo info,
							   Game game,
							   LibgdxActivityProvider provider,
							   int selectedIndex,
							   String itemBackground,
							   JDPoint drawBoundPositon,
							   JDDimension drawBoundsDimension,
							   CenterPositionMode centerMode,
							   int radius,
							   int itemPositions,
							   int reoccurrenceCycleSize) {
		super(wheelCenterPosition, dim, game, provider, itemBackground, defaultImageWidth);


			PI_EIGHTEENTH = Math.PI / 18;
			PI_THIRTHYSIXTH = Math.PI / 36;
			points = new JDPoint[36];

		this.hightlightItemPosition = selectedIndex;
			this.drawBoundPositon = drawBoundPositon;
			this.drawBoundsDimension = drawBoundsDimension;
			this.centerMode = centerMode;
			this.radius = radius;
			info.getSpellBuffer();
		this.binding = new LibgdxItemWheelBindingSetSimple(selectedIndex, itemPositions, provider, reoccurrenceCycleSize);
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
	public boolean handlePanEvent(float x, float y, float dx, float dy) {

		float movementX = dx * -1 ;
		float rotation = movementX / 400;

		// instant rotation
		changeRotationState(rotation, true);

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
	protected List<? extends LibgdxGUIElement> getAllSubElements() {
		// TODO (currently ItemWheel does not use GUIElements for acitivity tiles)
		return Collections.emptyList();
	}

	@Override
	public boolean handleClickEvent(int x, int y) {
		for (int i = 0; i < points.length; i++) {
			double distance = Math.hypot(x - points[i].getX(), y - points[i].getY());
			if (distance < 25) {
				Activity activity = getActivityForIndex(i);
				if(activity != null) {
					iconTouched(activity);
				}
				break;
			}
		}
		velocity = 0;
		return super.handleClickEvent(x, y);
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

	private synchronized void changeRotationState(float rotationChange, boolean pan) {
		this.currentRotationState = (float) ((this.currentRotationState + rotationChange + TWO_PI) % TWO_PI);
		updatePointCoordinates();


		// we jump with highlighting to the next element during rotation
		// but not during pan
		if(!pan) {
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
			EventManager.getInstance().fireEvent(new FocusEvent(activity, this));
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
		// TODO : refactor to use 'time' give instead of timer mechanism for deceleration
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
				if (timer > 1f) {
					// slow down on 60% velocity every 100ms
					velocity *= 0.8;
					timer = 0;
				}
				changeRotationState(velocity / 10, false);

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
	public void paint(SpriteBatch batch) {

		for (int i = 0; i < points.length; i++) {
			int toDraw = (markedPointIndex + i + 1) % points.length;
			int x = points[toDraw].getX();
			int y = points[toDraw].getY();

			if (x > screenPlusDefaultImageWidth || x < 0 - doubleImageWidth
					|| y > screenPlusDefaultImageHeight
					|| y < 0 - doubleImageHeight || !isInDrawBounds(x,y)) {
				continue;
			}
			Activity activity = this.binding.getActivity(toDraw);
			if (activity != null) {
				if (toDraw == this.markedPointIndex) {
					drawActivityLarge(batch, x, y, activity);
				}
				else {
					drawActivity(batch, x, y, activity);
				}
			}

		}

	}



}
