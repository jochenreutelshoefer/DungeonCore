package de.jdungeon.androidapp.animation;

import graphics.JDImageProxy;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import animation.AnimationSet;
import audio.AbstractAudioSet;
import de.jdungeon.game.Image;
import dungeon.JDPoint;

public class AnimationTask {

	private final AnimationSet ani;
	private final long startTime;
	private final Collection<AbstractAudioSet> soundsPlayed = new HashSet<AbstractAudioSet>();
	private final String text;

	public AnimationTask(AnimationSet ani, long timestemp) {
		this(ani, timestemp, null);
	}

	public AnimationTask(AnimationSet ani, long timestemp, String text) {
		this.ani = ani;
		this.startTime = timestemp;
		this.text = text;
	}

	public AnimationFrame getCurrentAnimationFrame() {
		long timePassed = System.currentTimeMillis() - startTime;
		int imageNr = ani.getImageNrAtTime(timePassed);

		JDImageProxy<?> currentImage = getCurrentImage(imageNr, timePassed);
		if (currentImage == null)
			return null;

		if (text == null) {

			/*
			 * TODO: optimize use of AnimationFrame Objects here
			 */
			return new AnimationFrame(currentImage);
		} else {
			Image image = (Image) currentImage.getImage();
			return new AnimationFrame(currentImage, text, new JDPoint(
image.getWidth() * 3 / 8,
							(image.getHeight() / 5) - imageNr));
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
		} else {
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
