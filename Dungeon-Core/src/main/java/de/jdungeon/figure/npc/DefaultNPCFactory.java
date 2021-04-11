package de.jdungeon.figure.npc;

import de.jdungeon.figure.hero.Character;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.figure.hero.HeroUtil;
import de.jdungeon.figure.hero.Profession;
import de.jdungeon.figure.hero.Zodiac;
import de.jdungeon.log.Log;

import static de.jdungeon.figure.hero.Hero.*;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 13.08.16.
 */
public class DefaultNPCFactory {

	public static Hero createDefaultNPC(String name, int heroCode) {
		double [] values = null;
		if(heroCode == HEROCODE_DRUID) {
			values = HeroUtil.druidBasic;
		}
		if(heroCode == HEROCODE_WARRIOR) {
			values = HeroUtil.warriorBasic;
		}
		if(heroCode == HEROCODE_HUNTER) {
			values = HeroUtil.hunterBasic;
		}
		if(heroCode == HEROCODE_MAGE) {
			values = HeroUtil.mageBasic;
		}
		if(values == null) {
			String message = "hero category not found";
			Log.warning(message);
			throw new IllegalArgumentException(message);
		}
		Hero npc = HeroUtil.getBasicHero(heroCode, name, Zodiac.Twin, Profession.Hunter);
		Character character = new Character(name, values, npc);
		npc.setCharacter(character);
		return npc;
	}
}
