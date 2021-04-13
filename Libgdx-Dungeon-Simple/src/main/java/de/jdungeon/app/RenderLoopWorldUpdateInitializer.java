package de.jdungeon.app;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.game.DungeonWorldUpdater;
import de.jdungeon.game.DungeonWorldUpdaterInitializer;
import de.jdungeon.game.DungeonWorldUpdaterRenderLoop;
import de.jdungeon.game.JDGUI;

public class RenderLoopWorldUpdateInitializer  implements DungeonWorldUpdaterInitializer {
    @Override
    public DungeonWorldUpdater initializeWorldUpdate(Dungeon dungeon, Hero hero, JDGUI gui) {
        return new DungeonWorldUpdaterRenderLoop(dungeon);
    }
}
