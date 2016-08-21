package io;

public interface AbstractImageLoader<T extends Object> {

	T loadImage(String filename);
	
}
