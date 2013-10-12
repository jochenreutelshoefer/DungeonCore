package gui.engine2D;

import java.awt.Image;

public interface JDImage {



	/**
	 * 
	 * @uml.property name="sizeX"
	 */
	public int getWidth() ;

	/**
	 * 
	 * @uml.property name="sizeY"
	 */
	public int getHeight() ;


	/**
	 * Returns the image.
	 * @return Image
	 * 
	 * @uml.property name="image"
	 */
	public Image getImage();
}
