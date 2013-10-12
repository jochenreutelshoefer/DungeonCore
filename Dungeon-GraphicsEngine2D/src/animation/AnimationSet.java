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
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AnimationSet {

	private JDImageProxy[] images;
	private JDImageProxy defaultImage = null;
	private int[] times;
	private int length;

	private Map<Integer, Set<AbstractAudioSet>> sounds = new HashMap<Integer, Set<AbstractAudioSet>>();

	/**
	 * @return Returns the images.
	 * 
	 */
	public JDImageProxy[] getImages() {
		return images;
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
		// this.gui = gui;
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
	}

	public JDImageProxy getImagesNr(int k) {
		if (images != null && images.length > k) {

			return images[k];
		} else {
			return null;
		}
	}

	// public void preLoad() {
	// if(!loaded) {
	// Graphics g = gui.getGraphics();
	// for(int i = 0; i < length; i++) {
	// g.drawImage(images[i],0,0,100,100,null);
	//
	// }
	// loaded = true;
	// }
	// }

	public int getTimeNr(int k) {
		return times[k];
	}

	/**
	 * @return Returns the length.
	 * 
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @return Returns the defaultImage.
	 */
	public JDImageProxy getDefaultImage() {
		return defaultImage;
	}

	/**
	 * @param defaultImage
	 *            The defaultImage to set.
	 * 
	 */
	public void setDefaultImage(JDImageProxy defaultImage) {
		this.defaultImage = defaultImage;
	}

}