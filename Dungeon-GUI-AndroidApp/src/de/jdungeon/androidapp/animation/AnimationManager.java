package de.jdungeon.androidapp.animation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import animation.AnimationSet;
import figure.FigureInfo;

public class AnimationManager {

	private static AnimationManager instance = null;

	private long latestAnimationStart;

	public static AnimationManager getInstance() {
		if (instance == null) {
			instance = new AnimationManager();
		}
		return instance;
	}

	private final Map<FigureInfo, Queue<AnimationTask>> animations = new HashMap<FigureInfo, Queue<AnimationTask>>();
	private final Queue<AnimationTask> singleQueue = new LinkedList<AnimationTask>();
	private AnimationTask currentTask;

	public AnimationFrame getAnimationImage(FigureInfo info) {
		if (currentTask == null) {
			if (singleQueue.size() > 0) {
				currentTask = singleQueue.remove();

			} else {
				return null;
			}
		}
		if (currentTask.isFinished()) {
			if (singleQueue.size() > 0) {
				currentTask = singleQueue.remove();
			} else {
				currentTask = null;
			}
		}
		if (currentTask != null && currentTask.getFigure().equals(info)) {
			AnimationFrame currentAnimationFrame = currentTask
					.getCurrentAnimationFrame();
			return currentAnimationFrame;
		}
		return null;
		// Queue<AnimationTask> animationTasks = animations.get(info);
		// if (animationTasks != null && animationTasks.size() > 0) {
		// AnimationTask animationTask = animationTasks.element();
		// if (animationTask.isFinished()) {
		// animationTasks.remove();
		// if (animationTasks.size() > 0) {
		// animationTask = animationTasks.element();
		// } else {
		// return null;
		// }
		// }
		// AnimationFrame currentAnimationFrame = animationTask
		// .getCurrentAnimationFrame();
		// return currentAnimationFrame;
		// }
		// return null;
	}

	public void startAnimation(AnimationSet ani, FigureInfo info, String text) {
		// System.out.println("Starting animation: " + info);
		singleQueue
.add(new AnimationTask(ani, System.currentTimeMillis(),
				text, info));

		// Queue<AnimationTask> queue = animations.get(info);
		// if (queue == null) {
		// queue = new LinkedList<AnimationTask>();
		// animations.put(info, queue);
		// }
		// long currentTimeMillis = System.currentTimeMillis();
		// long animationStartTime = currentTimeMillis;
		// long timeLatestAni = currentTimeMillis - latestAnimationStart;
		// int delayMin = 800;
		// if (timeLatestAni < delayMin) {
		// long delay = delayMin - timeLatestAni;
		// animationStartTime += delay;
		// }
		// queue.add(new AnimationTask(ani, currentTimeMillis, text));
		//
		// latestAnimationStart = animationStartTime;
	}

	public void clear() {
		this.singleQueue.clear();

	}

}
