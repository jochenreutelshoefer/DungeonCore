/*
 * Created on 04.05.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

package io;

import java.awt.*;
import java.applet.*;

/**
 * @author Jochen
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ImageLoader {
	public Object imageGetter;

	/**
	 * Constructor for ImageLoader. Takes an Object and saves it to use for
	 * loading Images.
	 * 
	 * @param imageGetter
	 *            Object to use for getting Images.
	 */
	public ImageLoader(Object imageGetter) {
		this.imageGetter = imageGetter;
	}

	/**
	 * Gets an Image from the Classpath location of the Object in the
	 * Constructor.
	 * 
	 * @param imageName
	 *            String name of the Image file to load.
	 * @return Image loaded
	 */

	public Image getImage(String fileName) throws Exception {
		if (fileName == null)
			return null;

		Image image = null;

		if (imageGetter instanceof Applet) {
			//System.out.println("Gueltiges Applet als ImageGetter!");
			Applet a;

			image = ((Applet) imageGetter).getImage(
					((Applet) imageGetter).getDocumentBase(), fileName);
		} else {
			//System.out.println("Keine applet-instance --> Versuch mit Toolkit");
			image = Toolkit.getDefaultToolkit().getImage(
					imageGetter.getClass().getResource(fileName));
		}
		if (image == null) {
			System.out.println("Image ist null: "+fileName);

		} else {
			//System.out.println("Image geladen!!!");
		}
		return image;
	}

}
