package graphics;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import dungeon.Dir;

import de.jdungeon.game.AbstractImageLoader;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 09.05.20.
 */
public class ImageManagerUtils {

	static JDImageProxy<?>[] loadArray(AbstractImageLoader a, String path, String fileNamePrefix, int dir) {

		if (!path.startsWith("animation")) {
			// new loading mechanism
			path = "animation/" + path + "/";
		}

		List<JDImageProxy<?>> imageList = new LinkedList<>();
		String dirChar = "";
		if (dir == Dir.EAST) {
			dirChar = "e";
		}
		if (dir == Dir.WEST) {
			dirChar = "w";
		}
		if (dir == Dir.NORTH) {
			dirChar = "n";
		}
		if (dir == Dir.SOUTH) {
			dirChar = "s";
		}
		int i = 0;

		String numberStr = Integer.toString(i);
		String suffix = "";
		for (int j = 0; j < 4 - numberStr.length(); j++) {
			suffix += "0";
		}
		suffix += numberStr;

		// we iterate to 15 to be safe as the longest sequences are up to 12
		while (i < 15) {

			// old file format
			JDImageProxy<?> im = new JDImageProxy<>(path + fileNamePrefix
					+ dirChar + suffix + "_trans.GIF", a);
			if (im.fileExists()) {
				imageList.add(im);
			}
			else {
				// new simple file name format
				im = new JDImageProxy<>(path + fileNamePrefix
						+ " " + dirChar + suffix + ".gif", a);
				if (im.fileExists()) {
					imageList.add(im);
				}
			}
			i++;

			numberStr = Integer.toString(i);
			suffix = "";
			for (int j = 0; j < 4 - numberStr.length(); j++) {
				suffix += "0";
			}
			suffix += numberStr;
		}

		JDImageProxy<?>[] ims = new JDImageProxy<?>[imageList.size()];
		int k = 0;
		for (Iterator<JDImageProxy<?>> iter = imageList.iterator(); iter
				.hasNext(); ) {
			JDImageProxy<?> element = iter.next();
			ims[k] = element;
			k++;
		}

		return ims;
	}
}
