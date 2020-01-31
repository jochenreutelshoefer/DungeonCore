package graphics;

import de.jdungeon.game.AbstractImageLoader;

public class JDImageProxy<T extends Object> {

	public String getFilename() {
		return filename;
	}

	private final String filename;

	private String filenameBlank;

	private T image = null;
	private AbstractImageLoader<T> loader;

	@Deprecated
	public JDImageProxy(String filename, AbstractImageLoader<T> loader) {
		this.filename = filename;
		this.loader = loader;
		init();

	}

	String pathSeparator = "/"; // todo: does this work on all platforms???

	private void init() {

		this.filenameBlank = filename;
		if (filename.toLowerCase().endsWith(".gif") || filename.toLowerCase().endsWith(".png")) {
			filenameBlank = filename.substring(0, filename.length() - 4);
		}

		if (filenameBlank.contains(pathSeparator)) {
			filenameBlank = filenameBlank.substring(filename.lastIndexOf(pathSeparator) + 1);
		}
	}


	public JDImageProxy(AbstractImageLoader<T> loader, String filename) {
		this.filename = filename;
		this.loader = loader;
		init();
	}

	public JDImageProxy(String filename) {
		this.filename = filename;
	}

	public String getFilenameBlank() {
		return filenameBlank;
	}

	public T getImage() {
		if(loader == null) {
			return null;
		}
		return getImage(loader);
	}
	
	public boolean fileExists() {
		return loader.fileExists(this.filename);
	}
	
	public T getImage(AbstractImageLoader<T> loader) {
		if(image == null) {
			image = loader.loadImage(filename);
		}
		return image;
	}
}
