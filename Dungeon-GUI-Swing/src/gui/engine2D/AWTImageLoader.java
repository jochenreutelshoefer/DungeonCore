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

import graphics.AbstractImageLoader;
import gui.mainframe.dialog.PictureLoadDialog;
import io.ResourceLoader;

import java.applet.Applet;
import java.awt.Image;
import java.awt.MediaTracker;
import java.io.File;
import java.net.URL;

import javax.swing.JPanel;

public class AWTImageLoader implements AbstractImageLoader<Image> {

	public static final String LOCAL_PICTURE_PATH = "resources/pics/";

	private final Applet applet;
	private static MediaTracker tracker = null;

	public AWTImageLoader(Applet a) {
		applet = a;
		setTracker(new MediaTracker(new JPanel()));
	}

	public static void setTracker(MediaTracker t) {
		tracker = t;
	}

	@Override
	public Image loadImage(String filename) {
		return AWTImageLoader.loadImage(applet, filename);
	}

	public static PictureLoadDialog dialog;

	protected static Image loadImage(Applet a, String filename) {
		Image im = null;
		String path = new String();

		if (a != null) {
			// Falls als Applet gestartet
			path = "";
			int k = filename.lastIndexOf('\\');
			filename = filename.substring(k + 1);
			if (dialog != null) {
				dialog.setPicName(filename);
			}
			try {
				String picsFolder = "/pics/";
				URL url = new URL(a.getCodeBase().toExternalForm() + picsFolder);
				int lastPathSep = filename.lastIndexOf("/");
				if (lastPathSep != -1) {
					String folderpath = filename.substring(0, lastPathSep + 1);
					filename = filename.substring(lastPathSep + 1);
					url = new URL(a.getCodeBase().toExternalForm() + picsFolder
							+ folderpath);
				}
				im = a.getImage(url, filename);
				if (im == null) {
					System.out.println("bild ist null! --> return null: "
							+ filename);
					return null;
				}
			} catch (Exception e) {
				System.out.println("Bild aus Applet laden gescheitert!");
			}
		} else {
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

			}
		}

		if (tracker != null) {
			tracker.addImage(im, 1);
			try {
				tracker.waitForAll();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return im;
	}

}
