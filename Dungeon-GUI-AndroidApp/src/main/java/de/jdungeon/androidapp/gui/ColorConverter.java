package de.jdungeon.androidapp.gui;

import util.JDColor;
import android.graphics.Color;

public class ColorConverter {

	public static int getColor(JDColor c) {
		if (c == null)
			return Color.BLACK;
		;
		if (c.equals(JDColor.black) || c.equals(JDColor.BLACK)) {
			return Color.BLACK;
		}

		if (c.equals(JDColor.blue)) {
			return Color.BLUE;
		}

		if (c.equals(JDColor.DARK_GRAY)) {
			return Color.DKGRAY;
		}

		if (c.equals(JDColor.green)) {
			return Color.GREEN;
		}

		if (c.equals(JDColor.orange)) {
			return Color.YELLOW;
		}

		if (c.equals(JDColor.red)) {
			return Color.RED;
		}

		if (c.equals(JDColor.WHITE)) {
			return Color.WHITE;
		}

		if (c.equals(JDColor.yellow) || c.equals(JDColor.YELLOW)) {
			return Color.YELLOW;
		}

		return 0;
	}

}
