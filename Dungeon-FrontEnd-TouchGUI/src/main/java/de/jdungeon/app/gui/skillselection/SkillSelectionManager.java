package de.jdungeon.app.gui.skillselection;

import java.util.HashMap;
import java.util.Map;

import de.jdungeon.spell.Bonebreaker;
import de.jdungeon.spell.Convince;
import de.jdungeon.spell.Fireball;
import de.jdungeon.spell.Heal;
import de.jdungeon.spell.Isolation;
import de.jdungeon.spell.KeyLocator;
import de.jdungeon.spell.Light;
import de.jdungeon.spell.Raid;
import de.jdungeon.spell.Search;
import de.jdungeon.spell.Spell;
import de.jdungeon.spell.Spy;

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
		skillMap.put(1, new Pair<>(new Raid(), new Heal()));
		skillMap.put(2, new Pair<>(new Search(), new Fireball()));
		skillMap.put(3, new Pair<>(new Convince(), new Bonebreaker()));
		skillMap.put(4, new Pair<>(new Spy(), new KeyLocator()));
		skillMap.put(5, new Pair<>(new Spy(), new Heal()));
		skillMap.put(6, new Pair<>(new Isolation(), new Light()));
	}

	public Pair<Spell, Spell> getOptions(int stage) {
		return skillMap.get(stage);
	}

}
