package de.jdungeon.preprocessing.filelists;

import com.badlogic.gdx.utils.StringBuilder;

import de.jdungeon.log.Log;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static de.jdungeon.asset.Assets.ASSET_FOLDER;
import static de.jdungeon.asset.FilelistFilenameLister.FILELIST_FILENAME;

public class FileListPreprocessor {

	public static void main(String[] args) throws IOException {
		Log.info("Starting " + FileListPreprocessor.class.getSimpleName());
		File assetFolder = new File(ASSET_FOLDER);
		File[] assetContentFolders = assetFolder.listFiles(File::isDirectory);
		for (File assetContentFolder : assetContentFolders) {
			createFilelistFile(assetContentFolder);
		}
		Log.info("Completed " + FileListPreprocessor.class.getSimpleName());
	}

	private static void createFilelistFile(File assetContentFolder) throws IOException {
		File[] files = assetContentFolder.listFiles(File::isFile);
		StringBuilder buffy = new StringBuilder();
		for (File file : files) {
			if (file.getName().equals(FILELIST_FILENAME)) {
				// we don't want to have the file-list-file itself in the list of file
				continue;
			}
			buffy.append(file.getName() + "\n");
		}
		Log.info("Files in folder " + assetContentFolder + ": \n" + buffy);
		File fileListFile = new File(assetContentFolder.getPath(), FILELIST_FILENAME);
		Files.write(fileListFile.toPath(), buffy.toString()
				.getBytes(Charset.defaultCharset()), StandardOpenOption.CREATE);
		Log.info("Wrote file list: " + fileListFile.getAbsolutePath() + "\n\n");

		File[] folders = assetContentFolder.listFiles(File::isDirectory);
		for (File folder : folders) {
			createFilelistFile(folder);
		}
	}
}
