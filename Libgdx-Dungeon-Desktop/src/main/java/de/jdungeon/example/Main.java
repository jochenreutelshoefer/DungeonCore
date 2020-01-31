package de.jdungeon.example;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import de.jdungeon.DesktopResourceBundleLoader;
import de.jdungeon.LibgdxDungeonMain;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 14.12.19.
 */
public class Main {

	public static final boolean rebuildAtlas = false;
	public static final boolean drawDebugOutline = true;

	public static void main(String[] args) {

		if(rebuildAtlas) {
			TexturePacker.Settings settings = new TexturePacker.Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.debug = drawDebugOutline;
			TexturePacker.process(settings, "Libgdx-Dungeon-Desktop/assets/data", "Libgdx-Dungeon-Desktop/assets/packs/", "canyonbunny.pack");
			//TexturePacker.process(settings, "Libgdx-Dungeon-Desktop/assets/pics/dungeon", "Libgdx-Dungeon-Desktop/assets/packs/", "dungeon.pack");

		}

		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Java Dungeon Libgdx";
		//cfg.vSyncEnabled = false; // not getting more than 60 fps
		//cfg.forceExit = true;
		cfg.useGL30 = false;
		cfg.width = 800;
		cfg.height = 480;
		new LwjglApplication(new LibgdxDungeonMain(new DesktopResourceBundleLoader()), cfg);
	}
}
