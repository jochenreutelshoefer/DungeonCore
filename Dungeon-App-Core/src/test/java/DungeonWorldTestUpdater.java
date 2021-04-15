import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.game.DungeonWorldUpdater;
import de.jdungeon.game.GameLoopMode;
import de.jdungeon.game.JDGUI;
import de.jdungeon.log.Log;
import de.jdungeonx.DungeonGameLoop;

public class DungeonWorldTestUpdater implements DungeonWorldUpdater {

    private final DungeonGameLoop dungeonGameLoop;

    DungeonWorldTestUpdater(Dungeon dungeon) {
        dungeonGameLoop = new DungeonGameLoop(dungeon, this);
    }

    @Override
    public void update() {
        // we do not need to do something, as there is a distinct thread running performing the updates
    }

    @Override
    public void stopRunning() {
        dungeonGameLoop.stopRunning();
    }

    @Override
    public int getCurrentGameRound() {
        return dungeonGameLoop.getRound();
    }

    @Override
    public GameLoopMode getGameLoopMode() {
        return GameLoopMode.DistinctWorldLoopThread;
    }

    @Override
    public void waitSomeTimeOnGuiAction(int milliseconds) {
       // should not happen in tests
    }
}
