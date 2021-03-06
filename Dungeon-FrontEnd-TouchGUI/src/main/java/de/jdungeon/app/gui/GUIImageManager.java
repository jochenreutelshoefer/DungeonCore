package de.jdungeon.app.gui;

import de.jdungeon.graphics.JDImageProxy;

import de.jdungeon.game.AbstractImageLoader;
import de.jdungeon.game.Image;

public class GUIImageManager {

	public static final String HOUR_GLASS_EMPTY = "guiItems/hour-glass-empty.gif";
	public static final String HOUR_GLASS_THIRD = "guiItems/hour-glass-third.gif";
	public static final String HOUR_GLASS_FULL = "guiItems/hour-glass-full.gif";
	public static final String HOUR_GLASS_HALF = "guiItems/hour-glass-half.gif";

	public static final String CIRCLE_HIGHLIGHT = "cancel_tp_outline.png";

	public static final String SCROLL = "guiItems/Scroll.gif";
	public static final String BOOK = "guiItems/Book.gif";
	public static final String PARCHMENT = "guiItems/Parchment.gif";
	public static final String MANA_BATTERY = "guiItems/ManaBattery.gif";
	public static final String POTION = "guiItems/Potion.gif";
	public static final String POTION_DUST = "guiItems/Zauberstaub.png";
	public static final String POTION_DUST_SW = "guiItems/ZauberstaubSW.png";
	public static final String BUNCH = "guiItems/bunch2.gif";
	public static final String POTION_BLUE = "guiItems/mana-potion.gif";
	public static final String ATTACK_SWORD = "guiItems/attack-sword.gif";
	public static final String LUPE = "guiItems/lupe.gif";
	public static final String LUPE2 = "guiItems/lupe2.gif";
	public static final String LUPE2a = "guiItems/lupe2a.gif";
	public static final String LUPE3 = "guiItems/lupe3.gif";
	public static final String PLUS = "guiItems/plus.gif";
	public static final String MINUS = "guiItems/minus.gif";
	public static final String SWORD_ICON = "guiItems/sword_icon.gif";
	public static final String SCOUT_ICON = "guiItems/eye130x130.png";
	public static final String FOOT_ICON = "guiItems/foot_icon.gif";
	public static final String HEART_ICON = "guiItems/heart_icon.gif";
	public static final String TARGET_ICON = "guiItems/target_icon.gif";
	public static final String OWL = "guiItems/Eule.png";
	public static final String LION_ICON = "guiItems/lion_icon.png";
	public static final String SHIELD_ICON = "guiItems/shield_icon.gif";
	public static final String HIGHLIGHT = "guiItems/highlight_square.gif";
	public static final String HAMMER = "guiItems/icon_hammer2.gif";
	public static final String HAMMER2 = "guiItems/TO_hammer.gif";
	public static final String RUN_ICON = "guiItems/run.gif";
	public static final String CHEST_OPEN = "guiItems/brown-trunk-open.png";
	public static final String FLOOR_BG = "guiItems/dan-rozanski-stone-floor-a1-baking.png";

	public static String SC_MOVE = "gui/move_element2.png";
	public static String SC_CIRCLE_WHITE = "gui/circle_white.png";
	public static String SC_RECT = "gui/quadrat.png";
	public static String SC_DOOR_CLOSED_KEY = "gui/door_element_closed_key.png";
	public static String SC_DOOR_CLOSED_NOKEY = "gui/door_element_closed_notunlockable.png";
	public static String SC_DOOR_OPEN_KEY = "gui/door_element_open_lockable.png";
	public static String SC_DOOR_OPEN_NOKEY = "gui/door_element_open_nokey.png";
	public static String SC_STEP_ELEMENT_POSSIBLE = "gui/step_element.png";
	public static String SC_STEP_ELEMENT_ENEMY = "gui/step_element_enemy.png";
	public static String SC_STEP_ELEMENT_FRIEND = "gui/step_element_friend.png";
	public static String SC_STEP_ELEMENT_SELF = "gui/step_element_self.png";

	public static final String BLACK_BACKGROUND = "gui/black.png";
	public static final String NO_IMAGE = "guiItems/no_image.gif";

	private final AbstractImageLoader loader;

	public GUIImageManager(AbstractImageLoader loader) {
		this.loader = loader;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static JDImageProxy<?> getImageProxy(String file,
			AbstractImageLoader<?> loader) {
		String prefix = "guiItems/";
		if(!file.startsWith(prefix)) {
			file = prefix + file;
		}
		return new JDImageProxy(loader, file);
	}

	public Image getImage(String file) {
		return (Image) new JDImageProxy(loader, file).getImage();
	}

	public JDImageProxy getJDImage(String file) {
		return new JDImageProxy(loader, file);
	}

	public JDImageProxy getJDImageProxy(String file) {
		return new JDImageProxy(loader, file);
	}
}
