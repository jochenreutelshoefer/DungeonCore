package de.jdungeon.adapter.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import de.jdungeon.adapter.graphics.LibgdxImageLoader;
import de.jdungeon.game.AbstractImageLoader;
import de.jdungeon.game.FileIO;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 21.12.19.
 */
public class LibgdxFileIO implements FileIO {

	AbstractImageLoader imageLoader = new LibgdxImageLoader();

	@Override
	public InputStream readFile(String file) throws IOException {
		throw new NotImplementedException();
	}

	@Override
	public List<String> readFileNamesOfFolder(String file) throws IOException {
		throw new NotImplementedException();
	}

	@Override
	public OutputStream writeFile(String file) throws IOException {
		throw new NotImplementedException();
	}

	@Override
	public InputStream readAsset(String file) throws IOException {
		throw new NotImplementedException();
	}

	@Override
	public AbstractImageLoader<?> getImageLoader() {
		return imageLoader ;
	}
}
