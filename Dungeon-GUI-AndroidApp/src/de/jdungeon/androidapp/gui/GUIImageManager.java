package de.jdungeon.androidapp.gui;

import graphics.AbstractImageLoader;
import graphics.JDImageProxy;
import de.jdungeon.androidapp.io.AndroidImageLoader;
import de.jdungeon.game.Game;
import de.jdungeon.game.Image;
import de.jdungeon.implementation.AndroidGame;

public class GUIImageManager {

	public static final String HOUR_GLASS_EMPTY = "hour-glass-empty.gif";
	public static final String HOUR_GLASS_THIRD = "hour-glass-third.gif";
	public static final String HOUR_GLASS_FULL = "hour-glass-full.gif";
	public static final String HOUR_GLASS_HALF = "hour-glass-half.gif";

	public static AbstractImageLoader<?> loader;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static JDImageProxy<?> getImageProxy(String file,
			AbstractImageLoader<?> loader) {
		return new JDImageProxy(loader, file);
	}

	public static Image getImage(String file, Game g) {
		if (loader == null) {
			loader = new AndroidImageLoader((AndroidGame) g);
		}
		return (Image) new JDImageProxy(loader, file).getImage();
	}
}
