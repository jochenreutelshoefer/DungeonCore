/**
 * @author Duke1
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
package graphics;

import graphics.util.DrawingRectangle;
import graphics.util.JDRectangle;
import util.JDColor;


public class JDGraphicObject extends GraphicObject {

	private final JDImageLocated image;

	public JDGraphicObject(JDImageLocated i, Object ob, DrawingRectangle rectangle, JDColor c) {
		super(ob, rectangle, c, null);
		image = i;

	}


	public JDGraphicObject(JDImageLocated i, Object ob, DrawingRectangle o, JDColor c, DrawingRectangle clickRect) {
		super(ob, o, c, null, clickRect);
		image = i;

	}

	public JDGraphicObject(JDImageLocated i, Object ob, DrawingRectangle rectangle) {
		this(i, ob, rectangle, null, null);
	}

	@Override
	public String toString() {
		if (clickedObject == null) {
			return image.toString();
		} else {
			return clickedObject.toString();
		}
	}

	public JDImageLocated getLocatedImage() {
		return image;
	}

}
