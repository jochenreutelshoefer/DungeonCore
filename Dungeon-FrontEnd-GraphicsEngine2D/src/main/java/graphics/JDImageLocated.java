package graphics;
import graphics.util.JDRectangle;



/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class JDImageLocated {


	private final JDImageProxy<?> image;
	private final int sizeX;
	private final int sizeY;
	private final int posY;
	private final int posX;

	public JDImageLocated(JDImageProxy<?> i, int posX, int posY, int sizeX, int sizeY) {
		image = i;
		this.posX = posX;
		this.posY = posY;
		this.sizeX = sizeX;
		this.sizeY = sizeY;

	}

	public JDImageLocated(JDImageProxy<?> i, JDRectangle r) {
		image = i;
		this.posX = r.getX();
		this.posY = r.getY();
		this.sizeX = r.getWidth();
		this.sizeY = r.getHeight();

	}
	public int getPosX() {
		return posX;
	}

	public int getPosY() {
		return posY;
	}



	public int getWidth() {
		return sizeX;
	}

	public int getHeight() {
		return sizeY;
	}

	public JDImageProxy<?> getImage() {
		return image;
	}

}
