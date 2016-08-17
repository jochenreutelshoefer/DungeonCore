package figure.npc;

import figure.hero.Character;
import figure.hero.Hero;
import figure.hero.HeroUtil;
import figure.hero.Zodiac;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 13.08.16.
 */
public class DefaultNPC extends Hero {

	public DefaultNPC(int heroCode, Zodiac sign, figure.hero.Character character) {
		super(heroCode, sign, character);
	}

	public static DefaultNPC createDefaultNPC(String name, int heroCode) {
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
		// TODO: refactor creation of hero objects
		DefaultNPC npc = new DefaultNPC(heroCode, Zodiac.Twin, null);
		Character character = new Character(name, values, npc);
		npc.setCharacter(character);
		return npc;
	}
}
