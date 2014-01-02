package de.jdungeon.androidapp;


import graphics.AbstractImageLoader;
import graphics.ImageManager;
import audio.AudioEffectsManager;
import audio.AudioLoader;
import de.jdungeon.androidapp.io.AndroidAudioLoader;
import de.jdungeon.androidapp.io.AndroidImageLoader;
import de.jdungeon.game.Audio;
import de.jdungeon.game.Graphics;
import de.jdungeon.game.Graphics.ImageFormat;
import de.jdungeon.game.Image;
import de.jdungeon.game.Music;
import de.jdungeon.game.Sound;

public class Assets {
	   
    public static Image gate;
    public static Image tiledirt, tilegrassTop, tilegrassBot, tilegrassLeft, tilegrassRight, characterJump, characterDown;
    public static Image button;
    public static Sound click;
    public static Music theme;
    
    private static ImageManager imageManager = null;
    private static AbstractImageLoader<Image> loader = null;
   

	public static void load(JDungeonApp game) {

		// audio
		Audio audio = game.getAudio();
		AudioLoader androidLoader = new AndroidAudioLoader(audio);
		AudioEffectsManager.init(androidLoader);

		// images
        Graphics g = game.getGraphics();
        Assets.gate= g.newImage("jd3.jpg", ImageFormat.RGB565);
        loader = new AndroidImageLoader(game);
        imageManager = ImageManager.getInstance(loader);
        imageManager.loadImages();
    }
   
	public static ImageManager getImageManager() {
		return imageManager;
	}

	public static AbstractImageLoader<Image> getLoader() {
		return loader;
	}
	
}
