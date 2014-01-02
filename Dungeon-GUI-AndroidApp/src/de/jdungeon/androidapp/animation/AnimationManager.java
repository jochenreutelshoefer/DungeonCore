package de.jdungeon.androidapp.animation;

import java.util.HashMap;
import java.util.Map;

import animation.AnimationSet;
import figure.FigureInfo;

public class AnimationManager {

	private static AnimationManager instance = null;

	public static AnimationManager getInstance() {
		if (instance == null) {
			instance = new AnimationManager();
		}
		return instance;
	}


	private final Map<FigureInfo, AnimationTask> animations = new HashMap<FigureInfo, AnimationTask>();


	public AnimationFrame getAnimationImage(FigureInfo info) {
		AnimationTask animationTask = animations.get(info);
		if (animationTask != null) {
			return animationTask.getCurrentAnimationFrame();
		}
		return null;
	}

	public void startAnimation(AnimationSet ani, FigureInfo info, String text) {
		animations
.put(info, new AnimationTask(ani, System.currentTimeMillis(),
				text));
	}

}
