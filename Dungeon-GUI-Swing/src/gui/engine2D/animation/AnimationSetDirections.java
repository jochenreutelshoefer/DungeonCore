package gui.engine2D.animation;

import gui.audio.AudioSet;

public class AnimationSetDirections {

	private AnimationSet[] animations;
	
	public AnimationSet[] getAnimations() {
		return animations;
	}

	public AnimationSetDirections(AnimationSet[] anis) {
		this.animations = anis;
	}
	
	public void addAudioClip(AudioSet sound, int beginNr) {
		for (AnimationSet ani : animations) {
			ani.addAudio(sound, beginNr);
		}
	}
	
	public void addAudioClipHalfTime(AudioSet sound) {
		for (AnimationSet ani : animations) {
			ani.addAudio(sound, animations[0].getLength()/2);
		}
	}
	
	public AnimationSet get(int dir) {
		return animations[dir];
	}
	
}
