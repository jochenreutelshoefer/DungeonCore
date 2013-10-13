/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package gui.engine2D;

import graphics.JDImageProxy;
import graphics.JDRectangle;

import java.awt.*;

public class JDGraphicObject extends GraphicObject {

	private JDImageAWT image;

	public JDGraphicObject(JDImageAWT i, Object ob, JDRectangle o, Color c) {
		super(ob, o, c, null);
		image = i;

	}

	public JDImageAWT getAWTImage() {
		return image;
	}

	public JDGraphicObject(JDImageAWT i, Object ob, JDRectangle o, Color c,
			JDRectangle clickRect) {
		super(ob, o, c, null, clickRect);
		image = i;

	}

	public JDGraphicObject(JDImageAWT i, Object ob, JDRectangle o, Color c,
			boolean paint) {
		super(ob, o, c, null);
		image = i;

	}


}
