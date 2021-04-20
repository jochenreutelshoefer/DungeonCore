package de.jdungeon.example;

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.PrefixFileHandleResolver;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.jdungeon.DesktopResourceBundleLoader;
import de.jdungeon.asset.FilelistFilenameLister;
import de.jdungeon.game.RenderLoopWorldUpdateInitializer;
import de.jdungeon.LibgdxDungeonFullMain;

import java.util.UUID;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 14.12.19.
 */
public class Main {

    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Libgdx Game";
        cfg.useGL30 = false;
        cfg.width = 1200;
        cfg.height = 880;
        cfg.allowSoftwareMode = true;
        cfg.foregroundFPS = 70;
        //new LwjglApplication(new LibgdxDungeonFullMain(new DesktopResourceBundleLoader(), new DesktopFilenameLister(), new DistinctWorldUpdateThreadInitializer()), cfg);
        new LwjglApplication(
                new LibgdxDungeonFullMain(
                        new DesktopResourceBundleLoader(),
                        new FilelistFilenameLister(),
                        new RenderLoopWorldUpdateInitializer(),
                        new PrefixFileHandleResolver(new InternalFileHandleResolver(), "assets/"),
                        () -> UUID.randomUUID().toString()
                )
                , cfg);
    }
}
