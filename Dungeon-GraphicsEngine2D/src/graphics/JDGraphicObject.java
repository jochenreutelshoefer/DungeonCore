/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package graphics;

import graphics.util.JDColor;
import graphics.util.JDRectangle;


public class JDGraphicObject extends GraphicObject {

	private JDImageLocated image;

	public JDGraphicObject(JDImageLocated i, Object ob, JDRectangle o, JDColor c) {
		super(ob, o, c, null);
		image = i;

	}

	public JDImageLocated getAWTImage() {
		return image;
	}

	public JDGraphicObject(JDImageLocated i, Object ob, JDRectangle o, JDColor c,
			JDRectangle clickRect) {
		super(ob, o, c, null, clickRect);
		image = i;

	}

	public JDGraphicObject(JDImageLocated i, Object ob, JDRectangle o, JDColor c,
			boolean paint) {
		super(ob, o, c, null);
		image = i;

	}


}
