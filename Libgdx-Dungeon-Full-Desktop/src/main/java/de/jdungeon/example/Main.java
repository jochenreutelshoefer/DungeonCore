package de.jdungeon.example;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.jdungeon.DesktopResourceBundleLoader;
import de.jdungeonx.LibgdxDungeonFullMain;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 14.12.19.
 */
public class Main {

    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Libgdx Game";
        cfg.useGL30 = false;
        cfg.width = 800;
        cfg.height = 480;
        cfg.allowSoftwareMode = true;
        cfg.foregroundFPS = 70;
        new LwjglApplication(new LibgdxDungeonFullMain(new DesktopResourceBundleLoader(), new DesktopFilenameLister(), new DistinctWorldUpdateThreadInitializer()), cfg);
    }
}
