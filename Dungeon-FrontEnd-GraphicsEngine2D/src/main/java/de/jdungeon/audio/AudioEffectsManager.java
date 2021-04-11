package de.jdungeon.audio;

import java.util.ArrayList;
import java.util.List;

import de.jdungeon.game.AbstractAudioSet;
import de.jdungeon.game.AudioLoader;

public class AudioEffectsManager {

	private static boolean initialized = false;

	public static AbstractAudioSet SMASH;
	public static AbstractAudioSet MONSTER_HURT;
	public static AbstractAudioSet HERO_HURT;

	public static AbstractAudioSet SKEL_HURT;
	public static AbstractAudioSet DOOR_CLOSE;
	public static AbstractAudioSet DOOR_SOUND;
	public static AbstractAudioSet DOOR_LOCK;
	public static AbstractAudioSet DOOR_ENTERS;
	public static AbstractAudioSet DOOR_SMASH;
	public static AbstractAudioSet CHEST_OPEN;
	public static AbstractAudioSet FOOTSTEPS_QUICK;
	public static AbstractAudioSet FOOTSTEPS_QUICK_FADING;

	public static AbstractAudioSet MAGIC_FIREBALL;
	public static AbstractAudioSet SPIDER_DIES;
	public static AbstractAudioSet WOLF_DIES;
	public static AbstractAudioSet WOLF_ATTACKS;
	public static AbstractAudioSet SPIDER_ATTACKS;
	public static AbstractAudioSet MAGIC_SOUND;
	public static AbstractAudioSet MAGIC_BLING;

	public static AbstractAudioSet TAKE_ITEM;

	// unused
	public static AbstractAudioSet HIT;

	public static List<AbstractAudioSet> allSounds = new ArrayList<>();

	/*
	public static void init(AudioLoader a) {

		if (!initialized) {
			initialized = true;
			HERO_HURT = a.createAudioSet(new String[] { "getroffen.au",
					"getroffen2.au" });

			SMASH = a.createAudioSet(new String[] { "smash.au", "punch.au" });
			HIT = a.createAudioSet(new String[] { "dang.au" });
			DOOR_CLOSE = a.createAudioSet(new String[] { "door-closed.au" });
			DOOR_LOCK = a.createAudioSet(new String[] { "door-lock.au" });

			MAGIC_SOUND = a
.createAudioSet(new String[] { "woow-anything.au" });
			MAGIC_FIREBALL = a.createAudioSet(new String[] { "fire-ball2.au",
					"magic-fireball.au" });
			MAGIC_BLING = a.createAudioSet(new String[] { "magic-bling1.au" });

			MONSTER_HURT = a.createAudioSet(new String[] { "monster-pain1.au",
					"monster-pain5.au" });

			SKEL_HURT = a.createAudioSet(new String[] {});

			SPIDER_DIES = a.createAudioSet(new String[] { "spider-dies.au" });
			SPIDER_ATTACKS = a.createAudioSet(new String[] { "hiss1.au",
					"hiss2.au" });

			WOLF_ATTACKS = a.createAudioSet(new String[] { "bark1.au",
					"bark2.au" });
			WOLF_DIES = a.createAudioSet(new String[] { "wolf-dies.au" });

		}
	}
*/
	public static void init(AudioLoader a) {

		if (!initialized) {
			initialized = true;
			HERO_HURT = create(a, "getroffen.wav",
					"getroffen2.wav");

			TAKE_ITEM = a.createAudioSet(new String[] { "qubodupItemHandling1.wav", "qubodupItemHandling2.wav", "qubodupItemHandling3.wav", "qubodupItemHandling4.wav", "qubodupItemHandling5.wav", });
			SMASH = create(a, "smash.wav", "punch.mp3");
			HIT = create(a, "dang.mp3");

			FOOTSTEPS_QUICK = create(a, "footsteps-running-1.mp3", "footsteps-running-2.mp3", "footsteps-running-3.mp3");
			FOOTSTEPS_QUICK_FADING = create(a, "footsteps-running-away-fading-short.mp3");

			DOOR_CLOSE = create(a, "door-closed.wav", "door-closed-2.mp3", "door-closed-3.mp3");
			DOOR_ENTERS = create(a, "door-squeak-enters-1a.mp3", "door-squeak-enters-2c.mp3");
			DOOR_LOCK = create(a, "door-lock.wav");
			DOOR_SOUND = create(a, "door-front-opening-a.mp3", "door-knock.mp3", "door-violently-closing-wooden.mp3");
			DOOR_SMASH = create(a, "punch_or_whack_-Vladimir-403040765.mp3", "Sharp Punch-SoundBible.com-1947392621.mp3", "Strong_Punch-Mike_Koenig-574430706.mp3");
			CHEST_OPEN = create(a, "Opening Casket-SoundBible.mp3", "Cracking Chest Open-SoundBible");
			MONSTER_HURT = create(a, "monster-pain1.wav",
					"monster-pain5.wav");
			WOLF_ATTACKS = create(a, "bark1.wav",
					"bark2.wav");

			MAGIC_SOUND = create(a, "woow-anything.wav");
			MAGIC_FIREBALL = create(a, "magic-fireball.wav"); // "fire-ball2.wav" wav files need to have 16 bits
			MAGIC_BLING = create(a, "magic-bling1.wav");

			//SKEL_HURT = create(a, new String[] {});

			SPIDER_DIES = create(a, "spider-dies.wav");
			SPIDER_ATTACKS = create(a, "hiss1.wav",
					"hiss2.wav");

			WOLF_DIES = create(a, "wolf-dies.wav");
		}
	}

	private static AbstractAudioSet create(AudioLoader loader, String... s) {
		AbstractAudioSet audioSet = loader.createAudioSet(s);
		allSounds.add(audioSet);
		return audioSet;
	}

	public static void playSound(AbstractAudioSet set) {
		if (set != null) {
			set.playRandomSound();
		}
	}
}
