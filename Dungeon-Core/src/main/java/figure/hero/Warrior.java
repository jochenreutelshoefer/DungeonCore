package figure.hero;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 10.02.20.
 */
public class Warrior extends Hero {

	public Warrior(String name, int heroCode, Zodiac sign, int HealthVal, int StrengthVal, int DexterityVal, int PsychoVal, int Axe, int Lance, int Sword, int Club, int Wolfknife, int nature, int creature, int undead, int scout, int dust, double dustReg, int ch) {
		super(name, heroCode, sign, HealthVal, StrengthVal, DexterityVal, PsychoVal, Axe, Lance, Sword, Club, Wolfknife, nature, creature, undead, scout, dust, dustReg, ch);
	}

	public Warrior(int heroCode, Zodiac sign, Character character) {
		super(heroCode, sign, character);
	}

	@Override
	public int getHeroCode() {
		return HeroCategory.Warrior.getCode();
	}
}
