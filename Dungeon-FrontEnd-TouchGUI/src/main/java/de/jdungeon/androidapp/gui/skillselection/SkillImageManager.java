package de.jdungeon.androidapp.gui.skillselection;

import spell.AbstractSpell;
import spell.SpellInfo;

import de.jdungeon.androidapp.gui.GUIImageManager;
import de.jdungeon.androidapp.gui.itemWheel.ItemWheelActivity;
import de.jdungeon.androidapp.gui.itemWheel.SkillActivityProvider;
import de.jdungeon.game.Image;

public class SkillImageManager {

	private final GUIImageManager guiImageManager;

	public SkillImageManager(GUIImageManager guiImageManager) {
		this.guiImageManager = guiImageManager;
	}

	public Image getSkillImage(ItemWheelActivity activity) {
		return getImage(activity.getObject());
	}

	public Image getImage(Object o) {

		Image im = null;
		if (o.equals(SkillActivityProvider.ATTACK)) {
			im = guiImageManager.getImage(GUIImageManager.SWORD_ICON);
		}
		else if (o.equals(SkillActivityProvider.SCOUT)) {
			im = guiImageManager.getImage(GUIImageManager.SPY_ICON);
			// } else if (o.equals(WALK)) {
			// im = GUIImageManager.getImage(GUIImageManager.FOOT_ICON,
			// screen.getGame());
		}
		else if (o.equals(SkillActivityProvider.FLEE)) {
			im = guiImageManager.getImage(GUIImageManager.FOOT_ICON);
		}
		else if (o.equals(SkillActivityProvider.LOOK)) {
			im = guiImageManager.getImage(GUIImageManager.LUPE);
		}
		else if (o instanceof SpellInfo) {
			SpellInfo spell = ((SpellInfo) o);
			int spellType = spell.getType();
			if (spellType == AbstractSpell.SPELL_GOLDENHIT || spellType == AbstractSpell.SPELL_RAID) {
				im = guiImageManager.getImage(GUIImageManager.TARGET_ICON);
			}
			else if (spellType == AbstractSpell.SPELL_HEAL) {
				im = guiImageManager.getImage(GUIImageManager.HEART_ICON);
			}
			else if (spellType == AbstractSpell.SPELL_REPAIR) {
				im = guiImageManager.getImage(GUIImageManager.HAMMER);
			}
			else {
				im = guiImageManager.getImage(GUIImageManager.NO_IMAGE);
			}
		}
		return im;
	}

}
