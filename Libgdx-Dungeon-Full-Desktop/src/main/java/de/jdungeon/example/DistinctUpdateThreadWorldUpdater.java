package de.jdungeon.example;

import de.jdungeon.dungeon.Dungeon;
import de.jdungeon.figure.hero.Hero;
import de.jdungeon.game.GameLoopMode;
import de.jdungeon.log.Log;
import de.jdungeonx.DungeonGameLoop;
import de.jdungeon.game.DungeonWorldUpdater;
import de.jdungeon.game.JDGUI;

public class DistinctUpdateThreadWorldUpdater implements DungeonWorldUpdater {

    private final DungeonGameLoop dungeonGameLoop;

    DistinctUpdateThreadWorldUpdater(Dungeon dungeon, Hero hero, JDGUI gui) {
        dungeonGameLoop = new DungeonGameLoop(dungeon, this);
        dungeonGameLoop.putGuiFigure(hero, gui);
        dungeonGameLoop.init();
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
        try {
            Thread.currentThread().sleep(milliseconds);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            Log.error("Waiting for Action was interrupted: ", e);
            e.printStackTrace();
        }
    }
}
