package de.jdungeon.androidapp.gui.dungeonselection;

import java.util.List;

import de.jdungeon.game.AbstractImageLoader;

import de.jdungeon.game.Image;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 31.07.16.
 */
public class LevelIconImageManager {

	private static LevelIconImageManager instance;

	public static LevelIconImageManager getInstance() {
		if(instance == null) {
			instance =new LevelIconImageManager();
		}
		return instance;
	}

	private List<String> filenames;

	public void setFilenames(List<String> names) {
		this.filenames = names;
	}

	public Image[] getIcons(AbstractImageLoader loader) {
		return null;
	}

	public Image getIcon(AbstractImageLoader loader, int iconNumber) {
		return (Image) loader.loadImage(filenames.get(iconNumber));
	}
}
