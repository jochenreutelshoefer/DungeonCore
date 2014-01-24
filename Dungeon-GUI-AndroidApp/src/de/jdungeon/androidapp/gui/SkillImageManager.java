package de.jdungeon.androidapp.gui;

import spell.Spell;
import spell.SpellInfo;
import de.jdungeon.androidapp.GameScreen;
import de.jdungeon.androidapp.gui.itemWheel.ItemWheelActivity;
import de.jdungeon.androidapp.gui.itemWheel.SkillActivityProvider;
import de.jdungeon.game.Image;

public class SkillImageManager {

	public static Image getSkillImage(ItemWheelActivity activity,
			GameScreen screen) {
		Image im = null;
		Object o = activity.getObject();

		if (o.equals(SkillActivityProvider.ATTACK)) {
			im = GUIImageManager.getImage(GUIImageManager.SWORD_ICON,
					screen.getGame());
		} else if (o.equals(SkillActivityProvider.SCOUT)) {
			im = GUIImageManager.getImage(GUIImageManager.SPY_ICON,
					screen.getGame());
			// } else if (o.equals(WALK)) {
			// im = GUIImageManager.getImage(GUIImageManager.FOOT_ICON,
			// screen.getGame());
		} else if (o.equals(SkillActivityProvider.FLEE)) {
			im = GUIImageManager.getImage(GUIImageManager.FOOT_ICON,
					screen.getGame());
		} else if (o.equals(SkillActivityProvider.LOOK)) {
			im = GUIImageManager.getImage(GUIImageManager.LUPE,
					screen.getGame());
		} else if (o instanceof SpellInfo) {
			SpellInfo spell = ((SpellInfo) o);
			int spellType = spell.getType();
			if (spellType == Spell.SPELL_GOLDENHIT) {
				im = GUIImageManager.getImage(GUIImageManager.TARGET_ICON,
						screen.getGame());
			} else if (spellType == Spell.SPELL_HEAL) {
				im = GUIImageManager.getImage(GUIImageManager.HEART_ICON,
						screen.getGame());
			} else if (spellType == Spell.SPELL_REPAIR) {
				im = GUIImageManager.getImage(GUIImageManager.HAMMER,
						screen.getGame());
			} else {
				im = GUIImageManager.getImage(GUIImageManager.NO_IMAGE,
						screen.getGame());
			}
		}
		return im;
	}

}
