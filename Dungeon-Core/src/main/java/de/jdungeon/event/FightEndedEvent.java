package de.jdungeon.event;

import de.jdungeon.dungeon.Room;
import de.jdungeon.event.Event;

public class FightEndedEvent extends Event{

    private Room room;

    public FightEndedEvent(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }
}
