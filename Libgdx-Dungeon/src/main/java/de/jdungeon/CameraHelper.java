package de.jdungeon;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 25.12.19.
 */
public class CameraHelper {

	private static final String TAG = CameraHelper.class.getName();

	private final float MAX_ZOOM_IN = 0.25f;
	private final float MAX_ZOOM_OUT = 8.0f;

	private final Vector2 position;

	private float currentZoom;

	private float userSelectedZoomLevel;

	private Sprite target;


	public CameraHelper() {
		position = new Vector2();
		init();

	}

	public CameraHelper(int posX, int posY) {
		position = new Vector2(posX, posY);
		init();
	}

	private void init() {
		currentZoom = 1.0f;
		userSelectedZoomLevel = currentZoom;
	}

	public float getCurrentZoom() {
		return currentZoom;
	}
	boolean userZoomLevelCalibrated = false;

	public void calibrateUserZoomLevel() {
		this.userSelectedZoomLevel = currentZoom;
		userZoomLevelCalibrated = true;
	}

	public float getUserSelectedZoomLevel() {
		if(!userZoomLevelCalibrated) {
			return currentZoom;
		}
		return userSelectedZoomLevel;
	}

	public void setUserSelectedZoomLevel(float userSelectedZoomLevel) {
		this.userSelectedZoomLevel = userSelectedZoomLevel;
	}

	public void addUserSelectedZoomLevel(float zoomStep) {
		setUserSelectedZoomLevel(getUserSelectedZoomLevel() + zoomStep);
	}

	public void applyUserSelectedZoomLevel() {
		currentZoom = userSelectedZoomLevel;
	}

	public void update(float deltaTime) {
		if(!hasTarget()) return;

		position.x = target.getX() + target.getOriginX();
		position.y = target.getY() + target.getOriginY();
	}

	public void setPosition(float x, float y) {
		this.position.set(x, y);
	}

	public void addZoom(float amount) {
		setZoom(currentZoom + amount);
	}

	public void setZoom(float value) {
		this.currentZoom = MathUtils.clamp(value, MAX_ZOOM_IN, MAX_ZOOM_OUT);
	}

	public float getZoom() {
		return currentZoom;
	}

	public Sprite getTarget() {
		return target;
	}

	public boolean hasTarget() {
		return this.target != null;
	}

	public void applyTo(OrthographicCamera camera) {
		camera.position.x = position.x;
		camera.position.y = position.y;
		camera.zoom = currentZoom;
		camera.update();
	}

	public Vector2 getPosition() {
		return this.position;
	}
}
