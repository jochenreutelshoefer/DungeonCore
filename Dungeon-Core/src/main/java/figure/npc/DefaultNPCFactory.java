package figure.npc;

import android.util.Log;
import figure.hero.Character;
import figure.hero.Hero;
import figure.hero.HeroUtil;
import figure.hero.Profession;
import figure.hero.Zodiac;

import static figure.hero.Hero.*;

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
			Log.w(DefaultNPCFactory.class.getSimpleName(), message);
			throw new IllegalArgumentException(message);
		}
		Hero npc = HeroUtil.getBasicHero(heroCode, name, Zodiac.Twin, Profession.Hunter);
		Character character = new Character(name, values, npc);
		npc.setCharacter(character);
		return npc;
	}
}
