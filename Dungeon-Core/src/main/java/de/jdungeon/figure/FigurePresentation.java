package de.jdungeon.figure;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 03.05.20.
 */
public enum FigurePresentation {

	DarkDwarf("darkdwarf"),
	Druid("druid"),
	Fir("fir"),
	Ghul("ghul"),
	Lioness("lioness"),
	Mage("mage"),
	Orc("orc"),
	Skeleton("skel"),
	Spider("spider"),
	Sailor("thief"),
	Troll("ogre"),
	Warrior("warrior"),
	WolfGrey("wolf");


	private final String filepath;

	FigurePresentation(String filepath) {
		this.filepath = filepath;
	}

	public String getFilepath() {
		return filepath;
	}
}
