package de.jdungeon.app.gui.skillselection;

import spell.AbstractSpell;
import spell.SpellInfo;

import de.jdungeon.app.gui.GUIImageManager;
import de.jdungeon.app.gui.activity.SkillActivityProvider;

public class SkillImageManager {

	private final GUIImageManager guiImageManager;

	public SkillImageManager(GUIImageManager guiImageManager) {
		this.guiImageManager = guiImageManager;
	}

	public String getImage(Object o) {

		String im = null;
		if (o.equals(SkillActivityProvider.ATTACK)) {
			im = GUIImageManager.SWORD_ICON;
		}
		else if (o.equals(SkillActivityProvider.SCOUT)) {
			im = "guiItems/eye130x130.png";
		}
		else if (o.equals(SkillActivityProvider.FLEE)) {
			im = GUIImageManager.FOOT_ICON;
		}
		else if (o.equals(SkillActivityProvider.LOOK)) {
			im = GUIImageManager.LUPE;
		}
		else if (o instanceof SpellInfo) {
			SpellInfo spell = ((SpellInfo) o);
			int spellType = spell.getType();
			if (spellType == AbstractSpell.SPELL_GOLDENHIT || spellType == AbstractSpell.SPELL_RAID) {
				im = GUIImageManager.TARGET_ICON;
			}
			else if (spellType == AbstractSpell.SPELL_HEAL) {
				im = GUIImageManager.HEART_ICON;
			}
			else if (spellType == AbstractSpell.SPELL_REPAIR) {
				im = GUIImageManager.HAMMER;
			}
			else {
				im = GUIImageManager.NO_IMAGE;
			}
		}
		return im;
	}

}
