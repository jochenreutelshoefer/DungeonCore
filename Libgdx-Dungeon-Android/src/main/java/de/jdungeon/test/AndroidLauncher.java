package de.jdungeon.test;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 14.12.19.
 */

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import de.jdungeon.LibgdxDungeonMain;
import de.jdungeon.androidapp.io.AndroidResourceBundleLoader;
import de.jdungeon.io.ResourceBundleLoader;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;
		ResourceBundleLoader loader;
		// todo: add resourceBundle loader
		initialize(new LibgdxDungeonMain(new AndroidResourceBundleLoader(this.getApplicationContext(), "")), config);
	}
}