package de.jdungeon.app.gui.dungeonselection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.jdungeon.log.Log;

import de.jdungeon.game.AbstractImageLoader;

import de.jdungeon.game.FileIO;
import de.jdungeon.game.Game;
import de.jdungeon.game.Image;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 31.07.16.
 */
public class LevelIconImageManager {

	private static LevelIconImageManager instance;
	boolean initialized = false;

	/*
	Lazy initialization of LevelIconImageManager's file names
	 */
	public void init(Game game, String levelIconsPath) {
		if(initialized) return;
		try {
			//if(levelIconsPath == null || levelIconsPath.length() == 0) {
			//	levelIconsPath = "assets/levelIcons";

			//}
			FileIO fileIO = game.getFileIO();

			List<String> filenames = fileIO.readFileNamesOfFolder(levelIconsPath);
			List<String> fullFilenames = new ArrayList<String>();
			for (String filename : filenames) {
				String fullFileName = levelIconsPath + "/" + filename;
				if (!fileIO.fileExists(fullFileName)) {
					Log.warning("file not found: " + fullFileName);
				}
				else {
					fullFilenames.add(fullFileName);
				}
			}
			LevelIconImageManager.getInstance().setFilenames(fullFilenames);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		initialized = true;

	}

	public static LevelIconImageManager getInstance() {
		if(instance == null) {
			instance =new LevelIconImageManager();
		}
		return instance;
	}

	private List<String> filenames;

	private void setFilenames(List<String> names) {
		Collections.sort(names);
		this.filenames = Collections.unmodifiableList(names);
	}

	public Image[] getIcons(AbstractImageLoader loader) {
		return null;
	}

	public Image getIcon(AbstractImageLoader loader, int iconNumber) {
		if(! initialized) {
			Log.severe(this.getClass().getName()+ " not yet (correctly) initialized!");
		}
		return (Image) loader.loadImage(filenames.get(iconNumber));
	}
}
