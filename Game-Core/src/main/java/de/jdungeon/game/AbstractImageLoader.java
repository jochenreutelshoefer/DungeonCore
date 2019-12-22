package de.jdungeon.game;

public interface AbstractImageLoader<T extends Object> {

	String PREFIX = "pics/";

	T loadImage(String filename);

	boolean fileExists(String filename);
	
}
