package de.jdungeon.libgdx;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import de.jdungeon.game.AbstractImageLoader;
import de.jdungeon.game.FileIO;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.12.19.
 */
public class LibgdxFileIO implements FileIO {

	AbstractImageLoader imageLoader = new LibgdxImageLoader();

	@Override
	public boolean fileExists(String file) throws IOException {
		return Gdx.files.internal(file).exists();
	}

	@Override
	public InputStream readFile(String file) throws IOException {
		return readAsset(file);
	}

	@Override
	public List<String> readFileNamesOfFolder(String file) throws IOException {
		FileHandle[] fileHandles = Gdx.files.getFileHandle(file, Files.FileType.Internal).list();
		List<String> filenames = new ArrayList<>();
		for (FileHandle fileHandle : fileHandles) {
			filenames.add(fileHandle.name());
		}
		return filenames;
	}

	@Override
	public OutputStream writeFile(String file) throws IOException {
		throw new NotImplementedException();
	}

	@Override
	public InputStream readAsset(String file) throws IOException {
		FileHandle fileHandle = Gdx.files.internal(file);
		if(!fileHandle.exists()) {
			throw new IOException("File does not exist: "+file);
		}
		return fileHandle.read();

	}

	@Override
	public AbstractImageLoader<?> getImageLoader() {
		return imageLoader ;
	}
}
