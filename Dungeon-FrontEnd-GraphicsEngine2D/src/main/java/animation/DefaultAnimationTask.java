package animation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.jdungeon.game.AbstractAudioSet;
import dungeon.JDPoint;
import dungeon.Position;
import dungeon.PositionInRoomInfo;
import dungeon.RoomInfo;
import figure.FigureInfo;
import figure.percept.Percept;
import graphics.JDImageProxy;

import de.jdungeon.game.Image;

public class DefaultAnimationTask implements AnimationTask {

	private final AnimationSet ani;
	private long startTime;
	private final Collection<AbstractAudioSet> soundsPlayed = new HashSet<>();
	private final String text;
	boolean wasStarted = false;
	private final Position.Pos from;
	private final RoomInfo room;

	public Percept getPercept() {
		return percept;
	}

	public void setPercept(Percept percept) {
		this.percept = percept;
	}

	private Percept percept;

	public void setUrgent(boolean urgent) {
		this.urgent = urgent;
	}

	// urgent animations are rendered instantly (not queued up)
	private boolean urgent;

	public RoomInfo getRoom() {
		return room;
	}

	private Position.Pos to = null;
	private final FigureInfo info;

	public FigureInfo getFigure() {
		return info;
	}

	public DefaultAnimationTask(AnimationSet ani, String text, FigureInfo info, Position.Pos from, Position.Pos to, RoomInfo room) {
		this.ani = ani;
		this.text = text;
		this.from = from;
		this.room = room;
		if(to == null) {
			PositionInRoomInfo pos = info.getPos();
			// might be null for instance for door-smash percepts in other (not visible) room
			if(pos != null) {
				this.to = Position.Pos.fromValue(pos.getIndex());
			}
		} else {
			this.to = to;
		}
		this.info = info;
	}

	@Override
	public boolean isFinished() {
		return getCurrentAnimationFrame() == null;
	}

	@Override
	public boolean isUrgent() {
		return this.urgent;
	}

	@Override
	public AnimationFrame getCurrentAnimationFrame() {
		if (!wasStarted) {
			wasStarted = true;
			startTime = System.currentTimeMillis();
		}
		long timePassed = System.currentTimeMillis() - startTime;
		// if (timePassed < 0) {
		// // has not started yet
		// return null;
		// }
		int imageNr = ani.getImageNrAtTime(timePassed);
		int totalDuration = ani.getTotalDuration();
		double currentProgress = ((double)timePassed)/totalDuration;

		// TODO: deliver position from-to information

		JDImageProxy<?> currentImage = getCurrentImage(imageNr, timePassed);
		if (currentImage == null) {
			return null;
		}

		if (text == null) {

			/*
			 * TODO: optimize use of AnimationFrame Objects here
			 */
			return new AnimationFrame(currentImage, currentProgress, from, to);
		}
		else {
			Image image = (Image) currentImage.getImage();
			return new AnimationFrame(currentImage, text, new JDPoint(
					image.getWidth() * 3 / 8,
					(image.getHeight() / 5) - imageNr), currentProgress, from, to);
		}
	}

	private JDImageProxy<?> getCurrentImage(int imageNr, long timePassed) {

		// check sound associated to this animation
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
