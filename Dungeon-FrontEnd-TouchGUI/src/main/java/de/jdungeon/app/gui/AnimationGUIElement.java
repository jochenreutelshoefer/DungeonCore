package de.jdungeon.app.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

import de.jdungeon.animation.AnimationSet;
import de.jdungeon.animation.DefaultAnimationSet;
import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.event.Event;
import de.jdungeon.event.EventManager;
import de.jdungeon.graphics.JDImageLocated;
import de.jdungeon.graphics.JDImageProxy;
import de.jdungeon.util.JDDimension;

import de.jdungeon.game.Game;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Image;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 17.01.18.
 */
public abstract class AnimationGUIElement extends ImageGUIElement {

	private final DefaultAnimationSet animation;
	private final Collection<DefaultGUIAnimationTask> animationTasks = new ArrayList<>();

	public AnimationGUIElement(JDPoint position, JDDimension dimension, DefaultAnimationSet animation, Game game) {
		super(position, dimension, (Image)animation.getImagesNr(0).getImage(), game);
		this.animation = animation;
	}

	public DefaultGUIAnimationTask addTask(JDPoint from, JDPoint to, int iterations, Event finishedEvent) {
		DefaultGUIAnimationTask task = new DefaultGUIAnimationTask(from, to, System.currentTimeMillis(), dimension.getHeight(), animation, iterations, finishedEvent);
		animationTasks.add(task);
		return task;
	}

	public DefaultGUIAnimationTask addTask(JDPoint from, JDPoint to, int iterations) {
		return addTask(from, to, iterations, null);
	}

	public DefaultGUIAnimationTask addTask(JDPoint from, JDPoint to) {
		return addTask(from, to, 1);
	}

	@Override
	public void paint(Graphics g, JDPoint viewportPosition) {
		Iterator<DefaultGUIAnimationTask> iterator = animationTasks.iterator();
		DefaultGUIAnimationTask activeTask = null;
		while (iterator.hasNext()) {
			DefaultGUIAnimationTask task = iterator.next();
			if(task.isFinished()) {
				// some clean up
				iterator.remove();
				// we also set the overall position to the end position of the de.jdungeon.animation
				this.position = task.endPos;
			} else if(task.isActive()) {
				activeTask = task;
				break;
			}
		}
		if(activeTask != null) {
			//do
			JDImageLocated currentImage = activeTask.getCurrentImage();
			super.paint(g, viewportPosition, currentImage);

		} else {
			super.paint(g, viewportPosition);
		}
	}

	interface GUIAnimationTask {

		boolean isFinished();

		boolean isActive();

		JDImageLocated getCurrentImage();
	}

	public static class DefaultGUIAnimationTask implements GUIAnimationTask {

		private final JDPoint startPos;
		private final JDPoint endPos;
		private final long startTime;
		private final int imageSize;
		private final long endTime;
		private final AnimationSet animation;
		private Event finishedEvent;
		private boolean fired = false;

		public DefaultGUIAnimationTask(JDPoint startPos, JDPoint endPos, long startTime, int imageSize, AnimationSet animation, int iterations, Event finishedEvent) {
			this(startPos, endPos, startTime, imageSize, animation, 1);
			this.finishedEvent = finishedEvent;
		}

		public DefaultGUIAnimationTask(JDPoint startPos, JDPoint endPos, long startTime, int imageSize, AnimationSet animation, int iterations) {
			this.startPos = startPos;
			this.endPos = endPos;
			this.startTime = startTime;
			this.imageSize = imageSize;
			this.animation = animation;
			endTime = startTime + animation.getTotalDuration() * iterations;
		}

		@Override
		public boolean isFinished() {
			boolean finished = System.currentTimeMillis() > endTime;
			if(finished && finishedEvent != null && !fired) {
				Logger.getLogger(this.getClass().getName()).info("Firing DungeonStartEvent");
				EventManager.getInstance().fireEvent(finishedEvent);
				fired = true;
			}
			return finished;
		}

		@Override
		public boolean isActive() {
			long currentTime = System.currentTimeMillis();
			return currentTime < endTime && currentTime > startTime;
		}

		@Override
		public JDImageLocated getCurrentImage() {
			long millisecondsPassed = System.currentTimeMillis() - startTime;
			JDImageProxy image = animation.getImageAtTime(millisecondsPassed % animation.getTotalDuration());
			double elapsedTimeRelative =  ((double)millisecondsPassed) / (endTime-startTime);
			int posX = startPos.getX() + (int)((endPos.getX() - startPos.getX()) * elapsedTimeRelative);
			int posY = startPos.getY() + (int)((endPos.getY() - startPos.getY()) * elapsedTimeRelative);
			return  new JDImageLocated(image, posX, posY, imageSize, imageSize);
		}
	}
}
