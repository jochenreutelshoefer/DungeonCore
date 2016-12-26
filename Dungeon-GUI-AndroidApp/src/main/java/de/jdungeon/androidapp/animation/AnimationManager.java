package de.jdungeon.androidapp.animation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import dungeon.RoomInfo;
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

	private final Map<FigureInfo, Queue<AnimationTask>> animations = new HashMap<>();
	//private final Map<RoomInfo, Queue<AnimationTask>> singleQueue = new HashMap<RoomInfo, Queue<AnimationTask>>();
	private final Map<FigureInfo, AnimationTask> currentTasks = new HashMap<>();;

	/**
	 * Returns the AnimationFrame for a Figure that should currently
	 * be drawn.
	 *
	 * @param info the figure to be drawn
	 * @param roomInfo the room where the animation happens to be
	 * @return AnimationFrame showing correct sprite of current move
	 */
	public synchronized AnimationFrame getAnimationImage(FigureInfo info, RoomInfo roomInfo) {
		Queue<AnimationTask> queue = animations.get(info);
		AnimationTask currentTask = currentTasks.get(info);
		if (currentTask == null) {
			// there is no current task for that figure
			if (queue != null && !queue.isEmpty()) {
				// there are more tasks in the queue, pop next as current
				currentTask = queue.remove();
				currentTasks.put(info, currentTask);
			}
			else {
				return null;
			}
		}
		if (currentTask.isFinished()) {
			// some task for figure has been finished
			if (queue != null && !queue.isEmpty()) {
				// there are more tasks in the queue, pop next as current
				currentTask = queue.remove();
			}
			else {
				currentTask = null;
			}
			currentTasks.put(info, currentTask);
		}
		if (currentTask != null) {
			//if(currentTask.getRoom().equals(roomInfo)) {
				return currentTask
						.getCurrentAnimationFrame();
			//}
		}
		// there is no animation in queue for this figure
		return null;
	}

	public synchronized void startAnimation(AnimationTask task, FigureInfo figure, String text, boolean delayed) {
		Queue<AnimationTask> queue = animations.get(figure);
		if(queue == null) {
			queue = new LinkedList<>();
			animations.put(figure, queue);
		}
		if(multipleAnimationsRunning()) {
			// when much is going on, we delay a little,
			// as otherwise everything happens synchronously
			// and the time order is not visible to the user any more
			queue.add(new DelayAnimationTask(350));
		}
		if(delayed) {
			queue.add(new DelayAnimationTask(600));
		}
		if(task.isUrgent()) {
			// we clear the queue to jump to this animation instantly after the current has been finished
			queue.clear();
		}

		queue.add(task);
	}

	private boolean multipleAnimationsRunning() {
		int parallelAnis = 0;
		for (FigureInfo figureInfo : animations.keySet()) {
			Queue<AnimationTask> queue = animations.get(figureInfo);
			if(queue != null && !queue.isEmpty()) {
				parallelAnis++;
				if(parallelAnis >= 2) {
					return true;
				}
			}
		}
		return false;
	}

	public void clearAll() {
		this.animations.clear();
	}

	public void clearFigure(FigureInfo figure) {
		this.animations.remove(figure);
	}
}
