package graphics;

import de.jdungeon.game.AbstractImageLoader;

public class JDImageProxy<T extends Object> {

	private final String filename;
	private T image = null;
	private AbstractImageLoader<T> loader;
	
	public JDImageProxy(String filename, AbstractImageLoader<T> loader) {
		this.filename = filename;
		this.loader = loader;
	}
	
	public JDImageProxy(AbstractImageLoader<T> loader, String filename) {
		this.filename = filename;
		this.loader = loader;
	}
	
	public JDImageProxy(String filename) {
		this.filename = filename;
	}
	
	public T getImage() {
		if(loader == null) {
			return null;
		}
		return getImage(loader);
	}
	
	public boolean fileExists() {
		return getImage(loader) != null;
	}
	
	public T getImage(AbstractImageLoader<T> loader) {
		if(image == null) {
			image = loader.loadImage(filename);
		}
		return image;
	}
}
