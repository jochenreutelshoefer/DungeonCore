package de.jdungeon.androidapp.gui;

import graphics.JDImageProxy;

import de.jdungeon.game.AbstractImageLoader;
import de.jdungeon.game.Image;

public class GUIImageManager {

	public static final String HOUR_GLASS_EMPTY = "hour-glass-empty.gif";
	public static final String HOUR_GLASS_THIRD = "hour-glass-third.gif";
	public static final String HOUR_GLASS_FULL = "hour-glass-full.gif";
	public static final String HOUR_GLASS_HALF = "hour-glass-half.gif";

	public static final String SCROLL = "guiItems/Scroll.gif";
	public static final String BOOK = "guiItems/Book.gif";
	public static final String PARCHMENT = "guiItems/Parchment.gif";
	public static final String MANA_BATTERY = "guiItems/ManaBattery.gif";
	public static final String POTION = "guiItems/Potion.gif";
	public static final String BUNCH = "guiItems/bunch2.gif";
	public static final String POTION_BLUE = "guiItems/mana-potion.gif";
	public static final String ATTACK_SWORD = "guiItems/attack-sword.gif";
	public static final String SPY_ICON = "guiItems/spy-icon.gif";
	public static final String LUPE = "guiItems/lupe.gif";
	public static final String LUPE2 = "guiItems/lupe2.gif";
	public static final String LUPE2a = "guiItems/lupe2a.gif";
	public static final String LUPE3 = "guiItems/lupe3.gif";
	public static final String PLUS = "guiItems/plus.gif";
	public static final String MINUS = "guiItems/minus.gif";
	public static final String SWORD_ICON = "guiItems/sword_icon.gif";
	public static final String FOOT_ICON = "guiItems/foot_icon.gif";
	public static final String HEART_ICON = "guiItems/heart_icon.gif";
	public static final String TARGET_ICON = "guiItems/target_icon.gif";
	public static final String SHIELD_ICON = "guiItems/shield_icon.gif";
	public static final String HIGHLIGHT = "guiItems/highlight_square.gif";
	public static final String HAMMER = "guiItems/icon_hammer2.gif";
	public static final String HAMMER2 = "guiItems/TO_hammer.gif";
	public static final String RUN_ICON = "guiItems/run.gif";
	public static final String CHEST_OPEN = "guiItems/brown-trunk-open.png";
	public static final String FLOOR_BG = "guiItems/dan-rozanski-stone-floor-a1-baking.png";

	public static final String NO_IMAGE = "guiItems/no_image.gif";

	private final AbstractImageLoader loader;

	public GUIImageManager(AbstractImageLoader loader) {
		this.loader = loader;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static JDImageProxy<?> getImageProxy(String file,
			AbstractImageLoader<?> loader) {
		return new JDImageProxy(loader, file);
	}

	public Image getImage(String file) {
		return (Image) new JDImageProxy(loader, file).getImage();
	}

	public JDImageProxy getJDImageProxy(String file) {
		return new JDImageProxy(loader, file);
	}
}
