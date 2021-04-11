package de.jdungeon.animation;

import de.jdungeon.dungeon.util.RouteInstruction;

import de.jdungeon.game.AbstractAudioSet;

public class AnimationSetDirections {

	private final DefaultAnimationSet[] animations;
	
	public DefaultAnimationSet[] getAnimations() {
		return animations;
	}

	public AnimationSetDirections(DefaultAnimationSet[] anis) {
		this.animations = anis;
	}
	
	public void addAudioClip(AbstractAudioSet sound, int beginNr) {
		for (DefaultAnimationSet ani : animations) {
			ani.addAudio(sound, beginNr);
		}
	}
	
	public void addAudioClipHalfTime(AbstractAudioSet sound) {
		for (DefaultAnimationSet ani : animations) {
			ani.addAudio(sound, animations[0].getLength()/2);
		}
	}
	
	public DefaultAnimationSet get(int dir) {
		if (dir < 0 || dir > 3)
			return null;
		return animations[dir];
	}

	public DefaultAnimationSet get(RouteInstruction.Direction direction) {
		return get(direction.getValue()-1);
	}
	
}
