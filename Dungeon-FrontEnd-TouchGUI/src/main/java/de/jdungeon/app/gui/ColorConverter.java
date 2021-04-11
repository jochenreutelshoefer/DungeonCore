package de.jdungeon.app.gui;

import de.jdungeon.util.JDColor;

import de.jdungeon.game.Color;
import de.jdungeon.game.Colors;

public class ColorConverter {

	public static Color getColor(JDColor c) {
		if (c == null)
			return Colors.BLACK;
		;
		if (c.equals(JDColor.black) || c.equals(JDColor.BLACK)) {
			return Colors.BLACK;
		}

		if (c.equals(JDColor.blue)) {
			return Colors.BLUE;
		}

		if (c.equals(JDColor.DARK_GRAY)) {
			return Colors.GRAY;
		}

		if (c.equals(JDColor.green)) {
			return Colors.GREEN;
		}

		if (c.equals(JDColor.orange)) {
			return Colors.YELLOW;
		}

		if (c.equals(JDColor.red)) {
			return Colors.RED;
		}

		if (c.equals(JDColor.WHITE)) {
			return Colors.WHITE;
		}

		if (c.equals(JDColor.yellow) || c.equals(JDColor.YELLOW)) {
			return Colors.YELLOW;
		}

		return null;
	}

}
