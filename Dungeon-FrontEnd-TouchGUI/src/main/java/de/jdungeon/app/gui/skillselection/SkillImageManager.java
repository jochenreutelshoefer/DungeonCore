package de.jdungeon.app.gui.skillselection;

import spell.AbstractSpell;
import spell.SpellInfo;

import de.jdungeon.app.gui.GUIImageManager;
import de.jdungeon.app.gui.activity.Activity;
import de.jdungeon.app.gui.activity.SkillActivityProvider;
import de.jdungeon.game.Image;

public class SkillImageManager {

	private final GUIImageManager guiImageManager;

	public SkillImageManager(GUIImageManager guiImageManager) {
		this.guiImageManager = guiImageManager;
	}

	public Image getSkillImage(Activity activity) {
		return getImage(activity.getObject());
	}

	public Image getImage(Object o) {

		Image im = null;
		if (o.equals(SkillActivityProvider.ATTACK)) {
			im = guiImageManager.getImage(GUIImageManager.SWORD_ICON);
		}
		else if (o.equals(SkillActivityProvider.SCOUT)) {
			//im = guiImageManager.getImage("guiItems/spy-icon.gif");
			//im = guiImageManager.getImage("guiItems/44741-eye-shape-variant-interface-view-symbol.png");
			im = guiImageManager.getImage("guiItems/eye130x130.png");
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
