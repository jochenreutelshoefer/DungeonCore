package de.jdungeon.androidapp.animation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import animation.AnimationSet;
import dungeon.Position;
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

	//private final Map<FigureInfo, Queue<AnimationTask>> animations = new HashMap<FigureInfo, Queue<AnimationTask>>();
	private final Map<RoomInfo, Queue<AnimationTask>> singleQueue = new HashMap<RoomInfo, Queue<AnimationTask>>();
	private AnimationTask currentTask;

	/**
	 * Returns the AnimationFrame for a Figure that should currently
	 * be drawn.
	 *
	 * @param info the figure to be drawn
	 * @param roomInfo
	 * @return AnimationFrame showing correct sprite of current move
	 */
	public synchronized AnimationFrame getAnimationImage(FigureInfo info, RoomInfo roomInfo) {
		Queue<AnimationTask> queue = singleQueue.get(roomInfo);
		if (currentTask == null) {
			if (queue != null && !queue.isEmpty()) {
				currentTask = queue.remove();
			}
			else {
				return null;
			}
		}
		if (currentTask.isFinished()) {
			if (queue != null && !queue.isEmpty()) {
				currentTask = queue.remove();
			}
			else {
				currentTask = null;
			}
		}
		if (currentTask != null && currentTask.getFigure().equals(info)) {
			if(currentTask.getRoom().equals(roomInfo)) {
				return currentTask
						.getCurrentAnimationFrame();
			}
		}
		return null;
	}

	public synchronized void startAnimation(AnimationSet ani, FigureInfo info, Position.Pos from, Position.Pos to, RoomInfo room, String text) {
		Queue<AnimationTask> queue = singleQueue.get(room);
		if(queue == null) {
			queue = new LinkedList<>();
			singleQueue.put(room, queue);
		}
		queue.add(new AnimationTask(ani, System.currentTimeMillis(),
						text, info, from, to, room));
	}

	public void clear() {
		this.singleQueue.clear();

	}

}
