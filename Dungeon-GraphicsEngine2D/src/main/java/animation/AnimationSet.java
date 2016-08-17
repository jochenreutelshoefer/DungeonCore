/*
 * Created on 09.02.2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package animation;

import graphics.JDImageProxy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import audio.AbstractAudioSet;

/**
 * @author Jochen
 * 
 */
public class AnimationSet {

	private final JDImageProxy[] images;
	private JDImageProxy defaultImage = null;
	private final int[] times;
	private final int length;
	private final int totalDuration;

	private final Map<Integer, Set<AbstractAudioSet>> sounds = new HashMap<Integer, Set<AbstractAudioSet>>();

	public Map<Integer, Set<AbstractAudioSet>> getSounds() {
		return sounds;
	}

	/**
	 * @return Returns the images.
	 * 
	 */
	public JDImageProxy[] getImages() {
		return images;
	}

	public JDImageProxy getImageAtTime(long millisecondsPassed) {
		return images[getImageNrAtTime(millisecondsPassed)];
	}

	public int getImageNrAtTime(long millisecondsPassed) {
		if (millisecondsPassed > totalDuration) {
			return images.length - 1;
		}
		long sum = 0;
		for (int i = 0; i < times.length; i++) {
			sum += times[i];
			if (millisecondsPassed < sum) {
				if (images.length > i) {
					return i;
				}
			}
		}
		return images.length - 1;
	}

	public int getTotalDuration() {
		return totalDuration;
	}

	public Set<AbstractAudioSet> getSound(int round) {
		return sounds.get(round);
	}

	public void addAudio(AbstractAudioSet set, int k) {
		if (sounds.containsKey(k)) {
			sounds.get(k).add(set);
		} else {
			Set<AbstractAudioSet> bag = new HashSet<AbstractAudioSet>();
			bag.add(set);
			sounds.put(k, bag);
		}
	}

	public AnimationSet(JDImageProxy[] ims, int[] t) {
		images = ims;
		if (ims != null) {
			this.length = ims.length;
		} else {
			this.length = 0;
		}
		if (t.length < length) {
			times = new int[length];
			for (int i = 0; i < t.length; i++) {
				times[i] = t[i];
			}
			for (int i = t.length; i < times.length; i++) {
				times[i] = 40;
			}
		} else {
			times = t;
		}

		totalDuration = sumArray(times);

	}

	private static int sumArray(int[] times) {
		int sum = 0;
		for (int i : times) {
			sum += i;
		}
		return sum;
	}

	public JDImageProxy getImagesNr(int k) {
		if (images != null && images.length > k) {

			return images[k];
		} else {
			return null;
		}
	}


	public int getTimeNr(int k) {
		return times[k];
	}

	public int getLength() {
		return length;
	}

	public JDImageProxy getDefaultImage() {
		return defaultImage;
	}

	public void setDefaultImage(JDImageProxy defaultImage) {
		this.defaultImage = defaultImage;
	}

}