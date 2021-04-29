package de.jdungeon.app;

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.PrefixFileHandleResolver;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.jdungeon.asset.FilelistFilenameLister;
import de.jdungeon.game.RenderLoopWorldUpdateInitializer;
import de.jdungeon.util.UUIDGenerator;

import java.util.UUID;

/**
 * @author Jochen Reutelshoefer (denkbares GmbH)
 * @created 14.12.19.
 */
public class DungeonSimpleDesktopMain {

    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "Libgdx Game";
        cfg.useGL30 = false;
        cfg.width = 800;
        cfg.height = 480;
        cfg.allowSoftwareMode = true;
        cfg.foregroundFPS = 70;
        new LwjglApplication(new LibgdxDungeonSimpleMain(
                new DesktopResourceBundleLoader(),
                new FilelistFilenameLister(),
                new RenderLoopWorldUpdateInitializer(),
                new PrefixFileHandleResolver(new InternalFileHandleResolver(), "assets/"),
                () -> UUID.randomUUID().toString()),
                cfg);
    }
}
