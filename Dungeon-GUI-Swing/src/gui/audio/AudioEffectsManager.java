package gui.audio;

import java.applet.Applet;

import audio.AbstractAudioSet;
import audio.AudioLoader;

public class AudioEffectsManager {

	private static boolean initialized = false;

	public static AbstractAudioSet SMASH;
	public static AbstractAudioSet MONSTER_HURT;
	public static AbstractAudioSet HERO_HURT;

	public static AbstractAudioSet SKEL_HURT;
	public static AbstractAudioSet DOOR_CLOSE;
	public static AbstractAudioSet DOOR_LOCK;

	public static AbstractAudioSet MAGIC_FIREBALL;
	public static AbstractAudioSet SPIDER_DIES;
	public static AbstractAudioSet WOLF_DIES;
	public static AbstractAudioSet WOLF_ATTACKS;
	public static AbstractAudioSet SPIDER_ATTACKS;
	public static AbstractAudioSet MAGIC_SOUND;
	public static AbstractAudioSet MAGIC_BLING;

	// unused
	public static AbstractAudioSet HIT;

	public static void init(AudioLoader a) {

		if (!initialized) {
			initialized = true;
			HERO_HURT = AudioSet.createAudioSet(a, new String[] {
					"getroffen.au", "getroffen2.au" });

			SMASH = AudioSet.createAudioSet(a, new String[] { "smash.au",
					"punch.au" });
			HIT = AudioSet.createAudioSet(a, new String[] { "dang.au" });
			DOOR_CLOSE = AudioSet.createAudioSet(a,
					new String[] { "door-closed.au" });
			DOOR_LOCK = AudioSet.createAudioSet(a,
					new String[] { "door-lock.au" });

			MAGIC_SOUND = AudioSet.createAudioSet(a,
					new String[] { "woow-anything.au" });
			MAGIC_FIREBALL = AudioSet.createAudioSet(a, new String[] {
					"fire-ball2.au", "magic-fireball.au" });
			MAGIC_BLING = AudioSet.createAudioSet(a,
					new String[] { "magic-bling1.au" });

			MONSTER_HURT = AudioSet.createAudioSet(a, new String[] {
					"monster-pain1.au", "monster-pain5.au" });

			SKEL_HURT = AudioSet.createAudioSet(a, new String[] {});

			SPIDER_DIES = AudioSet.createAudioSet(a,
					new String[] { "spider-dies.au" });
			SPIDER_ATTACKS = AudioSet.createAudioSet(a, new String[] {
					"hiss1.au", "hiss2.au" });

			WOLF_ATTACKS = AudioSet.createAudioSet(a, new String[] {
					"bark1.au", "bark2.au" });
			WOLF_DIES = AudioSet.createAudioSet(a,
					new String[] { "wolf-dies.au" });

		}
	}
	
	public static void playSound(AbstractAudioSet set) {
		if(set != null) {
			set.playRandomSound();
		}
	}

}
