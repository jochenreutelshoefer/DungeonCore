package de.jdungeon.animation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.jdungeon.game.AbstractAudioSet;

import de.jdungeon.dungeon.JDPoint;
import de.jdungeon.dungeon.Position;
import de.jdungeon.dungeon.PositionInRoomInfo;
import de.jdungeon.figure.FigureInfo;
import de.jdungeon.figure.percept.OpticalPercept;
import de.jdungeon.graphics.JDImageProxy;
import de.jdungeon.log.Log;

public class DefaultAnimationTask implements AnimationTask {

	private final DefaultAnimationSet ani;
	private long startTime;
	private final Collection<AbstractAudioSet> soundsPlayed = new HashSet<>();
	private final String text;
	boolean wasStarted = false;
	private final Position.Pos from;

	private final OpticalPercept percept;

	public void setUrgent(boolean urgent) {
		this.urgent = urgent;
	}

	// urgent animations are rendered instantly (not queued up)
	private boolean urgent;

	public JDPoint getRoom() {
		return percept.getPoint();
	}

	@Override
	public String toString() {
		return super.toString() + " "+ percept.toString()+" " +  getRoom()+ " " + text;
	}

	private Position.Pos to = null;
	private final FigureInfo info;

	public FigureInfo getFigure() {
		return info;
	}

	public DefaultAnimationTask(DefaultAnimationSet ani, String text, FigureInfo info, Position.Pos from, Position.Pos to, OpticalPercept percept) {
		this.ani = ani;
		this.text = text;
		this.from = from;
		this.percept = percept;
		if(to == null) {
			PositionInRoomInfo pos = info.getPos();
			// might be null for instance for door-smash percepts in other (not visible) room
			if(pos != null) {
				this.to = Position.Pos.fromValue(pos.getIndex());
			} else {
				Log.severe("Animation task without destination: "+ani);
			}
		} else {
			this.to = to;
		}
		this.info = info;
	}

	/*
	 * RENDER THREAD
	 */
	@Override
	public boolean isFinished() {
		return getCurrentAnimationFrame() == null;
	}

	/*
	 * RENDER THREAD
	 */
	@Override
	public boolean isUrgent() {
		return this.urgent;
	}

	/*
	 * RENDER THREAD
	 */
	@Override
	public AnimationFrame getCurrentAnimationFrame() {
		if (!wasStarted) {
			wasStarted = true;
			startTime = System.currentTimeMillis();
		}
		long timePassed = System.currentTimeMillis() - startTime;
		int imageNr = ani.getImageNrAtTime(timePassed);
		int totalDuration = ani.getTotalDuration();
		double currentProgress = ((double)timePassed)/totalDuration;

		// TODO: deliver position from-to information

		JDImageProxy<?> currentImage = getCurrentImage(imageNr, timePassed);
		if (currentImage == null) {
			return null;
		}

		if (text == null || text.length() == 0) {
			AnimationFrame frame = AnimationManager.getInstance().framePool.obtain();
			frame.setImage(currentImage);
			frame.setCurrentProgress(currentProgress);
			frame.setFrom(from);
			frame.setTo(to);
			return frame;
		}
		else {

			AnimationFrame frame = AnimationManager.getInstance().framePool.obtain();
			frame.setImage(currentImage);
			frame.setText(text);
			frame.setCurrentProgress(currentProgress);

			// TODO: find better way to determine size of image (might differ in some cases..)
			int imageSize = 96;
			frame.setTextCoordinatesOffsetX(imageSize * 1 / 8);
			frame.setTextCoordinatesOffsetY(imageSize * 1 / 12 - imageNr);
			frame.setFrom(from);
			frame.setTo(to);
			return frame;
		}
	}

	/*
	 * RENDER THREAD
	 */
	private JDImageProxy<?> getCurrentImage(int imageNr, long timePassed) {

		// check sound associated to this de.jdungeon.animation
		AbstractAudioSet sound = getSound(imageNr, timePassed);
		// TODO: attach sound to AnimationFrame object and play in GUI
		if (sound != null && !soundsPlayed.contains(sound)) {
			sound.playRandomSound();
			soundsPlayed.add(sound);
		}

		if (timePassed > ani.getTotalDuration()) {
			return null;
		}
		else {
			return ani.getImageAtTime(timePassed);
		}
	}

	private AbstractAudioSet getSound(int imageNr, long timePassed) {

		Map<Integer, Set<AbstractAudioSet>> sounds = ani.getSounds();
		Set<Integer> keySet = sounds.keySet();
		for (Integer soundStartTime : keySet) {
			if (imageNr >= soundStartTime) {
				Set<AbstractAudioSet> set = sounds.get(soundStartTime);
				for (AbstractAudioSet sound : set) {
					if (sound != null) {
						return sound;

					}
				}
			}
		}
		return null;
	}

}
