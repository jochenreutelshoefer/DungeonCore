package audio;

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

	public static AbstractAudioSet MUSIC_INTRO_1;

	// unused
	public static AbstractAudioSet HIT;

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
			HERO_HURT = a.createAudioSet(new String[] { "getroffen.wav",
					"getroffen2.wav" });

			SMASH = a.createAudioSet(new String[] { "smash.wav", "punch.mp3" });
			HIT = a.createAudioSet(new String[] { "dang.mp3" });
			DOOR_CLOSE = a.createAudioSet(new String[] { "door-closed.wav" });
			DOOR_LOCK = a.createAudioSet(new String[] { "door-lock.wav" });

			MAGIC_SOUND = a
					.createAudioSet(new String[] { "woow-anything.wav" });
			MAGIC_FIREBALL = a.createAudioSet(new String[] { "fire-ball2.wav",
					"magic-fireball.wav" });
			MAGIC_BLING = a.createAudioSet(new String[] { "magic-bling1.wav" });

			MONSTER_HURT = a.createAudioSet(new String[] { "monster-pain1.wav",
					"monster-pain5.wav" });

			SKEL_HURT = a.createAudioSet(new String[] {});

			SPIDER_DIES = a.createAudioSet(new String[] { "spider-dies.wav" });
			SPIDER_ATTACKS = a.createAudioSet(new String[] { "hiss1.wav",
					"hiss2.wav" });

			WOLF_ATTACKS = a.createAudioSet(new String[] { "bark1.wav",
					"bark2.wav" });
			WOLF_DIES = a.createAudioSet(new String[] { "wolf-dies.wav" });

			MUSIC_INTRO_1 = a.createAudioSet(new String[] { "Exciting_Trailer.mp3" });
		}
	}

	public static void playSound(AbstractAudioSet set) {
		if (set != null) {
			set.playRandomSound();
		}
	}

}
