/*
 * Created on 25.06.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author Jochen
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package gui.engine2D;

import io.ImageLoader;
import io.PictureLoadDialog;
import io.ResourceLoader;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.LookupOp;
import java.applet.*;
import java.io.File;
import java.net.*;

public class AWTImageLoader {

	public static final String LOCAL_PICTURE_PATH = "resources/pics/";

	Applet applet;
	static MediaTracker tracker = null;

	public AWTImageLoader(Applet a) {
		applet = a;
	}

	public static void setTracker(MediaTracker t) {
		tracker = t;
	}

	public Image loadImage(String filename) {
		return AWTImageLoader.loadImage(applet, filename);

	}

	public static PictureLoadDialog dialog;

	public static Image loadImage(Applet a, String filename) {
		Image im = null;
		String path = new String();

		//System.out.println("loadImage: " + filename);
		if (a != null) {
			// Falls als Applet gestartet
			path = "";
			int k = filename.lastIndexOf('\\');
			filename = filename.substring(k + 1);
			if (dialog != null) {
				dialog.setPicName(filename);
			}
			ImageLoader r3 = new ImageLoader(a);
			try {
				String file = path + filename;
				//System.out.println("versuche bild über applet zu laden: "+ file);
				// im = r3.getImage(file);
				String picsFolder = "/pics/";
				URL url = new URL(a.getCodeBase().toExternalForm() +picsFolder);
				int lastPathSep = filename.lastIndexOf("/");
				if (lastPathSep != -1) {
					String folderpath = filename.substring(0, lastPathSep + 1);
					filename = filename.substring(lastPathSep + 1);
					url = new URL(a.getCodeBase().toExternalForm() + picsFolder
							+ folderpath);
					//System.out.println("Url: " + url.toExternalForm()		+ "  file:  " + filename);
				} 
				im = a.getImage(url, filename);
				if (im == null) {
					System.out.println("bild ist null! --> return null: "+filename);
					return null;
				}
			} catch (Exception e) {
				System.out.println("Bild aus Applet laden gescheitert!");
			}
		} else {
			//System.out.println("lade bild ohne applet: "+filename);
			if (dialog != null) {
				dialog.setPicName(filename);
			}
			path = LOCAL_PICTURE_PATH;
			ResourceLoader rl = ResourceLoader.getDefaultResourceLoader();
			try {
				File f = new File(path + filename);
				if (f.exists()) {
					im = rl.getResourceImage(f);
				} else {
					im = rl.getResourceImage(new URL(path + filename));

				}
			} catch (Exception e) {
				// System.out.println("Bild nicht geladen: "+filename);

			}
		}

		tracker.addImage(im, 1);
		try {
			tracker.waitForAll();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return im;
	}



}
