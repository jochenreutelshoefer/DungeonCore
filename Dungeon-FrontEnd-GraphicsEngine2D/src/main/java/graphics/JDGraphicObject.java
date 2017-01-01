/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package graphics;

import graphics.util.JDRectangle;
import util.JDColor;


public class JDGraphicObject extends GraphicObject {

	private final JDImageLocated image;

	public JDGraphicObject(JDImageLocated i, Object ob, JDRectangle o, JDColor c) {
		super(ob, o, c, null);
		image = i;

	}

	@Override
	public String toString() {
		if (clickedObject == null) {
			return image.toString();
		} else {
			return clickedObject.toString();
		}
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
