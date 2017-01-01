package de.jdungeon.androidapp.gui;

import spell.Spell;
import spell.SpellInfo;

import de.jdungeon.androidapp.gui.itemWheel.ItemWheelActivity;
import de.jdungeon.androidapp.gui.itemWheel.SkillActivityProvider;
import de.jdungeon.game.Game;
import de.jdungeon.game.Image;

public class SkillImageManager {

	private final GUIImageManager guiImageManager;

	public SkillImageManager(GUIImageManager guiImageManager) {
		this.guiImageManager = guiImageManager;
	}

	public Image getSkillImage(ItemWheelActivity activity) {
		Image im = null;
		Object o = activity.getObject();

		if (o.equals(SkillActivityProvider.ATTACK)) {
			im = guiImageManager.getImage(GUIImageManager.SWORD_ICON);
		} else if (o.equals(SkillActivityProvider.SCOUT)) {
			im = guiImageManager.getImage(GUIImageManager.SPY_ICON);
			// } else if (o.equals(WALK)) {
			// im = GUIImageManager.getImage(GUIImageManager.FOOT_ICON,
			// screen.getGame());
		} else if (o.equals(SkillActivityProvider.FLEE)) {
			im = guiImageManager.getImage(GUIImageManager.FOOT_ICON);
		} else if (o.equals(SkillActivityProvider.LOOK)) {
			im = guiImageManager.getImage(GUIImageManager.LUPE);
		} else if (o instanceof SpellInfo) {
			SpellInfo spell = ((SpellInfo) o);
			int spellType = spell.getType();
			if (spellType == Spell.SPELL_GOLDENHIT) {
				im = guiImageManager.getImage(GUIImageManager.TARGET_ICON);
			} else if (spellType == Spell.SPELL_HEAL) {
				im = guiImageManager.getImage(GUIImageManager.HEART_ICON);
			} else if (spellType == Spell.SPELL_REPAIR) {
				im = guiImageManager.getImage(GUIImageManager.HAMMER);
			} else {
				im = guiImageManager.getImage(GUIImageManager.NO_IMAGE);
			}
		}
		return im;
	}

}
