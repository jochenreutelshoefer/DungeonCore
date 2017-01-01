package animation;

import audio.AbstractAudioSet;

public class AnimationSetDirections {

	private final AnimationSet[] animations;
	
	public AnimationSet[] getAnimations() {
		return animations;
	}

	public AnimationSetDirections(AnimationSet[] anis) {
		this.animations = anis;
	}
	
	public void addAudioClip(AbstractAudioSet sound, int beginNr) {
		for (AnimationSet ani : animations) {
			ani.addAudio(sound, beginNr);
		}
	}
	
	public void addAudioClipHalfTime(AbstractAudioSet sound) {
		for (AnimationSet ani : animations) {
			ani.addAudio(sound, animations[0].getLength()/2);
		}
	}
	
	public AnimationSet get(int dir) {
		if (dir < 0 || dir > 3)
			return null;
		return animations[dir];
	}
	
}
