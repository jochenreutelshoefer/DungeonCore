package de.jdungeon.game;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import de.jdungeon.io.FilenameLister;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.12.19.
 */
public class LibgdxFileIO implements FileIO {

    AbstractImageLoader imageLoader;
    FilenameLister filenameLister;

    public LibgdxFileIO(AbstractImageLoader imageLoader, FilenameLister filenameLister) {
        this.imageLoader = imageLoader;
        this.filenameLister = filenameLister;
    }

    public LibgdxFileIO() {
        imageLoader = new LibgdxImageLoader();
    }

    @Override
    public boolean fileExists(String file) throws IOException {
        return Gdx.files.internal(file).exists();
    }

    @Override
    public InputStream readFile(String file) throws IOException {
        return readAsset(file);
    }

    @Override
    public List<String> readFileNamesOfFolder(String file) {
        return this.filenameLister.listFilenamesOfFolder(file);
    }


    @Override
    public OutputStream writeFile(String file) throws IOException {
        return null;
    }

    @Override
    public InputStream readAsset(String file) throws IOException {
        FileHandle fileHandle = Gdx.files.internal(file);
        if (!fileHandle.exists()) {
            throw new IOException("File does not exist: " + file);
        }
        return fileHandle.read();

    }

    @Override
    public AbstractImageLoader<?> getImageLoader() {
        return imageLoader;
    }
}
