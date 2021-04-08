package fight;

import dungeon.Room;
import event.Event;

public class FightEndedEvent extends Event{

    private Room room;

    public FightEndedEvent(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }
}
