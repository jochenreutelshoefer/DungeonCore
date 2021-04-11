package de.jdungeon.animation;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.badlogic.gdx.utils.Pool;
import de.jdungeon.dungeon.Position;
import de.jdungeon.dungeon.RoomInfo;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.graphics.JDImageProxy;

public class AnimationManager {

	private static AnimationManager instance = null;

	private long latestAnimationStart;

	public static AnimationManager getInstance() {
		if (instance == null) {
			instance = new AnimationManager();
		}
		return instance;
	}

	public AnimationObjectPool framePool = new AnimationObjectPool();

	public static class AnimationObjectPool extends Pool<AnimationFrame> {

		@Override
		protected AnimationFrame newObject() {
			return new AnimationFrame();
		}

		public void free(AnimationFrame frame) {
			super.free(frame);
		}
	}


	private final Map<FigureInfo, Queue<AnimationTask>> animations = new HashMap<>();
	//private final Map<RoomInfo, Queue<AnimationTask>> singleQueue = new HashMap<RoomInfo, Queue<AnimationTask>>();
	private final Map<FigureInfo, AnimationTask> currentTasks = new HashMap<>();;
	private final Map<RoomInfo, Set<AnimationTask>> roomTasks = new HashMap<>();

	public boolean hasAnimations(RoomInfo room) {
		Set<AnimationTask> anis = roomTasks.get(room);
		if(anis == null) {
			return false;
		}
		if(anis.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * RENDER THREAD
	 *
	 * Returns the AnimationFrame for a Figure that should currently
	 * be drawn.
	 *
	 * @param info the de.jdungeon.figure to be drawn
	 * @param roomInfo the room that is currently drawn
	 * @return AnimationFrame showing correct sprite of current move
	 */
	public AnimationFrame getAnimationImage(FigureInfo info, RoomInfo roomInfo) {
		//Log.warning("asking ani for: "+info);
		Queue<AnimationTask> queue = animations.get(info);
		AnimationTask currentTask = currentTasks.get(info);
		if (currentTask == null) {
			// there is no current task for that de.jdungeon.figure
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
			Set<AnimationTask> roomSet = roomTasks.get(info.getRoomInfo());
			if(roomSet != null) {
				roomSet.remove(currentTask);
			}
			// some task for de.jdungeon.figure has been finished
			if (queue != null && !queue.isEmpty()) {
				// there are more tasks in the queue, pop next as current
				currentTask = queue.remove();
			}
			else {
				currentTask = null;
			}
			currentTasks.put(info, currentTask);
			Set<AnimationTask> set = roomTasks.get(info.getRoomInfo());
			if(set == null) {
				set = new HashSet<>();
				roomTasks.put(info.getRoomInfo(), set);
			}
			set.add(currentTask);
		}
		if (currentTask != null) {
			if(currentTask instanceof DefaultAnimationTask) {
				if(((DefaultAnimationTask)currentTask).getRoom().equals(roomInfo.getNumber())) {
					return currentTask.getCurrentAnimationFrame();
				}
			}else {
				return currentTask.getCurrentAnimationFrame();
			}

		}
		// there is no de.jdungeon.animation in queue for this de.jdungeon.figure
		return null;
	}

	public boolean isEmpty() {
		for (FigureInfo figureInfo : animations.keySet()) {
			Queue<AnimationTask> queue = animations.get(figureInfo);
			if(queue != null && !queue.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public void startAnimation(AnimationTask task, FigureInfo figure, boolean delayed, boolean postDelayed, JDImageProxy delayImage) {
		Queue<AnimationTask> figureQueue = animations.get(figure);
		if(figureQueue == null) {
			figureQueue = new LinkedList<>();
			animations.put(figure, figureQueue);
		}

		if(otherAnimationRunning(figure) && !delayed) {
			// when much is going on, we delay a little,
			// as otherwise everything happens synchronously
			// and the time order is not visible to the de.jdungeon.user any more
			delay(figure, delayImage, figureQueue, 400);
		}
		if(delayed) {
			// explicit (long) delay
			figureQueue.add(new DelayAnimationTask(500));
		}
		if(task.isUrgent()) {
			// we clear the figureQueue to jump to this de.jdungeon.animation instantly after the current has been finished
			figureQueue.clear();
			if(delayed) {
				// explicit (long) delay
				delay(figure, delayImage, figureQueue, 500);
			}
		}

		figureQueue.add(task);

		if(postDelayed) {
			figureQueue.add(new DelayAnimationTask(700));
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

	public  void clearAll() {
		this.animations.clear();
	}

	public  void clearFigure(FigureInfo figure) {
		this.animations.remove(figure);
	}

	public  Collection<FigureInfo> getDeadFigures() {
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
