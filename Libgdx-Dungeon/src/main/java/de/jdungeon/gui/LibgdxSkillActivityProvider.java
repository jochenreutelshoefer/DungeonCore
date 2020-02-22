package de.jdungeon.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import figure.FigureInfo;
import figure.hero.HeroInfo;
import game.InfoEntity;
import game.RoomInfoEntity;
import spell.AbstractSpell;
import spell.Raid;
import spell.SpellInfo;
import spell.TargetScope;

import de.jdungeon.app.ActionAssembler;
import de.jdungeon.app.audio.AudioManagerTouchGUI;
import de.jdungeon.app.gui.GUIImageManager;
import de.jdungeon.app.gui.activity.Activity;
import de.jdungeon.app.gui.activity.DefaultActivity;
import de.jdungeon.app.gui.activity.ExecutableActivity;
import de.jdungeon.app.gui.skillselection.SkillImageManager;
import de.jdungeon.app.gui.smartcontrol.UIFeedback;
import de.jdungeon.gui.activity.AttackActivity;
import de.jdungeon.gui.activity.FleeActivity;
import de.jdungeon.gui.activity.SpellActivity;
import de.jdungeon.world.GUIRenderer;

public class LibgdxSkillActivityProvider implements LibgdxActivityProvider {


	private static final Map<Class<? extends ExecutableActivity>, String> skillImageMap = new HashMap<>();

	static {
		skillImageMap.put(AttackActivity.class, GUIImageManager.SWORD_ICON);
		skillImageMap.put(FleeActivity.class, GUIImageManager.FOOT_ICON);
	}


	private final HeroInfo info;
	private final ActionAssembler actionAssembler;
	private final LibgdxFocusManager focusManager;
	private boolean fightState = false;

	private final List<Activity> activityCache = new ArrayList<>();


	private final Activity attack;
	private final Activity flee;


	public LibgdxSkillActivityProvider(HeroInfo info, ActionAssembler actionAssembler, LibgdxFocusManager focusManager, GUIRenderer guiRenderer) {
		super();
		this.info = info;
		this.actionAssembler = actionAssembler;
		this.focusManager = focusManager;
		updateActivityList();

		attack = new AttackActivity(focusManager, actionAssembler, guiRenderer);
		flee = new FleeActivity(actionAssembler);
	}

	@Override
	public List<Activity> getActivities() {

		updateActivityList();
		// TODO: (when) do we need to update the list?

		return activityCache;
	}

	private void updateActivityList() {
		activityCache.clear();
		activityCache.add(attack);
		activityCache.add(flee);

		List<SpellInfo> spells = info.getSpells();
		for (SpellInfo spell : spells) {
				activityCache.add(new SpellActivity(spell, actionAssembler, focusManager));
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
