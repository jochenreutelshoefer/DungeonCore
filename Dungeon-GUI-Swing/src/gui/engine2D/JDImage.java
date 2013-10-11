package gui.engine2D;
import java.awt.*;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class JDImage {

	
	Image image;


	int sizeX;

	
	int sizeY;

	int posX;
	int posY;
	public JDImage(Image i, int posX, int posY,int sizeX, int sizeY) {
		image = i;		
		this.posX = posX;
		this.posY = posY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	
	}
	public JDImage(Image i, Rectangle r) {
		image = i;		
		this.posX = r.x;
		this.posY = r.y;
		this.sizeX = r.width;
		this.sizeY = r.height;
	
	}

	/**
	 * @see java.awt.Image#getWidth(ImageObserver)
	 */
	public int getWidth(ImageObserver observer) {
		return 0;
	}

	/**
	 * 
	 * @uml.property name="sizeX"
	 */
	public int getWidth() {
		return sizeX;
	}

	/**
	 * 
	 * @uml.property name="sizeY"
	 */
	public int getHeight() {
		return sizeY;
	}


	/**
	 * @see java.awt.Image#getHeight(ImageObserver)
	 */
	public int getHeight(ImageObserver observer) {
		return 0;
	}

	/**
	 * @see java.awt.Image#getSource()
	 */
	public ImageProducer getSource() {
		return null;
	}

	/**
	 * @see java.awt.Image#getGraphics()
	 */
	public Graphics getGraphics() {
		return null;
	}

	/**
	 * @see java.awt.Image#getProperty(String, ImageObserver)
	 */
	public Object getProperty(String name, ImageObserver observer) {
		return null;
	}

	/**
	 * @see java.awt.Image#flush()
	 */
	public void flush() {
	}

	/**
	 * Returns the image.
	 * @return Image
	 * 
	 * @uml.property name="image"
	 */
	public Image getImage() {
		if (image == null) {
			////System.out.println("image ist null !");
		}
		return image;
	}

}
