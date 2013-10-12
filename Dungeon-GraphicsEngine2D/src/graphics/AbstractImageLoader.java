package graphics;

public interface AbstractImageLoader<T extends Object> {

	public T loadImage(String filename);
	
}
