package de.jdungeon.libgdx.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import de.jdungeon.androidapp.io.AndroidImageLoader;
import de.jdungeon.game.AbstractImageLoader;
import de.jdungeon.game.FileIO;

public class LibgdxFileIO implements FileIO {
    Context context;
    AssetManager assets;
    String externalStoragePath;

    public LibgdxFileIO(Context context) {
        this.context = context;
        this.assets = context.getAssets();
        this.externalStoragePath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator;
    }



    @Override
    public InputStream readAsset(String file) throws IOException {
        return assets.open(file);
    }

	public static AbstractImageLoader<?> loader;

	@Override
	public AbstractImageLoader<?> getImageLoader() {
		if (loader == null) {
			loader = new AndroidImageLoader((LibgdxGame) context);
		}
		return loader;
	}

	@Override
    public InputStream readFile(String file) throws IOException {
        return new FileInputStream(externalStoragePath + file);
    }

	@Override
	public List<String> readFileNamesOfFolder(String path) throws IOException {
		return Arrays.asList(assets.list(path));
	}

	@Override
    public OutputStream writeFile(String file) throws IOException {
        return new FileOutputStream(externalStoragePath + file);
    }

}