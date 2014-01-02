package de.jdungeon.androidapp.io;
import graphics.AbstractImageLoader;

import java.io.IOException;

import android.content.res.AssetManager;
import de.jdungeon.game.Graphics.ImageFormat;
import de.jdungeon.game.Image;
import de.jdungeon.implementation.AndroidGame;
public class AndroidImageLoader implements AbstractImageLoader<Image> {

	private final AndroidGame game;
	
	public AndroidImageLoader(AndroidGame g) {
		this.game = g;
	}
	
	@Override
	public Image loadImage(String filename) {
		String fullFilename = "pics/" + filename;
		if (fileExists(fullFilename)) {
			return game.getGraphics()
					.newImage(fullFilename, ImageFormat.RGB565);
		}
		return null;
	}

	
	public boolean fileExists(String file) {
		AssetManager mg = game.getResources().getAssets();
		try {
		  mg.open(file);

		} catch (IOException ex) {
		  return false;
		}
		return true;
	}

}
