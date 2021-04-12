package de.jdungeon.preprocessing;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 04.01.20.
 */
import com.badlogic.gdx.tools.FileProcessor;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import org.apache.commons.io.*;
import org.apache.commons.io.filefilter.*;

import java.io.*;
import java.util.*;

import de.jdungeon.asset.Assets;

public class AtlasGenerator {

	static final String TARGET_DIR = "Libgdx-Dungeon-Desktop/src/main/resources/packs/";

	public static void main (String[] args) throws Exception {
		//String atlasName = FigurePresentation.DarkDwarf.getFilepath();
		String atlasName = "de/jdungeon/gui";
		//String SOURCE_DIR = "Libgdx-Dungeon-Desktop/src/main/resources/pics/de.jdungeon.animation/"+atlasName; //de.jdungeon.animation/
		String SOURCE_DIR = "Libgdx-Dungeon-Desktop/src/main/resources/pics/"+atlasName; //de.jdungeon.animation/

		//Delete old pack
		File oldPackFile = new File(TARGET_DIR + "/" + atlasName + Assets.ATLAS_FILE_EXTENSION);
		if (oldPackFile.exists()){
			System.out.println("Deleting old pack file");
			oldPackFile.delete();
		}

		//Delete old font files
		Collection<File> oldFontFiles = FileUtils.listFiles(
				new File(TARGET_DIR),
				new RegexFileFilter(".*\\.fnt"),
				TrueFileFilter.INSTANCE
		);
		for (File file : oldFontFiles){
			System.out.println("Copying font file: " + file.getName());
			FileUtils.deleteQuietly(file);
		}

		//Create PNGs for GIF frames
		GifProcessor gifProcessor = new GifProcessor(0.015f);
		ArrayList<FileProcessor.Entry> gifFrames = gifProcessor.process(SOURCE_DIR, SOURCE_DIR);

		//Pack them
		TexturePacker.Settings settings = new TexturePacker.Settings();
		settings.atlasExtension = Assets.ATLAS_FILE_EXTENSION;
		settings.maxWidth = 2048;
		settings.maxHeight = 2048;
		TexturePacker.process(
				settings,
				SOURCE_DIR,
				TARGET_DIR,
				atlasName);


		//Copy over any fonts
		Collection<File> fontFiles = FileUtils.listFiles(
				new File(SOURCE_DIR),
				new RegexFileFilter(".*\\.fnt"),
				TrueFileFilter.INSTANCE
		);
		File destDir = new File(TARGET_DIR);
		for (File file : fontFiles){
			System.out.println("Copying font file: " + file.getName());
			FileUtils.copyFileToDirectory(file, destDir);
		}

		//Delete the GIF frames that were generated.
		for (File file : gifProcessor.getGeneratedFiles())
			file.delete();
	}


}
