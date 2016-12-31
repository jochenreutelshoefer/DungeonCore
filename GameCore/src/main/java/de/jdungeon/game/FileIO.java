package de.jdungeon.game;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;


public interface FileIO {

	InputStream readFile(String file) throws IOException;

	List<String> readFileNamesOfFolder(String file) throws IOException;

    OutputStream writeFile(String file) throws IOException;

    InputStream readAsset(String file) throws IOException;

	/*
    SharedPreferences getSharedPref();
    */
}
