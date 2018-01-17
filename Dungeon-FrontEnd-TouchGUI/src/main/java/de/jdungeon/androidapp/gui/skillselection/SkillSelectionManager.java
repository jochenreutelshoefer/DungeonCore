package de.jdungeon.androidapp.gui.skillselection;

import java.util.HashMap;
import java.util.Map;

import spell.Bonebreaker;
import spell.Convince;
import spell.Discover;
import spell.Fireball;
import spell.GoldenHit;
import spell.GoldenThrow;
import spell.Heal;
import spell.Isolation;
import spell.KeyLocator;
import spell.Light;
import spell.Raid;
import spell.Search;
import spell.Spell;
import spell.Spy;
import spell.Steal;

import de.jdungeon.util.Pair;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 17.01.18.
 */
public class SkillSelectionManager {

	private static SkillSelectionManager instance;

	public static SkillSelectionManager getInstance() {
		if(instance == null) {
			instance = new SkillSelectionManager();
		}
		return instance;
	}


	private SkillSelectionManager() {
	}

	private static final Map<Integer, Pair<Spell, Spell>> skillMap = new HashMap<>();

	static {
		skillMap.put(1, new Pair<Spell, Spell>(new Raid(), new Heal()));
		skillMap.put(2, new Pair<Spell, Spell>(new Search(), new Fireball()));
		skillMap.put(3, new Pair<Spell, Spell>(new Steal(), new Discover()));
		skillMap.put(4, new Pair<Spell, Spell>(new Convince(), new Bonebreaker()));
		skillMap.put(5, new Pair<Spell, Spell>(new GoldenHit(), new KeyLocator()));
		skillMap.put(6, new Pair<Spell, Spell>(new Spy(), new GoldenThrow()));
		skillMap.put(7, new Pair<Spell, Spell>(new Isolation(), new Light()));
	}

	public Pair<Spell, Spell> getOptions(int stage) {
		return skillMap.get(stage);
	}

}
