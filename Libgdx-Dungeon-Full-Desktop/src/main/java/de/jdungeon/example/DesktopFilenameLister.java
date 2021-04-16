package de.jdungeon.example;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import de.jdungeon.io.FilenameLister;

import java.util.ArrayList;
import java.util.List;

public class DesktopFilenameLister implements FilenameLister {

    private static final String RESOURCES = "src/main/resources/";
    @Override
    public List<String> listFilenamesOfFolder(String folder) {
        String classPathLocation = null;
        classPathLocation = this.getClass().getClassLoader().getResource(folder).getFile();
        FileHandle classpath = Gdx.files.classpath("foo");
        String externalStoragePath = Gdx.files.getExternalStoragePath();
        String localStoragePath = Gdx.files.getLocalStoragePath();
        if (classPathLocation.contains("%20")) {
            classPathLocation = classPathLocation.replaceAll("%20", " ");
        }

        String path = localStoragePath + RESOURCES + folder;
        FileHandle fileHandleFolder = Gdx.files.getFileHandle(folder, Files.FileType.Internal);
        boolean exists = fileHandleFolder.exists();
        FileHandle[] fileHandles = fileHandleFolder.list();
        List<String> filenames = new ArrayList<>();
        for (FileHandle fileHandle : fileHandles) {
            filenames.add(fileHandle.name());
        }
        return filenames;
    }
}
