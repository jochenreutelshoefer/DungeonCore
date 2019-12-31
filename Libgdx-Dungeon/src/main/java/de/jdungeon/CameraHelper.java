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
	private final float MAX_ZOOM_OUT = 10.0f;

	private final Vector2 position;

	private Sprite target;

	public CameraHelper() {
		position = new Vector2();
		zoom = 1.0f;
	}
	private float zoom;


	public void update(float deltaTime) {
		if(!hasTarget()) return;

		position.x = target.getX() + target.getOriginX();
		position.y = target.getY() + target.getOriginY();
	}

	public void setPosition(float x, float y) {
		this.position.set(x, y);
	}

	public void addZoom(float amount) {
		setZoom(zoom + amount);
	}

	public void setZoom(float value) {
		this.zoom = MathUtils.clamp(value, MAX_ZOOM_IN, MAX_ZOOM_OUT);
	}

	public float getZoom() {
		return zoom;
	}

	public Sprite getTarget() {
		return target;
	}

	public void setTarget(Sprite target) {
		this.target = target;
	}
	public boolean hasTarget(Sprite spr) {
		return hasTarget() && getTarget().equals(spr);
	}
	public boolean hasTarget() {
		return this.target != null;
	}

	public void applyTo(OrthographicCamera camera) {
		camera.position.x = position.x;
		camera.position.y = position.y;
		camera.zoom = zoom;
		camera.update();
	}

	public Vector2 getPosition() {
		return this.position;
	}
}
