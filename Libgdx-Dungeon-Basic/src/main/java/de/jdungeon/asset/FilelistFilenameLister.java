package de.jdungeon.asset;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import de.jdungeon.io.FilenameLister;
import de.jdungeon.log.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FilelistFilenameLister implements FilenameLister {

    public static final String FILELIST_FILENAME = "filelist.txt";

    @Override
    public List<String> listFilenamesOfFolder(String folder) {
        String filenamefilePath = folder + "/" + FILELIST_FILENAME;
        FileHandle fileHandleFolder = Gdx.files.getFileHandle(filenamefilePath, Files.FileType.Internal);
        if (!fileHandleFolder.exists()) {
            Log.severe("File list for asset subfolder not found: " + folder + " (" + filenamefilePath + ")");
            return Collections.emptyList();
        }
        String content = fileHandleFolder.readString();
        String[] files = content.split("\n");
        return Arrays.asList(files);
    }
}
