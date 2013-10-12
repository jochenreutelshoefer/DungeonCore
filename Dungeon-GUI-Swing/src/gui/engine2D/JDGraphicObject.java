/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package gui.engine2D;

import java.awt.*;

public class JDGraphicObject extends GraphicObject {

	private JDImageAWT image;

	public JDGraphicObject(JDImageAWT i, Object ob, Rectangle o, Color c) {
		super(ob, o, c, null);
		image = i;

	}

	public JDGraphicObject(JDImageAWT i, Object ob, Rectangle o, Color c,
			Rectangle clickRect) {
		super(ob, o, c, null, clickRect);
		image = i;

	}

	public JDGraphicObject(JDImageAWT i, Object ob, Rectangle o, Color c,
			boolean paint) {
		super(ob, o, c, null);
		image = i;

	}

	public JDGraphicObject(JDImageAWT i, Object ob, Rectangle o, Color c,
			boolean paint, Rectangle clickRect) {
		super(ob, o, c, null, clickRect);
		image = i;

	}


	public void fill(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		g2D.drawImage(image.getImage(), image.getPosX(), image.getPosY(),
				image.getWidth(), image.getHeight(), null);

	}
}
