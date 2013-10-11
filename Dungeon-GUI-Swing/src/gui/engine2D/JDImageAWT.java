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
public class JDImageAWT {

	
	private Image image;
	private int sizeX;
	private int sizeY;
	private int posY;
	
	public JDImageAWT(Image i, int posX, int posY,int sizeX, int sizeY) {
		image = i;		
		this.posX = posX;
		this.posY = posY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	
	}
	public JDImageAWT(Image i, Rectangle r) {
		image = i;		
		this.posX = r.x;
		this.posY = r.y;
		this.sizeX = r.width;
		this.sizeY = r.height;
	
	}
	public int getPosX() {
		return posX;
	}
	public int getPosY() {
		return posY;
	}
	
	private int posX;



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
	 * Returns the image.
	 * @return Image
	 * 
	 * @uml.property name="image"
	 */
	public Image getImage() {
		return image;
	}

}
