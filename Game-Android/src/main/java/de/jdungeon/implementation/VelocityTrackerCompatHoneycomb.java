package de.jdungeon.implementation;


import android.annotation.TargetApi;
import android.view.VelocityTracker;

/**
 * Implementation of velocity tracker compatibility that can call Honeycomb APIs.
 */
@TargetApi(23)
class VelocityTrackerCompatHoneycomb {
	public static float getXVelocity(VelocityTracker tracker, int pointerId) {
		return tracker.getXVelocity(pointerId);
	}
	public static float getYVelocity(VelocityTracker tracker, int pointerId) {
		return tracker.getYVelocity(pointerId);
	}
}
