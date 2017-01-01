package de.jdungeon.game;

public interface AbstractImageLoader<T extends Object> {

	T loadImage(String filename);
	
}
