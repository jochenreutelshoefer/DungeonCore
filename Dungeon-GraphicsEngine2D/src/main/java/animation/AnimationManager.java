package animation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import dungeon.Position;
import dungeon.RoomInfo;
import figure.FigureInfo;
import graphics.JDImageProxy;

import de.jdungeon.game.Image;

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

	public synchronized boolean isEmpty() {
		for (FigureInfo figureInfo : animations.keySet()) {
			Queue<AnimationTask> queue = animations.get(figureInfo);
			if(queue != null && !queue.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public synchronized void startAnimation(AnimationTask task, FigureInfo figure, String text, boolean delayed, boolean postDelayed, JDImageProxy delayImage) {
		Queue<AnimationTask> queue = animations.get(figure);
		if(queue == null) {
			queue = new LinkedList<>();
			animations.put(figure, queue);
		}

		if(otherAnimationRunning(figure) && !delayed) {
			// when much is going on, we delay a little,
			// as otherwise everything happens synchronously
			// and the time order is not visible to the user any more
			delay(figure, delayImage, queue, 400);
		}
		if(delayed) {
			// explicit (long) delay
			queue.add(new DelayAnimationTask(500));
		}
		if(task.isUrgent()) {
			// we clear the queue to jump to this animation instantly after the current has been finished
			queue.clear();
			if(delayed) {
				// explicit (long) delay
				delay(figure, delayImage, queue, 500);
			}
		}

		queue.add(task);

		if(postDelayed) {
			queue.add(new DelayAnimationTask(700));
		}
	}

	private void delay(FigureInfo figure, JDImageProxy delayImage, Queue<AnimationTask> queue, int duration) {
		if(delayImage == null) {
			queue.add(new DelayAnimationTask(duration));
		} else {
			queue.add(new DelayImageTask(duration, delayImage, Position.Pos.fromValue(figure.getPositionInRoomIndex())));
		}
	}

	private boolean otherAnimationRunning(FigureInfo figure) {
		for (FigureInfo figureInfo : animations.keySet()) {
			if(figure.equals(figureInfo)) continue;
			Queue<AnimationTask> queue = animations.get(figureInfo);
			if(queue != null && !queue.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	public synchronized void clearAll() {
		this.animations.clear();
	}

	public synchronized void clearFigure(FigureInfo figure) {
		this.animations.remove(figure);
	}

	public synchronized Collection<FigureInfo> getDeadFigures() {
		Collection<FigureInfo> result = new HashSet<>();
		for (FigureInfo figureInfo : animations.keySet()) {
			Boolean dead = figureInfo.isDead();
			Queue<AnimationTask> animationTasks = animations.get(figureInfo);
			if(dead != null && dead && animationTasks != null && !animationTasks.isEmpty()) {
				result.add(figureInfo);
			}
		}
		return result;
	}
}
