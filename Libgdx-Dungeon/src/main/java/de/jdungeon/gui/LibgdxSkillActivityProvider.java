package de.jdungeon.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import figure.hero.HeroInfo;
import spell.AbstractSpell;
import spell.SpellInfo;

import de.jdungeon.app.gui.GUIImageManager;
import de.jdungeon.app.gui.activity.Activity;
import de.jdungeon.app.gui.activity.ExecutableActivity;
import de.jdungeon.gui.activity.AttackActivity;
import de.jdungeon.gui.activity.FleeActivity;
import de.jdungeon.gui.activity.SpellActivity;
import de.jdungeon.world.PlayerController;

public class LibgdxSkillActivityProvider implements LibgdxActivityProvider {


	private static final Map<Class<? extends ExecutableActivity>, String> skillImageMap = new HashMap<>();

	static {
		skillImageMap.put(AttackActivity.class, GUIImageManager.SWORD_ICON);
		skillImageMap.put(FleeActivity.class, GUIImageManager.FOOT_ICON);
	}


	private final HeroInfo info;
	private final PlayerController controller;
	private final List<Activity> activityCache = new ArrayList<>();



	public LibgdxSkillActivityProvider(PlayerController controller) {
		super();
		this.info = controller.getHeroInfo();
		this.controller = controller;
		updateActivityList();
	}

	@Override
	public List<Activity> getActivities() {

		updateActivityList();
		// TODO: (when) do we need to update the list?

		return activityCache;
	}

	private void updateActivityList() {
		activityCache.clear();
		activityCache.add(controller.getAttackActivity());
		activityCache.add(controller.getFleeActivity());

		// todo: shouldn't these spell activities somehow also obtained from the controller?
		List<SpellInfo> spells = info.getSpells();
		for (SpellInfo spell : spells) {
				activityCache.add(new SpellActivity(spell, controller));
		}
	}

	@Override
	public String getActivityImage(Activity a) {
		if(a  == null) return null;
		if (skillImageMap.containsKey(a.getClass())) {
			return skillImageMap.get(a.getClass());
		}

		if (a instanceof SpellActivity) {
			SpellInfo spell = ((SpellInfo) a.getObject());
			int spellType = spell.getType();
			if (spellType == AbstractSpell.SPELL_GOLDENHIT || spellType == AbstractSpell.SPELL_RAID) {
				return GUIImageManager.TARGET_ICON;
			}
			else if (spellType == AbstractSpell.SPELL_HEAL) {
				return GUIImageManager.HEART_ICON;
			}
		}
		return GUIImageManager.NO_IMAGE;
	}

	@Override
	public void activityPressed(Activity activity) {
		if (activity == null) {
			return;
		}

		if (activity instanceof ExecutableActivity) {
			((ExecutableActivity) activity).execute();
			return;
		}

	}

}